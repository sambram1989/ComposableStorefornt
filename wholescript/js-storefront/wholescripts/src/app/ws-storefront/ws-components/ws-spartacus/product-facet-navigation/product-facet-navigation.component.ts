import { Component, ChangeDetectionStrategy, ViewChild, ElementRef, Input } from "@angular/core";
import { ICON_TYPE, BreakpointService } from "@spartacus/storefront";
import { BehaviorSubject, Observable, observeOn, asapScheduler, switchMap, of, delayWhen, interval } from "rxjs";


@Component({
  selector: 'ws-product-facet-navigation',
  templateUrl: './product-facet-navigation.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsProductFacetNavigationComponent {
  iconTypes = ICON_TYPE;

  protected CLOSE_DELAY = 300;

  @ViewChild('trigger') trigger: ElementRef<HTMLElement>;
  @Input() filterData: any;

  protected open$ = new BehaviorSubject(false);

  isOpen$: Observable<boolean> = this.breakpointService.breakpoint$.pipe(
  
    observeOn(asapScheduler),
    switchMap(() => (this.hasTrigger ? this.open$ : of(true))),
    delayWhen((launched) => interval(launched ? 0 : this.CLOSE_DELAY))
  );

  isActive$ = this.open$.pipe(
    observeOn(asapScheduler)
  );

  constructor(protected breakpointService: BreakpointService) {}

  launch() {
    this.open$.next(true);
  }

  close() {
    this.open$.next(false);
    this.trigger.nativeElement.focus();
  }

  get hasTrigger() {
    return this.trigger.nativeElement.offsetParent !== null;
  }
}
