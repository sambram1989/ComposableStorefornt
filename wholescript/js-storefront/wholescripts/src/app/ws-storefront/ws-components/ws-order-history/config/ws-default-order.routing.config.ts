import { RoutingConfig } from '@spartacus/core';

export const WsDefaultOrderRoutingConfig: RoutingConfig = {
  routing: {
    routes: {
      orderDetails: {
        paths: ['order/details/:salesOrder'],
        paramsMapping: { order: 'salesOrder' },
      },
    },
  },
};
