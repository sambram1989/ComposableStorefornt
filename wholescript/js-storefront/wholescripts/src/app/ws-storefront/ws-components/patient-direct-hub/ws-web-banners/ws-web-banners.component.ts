import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-web-banners',
  templateUrl: './ws-web-banners.component.html',
  styleUrl: './ws-web-banners.component.scss',
})
export class WsWebBannersComponent {
  alerts: any[] = [];
  referrals: any[] = [];
  selectedReferral = '';
  webBanners$!: Observable<any>;

  constructor(public patientService: PatientDirectService, private cd: ChangeDetectorRef) {
    this.getReferrals();
  }

  onKeyDown(event: KeyboardEvent, parameter: string, data: any) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      if (parameter === 'copyToClipboard') {
        this.copyToClipboard(data);
      }
    }
  }

  copyToClipboard(text: any) {
    this.add();
    navigator.clipboard
      .writeText(text)
      .then(() => {
        console.log('Text copied to clipboard successfully!');
      })
      .catch((err) => {});
  }

  add(): void {
    this.alerts.push({
      type: 'success',
      msg: `Code copied to clipboard.`,
      timeout: 2000,
    });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  getReferrals() {
    this.patientService.getAllReferralEntriesForCSV().subscribe((res) => {
      this.referrals = res.referralCodeListWsDto;
      if(this.referrals.length > 0) {
       this.selectedReferral = `${this.referrals[0].referralCode}|${this.referrals[0].lastName}`;
       this.fetchBanners()
       this.cd.detectChanges();
      }
    });
  }

  fetchBanners() {
    this.webBanners$ = this.patientService.getWebBannerAndScripts(this.selectedReferral);
  }

  getReferralLink(scriptLink: string, referralLink: string): string {
    let activeReferralLink: string = '';
    if(referralLink) {
      activeReferralLink = referralLink;
    } else {
      const [referralCode, lastName] = this.selectedReferral.split('|');
      activeReferralLink = `${referralCode}-${lastName}`
    }
    if (!activeReferralLink) return scriptLink;
    const _referral = `${window.location.origin}/register/${activeReferralLink}`;
    return scriptLink.replace('href_referralLink', `href="${_referral}"`);
  }
}
