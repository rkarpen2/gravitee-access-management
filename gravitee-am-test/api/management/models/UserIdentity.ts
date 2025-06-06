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
/**
 *
 * @export
 * @interface UserIdentity
 */
export interface UserIdentity {
  /**
   *
   * @type {string}
   * @memberof UserIdentity
   */
  userId?: string;
  /**
   *
   * @type {string}
   * @memberof UserIdentity
   */
  username?: string;
  /**
   *
   * @type {string}
   * @memberof UserIdentity
   */
  providerId?: string;
  /**
   *
   * @type {{ [key: string]: any; }}
   * @memberof UserIdentity
   */
  additionalInformation?: { [key: string]: any };
  /**
   *
   * @type {Date}
   * @memberof UserIdentity
   */
  linkedAt?: Date;
}

export function UserIdentityFromJSON(json: any): UserIdentity {
  return UserIdentityFromJSONTyped(json, false);
}

export function UserIdentityFromJSONTyped(json: any, ignoreDiscriminator: boolean): UserIdentity {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    userId: !exists(json, 'userId') ? undefined : json['userId'],
    username: !exists(json, 'username') ? undefined : json['username'],
    providerId: !exists(json, 'providerId') ? undefined : json['providerId'],
    additionalInformation: !exists(json, 'additionalInformation') ? undefined : json['additionalInformation'],
    linkedAt: !exists(json, 'linkedAt') ? undefined : new Date(json['linkedAt']),
  };
}

export function UserIdentityToJSON(value?: UserIdentity | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    userId: value.userId,
    username: value.username,
    providerId: value.providerId,
    additionalInformation: value.additionalInformation,
    linkedAt: value.linkedAt === undefined ? undefined : value.linkedAt.toISOString(),
  };
}
