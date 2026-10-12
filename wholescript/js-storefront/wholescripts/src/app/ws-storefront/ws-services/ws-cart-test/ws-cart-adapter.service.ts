

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccCartEntryAdapter } from '@spartacus/cart/base/occ';
import { ConverterService, OccEndpointsService } from '@spartacus/core';

@Injectable({providedIn:'root'})
export class WsOccCartEntryAdapter extends OccCartEntryAdapter {
  constructor(
     http: HttpClient,
     occEndpointsService: OccEndpointsService,
     converterService: ConverterService
  ) {super(http,occEndpointsService,converterService)}

}
