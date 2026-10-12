import { Component, input } from '@angular/core';
import { Image } from '@spartacus/core';
import { MediaService } from '@spartacus/storefront';

@Component({
  selector: 'ws-featured-webinar',
  templateUrl: './featured-webinar.component.html',
  styleUrl: './featured-webinar.component.scss'
})
export class FeaturedWebinarComponent {
  constructor(
      protected mediaService: MediaService,
    ) {}
  featuredWebinar = input<any>();

  getMedia(item: any) {
    const image: Image = {
      url: item.url
    }
    return this.mediaService.getMedia(image).src;
  }
}
