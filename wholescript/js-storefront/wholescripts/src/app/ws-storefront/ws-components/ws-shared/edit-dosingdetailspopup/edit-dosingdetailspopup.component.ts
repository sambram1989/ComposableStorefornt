import { Component, inject, Input, signal, input, DestroyRef, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { Observable, tap, shareReplay, Subject } from 'rxjs';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
   selector: 'ws-edit-dosingdetailspopup',
   templateUrl: './edit-dosingdetailspopup.component.html',
   styleUrl: './edit-dosingdetailspopup.component.scss'
})
export class EditDosingdetailspopupComponent {
   private protocolService = inject(ProtocolService);
   private eventService = inject(WsCustomMedPaxEvents);
   private destroyRef = inject(DestroyRef);
   private cd = inject(ChangeDetectorRef);

   savedAsCustomSignal = input<boolean>(false);
   @Input() wrapperForm: FormGroup;
   @Input() currentProduct: any;
   @Input() currentMedPaxProduct: any;
   @Output() emitResetEvent = new EventEmitter<any>();
   @Output() emitResetCustomEvent = new EventEmitter<any>();
   savedCustomSignal: boolean;
   MAX_QTY = 10;

   private _productCode: string;

   @Input()
   set productCode(value: string) {
      this._productCode = value;
      this.onProductCodeChange();
   }

   get productCode(): string {
      return this._productCode;
   }

   private onProductCodeChange() {
      this.getCustomDosing();
   }


   DisplayItemTimes: any[] = [];
   selectedTimes: string[] = [''];
   customDosingdData: any;

   customDosingList = signal<any>({});

