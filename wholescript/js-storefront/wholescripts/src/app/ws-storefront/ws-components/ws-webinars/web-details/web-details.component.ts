import { Component, inject, signal, DestroyRef } from '@angular/core';
import { WebinarlistService } from '../webinarlist.service';
import { ActivatedRoute } from '@angular/router';
import { map, Observable, tap, switchMap } from 'rxjs';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { DomSanitizer } from '@angular/platform-browser';
import { MediaService } from '@spartacus/storefront';
import { Image } from '@spartacus/core';
@Component({
  selector: 'ws-web-details',
  templateUrl: './web-details.component.html',
  styleUrl: './web-details.component.scss'
})
export class WebDetailsComponent {
 
  private service = inject(WebinarlistService);
  private activatedRoute = inject(ActivatedRoute);
  private protocolService = inject(ProtocolService);
  private destroyRef = inject(DestroyRef);
   private sanitizer = inject(DomSanitizer)
  
  webinarId = signal<string>('');
  WebinarDetails$: Observable<any>;
  webinarDetails = signal<any>({});
  presentorDetail = signal<any>({});
  webinar: any;
  userId: string;
  watched10Percent = false;
  imagePrefix = this.protocolService.getBaseUrl();
  currentUserData$ = inject(getUserData);
  mediaService = inject(MediaService)


  ngOnInit() {
    this.currentUserData$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => {
      this.userId = res?.uid;
    });
    this.WebinarDetails$ = this.activatedRoute.paramMap.pipe(
      map(params => params.get('id') || ''),
      tap(id => this.webinarId.set(encodeURI(id))), 
      switchMap(id => this.service.fetchWebinarDetails(this.webinarId())),
      map((webinar: any) => ({
        ...webinar,
        videoImage: `${this.imagePrefix}${webinar.videoImage}`,
        videoUrl: webinar?.videoUrl?.replace(/&amp;/g, '&'),
        presenters: webinar?.presenters?.map((presenterDetails: any) => ({
          ...presenterDetails,
          image: { url: `${this.imagePrefix}${presenterDetails?.image?.url}` }
        })),
        relatedWebinars: this.formatResults((webinar?.relatedWebinars || []).slice(0, 6)), //Temp
        description: this.sanitizer.bypassSecurityTrustHtml(webinar.description)
      })),
      tap((res: any) => {
        this.webinarDetails.set(res);
        this.presentorDetail.set(res.presenters.find((item: any) => {
          return res.presenter[0] === item.name;
        }));
        this.reloadVideo();
      })
    );
  }

  onVideoProgress(event: Event): void {
    const video = event.target as HTMLVideoElement;
    if (!video.duration) return;

    const percentage = (video.currentTime / video.duration) * 100;
    if (percentage >= 10 && !this.watched10Percent) {
      this.watched10Percent = true; 
      this.sendWatchStatus();
    }
  }

  sendWatchStatus() {
    const webinarId = this.webinarId(); 
    this.service
    .updateWebinarWatchStatus(this.userId, webinarId)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      error: (err) => {
        console.error('Failed to update watch status:', err);
      },
    });
  }

  private formatResults(results: any[]) {
    return results?.map((result: any) => ({
      ...result,
      videoImage: `${this.imagePrefix}${result?.videoImage}`,
    }));
  }

  onRightClick(event: MouseEvent) {
    event.preventDefault();
  }

  getMedia(item: any) {
    const image: Image = {
      url: item?.url
    }
    return this.mediaService.getMedia(image).src;
  }

  private reloadVideo() {
    setTimeout(() => {
      const videoElement = document.querySelector('video');
      if (videoElement) {
        videoElement.load();
      }
    }, 100);
  }
}

