import { Component, ChangeDetectionStrategy, Input ,EventEmitter , Output, SimpleChanges ,HostBinding, ViewChildren, QueryList, ElementRef, ViewChild, Optional, inject, ChangeDetectorRef } from "@angular/core";
import { Facet, FeatureConfigService, FacetValue } from "@spartacus/core";
import { FacetCollapseState, ICON_TYPE, FocusDirective } from "@spartacus/storefront";
import { exists } from "i18next";
import { Observable } from "rxjs";
import { Router } from "@angular/router";

@Component({
  selector: 'ws-custom-medpax-buy-again-facet',
  templateUrl: './custom-medpax-buy-again-facet.component.html',
  styleUrl: './custom-medpax-buy-again-facet.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CustomMedpaxBuyAgainFacetComponent {
  @Input() buyAgainFacets: any;
  @Input() savedFacet: any;
  @Input() savedActiveFacet: any;
  @Input() savedEvents: any;
  @Input() closeIcon = ICON_TYPE.CLOSE;
  state$: Observable<FacetCollapseState>;

  facetArray:any = [];
  activeFacetArray:any = [];

  selectedFacetArray:any = [];
  @Output() clearAllFilters = new EventEmitter<any>();
  @Output() selectedFacetOutput = new EventEmitter<any>();
  @Output() selectedActiveFacetOutput = new EventEmitter<any>();

  constructor(protected router: Router){
  }
  ngOnInit() {
    Object?.entries(this.buyAgainFacets).forEach(([key, value]) => {
      this.facetArray.push({name:key , values:value});
    });
    this.activeFacetArray = this.savedActiveFacet;
    if(this.activeFacetArray.length == 0 ) {
      this.resetFilters();
    }
  }

  ngAfterViewInit() {
    if(this.savedFacet?.length) {
      this.loadSelectedFacets();
    }
  }

  loadSelectedFacets () {    
    this.savedFacet?.map((facet:any , facetIndex:number) => {
      document.querySelectorAll('.buy-again-facet-values a')?.forEach((e:any) => {
        facet?.values?.map((filter:any , filterIndex:number) => {
          let className = (facet?.name +"_"+filter);
          try {
            if(e?.classList.contains(className)) {
              e?.classList?.add('value','selected');
            };
          } catch(e) {
            console.log(e)
          }
        })
      });
    });
  }

  resetFilters() {
    this.clearAllFilters.emit();
    //remove all active facets
    this.activeFacetArray = [];
    //remove all selected from lists
    const getFiltersList = document.querySelectorAll('.buy-again-facet-values > a');
    getFiltersList.forEach(e => {
      e.classList.remove('selected');
    });
  }

  clickOnFilterCheckbox(event?:any , facetName?:string , filterCode?: string) {
    
    if(event.target.classList.contains('value') && event.target.classList.contains('selected')) {
      //this means removing already selected filter
      event.target.classList.remove('value','selected');
      let checkIfFacetExists = this.savedFacet.some((e?:any) => (e?.name?.toLowerCase() == facetName?.toLowerCase()));
      if(checkIfFacetExists) {
        this.savedFacet.map((e?:any , i?: number) => {
          if(e?.name?.toLowerCase() == facetName?.toLowerCase()) {
            //Remove any filter selected
            const index = e?.values?.indexOf(filterCode);
            if (index > -1) { // only splice array when item is found
              e?.values?.splice(index, 1); // 2nd parameter means remove one item only
            }

            //Remove the facet from the array if it has no values
            if(e?.values?.length==0){
              this.savedFacet.splice(i, 1); 
            }
          }
        });
      }
      this.updateActiveFacets(event , filterCode , true);
    }
    else {
      //this means selecting a filter
      event.target.classList.add('value','selected');
      let checkIfFacetExists = this.savedFacet.some((e?:any) => (e.name.toLowerCase() == facetName.toLowerCase()));
      if(checkIfFacetExists) {
        this.savedFacet.map((e?:any) => {
          if(e.name.toLowerCase() == facetName.toLowerCase()) e.values.push(filterCode);
        });
      } else {
        this.savedFacet.push({name:facetName , values:[filterCode]})
      }
      this.updateActiveFacets(event , filterCode , false);
    }
    this.selectedFacetOutput.emit(this.savedFacet);
  }

  updateActiveFacets(event:any , filterCode:any , isRemovable:boolean) {
    const getFilterName = event?.target?.innerText;
    if(isRemovable) {
      this.activeFacetRemove(getFilterName)
    }  else {
      if(this.activeFacetArray.indexOf(getFilterName)<0) this.activeFacetArray.push(getFilterName);
    }
    this.selectedActiveFacetOutput.emit(this.activeFacetArray);
  }
  
  activeFacetRemove(activeFacet:any) {
    const index = this.activeFacetArray.indexOf(activeFacet);
      if (index > -1) { // only splice array when item is found
        this.activeFacetArray.splice(index, 1); // 2nd parameter means remove one item only
      }
  }
  
  activeFacetRemoveByIcon(activeFacet:any) {
    this?.activeFacetRemove(activeFacet); //remove fron the top selected lines

    // Also remove from the filter lists , hence below code
    const getFiltersList = document.querySelectorAll('.buy-again-facet-values > a');
    getFiltersList.forEach(e => {
      if(e?.textContent == activeFacet) { 
        e.classList.remove('selected');
      }
    });
  }

}
