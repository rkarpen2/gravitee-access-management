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
import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-new-client-secret',
  templateUrl: './new-client-secret.component.html',
  styleUrl: '../secrets-certificates.component.scss',
})
export class NewClientSecretComponent {
  descriptionControl = new FormControl('', [Validators.required, this.noWhitespaceValidator]);
  constructor(public dialogRef: MatDialogRef<NewClientSecretComponent>) {}

  closeDialog() {
    if (this.descriptionControl.invalid) {
      this.descriptionControl.markAsTouched();
      return;
    }
    this.dialogRef.close(this.descriptionControl.value);
  }
  public noWhitespaceValidator(control: FormControl) {
    return (control.value || '').trim().length ? null : { whitespace: true };
  }
}
