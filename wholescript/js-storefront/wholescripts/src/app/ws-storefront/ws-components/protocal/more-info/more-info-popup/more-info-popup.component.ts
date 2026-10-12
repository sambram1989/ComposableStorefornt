import { Component, inject, Input } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ProtocolService } from '../../../../ws-services/protocol/protocol.service';
import { CREATE_A_NEW_PROTOCOL, EDIT_PROTOCOL_NAME } from '../../../../ws-constants/ws-api-endpoint.constant';

@Component({
  selector: 'ws-more-info-popup',
  templateUrl: './more-info-popup.component.html',
  styleUrl: './more-info-popup.component.scss'
})
export class MoreInfoPopupComponent {
  @Input() typeOfProtocol: string = '';
  @Input() protocolName: string = '';
  @Input() protocolId: string = '';

  hasDisableContinue: boolean = false;

  createProtocol = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl(''),
    checkbox: new FormControl(false)
  });
  textBoxValue: string = '';
  constructor(public bsModalRef: BsModalRef, private router: Router, private service: ProtocolService) { }

  ngOnInit(): void {

    if (this.typeOfProtocol === 'DUPLICATE PROTOCOL') {
      this.textBoxValue = 'Copy Of ' + this.protocolName;
    } else {
      this.textBoxValue = this.protocolName;
    }
  }

  ngOnChanges() {

  }
  onSubmit() {
    let payLoad = {
      name: this.createProtocol.controls.name.value,
      description: this.createProtocol.controls.description.value
    }
    let edit: boolean = this.typeOfProtocol.toLowerCase().includes('edit');
    this.service.createNewProtocol(payLoad, edit ? 'patch' : 'post', edit ? EDIT_PROTOCOL_NAME(this.protocolId) : CREATE_A_NEW_PROTOCOL).subscribe((res: any) => {
      this.router.navigate([`protocol/edit/${res.id}`]);
      this.bsModalRef.hide();
    });

  }
}
