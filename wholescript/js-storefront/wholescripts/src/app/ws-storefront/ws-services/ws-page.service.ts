import { Injectable } from '@angular/core';
import { CmsBannerComponent, CmsService, Image } from '@spartacus/core';
import { slotMarkup } from '../ws-models/page.model';
import { map, Observable, of, shareReplay } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { ApiCall } from '../ws-components/ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
@Injectable({ providedIn: 'root' })
export class WsPage {
  user_id: string = '';
  constructor(
    private cmsService: CmsService,
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
  ) {}
    // TO-DO refactor changes
  getPage(componentData: slotMarkup, slotId?: string): Observable<slotMarkup> {
    let pageResponseSlotMarkup = new Observable<slotMarkup>();
    this.cmsService.getCurrentPage().subscribe((response: any) => {
      if (response?.slots) {
        for (var key in response.slots) {
          if (slotId === key) {
            response.slots[key].components.forEach((element: any) => {
              const id: string = element.uid;
              this.cmsService
                .getComponentData(id)
                .subscribe((comp: any) => componentData.components.push(comp));
            });
            componentData.slot = key;
          }
        }
      }
      pageResponseSlotMarkup = of(componentData);
    });
    return pageResponseSlotMarkup;
  }

  getImage(data: CmsBannerComponent): Image | null {
    if (!data || !data.media) {
      return null; 
    }
    return data.media as Image;
  }
  fetchWholesaleRecommendations(payload? : any): Observable<any> {
    let params = new HttpParams();
    Object.keys(payload).forEach((key: any) => {
      const value = payload[key as keyof ApiCall];
      const existing = (typeof value === 'string') ? (value.trim().length > 0) : true;
      if (existing) {
        params = (typeof value === 'string') ? params.append(key, value.trim()) : params.append(key, value);
      }
    });
    let url = `users/current/recommendation?${params}`;
    return this.httpClient.get(
      this.occEndPointService.buildUrl(url)
    );
  }
  getAllProducts() {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        'products/search?fields=products(code%2Cname%2Csummary%2Cconfigurable%2CconfiguratorType%2Cmultidimensional%2Cprice(FULL)%2Cimages(DEFAULT)%2Cstock(FULL)%2CaverageRating%2CvariantOptions)%2Cfacets%2Cbreadcrumbs%2Cpagination(DEFAULT)%2Csorts(DEFAULT)%2CfreeTextSearch%2CcurrentQuery%2CkeywordRedirectUrl&query=*%3Arelevance%3Aprice%3A%240-%2449.99&pageSize=12&lang=en&curr=USD'
      )
    );
  }
  getFilters(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl(`users/current/apiFilters`)).pipe(map(
      (response: any) => {
        return response.apiFilters
        .filter((item: any) => !item.title.toLowerCase().includes('discount'))
        .map(
          (item: any) => {
            item.config.isopen = item.config.isopen === 'true' ? true : false;
            item.config.viewMore = item.config.viewMore === 'true' ? true : false;
              return item;
          });
      }
    ), shareReplay(1));
  }
  getAdvertisingBrandsContent(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('topicPages/advertising?childTopic=new-brand-onboarding'))
  }
  getAdvertisingPromosContent(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('topicPages/advertising?childTopic=promo-banners'))
  }
  getApiOrderSubmissionContent(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('topicPages/api?childTopic=ExternalOrderSubmission'))
  }
  getApiEmrContent(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('topicPages/api?childTopic=EMRRecommendationServices'))
  }
  getWishlistEntries(): Observable<any> {
    return this.httpClient.get<any[]>(
      this.occEndPointService.buildUrl('productSearch',{
        queryParams: { query: 'FavouritesPage' },
      })
    ).pipe(shareReplay(1),
      map((data: any) => {
        let favoriteList: any= {
          wishLists: [{
            name: 'plpwishlist',
            wishListEntries: []
        }]
        };
        data?.products?.forEach((element: any) => {
          favoriteList.wishLists[0].wishListEntries.push({productCode: element?.code});
        });
        return favoriteList;
      })
    );
  }
  getSharedRewards(date: string) {
    return this.httpClient.post(this.occEndPointService.buildUrl(`users/current/get-shared-rewards?fields=DEFAULT&date=${date}`), {})
  }
}
