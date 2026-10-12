import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { OccEndpointsService, SearchboxService, SearchConfig } from "@spartacus/core";
import { Observable } from "rxjs";
import { BRAND_CONSENT_UPDATE, CART_REMOVE_ITEMS, CHECKOUT_DELIVERY_ADDRESSES_ADD, CHECKOUT_DELIVERY_ADDRESSES_CREATE, CHECKOUT_DELIVERY_ADDRESSES_VALIDATE, CHECKOUT_DELIVERY_ADDRESSES_VERIFICATION, GET_ALL_ADDRESSES, GET_CHECKOUT_DELIVERY_ADDRESSES } from "../ws-constants/ws-api-endpoint.constant";

@Injectable({
    providedIn: 'root',
})
export class WsCheckoutDeliveryAddressService {

    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);

    getAddresses(cartId: string, searchText: string, currentPage: number = 0, pageSize: number = 10): Observable<any> {
        return this.http.get(
            this.occ.buildUrl(GET_CHECKOUT_DELIVERY_ADDRESSES(cartId), {
                queryParams: {
                    currentPage: currentPage,
                    pageSize: pageSize,
                    addressSearch: searchText,
                    fields: 'FULL'
                }
            })
        )
    }

    getBrandConsetUpdate(payload: any): Observable<any> {
        return this.http.post(
            this.occ.buildUrl(BRAND_CONSENT_UPDATE), payload
        )
    }

    getAllAddresses() {
        return this.http
        .post(this.occ.buildUrl(GET_ALL_ADDRESSES), null);
    }

    validateAddress(payload: any) {
        return this.http.post(this.occ.buildUrl(CHECKOUT_DELIVERY_ADDRESSES_VALIDATE), payload);
    }

    verifyAddress(cartId: string, payload: any) {
        return this.http.post(this.occ.buildUrl(CHECKOUT_DELIVERY_ADDRESSES_VERIFICATION(cartId)), payload);
    }

    createDeliveryAddress(cartId: string, addressNumber: any, payload: any) {
        return this.http.post(
            this.occ.buildUrl(CHECKOUT_DELIVERY_ADDRESSES_CREATE(cartId), {
                queryParams: {
                    addressNumber: addressNumber
                }
            }), payload
        )
    }

    addAddress(cartId: string, payload: any) {
        return this.http.post(this.occ.buildUrl(CHECKOUT_DELIVERY_ADDRESSES_ADD(cartId)), payload);
    }

    removeAllItems(cartId: string, entryNumbers: string) {
        return this.http.delete(this.occ.buildUrl(CART_REMOVE_ITEMS(cartId), {
            queryParams: {
                entryNumbers: entryNumbers
            }
        }));
    }
}