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
package io.gravitee.am.management.service.impl.notifications;

import io.gravitee.node.api.notifier.NotificationAcknowledge;
import io.gravitee.node.api.notifier.NotificationDefinition;
import io.gravitee.node.api.notifier.ResendNotificationCondition;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Eric LELEU (eric.leleu at graviteesource.com)
 * @author GraviteeSource Team
 */
public class ExpireThresholdsResendNotificationCondition implements ResendNotificationCondition {

    private final List<Integer> expiryThresholds;

    public ExpireThresholdsResendNotificationCondition(List<Integer> expiryThresholds) {
        if (Objects.isNull(expiryThresholds) || expiryThresholds.isEmpty()) {
            throw new IllegalArgumentException("expiryThresholds requires at least one entry");
        }
        this.expiryThresholds = expiryThresholds;
    }

    @Override
    public Boolean apply(NotificationDefinition definition, NotificationAcknowledge notificationAcknowledge) {
        return notificationAcknowledge.getUpdatedAt() != null &&
                // test if one threshold has expired
                hasExpiredInterval(NotificationDefinitionUtils.getExpirationDate(definition).orElseThrow(),
                        notificationAcknowledge.getUpdatedAt());
    }

    private boolean hasExpiredInterval(Date expirationDate, Date lastUpdate) {
        final long remainingDays = lastUpdate.toInstant().isBefore(expirationDate.toInstant()) ? ChronoUnit.DAYS.between(lastUpdate.toInstant(), expirationDate.toInstant()) : 0;
        final int currentIndex = this.expiryThresholds.size() - (int)this.expiryThresholds.stream().filter(t -> t < remainingDays).count();

        return currentIndex < this.expiryThresholds.size() &&
                ChronoUnit.DAYS.between(lastUpdate.toInstant(), Instant.now()) != 0 &&
                Instant.now().plus(this.expiryThresholds.get(currentIndex), ChronoUnit.DAYS).isAfter(expirationDate.toInstant()) &&
                Instant.now().isBefore(expirationDate.toInstant());
    }

}
