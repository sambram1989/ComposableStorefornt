
import { Injectable } from '@angular/core';
import { CMS_FLEX_COMPONENT_TYPE, JSP_INCLUDE_CMS_COMPONENT_TYPE, Occ, OccCmsPageNormalizer } from '@spartacus/core';
import { WS_CMS_FLEX_COMPONENT_TYPE } from './ws-cms-config';

@Injectable({ providedIn: 'root' })
export class WsOccCmsPageNormalizer extends OccCmsPageNormalizer
{
  protected override getFlexTypeFromComponent(component: Occ.Component | any): string {
    if (component.typeCode === CMS_FLEX_COMPONENT_TYPE || component.typeCode === WS_CMS_FLEX_COMPONENT_TYPE) {
      return component.flexType;
    } else if (component.typeCode === JSP_INCLUDE_CMS_COMPONENT_TYPE) {
      return component.uid;
    }
    return component.typeCode;
  }
}
