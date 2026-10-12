import { Component, inject, Input, input, signal, SimpleChanges, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { Location } from '@angular/common';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'ws-med-pax-products-right-section',
  templateUrl: './med-pax-products-right-section.component.html',
  styleUrl: './med-pax-products-right-section.component.scss'
})
export class MedPaxProductsRightSectionComponent {
  
  private overlay = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  private wsEventService = inject(WsCustomMedPaxEvents);
  private customMedPaxService = inject(WsCustomMedPaxService);
  public location = inject(Location);

  products = input<any>();
  medPaxNote = signal<boolean>(false);
  medPaxNoteSectionToggler = signal<boolean>(false);
  @Input() isPlUser = false;
  @Input() medPaxId = ''
  @Input() medPaxNotes = '';
  productCodes: string[] = [];
  productName: string[] = [];
  alerts: { type: string; msg: string; timeout: number; }[] = [];
  @ViewChild('Supplements') supplementsRef: TemplateRef<any>;
  supplementPortal: any;
  metPaxNotes = new FormControl('');

  ngOnChanges(changes:SimpleChanges) {
    if(changes){
      this.metPaxNotes.setValue(this.medPaxNotes ? this.medPaxNotes : '');
      this.medPaxNoteSectionToggler.set(this.medPaxNotes?.trim()?.length ? true : false);
    }
    this.extractProductDetails();
  }

  ngAfterViewInit() {
    this.supplementPortal = new TemplatePortal(this.supplementsRef, this.vcr);
  }

  editDosing(Product: any) {
    this.wsEventService.emitEditClick({ prod: Product.product, code: Product?.product?.code, isMedPaxEntry: true });
  }

  removeProduct(Product: any, entryNumber: string) {
    this.wsEventService.emitDeleteClick({ EntryNumber: entryNumber, product: Product });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  extractProductDetails() {
    const medpaxEntries = this.products()?.medpaxEntry || [];
    this.productCodes = [];
    this.productName = [];
    medpaxEntries.forEach((entry: any) => {
      this.productCodes.push(entry.product?.code);
      this.productName.push(entry.product?.name);
    });
  }

  openSupplementsPopup() {
    this.overlay.openModal(this.supplementPortal, '50%');
  }

  createMedpaxNotes(isDelete: boolean) {
    if (isDelete) this.metPaxNotes.setValue('');
    const payLoad = {
      notes: this.metPaxNotes?.value?.trim(),
      code: this.medPaxId
    }
    this.customMedPaxService.createMedPaxNotes(payLoad).subscribe((res:any) => {
      this.metPaxNotes.setValue(res?.notes?.trim() ? res?.notes : '');
      this.medPaxNoteSectionToggler.set(res?.notes?.trim()?.length ? true : false);
      this.medPaxNote.set(false);
      this.alerts.push({
        type: 'info',
        msg: `Medpax note saved`,
        timeout: 2000,
      });
    });
  }

  deleteAllEntriesOfMedPax() {
    this.wsEventService.emitDeleteEntireMedPax();
  }

}
