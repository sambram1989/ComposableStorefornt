import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  Renderer2,
  signal,
} from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { WsAddressesService } from '../../../ws-services/ws-addresses.service';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { userMap } from '../../../ws-env/environment';
@Component({
  selector: 'ws-as-addresses',
  templateUrl: './ws-as-addresses.component.html',
  styleUrl: './ws-as-addresses.component.scss',
})
export class WsAsAddressesComponent {
  private overlayService = inject(WsOverlay);
  private currentUserData: any = inject(getUserData);
  private destroyRef = inject(DestroyRef);
  private searchSubject = new Subject<string>();
  allAddresses: any[] = [];
  allOriginalAddresses: any[] = [];
  defaultAddress:any = {};
  searchText: string = '';
  currentPage: number = 1;
  selectedRowIndex: number = -1;
  focusedRowIndex: number = -1;
  pageSize: number = 10;
  activeCartId: string;
  newAndValidateAddressDetails: any;
  addressToDelete: any;
  addressToEdit: any;
  isSearchLoading: Boolean = false;
  showNewAddress: boolean = false;
  showValidateAddress: boolean = false;
  showDeleteAddress: boolean = false;
  disableEmailForEdit: boolean = false;
  loader: boolean = false;
  isEdit: boolean = false;
  isAscendingName = true;
  isAscendingDescritpion = true;
  isAscendingAddress = true;
  searchKeys = [
    'addressDescription',
    'firstName',
    'lastName',
    'formattedAddress'
  ];
  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  orgUnitId: string;
  isPatient = signal<boolean>(false); 
  focusedButton: HTMLElement | null = null;
  showTooltip = false;

  constructor(
    private cd: ChangeDetectorRef,
    protected renderer: Renderer2,
    private addressesService: WsAddressesService
  ) {
    this.currentUserData.subscribe((res: any) => {
      this.orgUnitId = res.orgUnit?.uid?.replace(/^0+/, '');
      const keyForPatient = Object.keys(userMap).find(
        (key) => userMap[key] === 'patient'
      );
      const isPatient = keyForPatient
        ? res?.roles.includes(keyForPatient)
        : false;
      this.isPatient.set(isPatient);
      this.searchText = '';
      this.getDataForAddresses();
      this.subscribeSearch();
    });
  }

