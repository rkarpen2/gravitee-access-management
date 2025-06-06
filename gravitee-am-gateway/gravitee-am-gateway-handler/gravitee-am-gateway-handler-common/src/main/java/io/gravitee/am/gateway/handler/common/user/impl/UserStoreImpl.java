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

package io.gravitee.am.gateway.handler.common.user.impl;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gravitee.am.gateway.handler.common.user.UserStore;
import io.gravitee.am.gateway.handler.common.user.UserValueMapper;
import io.gravitee.am.model.User;
import io.gravitee.node.api.cache.Cache;
import io.gravitee.node.api.cache.CacheException;
import io.gravitee.node.api.cache.CacheManager;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.concurrent.TimeUnit;

/**
 * @author Eric LELEU (eric.leleu at graviteesource.com)
 * @author GraviteeSource Team
 */
@Slf4j
public class UserStoreImpl implements UserStore {

    private final Cache<String, User> idCache;

    private final int ttlInSec;

    public UserStoreImpl(CacheManager cacheManager, Environment environment) {
        final var mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.idCache = cacheManager.getOrCreateCache("userStoreById", new UserValueMapper(mapper));
        this.ttlInSec = environment.getProperty("user.cache.ttl", Integer.class, 36000);
    }

    @Override
    public Maybe<User> add(User user) {
        return idCache.rxPut(user.getId(), user, ttlInSec, TimeUnit.SECONDS)
                .onErrorResumeNext(exc -> {
                    if (exc instanceof CacheException) {
                        log.debug("Unable to add user with id {}, return user parameter as result", user.getId(), exc);
                        return Maybe.just(user);
                    }
                    return Maybe.error(exc);
                });
    }

    @Override
    public Completable remove(String userId) {
        return idCache.rxEvict(userId)
                .onErrorResumeNext(exc -> {
                    if (exc instanceof CacheException) {
                        log.debug("Unable to evict userId {}, return empty result", userId, exc);
                        return Maybe.empty();
                    }
                    return Maybe.error(exc);
                }).ignoreElement();
    }

    @Override
    public Maybe<User> get(String userId) {
        return idCache.rxGet(userId)
                .onErrorResumeNext(exc -> {
                    if (exc instanceof CacheException) {
                        log.debug("Unable to get userId {}, return empty result", userId, exc);
                        return Maybe.empty();
                    }
                    return Maybe.error(exc);
                });
    }

    @Override
    public Completable removeByInternalSub(String gis) {
        return remove(gis);
    }

    @Override
    public Maybe<User> getByInternalSub(String gis) {
        return idCache.rxGet(gis)
                .onErrorResumeNext(exc -> {
                    if (exc instanceof CacheException) {
                        log.debug("Unable to get gis {}, return empty result", gis, exc);
                        return Maybe.empty();
                    }
                    return Maybe.error(exc);
                });
    }

    @Override
    public Completable clear() {
        return idCache.rxClear()
                .onErrorResumeNext(exc -> {
                    if (exc instanceof CacheException) {
                        log.debug("Unable to clear cache", exc);
                        return Completable.complete();
                    }
                    return Completable.error(exc);
                });
    }
}
