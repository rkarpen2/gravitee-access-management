/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.am.gateway.handler.oauth2.resources.auth.provider;

import io.gravitee.am.common.oidc.ClientAuthenticationMethod;
import io.gravitee.am.gateway.handler.oauth2.exception.InvalidClientException;
import io.gravitee.am.gateway.handler.oauth2.resources.auth.handler.ClientAuthHandler;
import io.gravitee.am.model.oidc.Client;
import io.gravitee.am.service.impl.SecretService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.net.URLDecoder;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Client Authentication method : client_secret_basic
 *
 * Clients that have received a client_secret value from the Authorization Server authenticate with the Authorization Server
 * in accordance with Section 2.3.1 of OAuth 2.0 [RFC6749] using the HTTP Basic authentication scheme.
 *
 * @author Titouan COMPIEGNE (titouan.compiegne at graviteesource.com)
 * @author GraviteeSource Team
 */
public class ClientBasicAuthProvider implements ClientAuthProvider {

    private static final String TYPE = "Basic";

    private final SecretService appSecretService;

    public ClientBasicAuthProvider(SecretService appSecretService) {
        this.appSecretService = appSecretService;
    }

    @Override
    public boolean canHandle(Client client, RoutingContext context) {
        if (client != null && ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(client.getTokenEndpointAuthMethod())) {
            return true;
        }
        if ((client != null && (client.getTokenEndpointAuthMethod() == null || client.getTokenEndpointAuthMethod().isEmpty()))
                && getBasicAuthorization(context.request()) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Client client, RoutingContext context, Handler<AsyncResult<Client>> handler) {
        final String authorization = getBasicAuthorization(context.request());
        if (authorization == null) {
            handler.handle(Future.failedFuture(new InvalidClientException("Invalid client: missing or unsupported authentication method", authenticationHeader())));
            return;
        }
        try {
            // decode the payload
            String decoded = new String(Base64.getDecoder().decode(authorization));
            int colonIdx = decoded.indexOf(":");
            if (colonIdx == -1) {
                throw new IllegalArgumentException();
            }
            String clientId = urlDecode(decoded.substring(0, colonIdx));
            String clientSecret = urlDecode(decoded.substring(colonIdx + 1));

            if (!client.getClientId().equals(clientId)) {
                handler.handle(Future.failedFuture(new InvalidClientException(ClientAuthHandler.GENERIC_ERROR_MESSAGE, authenticationHeader())));
                return;
            }

            if (!isEmpty(client.getClientSecrets())) {
                boolean authenticated = appSecretService.validateSecret(client, clientSecret);

                if (!authenticated) {
                    handler.handle(Future.failedFuture(new InvalidClientException(
                            ClientAuthHandler.GENERIC_ERROR_MESSAGE, authenticationHeader())));
                    return;
                }

            } else if (!client.getClientSecret().equals(clientSecret)) {
                // Prior to 4.2, client secret where not hashed and directly stored into the clientSecret attribute
                handler.handle(Future.failedFuture(new InvalidClientException(ClientAuthHandler.GENERIC_ERROR_MESSAGE, authenticationHeader())));
                return;
            }

            handler.handle(Future.succeededFuture(client));
        } catch (RuntimeException e) {
            handler.handle(Future.failedFuture(new InvalidClientException("Invalid client: missing or unsupported authentication method", e, authenticationHeader())));
            return;
        }
    }

    /**
     * @param value
     * @return the URL value version of value or the input value if the URLDecode fails
     */
    private static String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, UTF_8);
        } catch (IllegalArgumentException e) {
            // Introduced to fix https://github.com/gravitee-io/issues/issues/8501.
            // https://github.com/gravitee-io/issues/issues/7803 introduced a URL decoding
            // action on the clientSecret/clientId to be compliant to the RFC. To avoid breaking the
            // behaviour for customer that are using some special characters like '%', we fall back to the
            // raw value if the URL decode process fails.
            return value;
        }
    }

    private String getBasicAuthorization(HttpServerRequest request) {
        final String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return null;
        }
        int idx = authorization.indexOf(' ');
        if (idx <= 0) {
           return null;
        }
        if (!TYPE.equalsIgnoreCase(authorization.substring(0, idx))) {
            return null;
        }
        return authorization.substring(idx + 1);
    }

    private String authenticationHeader() {
        return "Basic realm=\"gravitee-io\"";
    }
}