   fetchCustomDosing$:Subject<any> = new Subject();
   ngOnInit() {
      this.customEventListeners();
      this.protocolService.fetchIcons().
      subscribe((res: any) => {
         this.DisplayItemTimes = res.itemTimeListWsDTO
            .filter((data: any) => data.isDisplayable)
            .sort((a: any, b: any) => a.displayOrder - b.displayOrder)
            .map((data: any) => {
               return {
                  code: data?.code,
                  src: this.protocolService.getBaseUrl() + data?.iconImage?.url,
                  alt: data?.timeOfTheDay
               }
            });
         this.cd.detectChanges();
      })
   }
   getCustomDosing(onSaveAsCustom=false) {
       this.protocolService.getCustomDosingEntries(this.productCode)
      .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
          next:(res:any)=> {
            this.customDosingdData = res;
            this.customDosingList.set(this.customDosingdData);
            if(onSaveAsCustom){
               this.updateFormWithCustomDosing('saveCustom');
            } 
            else{
               this.resetToCustomDosing();
            } 
            this.fetchCustomDosing$.next(res);
          } 
         });
   }

   resetToCustomDosing() {
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      let isLabelDosing = false;
      this.removeAllindexesInFormArray();
      if (this.customDosingdData.dosingList?.length) {
         this.removeInputControl(0);
      }
      //Updating the values Of the form Array itemTimes
      if (this.currentMedPaxProduct && this.currentMedPaxProduct?.dosing?.length) {
         this.removeInputControl(0);
         this.currentMedPaxProduct.dosing?.forEach((doseData: any, index: number) => {
            if (index <= 3) {
               hobbiesArray.push(this.createHobbyGroup({ qty: doseData?.quantity || 1, radio: doseData?.itemTimes?.code, id: doseData?.id }));
               this.onRadioChange(index);
            }
         });//forEach
      } else {
         if (this.customDosingdData?.dosingList?.length) {
            this.updateFormWithCustomDosing('saveCustom');
         } else {
            isLabelDosing = true;
         }

      }
      if (isLabelDosing) {
         this.updateLabelDosing(this.currentProduct?.prod);
      }
      this.customDosingList.set(this.customDosingdData);
   }

   updateFormWithCustomDosing(custom: string) {
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      hobbiesArray.clear();
      this.customDosingdData.dosingList?.forEach((doseData: any, index: number) => {
         if (index <= 3) {
            hobbiesArray.push(this.createHobbyGroup({ qty: doseData?.quantity || 1, radio: doseData?.itemTimes?.code, id: doseData?.id }));
            this.onRadioChange(index);
         }
      });//forEach
      if(custom === 'resetCustom') {
         this.emitResetCustomEvent.emit();
         this.savedCustomSignal = false;
      }
   }
   removeAllindexesInFormArray() {
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      let length = hobbiesArray.length;
      for (let i = length; i > 0; i--) {
         this.removeInputControl(i);
      }
   }

   createHobbyGroup(hobby?: { qty: number; radio: string, id: string }): FormGroup {
      return new FormGroup({
         qty: new FormControl(hobby?.qty || 1, [Validators.required, Validators.min(1)]),
         radio: new FormControl(hobby?.radio || '', Validators.required),
         id: new FormControl(hobby?.id || '')
      });
   }

   get hobbies(): FormArray {
      return this.wrapperForm.get('hobbiesArray') as FormArray;
   }

   addInputControl() {
      this.hobbies.push(this.createHobbyGroup());
      this.selectedTimes.push('');
   }

   removeInputControl(idx: number) {
      this.hobbies.removeAt(idx);
      this.selectedTimes.splice(idx, 1);
   }

   onRadioChange(idx: number, time?: any) {
      const selectedValue = time? time.code : this.hobbies.at(idx).get('radio')?.value;
      this.selectedTimes[idx] = selectedValue;
      this.hobbies.at(idx).get('radio')?.setValue(selectedValue);
      this.hobbies.controls?.forEach((control, index) => {
        if (index !== idx) {
          this.updateAvailableTimes(index);
        }
      });
    }

   updateAvailableTimes(index: number) {
      const availableTimes = this.DisplayItemTimes.map(item => item.code);
      this.selectedTimes.forEach((time, idx) => {
         if (idx !== index && time) {
            const disableIndex = availableTimes.indexOf(time);
            if (disableIndex !== -1) {
               availableTimes.splice(disableIndex, 1);
            }
         }
      });
      const radioControl = this.hobbies.at(index).get('radio');
      radioControl.updateValueAndValidity();
   }

   isRadioEnabled(index: number, time: string): boolean {
      return !this.selectedTimes.some((selected, idx) => selected === time && idx !== index);
   }

   incrementQty(index: number) {
      const qtyControl = this.hobbies.at(index).get('qty');
      const currentValue = Number(qtyControl?.value || 0);
      if (currentValue < 10) {
        qtyControl?.setValue(currentValue + 1);
      }
   }

   decrementQty(index: number) {
      const qtyControl = this.hobbies.at(index).get('qty');
      if (Number(qtyControl.value) > 1) {
         qtyControl.setValue(Number(qtyControl.value) - 1);
      }
   }

   allowOnly1to9(event: KeyboardEvent) {
      const allowedKeys = ['Tab', 'ArrowLeft', 'ArrowRight', 'Delete'];
      const input = event.target as HTMLInputElement;
      const currentValue = input.value;
      const isNumberKey = /^[0-9]$/.test(event.key);
      const isAllowedKey = allowedKeys.includes(event.key);
    
      if (isAllowedKey) return;
    
      if (event.key === 'Backspace') {
        event.preventDefault();
        input.value = '1';
        input.dispatchEvent(new Event('input'));
        return;
      }
    
      if (!isNumberKey) {
        event.preventDefault();
        return;
      }
    
      const start = input.selectionStart!;
      const end = input.selectionEnd!;
      const newValue = currentValue.slice(0, start) + event.key + currentValue.slice(end);
      const numericValue = parseInt(newValue, 10);
    
      const isReplacing = start === 0 && end === currentValue.length;
    
      if (isReplacing || currentValue === '') {
        if (numericValue >= 1 && numericValue <= 10) {
          return;
        } else {
          event.preventDefault();
          input.value = '10';
          input.dispatchEvent(new Event('input'));
          return;
        }
      }
    
      if (numericValue > this.MAX_QTY || numericValue < 1) {
        event.preventDefault();
        return;
      }
      const numericCurrent = parseInt(currentValue, 10);
      if (!isNaN(numericCurrent) && numericCurrent >= 1 && numericCurrent <= 9) {
        event.preventDefault();
        input.value = '10';
        input.dispatchEvent(new Event('input'));
      }
   }

   getSelectedRadio(idx: number): string {
      return this.selectedTimes[idx] || '';
   }

   saveEditDosing() {
      this.eventService.emitSaveAsCustom();
      this.savedCustomSignal = true;
   }

   updateLabelDosing(product: any,reset=false) {
      const labelDosing = [
         ...((+product?.inAM || 0) > 0 ? [{ key: 'inAm', qty: product.inAm, code: 'AM' }] : []),
         ...((+product?.inPM || 0) > 0 ? [{ key: 'inPm', qty: product.inPm, code: 'PM' }] : []),
         ...((+product?.inDaily || 0) > 0 ? [{ key: 'inDaily', qty: product.inDaily, code: 'Daily' }] : []),
         ...((+product?.inBedtime || 0) > 0 ? [{ key: 'inBedtime', qty: product.inBedtime, code: 'Bedtime' }] : []),
      ];
      if(reset) this.emitResetEvent.emit();
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      hobbiesArray.clear();
      labelDosing.forEach((doseData: any, index: number) => {
         hobbiesArray.push(this.createHobbyGroup({
            qty: doseData.qty || 1,
            radio: doseData.code,
            id: product.id
         }));

         this.onRadioChange(index);
      });
      if (!hobbiesArray.length) {
         this.addInputControl();
      }
      this.savedCustomSignal = false;
   }

   updateCustomDosing(custom: string) {
      this.updateFormWithCustomDosing(custom);
   }

   customEventListeners() {
      this.eventService.customDoesSaved.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
         next: () => this.getCustomDosing(true)
      })
   }
}
