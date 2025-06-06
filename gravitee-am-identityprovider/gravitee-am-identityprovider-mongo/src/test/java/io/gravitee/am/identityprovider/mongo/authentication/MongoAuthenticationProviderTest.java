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
package io.gravitee.am.identityprovider.mongo.authentication;

import io.gravitee.am.common.exception.authentication.BadCredentialsException;
import io.gravitee.am.common.exception.authentication.UsernameNotFoundException;
import io.gravitee.am.identityprovider.api.Authentication;
import io.gravitee.am.identityprovider.api.AuthenticationContext;
import io.gravitee.am.identityprovider.api.AuthenticationProvider;
import io.gravitee.am.identityprovider.api.User;
import io.gravitee.am.identityprovider.mongo.MongoIdentityProviderConfiguration;
import io.gravitee.am.identityprovider.mongo.authentication.spring.MongoAuthenticationProviderConfiguration;
import io.reactivex.rxjava3.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * @author Titouan COMPIEGNE (titouan.compiegne at graviteesource.com)
 * @author GraviteeSource Team
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        MongoAuthenticationProviderTestConfiguration.class,
        MongoAuthenticationProviderConfiguration.class },
        loader = AnnotationConfigContextLoader.class)
public class MongoAuthenticationProviderTest {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private MongoIdentityProviderConfiguration configuration;

    @Before
    public void setup(){
        configuration.setUsernameCaseSensitive(false);
        configuration.setFindUserByMultipleFieldsQuery(null);
    }

    @Test
    public void shouldLoadUserByUsername_username_only() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername("BoB").test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "bob".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_username_only_sensitive() {
        configuration.setUsernameCaseSensitive(true);
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername("UserWithCase").test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "UserWithCase".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "bobspassword";
            }

            @Override
            public Object getPrincipal() {
                return "BoB";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "bob".equals(u.getUsername()));
    }

    @Test
    public void shouldNotLoadUserByUsername_authentication_caseInsensitive() {
        configuration.setUsernameCaseSensitive(true);
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "bobspassword";
            }

            @Override
            public Object getPrincipal() {
                return "BoB";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);
        testObserver.assertError(UsernameNotFoundException.class);
    }

    @Test
    public void shouldLoadUserByUsername_authentication_insensitive() {
        configuration.setUsernameCaseSensitive(true);
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "UserWithCase";
            }

            @Override
            public Object getPrincipal() {
                return "UserWithCase";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "UserWithCase".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multifield_username() {
        configuration.setFindUserByMultipleFieldsQuery("{ $or : [{username: ?}, {email: ?}]}");
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "user01";
            }

            @Override
            public Object getPrincipal() {
                return "user01";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "user01".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multifield_username_sensitive() {
        configuration.setUsernameCaseSensitive(true);
        configuration.setFindUserByMultipleFieldsQuery("{ $or : [{username: ?}, {email: ?}]}");
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "user01";
            }

            @Override
            public Object getPrincipal() {
                return "user01";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "user01".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multifield_username_with_spaces() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "b o bpassword";
            }

            @Override
            public Object getPrincipal() {
                return "b o b";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "b o b".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_getResourceOwner_username_with_spaces() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername("b o b").test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "b o b".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multifield_email() {
        configuration.setFindUserByMultipleFieldsQuery("{ $or : [{username: ?}, {email: ?}]}");
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "user01";
            }

            @Override
            public Object getPrincipal() {
                return "user01@acme.com";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "user01".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multipleMatch_validPassword() {
        configuration.setFindUserByMultipleFieldsQuery("{ $or : [{username: ?}, {email: ?}]}");

        connectUser("common@acme.com", "user02");
        connectUser("common@acme.com", "user03");
    }

    private void connectUser(String principal, String credentials) {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return credentials;
            }

            @Override
            public Object getPrincipal() {
                return principal;
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> credentials.equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_multipleMatch_invalidPassword() {
        configuration.setFindUserByMultipleFieldsQuery("{ $or : [{username: ?}, {email: ?}]}");
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {
            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "invalidpwd";
            }

            @Override
            public Object getPrincipal() {
                return "common@acme.com";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertError(BadCredentialsException.class);
    }

    @Test
    public void shouldLoadUserByUsername_authentication_case_insensitive() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {

            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "bobspassword";
            }

            @Override
            public Object getPrincipal() {
                return "BOB";
            }

            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();
        testObserver.assertValue(u -> "bob".equals(u.getUsername()));
    }

    @Test
    public void shouldLoadUserByUsername_authentication_badCredentials() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {

            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "wrongpassword";
            }

            @Override
            public Object getPrincipal() {
                return "bob";
            }


            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();
        testObserver.awaitDone(10, TimeUnit.SECONDS);
        testObserver.assertError(BadCredentialsException.class);
    }

    @Test
    public void shouldLoadUserByUsername_authentication_usernameNotFound() {
        TestObserver<User> testObserver = authenticationProvider.loadUserByUsername(new Authentication() {

            private final AuthenticationContext authenticationContext = mock(AuthenticationContext.class);

            @Override
            public Object getCredentials() {
                return "bobspassword";
            }

            @Override
            public Object getPrincipal() {
                return "unknownUsername";
            }


            @Override
            public AuthenticationContext getContext() {
                doReturn(authenticationContext).when(authenticationContext).set(anyString(), anyString());
                doReturn(getPrincipal().toString()).when(authenticationContext).get(getPrincipal().toString());
                return authenticationContext;
            }
        }).test();

        testObserver.awaitDone(10, TimeUnit.SECONDS);
        testObserver.assertError(UsernameNotFoundException.class);
    }
}
