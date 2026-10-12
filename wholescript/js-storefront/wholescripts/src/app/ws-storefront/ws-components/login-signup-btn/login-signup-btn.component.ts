import { Component } from '@angular/core';
import { WsSidebarService } from '../../ws-services/ws-sidebar.service';

@Component({
  selector: 'app-login-signup-btn',
  templateUrl: './login-signup-btn.component.html',
})
export class LoginSignupBtnComponent {

  constructor(protected wsSidebarService: WsSidebarService,
  ) { }
  hideMenu() {
    this.wsSidebarService.changeMenuState(false);
  }
}


