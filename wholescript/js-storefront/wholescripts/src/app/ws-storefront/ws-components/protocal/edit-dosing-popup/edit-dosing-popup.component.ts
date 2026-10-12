import { Component, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WsProtocolUtility } from '../../../ws-utility/ws-protocol-utility';
@Component({
  selector: 'ws-edit-dosing-popup',
  templateUrl: './edit-dosing-popup.component.html',
  styleUrl: './edit-dosing-popup.component.scss'
})
export class EditDosingPopupComponent implements OnInit {
  @Input() doseItemDetails:any = [];
  originalDoseItemDetails: any[] = [];
  doseResponse: any = {};
  @Input() productCode: string;

  constructor(public bsModalRef: BsModalRef, private protocolService: ProtocolService) {}

  ngOnInit(): void {
    this.originalDoseItemDetails = this.doseItemDetails;
    this.protocolService.getCustomDosingEntries(this.productCode).subscribe((response:any) => {
      this.doseResponse = response;
    })
  }

  /**
   * Click Event For Dose
   */
  onClickDosing(item: any, index: number) {
    console.log(item, index)
  }

  addRemoveDose(action: string, index: number) {
    let arry = [];
    if(action === 'add'){
    let originalData = [... this.doseItemDetails];
    console.log(originalData);
    let data = WsProtocolUtility.getDoseItem(originalData[0]);
    this.doseItemDetails.push(data);
    }
    else if(action === 'remove') {
      if (index > -1) {
        this.doseItemDetails.splice(index, 1);
        this.doseItemDetails?.forEach((dose:any, doseIndex: number) => {
          dose.doseTimeItems.forEach((element:any, itemIndex: number) => {
            console.log(index, doseIndex,itemIndex);
            if( doseIndex > index) {
              const id = element.id.split("-");
              this.doseItemDetails[doseIndex].doseTimeItems[itemIndex].id = id[0]+'-'+(Number(id[1]) - 1 );
            }
          });
        });
        console.log(this.doseItemDetails);
      }
    }
  }


  /**
   * Reset to Label
   */
  onClickReset(){
    this.doseItemDetails = [... this.originalDoseItemDetails];
  }
}
