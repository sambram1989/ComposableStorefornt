import { EventEmitter, Injectable, signal } from "@angular/core";

@Injectable({providedIn:'root'})
export class WsCustomMedPaxEvents{

    editButton = new EventEmitter<any>();
    addButton = new EventEmitter<any>();
    deleteButton = new EventEmitter<any>();
    editNameButton = new EventEmitter<any>();
    saveAsCustom = new EventEmitter<any>();
    customDoesSaved = new EventEmitter<any>();
    deleteEntireMedPax = new EventEmitter<any>();
    enableButton = new EventEmitter<any>(false);

    forceRefresh = signal<string>('');

    emitEditClick(data:any) {
        this.editButton.emit(data);
    }

    emitAddClick(data:any){
        this.addButton.emit(data);
    }

    emitDeleteClick(data:any){
        this.deleteButton.emit(data);
    }
    
    emitButtonEnable(data:any = false){
        this.enableButton.emit(data);
    }

    emitEditNameClick(data:any){
        this.editNameButton.emit(data);
    }

    emitSaveAsCustom(){
        this.saveAsCustom.emit();
    }

    emitcustomDoesSaved(ProductCode:string){
        this.forceRefresh.set(ProductCode)
        this.customDoesSaved.emit();
    }

    emitDeleteEntireMedPax(){
        this.deleteEntireMedPax.emit();
    }

}