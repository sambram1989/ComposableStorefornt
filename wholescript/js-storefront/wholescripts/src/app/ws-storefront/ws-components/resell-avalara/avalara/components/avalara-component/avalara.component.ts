import { ChangeDetectorRef, Component, OnInit, signal } from '@angular/core';
import { AvalaraService } from '../../core/facade/avalara.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-avalara',
  templateUrl: './avalara.component.html',
})
export class AvalaraComponent implements OnInit {
  avalaralink = signal<string>('');
  constructor(
    protected avalaraService: AvalaraService
  ) {}

  ngOnInit() {
    this.avalaraService
      .getAvalaraLink()
      .pipe(take(1))
      .subscribe((avlink: any) => {
        this.avalaralink.set(avlink?.avalaraTaxLink);
      });
  }
}
