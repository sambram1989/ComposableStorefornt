import { Component, input } from '@angular/core';


@Component({
  selector: 'ws-webinar-presenter',
  templateUrl: './webinar-presenter.component.html',
  styleUrl: './webinar-presenter.component.scss'
})
export class WebinarPresenterComponent {
  isMtpLink = input<boolean>(false);
  presentorDetail = input<any>({});
 
}
