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
package io.gravitee.am.management.service.impl.notifications.definition;

import io.gravitee.node.api.notifier.NotificationDefinition;
import io.reactivex.rxjava3.core.Maybe;

public interface NotificationDefinitionFactory<T extends NotifierSubject> {

     Maybe<NotificationDefinition> buildNotificationDefinition(T object);

     class StubNotificationDefinitionFactory implements NotificationDefinitionFactory {

         @Override
         public Maybe<NotificationDefinition> buildNotificationDefinition(NotifierSubject object) {
             return Maybe.empty();
         }
     }

     static <T extends NotifierSubject> NotificationDefinitionFactory<T> stub(){
         return new StubNotificationDefinitionFactory();
     }
}
