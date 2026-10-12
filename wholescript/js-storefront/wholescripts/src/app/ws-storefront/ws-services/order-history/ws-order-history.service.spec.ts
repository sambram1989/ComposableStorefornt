import { TestBed } from '@angular/core/testing';

import { WsOrderHistoryService } from './ws-order-history.service';

describe('WsOrderHistoryService', () => {
  let service: WsOrderHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WsOrderHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
