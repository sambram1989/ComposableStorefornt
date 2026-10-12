import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'ws-recommendation-review-notification',
  templateUrl: './ws-recommendation-review-notification.component.html',
  styleUrl: './ws-recommendation-review-notification.component.scss'
})
export class WsRecommendationReviewNotificationComponent {
  @Input()sms:FormControl;
  @Input()email:FormControl;

  onKeyToggle(event: KeyboardEvent, control: any) {
    if (event.code === 'Enter') {
      event.preventDefault();
      control.setValue(!control.value);
    }
  }
}
