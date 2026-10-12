import { Component, DestroyRef, inject, input, output, signal } from '@angular/core';
import { protocolListFetch, ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Observable } from 'rxjs';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { RoutingService } from '@spartacus/core';

@Component({
  selector: 'ws-swap-protocol-popup',
  templateUrl: './swap-protocol-popup.component.html',
  styleUrl: './swap-protocol-popup.component.scss'
})
export class SwapProtocolPopupComponent {
  private Service = inject(ProtocolService);
  private customMedPaxService = inject(WsCustomMedPaxService);
  private destroyRef = inject(DestroyRef);
  private overlayService = inject(WsOverlay);
  private routingService = inject(RoutingService);

  swappedProtocolId = input<string>('');
  isRecommendationHasEntries = input<boolean>(false);

  weekMapItems = weekMap;

  payLoad: protocolListFetch = {
    currentPage: 0,
    pageSize: 8,
    sort: '',
    searchText: ''
  };
  searchKeyPopup = '';
  protocols = signal<any>({});
  isWholeScriptsTab = signal<boolean>(false);
  pagination = signal<any>({});
  isViewMore = signal<boolean[]>([]);
  fetchProtocolDetails$: Observable<any>;
  openSwapProtocol = output<string>();
  SwapProtocol = output<string>();

  ngOnInit() {
    this.getProtocols();
  }

  getProtocols() {
    this.Service.fetchProtocolListforSwappingWithRecommendation(this.payLoad, this.isWholeScriptsTab() , true)
      .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: (res: any) => {
          this.protocols.set(res?.protocolsListWsDto);
          this.pagination.set(res?.pagination);
          this.isViewMore.set(new Array(res?.protocolsListWsDto.length).fill(false));
        }
      })
  }

  protocolDetailsMap = signal<Record<string, any>>({});

  fetchProtocolDetails(protocolID: string) {
    if (this.protocolDetailsMap()[protocolID]) {
      return;
    }
    this.Service.fetchProtocolDetailsForSwapping(protocolID, this.isWholeScriptsTab())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          this.protocolDetailsMap.update((map) => ({ ...map, [protocolID]: data }));
        },
        error: (err) => console.error('Error fetching details:', err)
      });
  }
  
  medPaxDetailsMap = signal<Record<string, any>>({});
  fetchMedPaxDetails(protocolId: string) {
    if (this.medPaxDetailsMap()[protocolId]) {
      return;
    }
    this.customMedPaxService.getMedPaxForOwner(protocolId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          this.medPaxDetailsMap.update((map) => ({ ...map, [protocolId]: data }));
        },
        error: (err) => console.error('Error fetching details:', err)
      })
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event;
    this.getProtocols();
  }

  closePopup() {
    this.overlayService.closeTopModal();
  }

  updateTab(state: boolean) {
    this.isWholeScriptsTab.set(state);
    this.getProtocols();
    //set this localstorage so as to make it available for the protocol details page
    if(this.isWholeScriptsTab()) localStorage.setItem('isWholeScriptsTab', 'true');
    else localStorage.setItem('isWholeScriptsTab', 'false');
    
    this.Service.isWsPage.set(this.isWholeScriptsTab());
  }

  updateIsViewMore(state: boolean, index: number, protocolId: string) {
    this.isViewMore.update(arr => {
      const newArr = [...arr];
      newArr[index] = !newArr[index];
      return newArr;
    });
    if (state) {
      this.fetchProtocolDetails(protocolId);
      this.fetchMedPaxDetails(protocolId);
    }
  }

  showSearchResults(key: string) {
    this.searchKeyPopup = key;
    this.payLoad.searchText = this.searchKeyPopup;
    this.getProtocols();

  }

  openSwapProtocolPopup(protocolId:string){
    this.Service.isWholeScriptsTab.set(this.isWholeScriptsTab());
   this.openSwapProtocol.emit(protocolId);
  }

swapProtocolWithoutPopup(protocolId:string){
  this.Service.isWholeScriptsTab.set(this.isWholeScriptsTab());
  this.SwapProtocol.emit(protocolId)
}

navigateToProtocolDetails(protocolId:string){
  this.routingService.go(`protocol/details/${protocolId}`);
}
}
