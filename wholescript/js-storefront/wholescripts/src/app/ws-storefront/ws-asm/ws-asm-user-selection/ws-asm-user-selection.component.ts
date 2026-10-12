import { Component } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { CustomerSelectionComponent } from '@spartacus/asm/components';
import { AsmService } from '@spartacus/asm/core';
import { AsmConfig } from '@spartacus/asm/root';
import { User } from '@spartacus/core';
import { DirectionService, LaunchDialogService } from '@spartacus/storefront';

@Component({
  selector: 'ws-customer-selection',
  templateUrl: './ws-asm-user-selection.component.html'
})
export class WsAsmCustomerSelectionComponent extends CustomerSelectionComponent {
  
    constructor(
        protected override fb: UntypedFormBuilder,
        protected override asmService: AsmService,
        protected override config: AsmConfig,
        protected override directionService: DirectionService,
        protected override launchDialogService: LaunchDialogService
      ) {
        super(fb, asmService, config, directionService, launchDialogService);
      }

      /**
   * set focus on previous searh result item.  If no previous item then go to end of item.
   * @param event keyboard event
   */
  override focusPreviousChild(event: any): void {
    event.preventDefault();
    this.activeFocusedButtonIndex--;
    if (this.activeFocusedButtonIndex < 0) {
      this.activeFocusedButtonIndex = this.searchResultItems.length - 1;
    }
    this.updateItemIndex(this.activeFocusedButtonIndex);
  }
  /**
   * set focus on next searh result item.  if no next item then go to the first item
   * @param event keyboard event
   */
  override focusNextChild(event: any): void {
    event.preventDefault();
    this.activeFocusedButtonIndex++;
    if (this.activeFocusedButtonIndex > this.searchResultItems.length - 1) {
      this.activeFocusedButtonIndex = 0;
    }
    this.updateItemIndex(this.activeFocusedButtonIndex);
  }
  /**
   * set focus to input search text
   * @param event keyboard event
   */
  override focusInputText(event: any): void {
    event.preventDefault();
    this.activeFocusedButtonIndex = -1;
    this.searchTerm.nativeElement.focus();
    if (this.searchTerm.nativeElement.value?.length) {
      let selectionPos = this.searchTerm.nativeElement.selectionEnd;
      const searchTermLength = this.searchTerm.nativeElement.value.length;

      if (this.isBackNavigation(event)) {
        selectionPos = selectionPos <= 0 ? 0 : selectionPos - 1;
      } else if (this.isForwardsNavigation(event)) {
        selectionPos =
          selectionPos >= searchTermLength
            ? searchTermLength
            : selectionPos + 1;
      } else if (event.code === 'Home') {
        selectionPos = 0;
      } else if (event.code === 'End') {
        selectionPos = searchTermLength;
      }
      this.searchTerm.nativeElement.selectionStart = selectionPos;
      this.searchTerm.nativeElement.selectionEnd = selectionPos;
    }
  }

  override selectCustomerFromList(event: any, customer: User) {
    this.selectedCustomer = customer;
    this.customerSelectionForm.controls['searchTerm'].setValue(
      this.selectedCustomer.name
    );
    this.asmService.customerSearchReset();
    this.searchTerm.nativeElement.focus();
    event.preventDefault();
    event.stopPropagation();
  }
  
  override closeResults(event: any) {
    this.asmService.customerSearchReset();
    this.searchTerm.nativeElement.focus();
    event.preventDefault();
    event.stopPropagation();
  }

   /**
   * set focus to the first searched item
   * @param event keyboard event
   */
   override focusFirstItem(event: any): void {
    event.preventDefault();
    this.activeFocusedButtonIndex = 0;
    this.updateItemIndex(this.activeFocusedButtonIndex);
  }

  override setSelectionEnd(event: any): void {
    event.preventDefault();
    if (this.searchTerm.nativeElement.value?.length) {
      const selectionStart = this.searchTerm.nativeElement.value.length;
      this.searchTerm.nativeElement.selectionStart = selectionStart;
      this.searchTerm.nativeElement.selectionEnd = selectionStart;
    }
  }

  override onSubmit(): void {
    if (this.customerSelectionForm.valid && !!this.selectedCustomer) {
      sessionStorage.setItem('user_config', JSON.stringify(this.selectedCustomer));
      this.submitEvent.emit({ customerId: this.selectedCustomer.customerId });
    } else {
      this.customerSelectionForm.markAllAsTouched();
    }
  }
}

