import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Location } from '@angular/common';
import { Observable, tap } from 'rxjs';
import { WsCustomMedPaxService } from '../medpaxService/customMedpaxservice.service';
import { WsPrivateLabelCartonService } from '../../../ws-services/ws-private-label/ws-private-label.service';
import { FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';

@Component({
  selector: 'ws-select-carton',
  templateUrl: './select-carton.component.html',
  styleUrl: './select-carton.component.scss',
})
export class SelectCartonComponent {
  private destroyRef = inject(DestroyRef);
  private service = inject(WsCustomMedPaxService);
  private plService = inject(WsPrivateLabelCartonService);
  private activatedRoute = inject(ActivatedRoute);
  private recommendationService = inject(RecommendationService);
  private protocolService = inject(ProtocolService);
  private router = inject(Router);
  location = inject(Location);
  medPaxProducts = signal<any>({});
  baseSite = this.plService.occBaseUrl;
  cartonCardControl = new FormControl('');
  currentUrlId = '';
  recommendationEntries = signal<any>({});
  proptocolEntries = signal<any>({});
  getAllCartons$: Observable<any> = this.plService.getMedPaxCartons().pipe(
    tap((data: any) => {
      const selectedCard = data.medpaxCartonList.find(
        (item: any) => item.defaultCarton
      );
      this.cartonCardControl.setValue(selectedCard?.productCode);
    })
  );
  flowType = window.location.pathname
    .toLowerCase()
    .startsWith('/recommendation')
    ? 'recommendation'
    : window.location.pathname.toLowerCase().startsWith('/protocol')
    ? 'protocol'
    : 'medpax';

  ngOnInit() {
    this.currentUrlId = this.activatedRoute.snapshot.paramMap.get('id');
    this.LoadSelectedProduct();
    if (this.flowType === 'recommendation') {
      this.loadRecommendationEntries();
    }
    if (this.flowType === 'protocol') {
      this.loadProtocolEntries();
    }
  }

  selectCard(card: any) {
    this.cartonCardControl.setValue(card.productCode);
    const payload = { ...card, medpaxId: this.medPaxProducts().code };
    this.plService
      .postSelectedCarton(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          let navigateToUrl: string = '';
          if (
            this.location
              ?.path()
              ?.includes('/medpax/review/PrivateLabelSelectCarton')
          ) {
            navigateToUrl =
              '/recommendation/edit/medpax/review/' + this.currentUrlId;
          } else if (this.location?.path()?.includes('recommendation')) {
            navigateToUrl =
              '/Recommendation/RecommendationReview/' + this.currentUrlId;
          } else if (this.location?.path()?.includes('protocol')) {
            navigateToUrl = '/protocol/edit/medpax/review/' + this.currentUrlId;
          } else {
            navigateToUrl = '/medpax/review/' + this.currentUrlId;
          }
          this.router.navigate([navigateToUrl]);
        },
      });
  }

  LoadSelectedProduct() {
    const path = this.location.path().toLowerCase();
    const isRecommendationOrProtocol =
      path.includes('recommendation') || path.includes('protocol');

    const isMedpax = path.includes('medpax');
    const subScribableService$ = isRecommendationOrProtocol
      ? this.service.getMedPaxForOwner(
          this.currentUrlId,
          path.includes('recommendation')
        )
      : isMedpax
      ? this.service.fetchMedPaxById(this.currentUrlId)
      : this.service.fetchMedPaxDetails();

    subScribableService$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((item: any) => {
        if(item?.carton?.productCode){
          this.cartonCardControl.setValue(item?.carton?.productCode);
        }
        this.medPaxProducts.set(item);
      });
  }

  loadRecommendationEntries() {
    this.recommendationService
      .getRecommendationEntries(this.currentUrlId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.recommendationEntries.set(data);
      });
  }

  loadProtocolEntries() {
    this.protocolService
      .fetchEditableProducts(this.currentUrlId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((item: any) => {
        this.proptocolEntries.set(item);
      });
  }
}
