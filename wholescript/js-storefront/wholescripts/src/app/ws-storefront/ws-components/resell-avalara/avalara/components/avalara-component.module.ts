import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AvalaraComponent } from './avalara-component/avalara.component';
import { CmsConfig, provideConfig } from '@spartacus/core';

@NgModule({
  declarations: [AvalaraComponent],
  imports: [CommonModule],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        resellingPageAvalaraFlexComponent: {
          component: () =>
            import('./avalara-component/avalara.component').then(
              (c) => c.AvalaraComponent
            ),
        },
      },
    }),
  ],
})
export class AvalaraComponentModule {}
