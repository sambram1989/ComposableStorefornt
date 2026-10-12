import { Component, DestroyRef, inject, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { Router } from '@angular/router';
import { CmsService } from '@spartacus/core';
import { Observable} from 'rxjs';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { BREAKPOINT, BreakpointService } from '@spartacus/storefront';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-w9-landing-page',
  templateUrl: './w9-landing-page.component.html',
  styleUrl: './w9-landing-page.component.scss'
})
export class W9LandingPageComponent {
  private cmsService = inject(CmsService);
  public router = inject(Router);
  public overLay = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  private responsiveService = inject(BreakpointService);
  private destroyRef = inject(DestroyRef);

  checkBox = false;
  @ViewChild('Popup') popup!: TemplateRef<any>;
  confirmPopup: any;
  isMobile = false;
  modalWidth = window.innerWidth * 0.24 + 'px';

  getStartedLink$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageGetStartedLinkComponent');
  whatstheProcess$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageParagraphComponent4');
  consultationFeesOpt$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageParagraphComponent5');
  bannerOne$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageBanner3');
  bannerTwo$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageBanner4');
  bannerThree$: Observable<any> = this.cmsService.getComponentData('patientDirectStartPageBanner5');

  ngOnInit() {
    this.responsiveService.isDown(BREAKPOINT.sm)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((is: boolean) => this.isMobile = is)
  }

  ngAfterViewInit() {
    this.confirmPopup = new TemplatePortal(this.popup, this.vcr);
  }

  goTow9Form(url: string) {
    if (this.checkBox) {
      this.closePopup();
      this.router.navigate([url]);
    } else {
      this.showPopup();
    }
  }

  showPopup() {
    this.overLay.openModal(this.confirmPopup, this.isMobile ? '100%' : this.modalWidth);
  }

  navigator() {
    this.closePopup();
    this.router.navigate(['practitioner/patient-direct/referral-code']);
  }

  closePopup() {
    this.overLay.closeAllModals();
  }
}
