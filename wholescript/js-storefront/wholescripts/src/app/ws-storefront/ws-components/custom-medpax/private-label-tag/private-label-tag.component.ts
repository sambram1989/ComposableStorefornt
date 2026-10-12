import {
  Component,
  inject,
  input,
  Output,
  signal,
  EventEmitter,
  OnInit,
  DestroyRef,
} from '@angular/core';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { tap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-private-label-tag',
  templateUrl: './private-label-tag.component.html',
  styleUrls: ['./private-label-tag.component.scss'],
  host: {
    '[style.display]': 'this.isPlUser() ? "inline-flex" : "none"',
    style: `
      align-items: center;
      gap: 6px;
    `,
  },
})
export class PrivateLabelTagComponent implements OnInit {
  isPlUser = signal<boolean>(false);
  labelName = input<string>('');
  productCode = input<string>('');
  color = input<string>('#584091');
  fontWeight = input<number>(800);
  fontSize = input<number>(18);
  @Output() labelClick = new EventEmitter<string>();
  private destroyRef = inject(DestroyRef);
  curuserService = inject(WsCurrentUserService);

  ngOnInit() {
    this.curuserService
      .isRoleExist('pluser')
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        tap((res) => this.isPlUser.set(res))
      )
      .subscribe();
  }

  openPrivateLabelModal() {
    if (this.productCode()) {
      this.labelClick.emit(this.productCode());
    }
  }
}
