import { Injectable, signal } from "@angular/core";

@Injectable({providedIn:'root'})
export class WsLeftFilterFacet{
   public clearFilter = signal<boolean>(false);
}