import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { catchError, debounceTime, distinctUntilChanged, map, Observable, of, Subject, switchMap, takeUntil } from 'rxjs';
import { LocationService } from '../../../ws-services/ws-location.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FindPractitionerService } from '../../../ws-services/ws-find-practitioner.service';

interface Store {
  address: {
    defaultAddress: boolean,
    firstName: string,
    formattedAddress: string,
    id: string,
    line1: string,
    line2: string,
    postalCode: string,
    town: string,
  },
  displayName: string,
  formattedDistance: string,
  geoPoint: {
    latitude: number,
    longitude: number
  },
  mapIcon: {
    url: string,
  },
  name: string,
  phoneNumber: string,
  email: string,
  webSite: string
}

@Component({
  selector: 'ws-patient-find-practitioner-search-map',
  templateUrl: './ws-patient-find-practitioner-search-map.component.html',
  styleUrl: './ws-patient-find-practitioner-search-map.component.scss'
})

export class WsPatientFindPractitionerSearchMapComponent implements OnInit, AfterViewInit, OnDestroy {
  practionerDetails: any;
  searchControl = new FormControl();
  private ngUnsubscribe = new Subject<void>();

  latitude: number | null = null;
  longitude: number | null = null;
  mapUrl: SafeResourceUrl;

  currentPage: number = 0;
  totalPages: number = 0;

  stores: Store[] = [];
  activeStore: Store;

  allLoaded: boolean = false;
  @ViewChild('storeDetailsScrollableSectoin') storeDetailsScrollableSectoin!: ElementRef;

  constructor(private findPractitionerService: FindPractitionerService,
    private locationService: LocationService,
    private cd: ChangeDetectorRef,
    private sanitizer: DomSanitizer
  ) {
  }

  ngOnInit(): void {
    this.getLocation();
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => this.search(value))
    ).subscribe(results => {
      this.stores = results?.stores || [];
      this.currentPage = results?.pagination?.currentPage || 0;
      this.totalPages = results?.pagination?.totalPages || 0;
      this.allLoaded = false;
      this.latitude = null;
      this.longitude = null;
      this.setActiveStore(results?.stores?.[0] || null);
      this.cd.detectChanges();
    });
  }

  ngAfterViewInit(): void {
    this.storeDetailsScrollableSectoin?.nativeElement?.addEventListener('scroll', this.onScroll.bind(this));
  }


  search(searchTerm: string) {
    if (searchTerm === '' || searchTerm || (this.latitude && this.longitude)) {
      this.currentPage = 0;
      return this.findPractitionerService.fetchAddressDetails(searchTerm, this.latitude, this.longitude, this.currentPage)
        .pipe(takeUntil(this.ngUnsubscribe))
        .pipe(
          map(response => response),
          catchError(error => {
            return of({});
          })
        )
    }
    return new Observable(observer => {
      observer.next({});
      observer.complete();
    });
  }


  getLocation() {
    this.locationService.getCurrentLocation()
      .then(coords => {
        this.latitude = coords.latitude;
        this.longitude = coords.longitude;
        this.searchControl.reset();
        this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.getMapUrl());
        this.search('').pipe(takeUntil(this.ngUnsubscribe)).subscribe(results => {
          this.stores = results?.stores || [];
          this.currentPage = results?.pagination?.currentPage || 0;
          this.totalPages = results?.pagination?.totalPages || 0;
          this.allLoaded = false;
          this.setActiveStore(results?.stores?.[0] || null);
          this.cd.detectChanges();
        })
      })
      .catch(err => { });
  }


  loadData() {
    if (this.allLoaded) return;
    this.currentPage += 1;
    this.findPractitionerService.fetchAddressDetails(this.searchControl.value, this.latitude, this.longitude, this.currentPage)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(results => {
        if (results?.pagination?.totalPages === results?.pagination?.currentPage + 1) {
          this.allLoaded = true;
          this.latitude = null;
          this.longitude = null;
        } else {
          this.stores = [...this.stores, ...results?.stores];
          this.currentPage = results?.pagination?.currentPage || 0;
          this.totalPages = results?.pagination?.totalPages || 0;
          this.cd.detectChanges();
        }
      }, error => { });
  }

  onScroll() {
    const element = this.storeDetailsScrollableSectoin.nativeElement;
    if ((element.scrollTop + element.clientHeight + 1) >= element.scrollHeight) {
      this.loadData();
    }
  }

  getMapUrl(): string {
    return `https://www.google.com/maps/embed/v1/place?key=AIzaSyCGWKQKvRxg17mE8cfE0DY_6BvSvwjZHPM&center=${this.latitude},${this.longitude}&zoom=15&maptype=satellite&q=${this.latitude},${this.longitude}`;
  }

  setActiveStore(store: Store) {
    if (store) {
      this.activeStore = store;
      this.latitude = store?.geoPoint?.latitude || 0;
      this.longitude = store?.geoPoint?.longitude || 0;
      this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(this.getMapUrl());
    }
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

}