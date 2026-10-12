import { Location } from '@angular/common';
import { Component, DestroyRef, inject, input ,Pipe, PipeTransform} from '@angular/core';
import { catchError, of, switchMap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { currentState } from '../../../ws-services/state-services/currentuser.service';
import { medPaxPacketEnum } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';

const medPaxEnum: Record<string, string> = medPaxPacketEnum.reduce((acc, entry) => {
  const key = Object.keys(entry)[0] as keyof typeof entry; 
  acc[key] = entry[key] as string;  
  return acc;
}, {} as Record<string, string>);


@Component({
  selector: 'ws-medpax-entry-cart',
  templateUrl: './medpax-entry-cart.component.html',
  styleUrl: './medpax-entry-cart.component.scss'
})
export class MedpaxEntryCartComponent {
  medPaxEnumValue = medPaxEnum;
  medpaxItem = input<any>();
  medPaxID = input<string>();
  public location = inject(Location);
  public currentUser$ = inject(currentState);
  public wsCartEvents = inject(WsCartEvents);
  public destroyRef = inject(DestroyRef);
  public activeCartService = inject(ActiveCartFacade);
  private eventService = inject(WsCustomMedPaxEvents);

  removeMedpaxEntryFromCart(medPaxEntryNumber: any,totalEntries:any, itemTimesCode:string) {
    this.wsCartEvents.emitOpenRemoveProductPopupClick(medPaxEntryNumber);
    this.wsCartEvents.remove$.subscribe(() => this.removeMedPaxEntry(medPaxEntryNumber,totalEntries, itemTimesCode));
  }

  removeMedPaxEntry(medPaxEntryNumber: any,totalEntries:any, itemTimesCode:string) {
    this.eventService.emitDeleteClick({EntryNumber:medPaxEntryNumber.entryNumber,product:medPaxEntryNumber.product});
    this.wsCartEvents.isLoading.set(true);
    const entryNumber = totalEntries.find((rowItem: any) =>
      rowItem?.entries?.some((lineItem: any) =>
        lineItem?.product?.code === medPaxEntryNumber?.product?.code && lineItem?.medPaxEntryNumber
      )
    )?.entries.find((lineItem: any) =>
      lineItem?.product?.code === medPaxEntryNumber?.product?.code && lineItem?.medPaxEntryNumber
    )?.medPaxEntryNumber;

    this.activeCartService.takeActiveCartId().pipe(
      switchMap((cartId:any)=>  this.wsCartEvents.removeMedpaxFormulaEntries(cartId,entryNumber,this.medPaxID(),this.getItemTimesCode(itemTimesCode))),
      catchError(()=>{this.wsCartEvents.isLoading.set(false); return of({})})
    ).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {this.activeCartService.reloadActiveCart(); } 
      })
  }

  getItemTimesCode(itemCode:any){
    return this.medPaxEnumValue[itemCode.toLowerCase()];
  }

}



@Pipe({
  name: 'totalEntriesCheck',
  pure: true, 
})
export class TotalEntriesCheckPipe implements PipeTransform {
  transform(medpaxitem: any[]): boolean {
    if (!Array.isArray(medpaxitem)) return false; 

    const count = medpaxitem.reduce((acc, group) => {
      return acc + group.entries.filter((entry:any) => entry.medPaxEntryNumber).length;
  }, 0);
  
    return count > 3;
  }
}

