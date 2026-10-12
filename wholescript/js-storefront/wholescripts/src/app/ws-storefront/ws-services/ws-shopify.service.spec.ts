import { TestBed } from '@angular/core/testing';

import { WsShopifyService } from './ws-shopify.service';

describe('WsShopifyService', () => {
  let service: WsShopifyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WsShopifyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
