import { HttpClient, HttpParams } from "@angular/common/http";
import { computed, inject, Injectable, signal } from "@angular/core";
import { OccEndpointsService } from "@spartacus/core";
import { Observable } from "rxjs";
import {
  ADD_MEDPAX_TO_CART,
  CREATE_MEDPAX,
  CREATE_MEDPAX_NOTES,
  DELETE_ENTIRE_MEDPAX,
  DELETE_MEDPAX,
  EDIT_PROTOCOL_LIST_API,
  GET_MEDPAX_BY_CODE,
  GET_MEDPAX_DETAILS,
  GET_MEDPAX_FOR_OWNER,
  GET_MEDPAX_FOR_OWNER_WHOLESCRIPTS_PROTOCOL,
  GET_MEDPAX_PACKETS,
  POST_CUSTOM_DOSING_ENTRIES,
  UPDATE_MEDPAX,
  UPDATE_MEDPAX_ENTRY,
  UPDATE_MEDPAX_PACKET,
  UPDATE_PACKET_NAME,
  MEDPAX_BUY_AGAIN_API
} from "../../../ws-constants/ws-api-endpoint.constant";
import { SearchCriteria } from "@spartacus/storefront";
import { ActivatedRoute, Router } from "@angular/router";
import { saveAsCustom } from "../../../ws-services/protocol/protocol.service";

export const medPaxPacketEnum = [
  { am: 'AM' },
  { pm: 'PM' },
  { daily: 'Daily' },
  { bedtime: 'Bedtime' },
  { amWithFood: 'AM_WITH_FOOD' },
  { pmWithFood: 'PM_WITH_FOOD' },
  { dailyWithFood: 'DAILY_WITH_FOOD' },
  { bedtimeWithFood: 'BEDTIME_WITH_FOOD' },
  { amWithOutFood: 'AM_WITHOUT_FOOD' },
  { pmWithOutFood: 'PM_WITHOUT_FOOD' },
  { dailyWithOutFood: 'DAILY_WITHOUT_FOOD' },
  { bedtimeWithOutFood: 'BEDTIME_WITHOUT_FOOD' }
];
export interface dosing {
  quantity: number,
  product: {
    code: string
  },
  itemTimes: [
    {
      code: string
    }
  ]
}
export interface medpaxEntry {
  price: string,
  product: {
    code: string
  },
  dosing?: dosing[]
}
export interface createmedpax {
  name: string,
  description?: string,
  protocolId?: string,
  recommendationId?: string,
  medpaxEntry: medpaxEntry[]
}

export interface updatePacket {
  medPaxId: string,
  changeItemTimeTo: string,
  changeItemTimeFrom: string
}
@Injectable({ providedIn: 'root' })
export class WsCustomMedPaxService {
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  currentSearchQuery = signal<string>('');
  currentMedPaxID = signal<string>('');
  localCurrentmedPaxId = computed(() => { localStorage.setItem('MedPaxId', this.currentMedPaxID()); return localStorage.getItem('MedPaxId') });

  fetchMedPaxProducts(page: any, size: any,   query: string = '', sort?: string): Observable<any> {
    const paginationQuery = `&currentPage=${page}&pageSize=${size}`;
    const sortQuery = sort ? `&sort=${sort}` : '';

    return this.http.get(
      `${this.occ.buildUrl( EDIT_PROTOCOL_LIST_API + query )}&filters=isMedpax:true${paginationQuery}${sortQuery}`
    );
  }

  fetchBuyAgainProducts(): Observable<any> {
    return this.http.get(`${this.occ.buildUrl(MEDPAX_BUY_AGAIN_API())}?fields=FULL&page=1&pageSize=10`);
  }

  fetchMedPaxDetails() {
    return this.http.get(this.occ.buildUrl(GET_MEDPAX_DETAILS));
  }

  fetchMedPaxById(medPaxId:string){
    let params = new HttpParams()
    .append('fields','DEFAULT')
    .append('medpaxId',medPaxId);
    return this.http.get(this.occ.buildUrl(GET_MEDPAX_BY_CODE),{params})
  }

  createCustomMedPax(payLoad: createmedpax) {
    return this.http.post(this.occ.buildUrl(CREATE_MEDPAX), payLoad);
  }

  updateMedpax(payLoad: medpaxEntry, medPaxID: string) {
    return this.http.put(this.occ.buildUrl(UPDATE_MEDPAX(medPaxID)), payLoad);
  }

  updateMedpaxEntry(payLoad: medpaxEntry, medPaxEntryNumber: string) {
    return this.http.patch(this.occ.buildUrl(UPDATE_MEDPAX_ENTRY(medPaxEntryNumber)), payLoad);
  }

  saveCustomDosingEntries(params: saveAsCustom, payload: any) {
    return this.http.post(this.occ.buildUrl(POST_CUSTOM_DOSING_ENTRIES(params)), payload);
  }

  fetchMedPaxPackets(medPaxId: string) {
    return this.http.get(this.occ.buildUrl(GET_MEDPAX_PACKETS(medPaxId)));
  }

  postMedPaxPackets(payLoad: updatePacket) {
    return this.http.patch(this.occ.buildUrl(UPDATE_MEDPAX_PACKET), payLoad);
  }

  updateMedpaxName(payLoad: any) {
    return this.http.post(this.occ.buildUrl(UPDATE_PACKET_NAME), payLoad);
  }

  addMedPaxToCart(medPaxId: string, cartId: string) {
    let params = new HttpParams().append('medpaxId', medPaxId);
    return this.http.post(this.occ.buildUrl(ADD_MEDPAX_TO_CART(cartId)), null, { params });
  }

  getMedPaxForOwner(protocolId: string, isRecommendation = false) {
    const key = isRecommendation ? 'recommendationId' : 'protocolId';
    let params = new HttpParams().append(key, protocolId);
    return this.http.get(this.occ.buildUrl(GET_MEDPAX_FOR_OWNER), { params });
  }

  getWholeScriptsProtocolMedPaxForOwner(protocolId: string) {
    const params = new HttpParams().append('protocolId', protocolId);
    return this.http.get(this.occ.buildUrl(GET_MEDPAX_FOR_OWNER_WHOLESCRIPTS_PROTOCOL), { params });
  }  

  //common things for Search Query Updates
  query(query: string) {
    this.route({ query });
  }

  sort(sort: string): void {
    this.route({ sort });
  }

  currentPage(currentPage: number): void {
    this.route({ currentPage });
  }

  pageSize(pageSize: number): void {
    this.route({ pageSize });
  }

  protected route(queryParams: any): void {
    this.router.navigate([], {
      queryParams,
      queryParamsHandling: 'merge',
      relativeTo: this.activatedRoute,
      replaceUrl: false,
      onSameUrlNavigation: 'reload',
      skipLocationChange: false
    });
  }

  createMedPaxNotes(payload: any) {
    return this.http.post(this.occ.buildUrl(CREATE_MEDPAX_NOTES), payload);
  }

  deleteMedPaxForOwner(medPaxId: string, medPaxEntryNumber: string) {
    return this.http.delete(this.occ.buildUrl(DELETE_MEDPAX({ medPaxId, medPaxEntryNumber })), { responseType: 'text' });
  }

  deleteAllMedPaxProducts(medpaxId: string) {
    let params = new HttpParams().append('medpaxId', medpaxId?.trim());
    // wholescripts/users/current/medPax/delete?medpaxId=100071001
    return this.http.delete(this.occ.buildUrl(DELETE_ENTIRE_MEDPAX), { params, responseType: 'text' });
  }
}


