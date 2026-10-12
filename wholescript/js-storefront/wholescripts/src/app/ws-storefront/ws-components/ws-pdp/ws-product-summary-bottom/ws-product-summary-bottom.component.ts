import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { CurrentProductService } from '@spartacus/storefront';
import { Observable, switchMap, tap } from 'rxjs';
import { PdpDataservice } from '../data-pdp/pdpData.service';
import { AuthService } from '@spartacus/core';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { userMap } from '../../../ws-env/environment';
import { Router } from '@angular/router';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';

@Component({
  selector: 'ws-ws-product-summary-bottom',
  templateUrl: './ws-product-summary-bottom.component.html',
  styleUrl: './ws-product-summary-bottom.component.scss'
})
export class WsProductSummaryBottomComponent implements OnInit {
private customMedPaxService = inject(WsCustomMedPaxService);
private router = inject(Router);
private currentuser = inject(WsCurrentUserService);
private destroyRef = inject(DestroyRef);

  otherAvailableProducts: any[] = [];
  longDescription: any[] = [];
  clinicalApplicationsList: string[] = [];
  show = false;
  product$: Observable<any>;
  public dataService = inject(PdpDataservice);
  curUser$: Observable<any>;
  isLoggedIn$: Observable<boolean>;
  isPractioner: boolean;
  canShowMedPax:boolean;

  constructor(
    private currentProductService: CurrentProductService,
    private authService: AuthService,
    private recommendationService: RecommendationService
  ) { 
    this.isLoggedIn$ = authService.isUserLoggedIn();
  }

  ngOnInit(): void {
    this.product$ = this.currentProductService.getProduct().pipe(
      tap((res: any) => {
        this.otherAvailableProducts = res?.otherAvailableProducts || [];
        const relevantClassification = res?.classifications?.find((classification: any) =>
          classification.code === "pimClassificationDataClass"
        );
        if (relevantClassification) {
          const requiredCodes = [
            "PIMClassification/1.0/pimClassificationDataClass.longdescription",
          ];
          this.longDescription = relevantClassification?.features?.filter((feature: any) =>
            requiredCodes?.includes(feature.code)
          );

          // filetr clinical applications data
          const clinicalApplications = relevantClassification?.features?.find((feature: any) =>
            feature.name === 'Clinical Applications'
          );
          if (clinicalApplications && clinicalApplications?.featureValues) {
            const value = clinicalApplications?.featureValues[0]?.value;
            this.clinicalApplicationsList = value.split('*').map((item: any) => item.trim());
          }
        }
      })
    );
    this.curUser$ = this.recommendationService.getUserDetails().pipe(
      tap((res:any) => {
        const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
        this.isPractioner = (res?.roles || []).includes(key);
      })
    );
    this.IsUserIsaPractitioner();
  }

  goToMedPax(product:any){
   const queryString = product?.productUrl.replace('?q=', '');
   this.router.navigate(['/medpax'],{ queryParams: { query: queryString } });
   }
   private IsUserIsaPractitioner() {
    let isPatient = false;
    this.currentuser.isRoleExist('patient').pipe(
      switchMap((user: boolean) => {
        isPatient = user;
        return this.authService.isUserLoggedIn();
      })
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: boolean) => this.canShowMedPax = (res && !isPatient));
  }

  canShowAlsoAvailableInTag(otherAvailableProducts:any[]):boolean{
   if(this.canShowMedPax){
      return otherAvailableProducts?.length == 0 ;
   }else{
    return otherAvailableProducts?.filter((item:any)=> item?.productDisplyName?.toLowerCase() !== 'medpax')?.length > 1;
   }
  }

}
