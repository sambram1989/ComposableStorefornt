import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { OccEndpointsService } from "@spartacus/core";
import { GET_ALL_REFERRAL_CODES, REFERRAL_CODE_FORM, W9_FORM } from "../../../ws-constants/ws-api-endpoint.constant";
@Injectable({ providedIn: 'root' })
export class W9Formservice {
    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);
    fedTaxClass = [
        { key: 'TaxClassIndividual', value: 'Individual/sole proprietor or single-member LLC' },
        { key: 'TaxClasscCorporation', value: 'CCorporation' },
        { key: 'TaxClasssCorporation', value: 'SCorporation' },
        { key: 'TaxClassPartnership', value: 'Partnership' },
        { key: 'TaxClassTrust', value: 'Trust/Estate' },
    ]
    limitedLiabilityFedTaxClass = { key: 'TaxClassLimited', value: 'Limited liability company. Enter the tax classification (C=C Corporation, S=S Corporation,P=Partnership)' };
    otherFedtaxClass = { key: 'TaxClassOther', value: 'Other (See instructions on page 3)' };
    selectDiscount =[{value:'Select discount',code:'0'},{value:'5%',code:5},{value:'10%',code:10},{value:'15%',code:15},{value:'20%',code:20}];

    postW9form(payload: any) {
        return this.http.post(this.occ.buildUrl(W9_FORM),payload );
    }

    postReferralCodeForm(payload: any) {
        return this.http.post(this.occ.buildUrl(REFERRAL_CODE_FORM), payload );
    }

    getReferalCode(){
        return this.http.get(this.occ.buildUrl(GET_ALL_REFERRAL_CODES));
    }

}