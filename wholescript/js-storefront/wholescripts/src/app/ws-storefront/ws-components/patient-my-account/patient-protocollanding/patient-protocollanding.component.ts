import { HttpParams } from '@angular/common/http';
import { ChangeDetectorRef, Component, DestroyRef, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PatientProtocolListService } from '../service/patient-protocol-listing.service';
import { FormControl } from '@angular/forms';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { ActivatedRoute } from '@angular/router';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';

@Component({
  selector: 'ws-patient-protocollanding',
  templateUrl: './patient-protocollanding.component.html',
  styleUrl: './patient-protocollanding.component.scss'
})
export class PatientProtocollandingComponent {

  private service = inject(PatientProtocolListService);
  private destroyRef = inject(DestroyRef);
  private recommendationService = inject(RecommendationService);
  private vcr = inject(ViewContainerRef);
  private route = inject(ActivatedRoute);
  private overlayService = inject(WsOverlay);
  private cdr = inject(ChangeDetectorRef);

  currentPage = 1;
  sort = 'modifiedtime:desc'
  itemPerPage:number = 3;
  totalItems: number = 0;
  protocollist = signal<any>({});
  sortArray:any=[
    {value:'Last Updated',code:'modifiedtime:desc'},
    {value:'A-Z',code:'name:asc'},
    {value:'Z-A',code:'name:desc'},
    {value:'Price high to low',code:'totalPrice:desc'},
    {value:'Price low to high',code:'totalPrice:asc'}
  ];
  sortControl:FormControl = new FormControl('modifiedtime:desc');
  @ViewChild('moreInfoPopUp') moreInfoPopUp: TemplateRef<any>;
  moreInfoPopUpPortal: any;
  toastMessage :string = '';

  ngOnInit() {
    this.load();
  }

  ngAfterViewInit(){
    this.sortChanged();

    this.moreInfoPopUpPortal = new TemplatePortal(this.moreInfoPopUp, this.vcr);
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if(id) {
        this.service.currentProtocolId = id;
        this.overlayService.openModal(this.moreInfoPopUpPortal, '55%', '95vh', true);
      }
    });
  }

  load() {
    let params = new HttpParams();
    if (this.currentPage >= 0) {
     params = params.append('currentPage', this.currentPage - 1);
    }
    if (this.sort.length > 1) {
    params =  params.append('sort', this.sort);
    }
    params = params.append('pageSize',this.itemPerPage);
    this.service.getProtocolList(params).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((data: any) => {
      this.protocollist.set(data);
      this.totalItems = data?.pagination?.totalCount || 0;
      this.getMedpaxEntries(data);
    })
  }

  getMedpaxEntries(res:any) {

    res?.protocolsListWsDto.forEach((item: any) => {
      this.recommendationService.getMedPaxForOwner(item.id, false).subscribe((medpax: any) => {
        if(medpax.medpaxEntry.length) {
          item.medpax = medpax;
          item.isMedpax = true;
        } else {
          item.isMedpax = false;
        }
      })
    });
    this.protocollist.set(res);
  }

  pageChanged(event: any) {
    this.currentPage = event;
    this.load();
  }
  
  sortChanged(){
    this.sortControl.valueChanges.
    pipe(takeUntilDestroyed(this.destroyRef)).
    subscribe((value:string)=>{
      this.sort = value;
      this.load();
    });
  }

  showToasterMsg(msg: string) {
    this.toastMessage = msg;
    this.cdr.detectChanges();
    this.resetToastMessage();
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cdr.detectChanges();
    }, 4000)
  }
}
