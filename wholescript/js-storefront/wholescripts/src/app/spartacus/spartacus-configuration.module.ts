import { NgModule } from '@angular/core';
import { translationChunksConfig, translations } from "@spartacus/assets";
import { FeaturesConfig, I18nConfig, OccConfig, PageMetaConfig, provideConfig, provideDefaultConfig, SiteContextConfig } from "@spartacus/core";
import { defaultB2bOccConfig } from "@spartacus/setup";
import { defaultCmsContentProviders, mediaConfig } from "@spartacus/storefront";
import { wsLayoutConfig } from '../ws-storefront/ws-config/config/layout-config';
import { selectedEnv } from '../ws-storefront/ws-env/environment';

@NgModule({
  declarations: [],
  imports: [
  ],
  providers: [provideConfig(wsLayoutConfig),
     provideConfig(mediaConfig),
      ...defaultCmsContentProviders,
       provideConfig(<OccConfig>{
    backend: {
      occ: {
        baseUrl: 'https://localhost:9002',
        endpoints:{
          cart:'users/${userId}/carts/${cartId}?fields=FULL',
          carts:'users/${userId}/carts?fields=FULL',
          activityLog: 'users/${userId}/activity'
        }
      }
    },
  }), provideConfig(<SiteContextConfig>{
    context: {
      baseSite: ['wholescripts']
    },
  }), provideConfig(<I18nConfig>{
    i18n: {
      resources: translations,
      chunks: translationChunksConfig,
      fallbackLang: 'en'
    },
  }), provideConfig(<FeaturesConfig>{
    features: {
      level: '2211.25'
    }
  }),
  provideConfig(defaultB2bOccConfig),
  provideDefaultConfig(
    <PageMetaConfig>{
      pageMeta:{
        resolvers:[
          {
            property: 'title',
            method: 'resolveTitle',
            disabledInCsr: false,
          },
          {
            property: 'description',
            method: 'resolveDescription',
            disabledInCsr: false,
          },
          {
            property: 'keywords',
            method: 'resolveKeywords',
            disabledInCsr: false,
          }
        ]
      }
    }
  )
 ]
})
export class SpartacusConfigurationModule { }
