import { Component, computed, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { currentState, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivityLogService } from '../../../ws-services/ws-activityLog.service';

@Component({
  selector: 'ws-review-protocol',
  templateUrl: './review-protocol.component.html',
  styleUrl: './review-protocol.component.scss',
})
export class ReviewProtocolComponent implements OnInit {
  private protocolService = inject(ProtocolService);
  private router = inject(Router);
  private userType = inject(currentState);
  private destroyRef = inject(DestroyRef);
  private activityLogService =  inject(ActivityLogService);
  private currentUser = inject(WsCurrentUserService);

  role = signal<boolean>(false);
  totalPrice = signal<string>('');
  protocol = signal<any>({});
  totalMsrpPrice = signal<string>('');
  protocolId: string = '';
  selectedProduct$: Observable<any>;
  notes: string;
  currentUser$: Observable<any> = this.currentUser.currentState();

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.protocolId = this.activatedRoute.snapshot.paramMap.get('id');
    this.updateUserRole();
    if (this.protocolId) {
      this.LoadSelectedProduct();
    }
  }
  updateUserRole() {
    this.currentUser$.subscribe((res) => {
      this.role.set(res?.includes('doctor'));
    });
  }
  LoadSelectedProduct() {
    this.selectedProduct$ = this.protocolService.fetchReviewProtocol(
      this.protocolId
    ).pipe(tap((item: any) => {
      this.protocol = item;
      this.totalPrice.set(item?.totalPrice?.formattedValue);
      this.totalMsrpPrice.set(item?.msrpSubTotalPrice?.formattedValue);
    }));
  }

  navigateToProtocolEdit() {
     this.router.navigate([`/protocol/edit/`]);
  }

  gotToConfirmation() {
    const payload = {
      notes: this.notes,
      protocolStatus: this.role() ? 'UNPUBLISHED' : 'COMPLETED'
    };
    this.protocolService
      .sendProtocol(this.protocolId, payload)
      .subscribe(() => {
        this.activityLogService.sendLog('protocol_create');
        this.router.navigate([`/protocol/complete/${this.protocolId}`]);
      });
  }
}
