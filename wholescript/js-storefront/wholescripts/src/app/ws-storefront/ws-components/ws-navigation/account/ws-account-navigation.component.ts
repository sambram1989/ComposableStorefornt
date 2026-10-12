import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  inject,
  Renderer2,
  ViewChild,
  SimpleChanges
} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, User, WindowRef } from '@spartacus/core';
import { HamburgerMenuService, NavigationUIComponent } from '@spartacus/storefront';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { Observable, switchMap, of } from 'rxjs';
import { UserAccountFacade } from '@spartacus/user/account/root';


@Component({
  selector: 'ws-acc-navigation-ui',
  templateUrl: './ws-account-navigation.component.html',
  styleUrls: ['./ws-account-navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsAccountNavigationUIComponent extends NavigationUIComponent {
  currentUserData: any = inject(getUserData);
  @ViewChild('nav') elRef!: ElementRef;
  @ViewChild('wrapper') wrapper!: ElementRef;
  user$: Observable<User | undefined>;
  shouldAddTabindex = false;
  constructor(
    router: Router,
    renderer: Renderer2,
    elemRef: ElementRef,
     hamburgerMenuService: HamburgerMenuService,
     winRef: WindowRef,
     private auth: AuthService,
     private userAccount: UserAccountFacade
  ) {
    super(router, renderer, elemRef, hamburgerMenuService, winRef);
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

  removeHiddenClass() {
    if(this.wrapper?.nativeElement.classList?.contains('hidden')) {
      this.wrapper?.nativeElement.classList?.remove('hidden');
    }
  }

  ngAfterViewInit() {
    const hasClass = this.elRef?.nativeElement?.classList?.contains('my-class');
    this.shouldAddTabindex = hasClass;
  }
  
}

