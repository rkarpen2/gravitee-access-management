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
import { NewUser, NewUserFromJSON, NewUserFromJSONTyped, NewUserToJSON } from './NewUser';

/**
 *
 * @export
 * @interface BulkCreateUser
 */
export interface BulkCreateUser {
  /**
   *
   * @type {string}
   * @memberof BulkCreateUser
   */
  action: BulkCreateUserActionEnum;
  /**
   *
   * @type {number}
   * @memberof BulkCreateUser
   */
  failOnErrors?: number;
  /**
   *
   * @type {Array<NewUser>}
   * @memberof BulkCreateUser
   */
  items: Array<NewUser>;
}

/**
 * @export
 */
export const BulkCreateUserActionEnum = {
  Create: 'CREATE',
  Update: 'UPDATE',
  Delete: 'DELETE',
} as const;
export type BulkCreateUserActionEnum = typeof BulkCreateUserActionEnum[keyof typeof BulkCreateUserActionEnum];

export function BulkCreateUserFromJSON(json: any): BulkCreateUser {
  return BulkCreateUserFromJSONTyped(json, false);
}

export function BulkCreateUserFromJSONTyped(json: any, ignoreDiscriminator: boolean): BulkCreateUser {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    action: json['action'],
    failOnErrors: !exists(json, 'failOnErrors') ? undefined : json['failOnErrors'],
    items: (json['items'] as Array<any>).map(NewUserFromJSON),
  };
}

export function BulkCreateUserToJSON(value?: BulkCreateUser | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    action: value.action,
    failOnErrors: value.failOnErrors,
    items: (value.items as Array<any>).map(NewUserToJSON),
  };
}
