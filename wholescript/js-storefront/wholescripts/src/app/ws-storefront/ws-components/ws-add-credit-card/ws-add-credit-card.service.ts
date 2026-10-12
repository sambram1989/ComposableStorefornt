import { HttpClient } from '@angular/common/http';
import { EventEmitter, inject, Injectable, signal } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { COMMERCE_ALL_ADDRESSES, GET_ALL_ADDRESSES } from '../../ws-constants/ws-api-endpoint.constant';
import { Observable, shareReplay } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WsAddCreditCardService {
  private occ = inject(OccEndpointsService);
  private http = inject(HttpClient);
  private addressListCache$: Observable<any> | null = null;

  userSelectedAddress = signal<any>({});
  addAddressButton = new EventEmitter();
  closeMeButton = new EventEmitter();

  emitaddAddressButton(addr: any) {
    this.addAddressButton.emit(addr);
  }

  emitcloseMeButton() {
    this.closeMeButton.emit();
  }

  getListOfAddresses() {
      this.addressListCache$ = this.http
          .get(this.occ.buildUrl(COMMERCE_ALL_ADDRESSES))
        .pipe(shareReplay(1));
    return this.addressListCache$;
  }

  updateAddressList(){
    this.addressListCache$ = this.http
          .get(this.occ.buildUrl(COMMERCE_ALL_ADDRESSES))
    return this.addressListCache$;
  }
}
