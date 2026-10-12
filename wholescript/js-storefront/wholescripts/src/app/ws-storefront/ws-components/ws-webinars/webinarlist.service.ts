import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService, SortModel } from '@spartacus/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import {
  EDIT_PROTOCOL_LIST_API,
  PRESENTOR_DETAILS,
  PRESENTOR_LIST,
  WEBINAR_DETAILS,
  WEBINAR_SEARCH,
  WEBINAR_SEARCH_GET_FEATURED,
  WEBINAR_WATCH_STATUS
} from '../../ws-constants/ws-api-endpoint.constant';
import { ActivatedRoute, Router } from '@angular/router';


// customised OOTB SearchCriteria as we changed sortCode to sort

export interface SearchCriteria {
  /**
   * Contains the search text that is used to query the backend API.
   */
  query?: string;
  /**
   * The current page of the search result.
   */
  currentPage?: number;
  /**
   * The number of items in the search results.
   */
  pageSize?: number;
  /**
   * The sort code that is used to sort the items in the search result.
   */
  sort?: string;
}

@Injectable({
  providedIn: 'root',
})
export class WebinarlistService {
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  private presenterSubject = new BehaviorSubject('');
  public presenter$ = this.presenterSubject.asObservable();

  private webinarDetaisSubject = new BehaviorSubject('');
  public webinarDetails$ = this.webinarDetaisSubject.asObservable();

  fetchWebinars(url?: string): Observable<any> {
    return this.http.get(
      this.occ.buildUrl(url ? WEBINAR_SEARCH + url : WEBINAR_SEARCH)
    );
  }

  fetchFeaturedWebinars() {
    return this.http.get(this.occ.buildUrl(WEBINAR_SEARCH_GET_FEATURED));
  }

  fetchWebinarDetails(webinarId: string) {
    return this.http.get(this.occ.buildUrl(WEBINAR_DETAILS(webinarId)))
    .pipe(
      tap((response: any) => {
        return this.webinarDetaisSubject.next(response?.title ? response.title : '');
      })
    );
  }

  updateWebinarWatchStatus(userId: string, webinarCode: string): Observable<any> {
    const url = this.occ.buildUrl(WEBINAR_WATCH_STATUS, {});
    const params = {
      userId: userId,
      webinarCode: webinarCode,
    };
    return this.http.post(url, null, { params });
  }

  fetchPresentorDetails(presentorId: string) {
    return this.http
      .get(this.occ.buildUrl(PRESENTOR_DETAILS(presentorId)))
      .pipe(
        tap((response: any) => {
          return this.presenterSubject.next(response?.name ? response.name : '');
        })
      );
  }

  fetchPresentorlist() {
    return this.http.get(this.occ.buildUrl(PRESENTOR_LIST));
  }

  query(query: string) {
    this.route({ query });
  }

  sort(sort: string): void {
    this.route({ sort });
  }

  currentPage(currentPage: number): void {
    this.route({ currentPage });
  }

  updatePageSize(pageSize: number): void {
    this.route({ pageSize, currentPage: 0 });
  }

  pageSize(pageSize: number): void {
    this.route({ pageSize });
  }

  protected route(queryParams: SearchCriteria): void {
    this.router.navigate([], {
      queryParams,
      queryParamsHandling: 'merge',
      relativeTo: this.activatedRoute,
      replaceUrl: false,
      onSameUrlNavigation: 'reload',
      skipLocationChange: false,
    });
  }
}
