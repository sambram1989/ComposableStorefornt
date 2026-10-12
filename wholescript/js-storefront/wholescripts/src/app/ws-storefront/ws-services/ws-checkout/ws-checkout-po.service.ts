import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WsPurchaseOrderService {
  private purchaseOrderNumberSubject = new BehaviorSubject<string>('');
  purchaseOrderNumber$ = this.purchaseOrderNumberSubject.asObservable();

  setPurchaseOrderNumber(purchaseOrderNumber: string): void {
    this.purchaseOrderNumberSubject.next(purchaseOrderNumber);
  }
}