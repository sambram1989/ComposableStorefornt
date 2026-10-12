import { NgModule } from '@angular/core';
import { CdcConfig, CdcRootModule, CDC_FEATURE } from '@spartacus/cdc/root';
import { provideConfig } from '@spartacus/core';
import { selectedEnv } from '../../../ws-storefront/ws-env/environment';

@NgModule({
  declarations: [],
  imports: [CdcRootModule],
  providers: [
    provideConfig({
      featureModules: {
        [CDC_FEATURE]: {
          module: () => import('@spartacus/cdc').then((m) => m.CdcModule),
        },
      },
    }),
    provideConfig(<CdcConfig>{
      cdc: [
        {
          baseSite: 'wholescripts',
          javascriptUrl: `https://cdns.us1.gigya.com/js/gigya.js?apikey=${selectedEnv.cdcKey}`,
          sessionExpiration: 86400,
        },
      ],
    }),
  ],
})
export class CdcFeatureModule {}
