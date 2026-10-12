import { LayoutConfig } from '@spartacus/storefront';
import { WsAsmMainUiComponent } from './ws-asm-main-ui.component';

export const WsAsmLayoutConfig: LayoutConfig = {
  launch: {
    ASM: {
      outlet: 'cx-storefront',
      component: WsAsmMainUiComponent,
    },
  },
};
