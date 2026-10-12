import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { Location } from '@angular/common';

@Component({
  selector: 'ws-register',
  templateUrl: './ws-register.component.html',
  styleUrl: './ws-register.component.scss'
})
export class WsRegisterComponent implements OnInit {

  private auth = inject(AuthService);
  loader = false;

  heading$: Observable<any> = this.cmsservice.getComponentData('regsiterPageCmsPara');
  smallLogoBanner$: Observable<any> = this.cmsservice.getComponentData('registerSmallLogoBanner');
  practitionerLink$: Observable<any> = this.cmsservice.getComponentData('iampractitionerlink');
  patientLink$: Observable<any> = this.cmsservice.getComponentData('iampatientlink');
  loginLink$: Observable<any> = this.cmsservice.getComponentData('loginLink');
  termsAndConditionsLink$: Observable<any> = this.cmsservice.getComponentData('TermsandConditionsFooterLinkComponent');
  privacyPolicyLink$: Observable<any> = this.cmsservice.getComponentData('PrivacyPolicyFooterLinkComponent');
  disclaimer$: Observable<any> = this.cmsservice.getComponentData('FooterParaComponent-Disclaimer');
  copyright$: Observable<any> = this.cmsservice.getComponentData('FooterParaComponent-Copyright');

  activeUserOptionLink: string = '';

  constructor(public cmsservice: CmsService, private router: Router, private location: Location) {}


  ngOnInit() {
    this.auth.isUserLoggedIn().subscribe((isUserLoggedIn) => {
      if (isUserLoggedIn) {
        this.loader = true;
      }
    });
  }

  userOptionChange(link: string) {
    this.activeUserOptionLink = link;
  }

  back() {
    this.location.back();
  }

  continue() {
    this.router.navigateByUrl(this.activeUserOptionLink);
  }
}
