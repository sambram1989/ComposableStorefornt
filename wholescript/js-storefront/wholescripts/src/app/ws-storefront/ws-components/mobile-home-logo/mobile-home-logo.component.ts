import { Component, HostListener, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OutletPosition } from '@spartacus/storefront';

@Component({
  selector: 'app-mobile-home-logo',
  templateUrl: './mobile-home-logo.component.html',
})
export class MobileHomeLogoComponent implements OnInit {
 private router = inject(Router);
  outletPosition = OutletPosition.AFTER;
  isMobile!: boolean;
  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkIfMobile();
  }

  ngOnInit(): void {
    this.checkIfMobile();
  }

  private checkIfMobile(): void {
    const width = window.innerWidth;

    // You can also add user agent detection here if needed
    this.isMobile = width <= 991; // You can adjust the width threshold
  }

  homeRedirection() {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user && user.rootUrl) {
      this.router.navigate([user.rootUrl]);
    } else {
      this.router.navigate(['/']);
    }
  }
}
