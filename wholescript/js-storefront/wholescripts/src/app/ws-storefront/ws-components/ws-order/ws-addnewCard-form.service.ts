import { Injectable } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";

@Injectable({providedIn:'root'})
export class WsAddNewCardService{

    addNewcard = new FormGroup({
        address:new FormControl('',Validators.required),
        firstName:new FormControl('',Validators.required),
        lastName:new FormControl('',Validators.required),
        email:new FormControl(''),
        streetAddress:new FormControl('',Validators.required),
        apartment:new FormControl(''),
        city:new FormControl('',Validators.required),
        country:new FormControl('',Validators.required),
        state:new FormControl('',Validators.required),
        postalCode:new FormControl('',Validators.required),
        phoneNo:new FormControl('')
    })
}