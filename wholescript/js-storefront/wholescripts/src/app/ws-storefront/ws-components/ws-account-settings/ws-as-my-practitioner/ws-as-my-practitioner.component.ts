import { ChangeDetectorRef, Component, DestroyRef, inject, OnInit } from '@angular/core';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { GlobalMessageService, GlobalMessageType } from '@spartacus/core';

@Component({
  selector: 'ws-as-my-practitioner',
  templateUrl: './ws-as-my-practitioner.component.html',
  styleUrl: './ws-as-my-practitioner.component.scss',
})
export class WsAsMyPractitionerComponent implements OnInit {
  private accountSettingsService = inject(AccountSettingsService);
  private cd = inject(ChangeDetectorRef);
  private destroyRef = inject(DestroyRef);

  protected globalMessageService = inject(GlobalMessageService);

  practitionerForm!: FormGroup;
  isSubmitting: boolean = false;
  updatePractitionerError: boolean = false;
  errorMessage: string = '';
  isFormSubmitted: boolean = false;
  isCollapsed: boolean = true;
  isDetails: boolean = true;
  practitionerDetails: any[] = [];

  ngOnInit(): void {
    this.practitionerForm = new FormGroup({
      referralCode: new FormControl('', [
        Validators.required,
        Validators.maxLength(60),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.maxLength(35),
      ]),
    });
    this.fetchPractitionerDetails();
  }

  fetchPractitionerDetails(): void {
    this.accountSettingsService
      .getMyPractitioner()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.practitionerDetails = res || '';
          if(!this.isCollapsed) {
            this.toggleCollapse();
          }
          this.cd.detectChanges();
        },
      });
  }
    onSaveChanges(): void {
      this.isFormSubmitted = true;
      if (this.practitionerForm.valid) {
        this.isSubmitting = true;
        this.updatePractitionerError = false;
        this.errorMessage = '';

      const payload = this.practitionerForm.value;

      this.accountSettingsService
        .updateMyPractitioner(payload)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: () => {
            this.globalMessageService.add(
              'Changed Successfully',
              GlobalMessageType.MSG_TYPE_INFO,
              3000
            );
            this.fetchPractitionerDetails();
            this.isSubmitting = false;
            this.isFormSubmitted = false; 
            this.practitionerForm.markAsUntouched();
            this.cd.detectChanges();
          },
          error: (error) => {
            this.isSubmitting = false;

            if (error.status === 400 || error.status === 404) {
              this.errorMessage = 'Invalid referral code and last name';
            }

            this.updatePractitionerError = true;
            this.cd.detectChanges();
          },
        });
      }
      else {
    this.practitionerForm.markAllAsTouched();
    }
    }

  hasError(field: string, errorType: string): boolean {
    const control = this.practitionerForm.get(field);
    return (
      control &&
      control.hasError(errorType) &&
      this.isFormSubmitted
    );
  }

  toggleCollapse() {
    this.isCollapsed = !this.isCollapsed;
  }

  toggleCollapsed() {
    this.isDetails = !this.isDetails;
  }
}
