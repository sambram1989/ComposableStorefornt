import { TestBed } from '@angular/core/testing';

import { WebinarlistService } from './webinarlist.service';

describe('WebinarlistService', () => {
  let service: WebinarlistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebinarlistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
