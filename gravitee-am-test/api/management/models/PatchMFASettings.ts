/*
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
/* Gravitee.io - Access Management API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

/* tslint:disable */
/* eslint-disable */
import { exists, mapValues } from '../runtime';
import {
  PatchChallengeSettings,
  PatchChallengeSettingsFromJSON,
  PatchChallengeSettingsFromJSONTyped,
  PatchChallengeSettingsToJSON,
} from './PatchChallengeSettings';
import {
  PatchEnrollSettings,
  PatchEnrollSettingsFromJSON,
  PatchEnrollSettingsFromJSONTyped,
  PatchEnrollSettingsToJSON,
} from './PatchEnrollSettings';
import {
  PatchEnrollmentSettings,
  PatchEnrollmentSettingsFromJSON,
  PatchEnrollmentSettingsFromJSONTyped,
  PatchEnrollmentSettingsToJSON,
} from './PatchEnrollmentSettings';
import {
  PatchFactorSettings,
  PatchFactorSettingsFromJSON,
  PatchFactorSettingsFromJSONTyped,
  PatchFactorSettingsToJSON,
} from './PatchFactorSettings';
import {
  PatchRememberDeviceSettings,
  PatchRememberDeviceSettingsFromJSON,
  PatchRememberDeviceSettingsFromJSONTyped,
  PatchRememberDeviceSettingsToJSON,
} from './PatchRememberDeviceSettings';
import {
  PatchStepUpAuthentication,
  PatchStepUpAuthenticationFromJSON,
  PatchStepUpAuthenticationFromJSONTyped,
  PatchStepUpAuthenticationToJSON,
} from './PatchStepUpAuthentication';

/**
 *
 * @export
 * @interface PatchMFASettings
 */
export interface PatchMFASettings {
  /**
   *
   * @type {string}
   * @memberof PatchMFASettings
   */
  loginRule?: string;
  /**
   *
   * @type {PatchFactorSettings}
   * @memberof PatchMFASettings
   */
  factor?: PatchFactorSettings;
  /**
   *
   * @type {string}
   * @memberof PatchMFASettings
   */
  stepUpAuthenticationRule?: string;
  /**
   *
   * @type {PatchStepUpAuthentication}
   * @memberof PatchMFASettings
   */
  stepUpAuthentication?: PatchStepUpAuthentication;
  /**
   *
   * @type {string}
   * @memberof PatchMFASettings
   */
  adaptiveAuthenticationRule?: string;
  /**
   *
   * @type {PatchRememberDeviceSettings}
   * @memberof PatchMFASettings
   */
  rememberDevice?: PatchRememberDeviceSettings;
  /**
   *
   * @type {PatchEnrollmentSettings}
   * @memberof PatchMFASettings
   */
  enrollment?: PatchEnrollmentSettings;
  /**
   *
   * @type {PatchEnrollSettings}
   * @memberof PatchMFASettings
   */
  enroll?: PatchEnrollSettings;
  /**
   *
   * @type {PatchChallengeSettings}
   * @memberof PatchMFASettings
   */
  challenge?: PatchChallengeSettings;
}

export function PatchMFASettingsFromJSON(json: any): PatchMFASettings {
  return PatchMFASettingsFromJSONTyped(json, false);
}

export function PatchMFASettingsFromJSONTyped(json: any, ignoreDiscriminator: boolean): PatchMFASettings {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    loginRule: !exists(json, 'loginRule') ? undefined : json['loginRule'],
    factor: !exists(json, 'factor') ? undefined : PatchFactorSettingsFromJSON(json['factor']),
    stepUpAuthenticationRule: !exists(json, 'stepUpAuthenticationRule') ? undefined : json['stepUpAuthenticationRule'],
    stepUpAuthentication: !exists(json, 'stepUpAuthentication')
      ? undefined
      : PatchStepUpAuthenticationFromJSON(json['stepUpAuthentication']),
    adaptiveAuthenticationRule: !exists(json, 'adaptiveAuthenticationRule') ? undefined : json['adaptiveAuthenticationRule'],
    rememberDevice: !exists(json, 'rememberDevice') ? undefined : PatchRememberDeviceSettingsFromJSON(json['rememberDevice']),
    enrollment: !exists(json, 'enrollment') ? undefined : PatchEnrollmentSettingsFromJSON(json['enrollment']),
    enroll: !exists(json, 'enroll') ? undefined : PatchEnrollSettingsFromJSON(json['enroll']),
    challenge: !exists(json, 'challenge') ? undefined : PatchChallengeSettingsFromJSON(json['challenge']),
  };
}

export function PatchMFASettingsToJSON(value?: PatchMFASettings | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    loginRule: value.loginRule,
    factor: PatchFactorSettingsToJSON(value.factor),
    stepUpAuthenticationRule: value.stepUpAuthenticationRule,
    stepUpAuthentication: PatchStepUpAuthenticationToJSON(value.stepUpAuthentication),
    adaptiveAuthenticationRule: value.adaptiveAuthenticationRule,
    rememberDevice: PatchRememberDeviceSettingsToJSON(value.rememberDevice),
    enrollment: PatchEnrollmentSettingsToJSON(value.enrollment),
    enroll: PatchEnrollSettingsToJSON(value.enroll),
    challenge: PatchChallengeSettingsToJSON(value.challenge),
  };
}
