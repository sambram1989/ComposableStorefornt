import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({providedIn:'root'})
export class WsCommonFacetService{
   public currentSearchList$ = new BehaviorSubject<any>({});
}