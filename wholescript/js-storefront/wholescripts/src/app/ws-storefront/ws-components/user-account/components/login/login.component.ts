/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AuthService, CmsNavigationComponent, CmsService, useFeatureStyles } from '@spartacus/core';
import { User, UserAccountFacade } from '@spartacus/user/account/root';
import { Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { WsNavigationNode, WsNavigationService } from '../../../../ws-services/ws-navigation.service';
import { CmsComponentData } from '@spartacus/storefront';

@Component({
  selector: 'cx-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  user$: Observable<User | undefined>;
   node$: Observable<WsNavigationNode> = this.service.createNavigation(
      this.componentData.data$
    );
  
    name$: Observable<string | undefined> = this.componentData.data$.pipe(
      map((d) => d?.navigationNode?.title)
    );
  
    styleClass$: Observable<string | undefined> = this.componentData.data$.pipe(
      map((d) => d?.styleClass)
    );
    nodeTab$: Observable<WsNavigationNode>;
    componentDataForAccountNav$: Observable<any>;
    isVisible = false;

  constructor(
    private auth: AuthService,
    private userAccount: UserAccountFacade,
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected service: WsNavigationService,
    protected cmsservice: CmsService,
    protected cdr: ChangeDetectorRef,
  ) {
    useFeatureStyles('a11yMyAccountLinkOutline');
  }

  getComponentData() {
    return this.cmsservice.getContentSlot('HeaderLinks').subscribe((res: any) => {
      const WWGLatestFeatureComponent = res.components.filter((res: any) => {
        return res.uid == 'MyAccountTabSideNavigationComponent';
      });
      if (WWGLatestFeatureComponent.length) {
        this.componentDataForAccountNav$ = this.cmsservice.getComponentData(
          WWGLatestFeatureComponent[0].uid
        );
        this.nodeTab$ = this.service.createNavigation(this.componentDataForAccountNav$);
        this.cdr.detectChanges();
      }
    });
}

  ngOnInit(): void {
    this.getComponentData();
    this.user$ = this.auth.isUserLoggedIn().pipe(
      switchMap((isUserLoggedIn) => {
        if (isUserLoggedIn) {
          return this.userAccount.get();
        } else {
          return of(undefined);
        }
      })
    );
  }
  
  toggle(event: UIEvent): void {
    this.isVisible = !this.isVisible;
    const wrapperDiv = document.querySelectorAll('li > .wrapper')[0];
    if(this.isVisible) {
      wrapperDiv?.classList.remove('hidden');
    } else {
      wrapperDiv?.classList.add('hidden');
    }
  }
}
