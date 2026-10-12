import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WsSidebarService {
  menuOpen: Boolean = false;
  menuOpen$: BehaviorSubject<Boolean> = new BehaviorSubject(this.menuOpen);
  constructor() {}

  loadMenuState$(): Observable<Boolean> {
    return this.menuOpen$.asObservable();
  }

  toggleMenuState() {
    this.menuOpen$.next(!this.menuOpen);
  }

  changeMenuState(menuState: Boolean) {
    this.menuOpen$.next(menuState);
  }
}