  getDataForAddresses() {
    this.loader = true;
    this.addressesService
      .getListOfCommerceAddresses()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data: any) => {
          this.allOriginalAddresses = data?.addresses || [];
          this.defaultAddress = this.allOriginalAddresses.find(
            (address) => this.orgUnitId == address.addressNumber
          );
          if (this.defaultAddress?.addressNumber) {
            this.allAddresses = [
              this.defaultAddress,
              ...this.allOriginalAddresses.filter(
                (address) => address?.addressNumber !== this.defaultAddress?.addressNumber
              ),
            ];
          } else {
            this.allAddresses = [...this.allOriginalAddresses];
          }
          this.updatePagination();
          this.loader = false;
          this.cd.detectChanges();
        },
        error: (err: any) => {
          this.allOriginalAddresses = [];
          this.updatePagination();
          this.loader = false;
          this.isSearchLoading = true;
          this.cd.detectChanges();
          console.error('Error fetching addresses:', err);
        },
      });
  }

  onRowKeydown(event: KeyboardEvent, index: number) {
    const target = event.target as HTMLElement;
    const tag = target.tagName;
    if (['A', 'BUTTON', 'INPUT', 'TEXTAREA', 'SELECT'].includes(tag)) {
      return; 
    }
    switch (event.key) {
      case 'ArrowUp':
        if (index > 0) {
          this.selectedRowIndex = index - 1;
        }
        event.preventDefault();
        break;
      case 'ArrowDown':
        if (index < this.allAddresses.length - 1) {
          this.selectedRowIndex = index + 1;
        }
        event.preventDefault();
        break;
      case 'Enter':
      case 'Space':
        this.editAddress(this.allAddresses[index]);
        event.preventDefault();
        break;
      default:
        break;
    }
  }
  selectRow(index: number) {
    this.selectedRowIndex = index;
  }

  onRowFocus(index: number) {
    this.focusedRowIndex = index;
  }

  sortByName(): void {
    this.isAscendingName = !this.isAscendingName;
    const defaultAddress = this.defaultAddress;
    const remainingAddress = this.allAddresses.filter(
      (address) => address?.addressNumber !== defaultAddress?.addressNumber
    );
    remainingAddress.sort((a, b) => {
      const nameA = a.firstName || '';
      const nameB = b.firstName || '';
      return this.isAscendingName
        ? nameA.localeCompare(nameB)
        : nameB.localeCompare(nameA);
    });
    if(defaultAddress?.addressNumber) {
      this.allAddresses = [defaultAddress, ...remainingAddress];
    } else {
      this.allAddresses = [...remainingAddress];
    }
    
  }

  sortByDescription(): void {
    this.isAscendingDescritpion = !this.isAscendingDescritpion;
    const defaultAddress = this.defaultAddress;
    const remainingAddress = this.allAddresses.filter(
      (address) => address?.addressNumber !== defaultAddress?.addressNumber
    );
    remainingAddress.sort((a, b) => {
      const descriptionA = a.addressDescription || '';
      const descriptionB = b.addressDescription || '';
      return this.isAscendingDescritpion
        ? descriptionA.localeCompare(descriptionB)
        : descriptionB.localeCompare(descriptionA);
    });
    if(defaultAddress?.addressNumber) {
      this.allAddresses = [defaultAddress, ...remainingAddress];
    } else {
      this.allAddresses = [...remainingAddress];
    }
  }

  sortByAddress(): void {
    this.isAscendingAddress = !this.isAscendingAddress;
    const defaultAddress = this.defaultAddress;
    const remainingAddress = this.allAddresses.filter(
      (address) => address?.addressNumber !== defaultAddress?.addressNumber
    );
    remainingAddress.sort((a, b) => {
      const descriptionA = a.formattedAddress || '';
      const descriptionB = b.formattedAddress || '';
      return this.isAscendingAddress
        ? descriptionA.localeCompare(descriptionB)
        : descriptionB.localeCompare(descriptionA);
    });
    if(defaultAddress?.addressNumber) {
      this.allAddresses = [defaultAddress, ...remainingAddress];
    } else {
      this.allAddresses = [...remainingAddress];
    }
  }

  subscribeSearch() {
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((searchText) => {
        searchText = searchText.toLowerCase();
        let filteredAddresses = this.allOriginalAddresses?.filter((item) => {
          for (let key of this.searchKeys) {
            if (
              item.hasOwnProperty(key) &&
              item[key] !== null &&
              item[key].toString().toLowerCase().includes(searchText)
            ) {
              return true;
            }
          }
          return false;
        });
        if (!searchText) {
          filteredAddresses = [...this.allOriginalAddresses];
        }
        if (
          this.defaultAddress &&
          filteredAddresses.some((address) => address?.addressNumber === this.defaultAddress?.addressNumber)
        ) {
          filteredAddresses = [
            this.defaultAddress,
            ...filteredAddresses.filter(
              (address) => address?.addressNumber !== this.defaultAddress?.addressNumber
            ),
          ];
        }
        this.allAddresses = filteredAddresses;
        this.updatePagination();
        this.cd.detectChanges();
      });
  }

  updateSearch(searchText: string) {
    this.searchText = searchText;
    this.searchSubject.next(searchText);
  }

  closePopup() {
    this.overlayService.closeAllModals();
    this.renderer.removeClass(document.body, 'modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
      this.renderer.removeClass(backdrop, 'fade');
      this.renderer.removeClass(backdrop, 'in');
      this.renderer.removeClass(backdrop, 'modal-backdrop');
    }
    setTimeout(() => {
      this.focusedButton?.focus();
      this.focusedButton = null;
    }, 100);
  }

  hideNewAddressModal() {
    this.showNewAddress = false;
    this.closePopup();
  }

  showValidateAddressModal(addressDetails: any) {
    this.showNewAddress = false;
    this.showValidateAddress = true;
    this.newAndValidateAddressDetails = addressDetails;
  }

  hideValidateAddressModal() {
    this.showValidateAddress = false;
    this.closePopup();
    this.cd.detectChanges();
  }

  refreshAddresses() {
    this.getDataForAddresses();
  }
  
  refreshEditedAddresses(event: any) {
    const { updatedAddress, originalIndex } = event;
    const index = this.allAddresses.findIndex(
      (address) => address.addressNumber === updatedAddress.addressNumber
    );
    updatedAddress.fullName = `${updatedAddress.firstName} ${updatedAddress.lastName}`;
    updatedAddress.street = `${updatedAddress.line1} ${updatedAddress.line2}`; 
    updatedAddress.city = `${updatedAddress.town}`; 
    updatedAddress.region = `${updatedAddress.region}`; 
    updatedAddress.nameCountry = `${updatedAddress.country}`; 
    updatedAddress.postalCode = `${updatedAddress.postalCode}`; 
    if (index !== -1) {
      this.allAddresses[index] = updatedAddress;
    }
    this.updatePagination();
    this.cd.detectChanges();
  }

  deleteAddress(address: any, eleRef?: HTMLElement,event?: Event) {
    event?.preventDefault();
    event?.stopPropagation();
    this.addressToDelete = address;
    if (!this.focusedButton) {
      this.focusedButton = eleRef;
    }
    this.showDeleteAddress = true;
    this.cd.detectChanges;
  }

 onTooltipKey(event: KeyboardEvent): void {
  if (event.code === 'Enter' || event.code === 'Space') {
    event.preventDefault();
    this.showTooltip = !this.showTooltip;
  }
}

onTooltipBlur(): void {
  this.showTooltip = false;
}

  hideDeleteAddressModal(refreshFlag: boolean) {
    if (refreshFlag) {
      this.refreshAddresses();
    }
    this.showDeleteAddress = false;
    this.closePopup();
    this.cd.detectChanges();
  }

  updatePagination() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.allAddresses.length,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.allAddresses.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  editAddress(address: any,buttonRef?: HTMLElement,event?: Event) {
    event?.preventDefault();
    event?.stopPropagation();
    this.addressToEdit = address;
    if (!this.focusedButton) {
    this.focusedButton = buttonRef;
    }
    this.disableEmailForEdit = this.isPatient() && address?.addressNumber === this.orgUnitId;
    this.isEdit = true;
    this.showNewAddress = true;
    this.cd.detectChanges();
  }
}
