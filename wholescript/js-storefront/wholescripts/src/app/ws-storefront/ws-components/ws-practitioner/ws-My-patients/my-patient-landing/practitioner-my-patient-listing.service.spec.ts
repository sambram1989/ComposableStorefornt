import { TestBed } from '@angular/core/testing';

import { PractitionerMyPatientListingService } from './practitioner-my-patient-listing.service';

describe('PractitionerMyPatientListingService', () => {
  let service: PractitionerMyPatientListingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PractitionerMyPatientListingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
