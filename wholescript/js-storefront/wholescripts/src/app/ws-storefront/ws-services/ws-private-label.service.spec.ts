import { TestBed } from '@angular/core/testing';

import { WsPrivateLabelService } from './ws-private-label.service';

describe('WsPrivateLabelService', () => {
  let service: WsPrivateLabelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WsPrivateLabelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
