import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnChanges,
  SimpleChanges,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-recommendation-filters',
  templateUrl: './ws-recommendation-filters.component.html',
  styleUrl: './ws-recommendation-filters.component.scss',
})
export class WsRecommendationFiltersComponent implements OnInit {
  @Input() facets: any[];
  constructor(public bsModalRef: BsModalRef) {}
  close() {
    this.bsModalRef.hide();
  }
  ngOnInit() {
  }
}
