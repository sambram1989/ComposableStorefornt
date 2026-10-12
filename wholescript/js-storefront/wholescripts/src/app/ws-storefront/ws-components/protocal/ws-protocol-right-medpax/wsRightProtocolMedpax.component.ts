import { Component, inject, input } from "@angular/core";
import { Location } from '@angular/common';
import { WsCustomMedPaxEvents } from "../../custom-medpax/medpaxService/medpaxEventservice.service";
import { BsModalService, BsModalRef } from "ngx-bootstrap/modal";
import { WsProductDetailsPopupComponent } from "../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component";

@Component({
    selector: 'ws-protocol-medpax-right-table',
    templateUrl: './wsRightProtocolMedpax.component.html',
    styleUrl: '../med-pax-products-right-section/med-pax-products-right-section.component.scss'

})
export class ProtocolMedpaxRightTable {
    private wsEventService = inject(WsCustomMedPaxEvents);
    public location = inject(Location);
    private bsModalService = inject(BsModalService);
    bsModalRef?: BsModalRef;
    
    checkForDefaultCarton =  this.location.path().includes('/Recommendation/BasicRecommendation/') || this.location.path().includes('/protocol/edit/') ;
    products = input<any>();
    hideActions = input<boolean>(false)

    editDosing(Product: any) {
        this.wsEventService.emitEditClick({ prod: Product.product, code: Product?.product?.code, isMedPaxEntry: true });
    }

    removeProduct(Product: any, entryNumber: string) {
        this.wsEventService.emitDeleteClick({ EntryNumber: entryNumber, product: Product });
    }

    openProductDetailPopup(productCode: string) {
        const initialState = {
          productCode: productCode
        };
        this.bsModalRef = this.bsModalService.show(WsProductDetailsPopupComponent, {
          initialState: initialState
        });
        this.bsModalRef.content.productCode = productCode;
     }
}