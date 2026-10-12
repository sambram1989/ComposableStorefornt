import { Component, input, OnInit } from '@angular/core';

@Component({
  selector: 'ws-webinar-title-icon',
  templateUrl: './webinar-title-icon.component.html',
  styleUrl: './webinar-title-icon.component.scss'
})
export class WebinarTitleIconComponent implements OnInit{
webinarTitle= input<string>('');
titleSrc = input<string>('/assets/qa/grey-hex.svg');
bgColor = input<string>('#584091');
titleFontColor = input<string>('#333333');
isWebinarPresenters = input<boolean>(false);
webinarTitleWithSuffix = ''; 

  ngOnInit() {
    const title = this.webinarTitle();
    const isPresenter = this.isWebinarPresenters();

    this.webinarTitleWithSuffix = (title && isPresenter)
      ? `${title} Webinars`
      : title;
  }
}
