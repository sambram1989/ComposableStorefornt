import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { OccEndpointsService, SearchboxService, SearchConfig } from "@spartacus/core";
import { Observable } from "rxjs";
import { CONTENT_SEARCH, PRIVATE_LABEL, WEBINAR_SEARCH } from "../ws-constants/ws-api-endpoint.constant";

@Injectable({
    providedIn: 'root',
})
export class CustomSearchboxService extends SearchboxService {

    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);

    searchResults(query: string, config?: SearchConfig): void {
        super.search(query, { ...config } as SearchConfig);
    }

    getCustomProductResults() {
        super.getResults();
    }

    getWSSearchContent(searchText: string, contentType: string, currentPage: number = 0, pageSize: number = 10, isMedPax = false): Observable<any> {
        return this.http.get(
            this.occ.buildUrl(CONTENT_SEARCH, {
                queryParams: {
                    query: `${searchText ? searchText : ''}::contentType:${contentType}`,
                    currentPage: currentPage,
                    pageSize: pageSize,
                    filters:`isMedpax:${isMedPax}`
                }
            })
        )
    }

    getWebinarSSearchContent(searchText: string, currentPage: number = 0, pageSize: number = 10): Observable<any> {
        return this.http.get(
            this.occ.buildUrl(WEBINAR_SEARCH, {
                queryParams: {
                    query: `${searchText}`,
                    currentPage: currentPage,
                    pageSize: pageSize,
                }
            })
        )
    }

    getPrivateLabelSearchContent(searchText: string, currentPage: number = 0, pageSize: number = 10): Observable<any> {
        return this.http.get(
            this.occ.buildUrl(PRIVATE_LABEL, {
                queryParams: {
                    query: `${searchText}`,
                    currentPage: currentPage,
                    pageSize: pageSize,
                }
            })
        )
    }
}
