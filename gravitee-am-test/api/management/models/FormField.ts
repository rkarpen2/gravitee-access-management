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
 * @interface FormField
 */
export interface FormField {
  /**
   *
   * @type {string}
   * @memberof FormField
   */
  key?: string;
  /**
   *
   * @type {string}
   * @memberof FormField
   */
  label?: string;
  /**
   *
   * @type {string}
   * @memberof FormField
   */
  type?: string;
}

export function FormFieldFromJSON(json: any): FormField {
  return FormFieldFromJSONTyped(json, false);
}

export function FormFieldFromJSONTyped(json: any, ignoreDiscriminator: boolean): FormField {
  if (json === undefined || json === null) {
    return json;
  }
  return {
    key: !exists(json, 'key') ? undefined : json['key'],
    label: !exists(json, 'label') ? undefined : json['label'],
    type: !exists(json, 'type') ? undefined : json['type'],
  };
}

export function FormFieldToJSON(value?: FormField | null): any {
  if (value === undefined) {
    return undefined;
  }
  if (value === null) {
    return null;
  }
  return {
    key: value.key,
    label: value.label,
    type: value.type,
  };
}
