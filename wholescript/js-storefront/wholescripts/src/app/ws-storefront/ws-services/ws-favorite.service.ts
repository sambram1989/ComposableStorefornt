import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { BehaviorSubject, map, Observable, shareReplay, tap } from 'rxjs';
import { PlpService } from './ws-plp.service';



@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private favoriteItems = new BehaviorSubject<any>({});
  favoriteItems$ = this.favoriteItems.asObservable();
  private allproductSubject = new BehaviorSubject({});
  public addMedpax = new BehaviorSubject<any>('');
  public allproducts$ = this.allproductSubject.asObservable();

  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
    private plpService: PlpService,
  ) {}

  getFavoriteItems(): Observable<any[]> {
    return this.favoriteItems.asObservable();
  }

  addToFavorites(productCode: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(`users/current/favorites?productCode=${productCode}`),
      {}
    ).pipe(
      map((data) => {
        return data;
      }),
      tap(() => {
        this.fetchFavorites().subscribe();
      })
    );
  }

  removeFromFavorites(productCode: any) {
    return this.httpClient.delete(
      this.occEndPointService.buildUrl(`users/current/removeFavoriteEntry?productCode=${productCode}`),
      {}
    ).pipe(
      map((data) => {
        return data;
      }),
      tap(() => {
        this.fetchFavorites().subscribe();
      })
    );
  }

  fetchFavorites(): Observable<any[]> {
    return this.httpClient.post<any[]>(
      this.occEndPointService.buildUrl('users/current/getAllWishList'),
      {}
    ).pipe(
      map((data: any) => {
        let favoriteList: any= {
          wishLists: [{
            name: 'plpwishlist',
            wishListEntries: data.wishLists.flatMap((wl: any) => wl.wishListEntries.map((entry: any) => {
              return {productCode: entry.productCode, isMedpax: wl.name.includes('medpax')}
            }))
        }]
        };
        this.favoriteItems.next(favoriteList);
        return favoriteList;
      })
    );
  }

  getSelectedItems(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('productSearch',{
        queryParams: { query: 'FavouritesPage' },
      })
    );
  }

  getAllProducts(payload?: any): Observable<any> {
    let params = new HttpParams();
    Object.keys(payload).forEach((key: any) => {
      const value = payload[key];
      const existing = (typeof value === 'string') ? (value?.trim().length > 0) : (typeof value === 'boolean') ? true : (value > 0);
      if (existing) {
        params = (typeof value === 'string') ? params.append(key, value.trim()) : params.append(key, value);
      }
    });
    let url = `products/search?${params}`.replace(/\%2B/g,"+").replace(/\%252F/g,"/")
    .replace(/%25C2%25AE/g, '®').replace(/%25E2%2584%25A2/g, '™').replace(/%25E2%2580%2599/g, '’')
    .replaceAll('+%2526+', ' & ').replace(/%2527/g,"'");
    const apiData$ =  this.httpClient.get(this.occEndPointService.buildUrl(url)).pipe(
      tap((response) => { 
        return this.allproductSubject.next(response);
      })
    );
    apiData$.subscribe();
    return this.allproducts$;
    
  }


  filterData(query: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('productSearch',{
        queryParams: { query },
      })
    );
  }
}
