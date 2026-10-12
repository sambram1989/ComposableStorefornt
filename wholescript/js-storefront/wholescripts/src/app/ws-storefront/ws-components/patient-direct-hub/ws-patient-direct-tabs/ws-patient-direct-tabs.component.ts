import { AfterViewInit, Component, inject, signal } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { BREAKPOINT, BreakpointService } from '@spartacus/storefront';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-patient-direct-tabs',
  templateUrl: './ws-patient-direct-tabs.component.html',
  styleUrl: './ws-patient-direct-tabs.component.scss',
})
export class WsPatientDirectTabsComponent implements AfterViewInit {
  private router = inject(Router);
  private breakpointService = inject(BreakpointService);
  private location = inject(Location);

  isMobile: Observable<any>;
  showNav = false;
  selectedTab = signal<string>('Referral Code');
  tabIndex = signal<number>(1);

  private tabMappings: Record<string, { label: string; index: number }> = {
    'Consultation-fees': { label: 'Consultation fees', index: 4 },
    Accruals: { label: 'Accurals', index: 2 },
    'Web-banners': { label: 'Web banners and snippets', index: 3 },
  };

  ngOnInit() {
    this.isMobile = this.breakpointService.isDown(BREAKPOINT.sm);
    const path = this.location.path();
    const match = Object.keys(this.tabMappings).find((key) => path.includes(key));

    if (match) {
      this.selectedTab.set(this.tabMappings[match].label);
      this.tabIndex.set(this.tabMappings[match].index);
    }
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Patient Direct Hub';
  }

  onKeyDown(event: KeyboardEvent, parameter: string, data: any) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
        this.goToPage(parameter, data); 
    }
  }

  goToPage(link: string, tab: number) {
    this.setTabIndex(tab);
    this.router.navigate(['/patient-direct-hub', link]);
  }

  private setTabIndex(index: number) {
    const tab = Object.values(this.tabMappings).find((t) => t.index === index) ||
      { label: 'Referral Code', index: 1 };
    this.selectedTab.set(tab.label);
    this.tabIndex.set(tab.index);
  }

}
