import { DestroyRef, inject, Injectable } from '@angular/core';
import {
  MEDPAX_STANDALONE,
  MEDPAX_PRINTED_EXTERIOR,
  MEDPAX_CARTON_PRODUCT,
  MEDPAX_HEADER_PRODUCT,
} from '../../ws-constants/ws-api-endpoint.constant';
import { Order } from '@spartacus/order/root';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductImageNormalizer } from '@spartacus/core';
import { Config } from '@spartacus/core';
import { WsCurrentUserService } from '../state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';


@Injectable({ providedIn: 'root' })
export class WsCartResponseModifier {
  productImageNormalizer = inject(ProductImageNormalizer);
  curuserService = inject(WsCurrentUserService);
  destroyRef = inject(DestroyRef);
  config = inject(Config)
  placedOrder$ = new BehaviorSubject<Order | undefined>(undefined);
  isPlUser =  false;
  isPatientUser = false;

  constructor() {
    this.checkRoleExist('pluser');
    this.checkRoleExist('patient');
  }

  mapCartEntries(cart: any): any {
    if (!cart) return cart;

    const filteredEntryGroups = cart.entryGroups?.filter(
      (group: any) =>
        group?.type !== MEDPAX_STANDALONE &&
        group?.label !== MEDPAX_PRINTED_EXTERIOR
    );

    const medpaxMapping = filteredEntryGroups?.reduce((acc: any[], group: any, groupIndex: number) => {
      let shippingDelayFlag = false;
      group.entries?.map((shippingEntry: any) => {
        if(shippingEntry?.hidden && (shippingEntry?.product?.stock?.forceOutOfStock || shippingEntry?.product?.stock?.stockLevelStatus === 'outOfStock')) {
          shippingDelayFlag = true;
        }
      })
      if (group?.entries?.some((entry: any) => entry?.product?.code === MEDPAX_CARTON_PRODUCT)) {
        const entryNumbers = group?.entries?.map((entry: any) => entry?.entryNumber);
        const subEntries = filteredEntryGroups?.filter((entry: any) =>
          entryNumbers.includes(entry.entryGroupNumber)
        );
        const detailsGroup = {
          ...group,
          entries: group?.entries?.filter((entry: any) => entry?.product?.code !== MEDPAX_HEADER_PRODUCT),
        };
        let updatedActualDetails = cart?.entries?.find((item: any) => item?.entryNumber === group?.entryGroupNumber);
        const hiddenEntries = group?.entries?.find((entry: any) => entry?.hidden);
      
        if(hiddenEntries) {
          if (updatedActualDetails && updatedActualDetails.product) {
            updatedActualDetails = {
              ...updatedActualDetails,
              product: {
                ...updatedActualDetails.product,
               images: this.productImageNormalizer.normalize(hiddenEntries?.product?.images),
                no_images: {
                  url: this.isPlUser || this.isPatientUser ? 'assets/images/private-label/pl-generic-icon.png' : `/assets/images/home/medpax-carton.png`,
                },
              }
            };
          }
        }
        
        const basePrice = subEntries.reduce((sum: any, group: any) => {
          const groupTotal = group.entries.reduce((groupSum: any, entry: any) => {
            return groupSum + entry.basePrice?.value;
          }, 0);
          return sum + groupTotal;
        }, 0);
          const actualDetails = {
            ...updatedActualDetails,
            basePrice: {
              ...updatedActualDetails.basePrice,
              value: basePrice,
            },
          };

          acc.push({
            medpaxLabel: `Medpax ${groupIndex + 1}`,
            details: detailsGroup,
            entryNumbers,
            entryNumber: group.entryGroupNumber,
            actualDetails: {
              ...actualDetails,
              medpaxName: actualDetails?.medpaxName || detailsGroup?.label,
              shippingDelayFlag,
            },
            isMedPax: true,
            subEntries,
          });
        }
        return acc;
      },
      []
    );

    const updatedEntries = cart?.entries?.filter(
      (entry: any) =>
        !medpaxMapping?.some(
          (medpax: any) =>
            entry?.entryNumber >= medpax?.entryNumber &&
            entry?.entryNumber <= medpax?.entryNumber + 99
        )
    );

    const updatedEntriesWithImages =  [...updatedEntries]?.map((entry: any) => {
      if (!entry?.product?.images) {
        return {
          ...entry,
          product: {
            ...entry.product,
            no_images: {
              url: `/assets/images/recomendation/default-image.png`,
            },
          },
        };
      }
      return entry;
    });

    const newEntries = [
      ...(updatedEntriesWithImages || []),
      ...(medpaxMapping || []),
    ]?.sort((a: any, b: any) => a?.entryNumber - b?.entryNumber);

    return { ...cart, entries: newEntries || [] };
  }

    checkRoleExist(role: string) {
       this.curuserService.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
         this.isPlUser = res;
         this.isPatientUser = res;
       });
    }
}
