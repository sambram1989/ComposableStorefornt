import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxPharmacyPageComponent } from './custom-medpax-pharmacy-page.component';

describe('CustomMedpaxPharmacyPageComponent', () => {
   let component: CustomMedpaxPharmacyPageComponent;
   let fixture: ComponentFixture<CustomMedpaxPharmacyPageComponent>;

   beforeEach(async () => {
      await TestBed.configureTestingModule({
         declarations: [CustomMedpaxPharmacyPageComponent]
      })
         .compileComponents();

      fixture = TestBed.createComponent(CustomMedpaxPharmacyPageComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
   });

   it('should create', () => {
      expect(component).toBeTruthy();
   });
});
