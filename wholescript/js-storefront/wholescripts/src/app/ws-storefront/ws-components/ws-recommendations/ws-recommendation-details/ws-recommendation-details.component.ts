import { ChangeDetectorRef, Component, DestroyRef, OnInit } from '@angular/core';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Observable, shareReplay, switchMap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-recommendation-details',
  templateUrl: './ws-recommendation-details.component.html',
  styleUrl: './ws-recommendation-details.component.scss',
})
export class WsRecommendationDetailsComponent implements OnInit {
  recommendationId: string = '';
  recommendations$: Observable<any>;
  baseUrl: string = '';
  user: any;
  item: any;
  payload: string;
  weekMapItems = weekMap;
  alerts :any[] = [];

  constructor(
    public wsPageService: WsPage,
    private cdRef: ChangeDetectorRef,
    private recommendationService: RecommendationService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private destroyRef: DestroyRef,
  ) {
    // this.activatedRoute.paramMap.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((params: any) => {
    //   this.recommendationId = params.get('id');
    // });
  }
  ngOnInit(): void {
    this.baseUrl = window.location.origin;
    this.recommendationService.getUserDetails().subscribe((res) => this.user = res)
    this.recommendations$ = this.activatedRoute.paramMap.pipe(
      switchMap((params: any) => {
        this.recommendationId = params.get('id');
        return this.recommendationService.fetchRecommendationsForReview(
          this.recommendationId
        );
      }),
      shareReplay({ bufferSize: 1, refCount: true })
    );
  }


  getDetails() {
    this.recommendations$ = this.recommendationService.fetchRecommendationsForReview(
      this.recommendationId
    );
    this.cdRef.detectChanges();
  }

  goBackToRecommendationReveiw(item: any) {
    this.router.navigate([
      '/Recommendation/RecommendationReview', item.id
    ]);
  }

  cancelRecommendation(id: string) {
    this.recommendationService.cancelRecommendationId(id, this.payload).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res) => this.getDetails())
  }

  goToEdit(item: any) {
    this.router.navigate(['/Recommendation/BasicRecommendation/', item.id])
  }
  navigateToPatientProfile(item: any) {
    this.router.navigateByUrl('practitioner/patientprofile?uid=' + item?.email);
  }
  copyUrl(url: any) {
    this.copyToClipboard(url);
  }
  copyToClipboard(text: any) {
    navigator.clipboard.writeText(text).then(() => {
      this.add('Link copied', 'success');
      console.log('Text copied to clipboard successfully!');
    }).catch(err => {
    });
  }
  add(err: any, type: any): void {
    this.alerts.push({
      type: type,
      msg: err,
      timeout: 2000
    });
  }
 
  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }
  onSubmit() {
    const payload = {
      recommendationId: this.recommendationId,
      name: 'test'
    }
    this.recommendationService.cloneEntryToProtocol(payload).subscribe((res: any) => {
      this.router.navigate([`protocol/edit/${res.Text}`]);
    });
  }
}
