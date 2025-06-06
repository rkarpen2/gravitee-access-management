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
package io.gravitee.am.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.function.ThrowingFunction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Titouan COMPIEGNE (titouan.compiegne at graviteesource.com)
 * @author GraviteeSource Team
 */
// todo: remove @Schema once this class is not directly used in any API responses
@Data
public class Certificate {

    private String id;

    private String name;

    private String type;

    private String configuration;

    private String domain;

    private Map<String, Object> metadata;

    @Schema(type = "java.lang.Long")
    private Date createdAt;

    @Schema(type = "java.lang.Long")
    private Date updatedAt;

    @Schema(type = "java.lang.Long")
    private Date expiresAt;

    private boolean system;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private JsonNode parsedConfig;

    public boolean hasUse(String use, ThrowingFunction<String, JsonNode> parseConfig) {
        return Optional.ofNullable(parseConfig.apply(configuration))
                .map(config -> (ArrayNode) config.get("use"))
                .map(Iterable::spliterator)
                .map(it -> StreamSupport.stream(it, false))
                .orElse(Stream.of(new TextNode("sig"))) // if 'use' is not present, consider it as use=[sig]
                .anyMatch(x -> use.equals(x.textValue()));
    }

    public Certificate() {
    }

    public Certificate(Certificate other) {
        this.id = other.id;
        this.name = other.name;
        this.type = other.type;
        this.configuration = other.configuration;
        this.domain = other.domain;
        this.metadata = other.metadata != null ? new HashMap<>(other.metadata) : null;
        this.createdAt = other.createdAt;
        this.updatedAt = other.updatedAt;
        this.expiresAt = other.expiresAt;
        this.system = other.system;
    }

    /**
     * Clone the certificate instance but reset the MetaData map with an empty & immutable map.
     * It is used as for example during audit generation
     * @return
     */
    public Certificate asSafeCertificate() {
        var cert = new Certificate(this);
        cert.setMetadata(Map.of());
        return cert;
    }
}
