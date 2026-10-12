import {
  Component,
  computed,
  inject,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, switchMap, shareReplay } from 'rxjs';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'ws-ws-recommendation-confirm',
  templateUrl: './ws-recommendation-confirm.component.html',
  styleUrl: './ws-recommendation-confirm.component.scss',
})
export class WsRecommendationConfirmComponent implements OnInit {
  private protocolService = inject(ProtocolService);
  private overLayService = inject(WsOverlay);
  private _viewContainerRef = inject(ViewContainerRef);
  isMedpax = computed(() => this.protocolService.isMedPaxPage());
  reviewId: string;
  recoId$: Observable<string>;
  recommendations$: Observable<any>;
  recommendationEntries: any;
  itemDurationDefault: string = 'Ongoing';
  weekMapItems = weekMap;

  @ViewChild('cloneRecommendation')
  sendRecommendationPoup: TemplateRef<unknown>;
  templatePortal: TemplatePortal<any>;

   createProtocol = new FormGroup({
      name: new FormControl('', [Validators.required,Validators.maxLength(50)]),
    });

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private recommendationService: RecommendationService
  ) {}

  ngOnInit(): void {
    this.recommendations$ = this.activatedRoute.paramMap.pipe(
      switchMap((params) => {
        this.reviewId = params.get('id');
        return this.recommendationService.fetchRecommendationsForReview(
          this.reviewId
        );
      }),
      shareReplay({ bufferSize: 1, refCount: true })
    );
    this.recommendations$.subscribe({
      next: (res) => (this.recommendationEntries = res.entries),
      error: (error) => {
        if (error) {
          this.router.navigate(['not-found']);
        }
      },
      complete: () => {}
    });
  }


  ngAfterViewInit() {
    this.templatePortal = new TemplatePortal(
      this.sendRecommendationPoup,
      this._viewContainerRef
    );
  }

  onSubmit() {
    const payload = {
      recommendationId: this.reviewId,
      name: this.createProtocol.controls.name.value,
    };
    this.recommendationService
      .cloneEntryToProtocol(payload)
      .subscribe((res: any) => {
        this.router.navigate([`protocol/edit/${res.Text}`]);
      });
  }

  cloneRcommendation() {
    this.overLayService.openModal(this.templatePortal, '500px');
  }

  closeFilterModal() {
    this.overLayService.closeAllModals();
  }
}
