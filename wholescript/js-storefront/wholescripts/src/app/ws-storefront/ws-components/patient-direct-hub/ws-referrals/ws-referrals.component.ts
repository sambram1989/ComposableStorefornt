import { ChangeDetectorRef, Component, DestroyRef, OnInit, signal } from '@angular/core';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsNewReferralCodeComponent } from '../ws-new-referral-code/ws-new-referral-code.component';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import * as XLSX from 'xlsx';
import { WsShareReferralCodeComponent } from '../ws-share-referral/ws-share-referral.component';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { pipe } from 'rxjs';
import { NavigationExtras, Router } from '@angular/router';

@Component({
  selector: 'ws-referrals',
  templateUrl: './ws-referrals.component.html',
  styleUrl: './ws-referrals.component.scss',
})
export class WsReferralsComponent implements OnInit {
  referralCodes: any = null; 
  isReferralCode: boolean = false;
  bsModalRef?: BsModalRef;
  searchText: string = '';
  CODE_EDIT: boolean = false;
  selectedCode: any;
  showNav: boolean = false;
  isAccurals: boolean = true;
  currentPage = 0;
  pagination = signal<any>({});
  loader: boolean = false;

  constructor(
    private dialog: BsModalService,
    public referralCodeService: PatientDirectService,
    private cdref: ChangeDetectorRef,
    private destroyRef: DestroyRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.referralCodeService.CODE_EDIT = false;
    this.getReferralCodes();
    this.referralCodeService.editReferral.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(res => {
      if(res) this.changesSavedAfterEdit();
    })
  }

  changesSavedAfterEdit() {
    this.getReferralCodes();
    this.cdref.detectChanges();
  }

  setActiveTab(tabIndex: number) {
    if (tabIndex === 1) this.isReferralCode = true;
    if (tabIndex === 2) this.isAccurals = true;
  }
  createNewReferral() {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
    };
    this.bsModalRef = this.dialog.show(
      WsNewReferralCodeComponent,
      initialState
    );
    this.dialog.onHidden.subscribe((data) => {
      this.getReferralCodes();
      console.log(data);
    });
  }

  pageChanged(event: any) {
    this.currentPage = event
    this.getReferralCodes();
  }

  getReferralCodes() {
    this.loader = true;
    this.referralCodeService.getReferralEntries(this.currentPage, 10)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res) => {
      this.referralCodes = res;
      this.pagination.set(res?.pagination);
      this.loader = false;
      this.cdref.detectChanges();
    }, () => {
      this.loader = false;
      this.cdref.detectChanges();
    });
  }

  exportReferrals() {
    this.referralCodeService.getAllReferralEntriesForCSV().subscribe((res) => {
      this.export(res.referralCodeListWsDto);
    });
  }

  searchReferrals(searchText: string) {
    this.searchText = searchText;
    this.referralCodeService.getReferralEntries(0, 10)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res) => {
      res.referralCodeListWsDto =
        searchText !== ''
          ? res.referralCodeListWsDto.filter(
              (item: any) =>
                item.referralCode.toLowerCase().includes(searchText.toLowerCase()) ||
                item.lastName.toLowerCase().includes(searchText.toLowerCase())
            )
          : res.referralCodeListWsDto;

      this.referralCodes = res;
      this.pagination.set(res?.pagination);
      this.cdref.detectChanges();
    });
  }

  shareReferral(code: any) {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
      initialState: {
        referralCode: code,
      },
    };
    this.bsModalRef = this.dialog.show(
      WsShareReferralCodeComponent,
      initialState
    );
  }
  export(data: any) {
    const workBook = XLSX.utils.book_new();
    const workSheet = XLSX.utils.json_to_sheet(data);

    XLSX.utils.book_append_sheet(workBook, workSheet, 'all-codes');
    XLSX.writeFile(workBook, 'wholescripts.xlsx');
  }
  showEditCode(code: any) {
    this.selectedCode = code;
    const navigationExtras: NavigationExtras = {
      state: {data: code}
    }
    this.router.navigate(['patient-direct-hub/edit-referral'], navigationExtras);
    sessionStorage.setItem('editReferral', code);
  }
}
