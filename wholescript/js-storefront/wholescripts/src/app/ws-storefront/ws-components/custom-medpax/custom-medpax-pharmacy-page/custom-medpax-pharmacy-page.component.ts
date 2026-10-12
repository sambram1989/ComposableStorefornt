import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CmsService } from '@spartacus/core';

@Component({
   selector: 'ws-custom-medpax-pharmacy-page',
   templateUrl: './custom-medpax-pharmacy-page.component.html',
   styleUrl: './custom-medpax-pharmacy-page.component.scss'
})
export class CustomMedpaxPharmacyPageComponent {
  private cmsService = inject(CmsService);
  private destroyRef = inject(DestroyRef);
  isPatient = true;

  section1Data = signal<any[]>([]);
  section2Data = signal<any[]>([]);
  section3Data = signal<any[]>([]);
  section4Data = signal<any[]>([]);
  section5Data = signal<any[]>([]);

  changePatient() {
    this.isPatient = true;
  }
  
  changePractitioner() {
    this.isPatient = false;
  }
  
  ngOnInit() {
    this.cmsService.getCurrentPage()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((pageData: any) => {
        const sections = [
          { slot: 'Section1', dataSignal: this.section1Data },
          { slot: 'Section2', dataSignal: this.section2Data },
          { slot: 'Section3', dataSignal: this.section3Data },
          { slot: 'Section4', dataSignal: this.section4Data },
          { slot: 'Section5', dataSignal: this.section5Data }
        ];

        sections.forEach(({ slot, dataSignal }) => {
          const sectionData: any[] = [];
          pageData?.slots?.[slot]?.components.forEach((item: any) => {
            this.cmsService.getComponentData(item.uid)
              .pipe(takeUntilDestroyed(this.destroyRef))
              .subscribe((res: any) => {
                sectionData.push(res);
                dataSignal.set([...sectionData]);
              });
          });
        });
      });
  }
}
