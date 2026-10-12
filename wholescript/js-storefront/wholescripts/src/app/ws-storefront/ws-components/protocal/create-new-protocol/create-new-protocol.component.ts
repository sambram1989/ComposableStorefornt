import { Component, inject, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { Router } from '@angular/router';
import { CREATE_A_NEW_PROTOCOL, EDIT_PROTOCOL_NAME } from '../../../ws-constants/ws-api-endpoint.constant';

@Component({
  selector: 'ws-create-new-protocol',
  templateUrl: './create-new-protocol.component.html',
  styleUrl: './create-new-protocol.component.scss'
})
export class CreateNewProtocolComponent implements OnInit {
  @Input() typeOfProtocol: string = '';
  @Input() protocolName: string = '';
  @Input() protocolId: string = '';
  @Input() protocolDesc: string = '';
  
  private service = inject(ProtocolService);
  private router = inject(Router);
  hasDisableContinue: boolean = false;
  isNameTooLong: boolean = false;
  isDescTooLong: boolean = false;
  isNameEmpty = false;
  submitted = false;

  createProtocol = new FormGroup({
    name: new FormControl('', Validators.required),
    description: new FormControl(''),
    checkbox: new FormControl(false)
  });
  textBoxValue: string = '';
  constructor(public bsModalRef: BsModalRef) { }

  ngOnInit(): void {

    if (this.typeOfProtocol === 'DUPLICATE PROTOCOL') {
      this.textBoxValue = 'Copy Of ' + this.protocolName;
    } else {
      this.textBoxValue = this.protocolName;
    }
  }

  onSubmit() {
    this.submitted = true;

    const rawName = this.createProtocol.controls.name.value || '';
    const trimmedName = rawName.trim();
    const description = this.createProtocol.controls.description.value || '';
  
    const nameWithoutSpaces = rawName.replace(/\s+/g, '');
  
    this.isNameTooLong = nameWithoutSpaces.length > 50;
    this.isDescTooLong = description.length > 500;
    this.isNameEmpty = nameWithoutSpaces.length === 0;
  
    if (this.isNameTooLong || this.isDescTooLong || this.isNameEmpty || this.createProtocol.invalid) {
      this.createProtocol.markAllAsTouched(); // ensure error messages show
      return;
    }
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
