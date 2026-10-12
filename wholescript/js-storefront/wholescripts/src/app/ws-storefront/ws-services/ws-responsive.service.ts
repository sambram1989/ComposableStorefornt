import { computed, inject, Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
@Injectable({
  providedIn: 'root'
})
export class WsResponsiveService {

  private readonly xs = '(max-width:500px)';
  private readonly sm = '(min-width:501px) and (max-width:768px)';
  private readonly md = '(min-width:769px) and (max-width:1000px)';
  private readonly lg = '(min-width:1001px)and (max-width:1099px)';
  private readonly xl = '(min-width:1100px) and (max-width:1200px)';

  breakpointObserver = inject(BreakpointObserver);

  screenWidth$ = this.breakpointObserver.observe([this.xs, this.sm, this.md, this.lg, this.xl]);


  screenwidth = toSignal(this.screenWidth$);

  public xsmallWidth = computed(() => this.screenwidth()?.breakpoints[this.xs]);
  public smallWidth = computed(() => this.screenwidth()?.breakpoints[this.sm]);
  public mediumWidth = computed(() => this.screenwidth()?.breakpoints[this.md]);
  public largeWidth = computed(() => this.screenwidth()?.breakpoints[this.lg]);
  public extraLargeWidth = computed(() => this.screenwidth()?.breakpoints[this.xl]);


}
