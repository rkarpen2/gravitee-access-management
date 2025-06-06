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
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';

import { DomainService } from '../services/domain.service';
import { NavbarService } from '../components/navbar/navbar.service';
import { DomainStoreService } from '../stores/domain.store';

@Injectable()
export class DomainResolver {
  constructor(
    private domainService: DomainService,
    private domainStore: DomainStoreService,
    private navbarService: NavbarService,
  ) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    const domainId = route.paramMap.get('domainId');
    if (this.domainStore.current?.id === domainId) {
      return of(this.domainStore.current);
    }
    return this.domainService.getById(domainId).pipe(
      mergeMap((domain) =>
        this.domainService.permissions(domain.id).pipe(
          map((__) => {
            this.domainStore.set(domain);
            this.navbarService.notifyDomain(domain);
            return domain;
          }),
        ),
      ),
    );
  }
}
