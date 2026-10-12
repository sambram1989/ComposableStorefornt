import { Component, inject, Input, input, signal } from '@angular/core';
import { WsCustomMedPaxEvents } from '../medpaxService/medpaxEventservice.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';

@Component({
  selector: 'ws-rightmedpax-table',
  templateUrl: './rightmedpax-table.component.html',
  styleUrl: './rightmedpax-table.component.scss'
})
export class RightmedpaxTableComponent {
private eventService = inject(WsCustomMedPaxEvents);
private bsModalService = inject(BsModalService)
bsModalRef?: BsModalRef;
products = input<any>({});
isFromProtocol = input<boolean>(false);
@Input() isActionsVisible:boolean = true;
@Input() showMedpaxPLNameOrName: boolean = false;
isMedPaxAvailable = input<boolean>(false);
medPaxData = input<any>();

openProductDetailPopup(productCode: string) {
  const initialState = {
    productCode: productCode
  };
  this.bsModalRef = this.bsModalService.show(WsProductDetailsPopupComponent, {
    initialState: initialState
  });
  this.bsModalRef.content.productCode = productCode;
}

editDosing(prod:any){
this.eventService.emitEditClick( { type: 'Edit', prod: prod, code: prod?.code ,isMedPaxEntry:true });
}

removeProduct(product:any) {
    this.eventService.emitDeleteClick({EntryNumber:product.entryNumber,product:product});
  }

}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'isArray'
})
export class IsArrayPipe implements PipeTransform {
  transform(value: any): boolean {
    return Array.isArray(value);
  }
}

