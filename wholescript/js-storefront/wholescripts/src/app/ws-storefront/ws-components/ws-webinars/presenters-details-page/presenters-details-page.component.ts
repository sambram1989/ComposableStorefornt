import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, map, tap } from 'rxjs';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WebinarlistService } from '../webinarlist.service';
import { Location } from '@angular/common';

@Component({
  selector: 'ws-presenters-details-page',
  templateUrl: './presenters-details-page.component.html',
  styleUrl: './presenters-details-page.component.scss'
})
export class PresentersDetailsPageComponent {
  private service = inject(WebinarlistService);
  private activatedRoute = inject(ActivatedRoute);
  private protocolService = inject(ProtocolService);
  private location = inject(Location);

  presentorDetails$: Observable<any>;
  presentorList$: Observable<any>;
  presentorId = signal<string>('');

  imagePrefix = this.protocolService.getBaseUrl();

  ngOnInit() {
    this.activatedRoute.params.subscribe(params => {
      const id = params['id']; 
      this.presentorId.set(encodeURI(id));
      this.fetchPresentorDetail();
    });
    this.loadData();
  }

  loadData() {
    let id = this.activatedRoute.snapshot.paramMap.get('id');
    this.presentorId.set(encodeURI(id));
    if (id) {
      this.fetchPresentorDetail();
    }
    else {
      this.location.back();
    }
    this.presentorList$ = this.service.fetchPresentorlist();
  }

  fetchPresentorDetail() {
    this.presentorDetails$ = this.service.fetchPresentorDetails(this.presentorId())
      .pipe(
        map((webinar: any) => {
          return {
            ...webinar,
            image: { url: `${this.imagePrefix}${webinar.image.url}` },
            relatedWebinars: this.formatResults(webinar?.relatedWebinars)
          };
        })
      );
  }

  private formatResults(results: any[]) {
    return results.map((result: any) => ({
      ...result,
      videoImage: `${this.imagePrefix}${result?.videoImage}`,
    }));
  }
}
