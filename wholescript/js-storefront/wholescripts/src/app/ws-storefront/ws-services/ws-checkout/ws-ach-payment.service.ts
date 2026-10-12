import { inject, Injectable } from '@angular/core';
import { selectedEnv } from '../../ws-env/environment';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { GlobalMessageService, GlobalMessageType, OccEndpointsService } from '@spartacus/core';
import { HttpClient } from '@angular/common/http';
import { DELEGO_TOKEN } from '../../ws-constants/ws-api-endpoint.constant';
import { ActivityLogService } from '../ws-activityLog.service';

declare let window: any;

@Injectable({
    providedIn: 'root',
})
export class WsACHPaymentService {
    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);
    protected router = inject(Router);
    protected globalMessageService = inject(GlobalMessageService);
    private readonly SCRIPT_URL = selectedEnv.achPayment.url + 'sdk/sdk.bundle.js';
    isACHPaymentMethod$ = new BehaviorSubject<boolean>(false);
    isActivityLogName$ = new BehaviorSubject<string>('');
    achResponseForPlaceOrder: any;
    RapidPay: any;


    loadACHPaymentScript(): Promise<void> {
        return new Promise((resolve, reject) => {
            if (window.DelegoRapidPay) {
                resolve();
            }
            const script = document.createElement('script');
            script.src = this.SCRIPT_URL;
            script.async = true;
            script.onload = () => {
                resolve();
                return;
            }
            script.onerror = (error) => {
                this.callAchError();
                reject(error)
            };
            document.head.appendChild(script);
        }
        )
    }

    getDelegoToken() {
        return this.http.get(this.occ.buildUrl(DELEGO_TOKEN));
    }

    OpenDelegoIFrame(totalAmount: number, tokenData: any) {
        const DelegoPaymentRequest = {
            currencyCode: 'USD',
            total: totalAmount,
            merchantDisplayName: 'xymogen'
        };
        if (window.DelegoRapidPay) {
            const that = this;
            this.RapidPay = window.DelegoRapidPay.open({
                url: selectedEnv.achPayment.url,
                token: tokenData?.token,
                paymentRequest: DelegoPaymentRequest,
                element: '#iframehost',
                sorg: tokenData?.sorg,
                iframeclass: 'delego-iframe',
                events: {
                    onApplicationInitialized: function () { },
                    onTokenizeSuccess: function (tokenizeResponse: any, signedTokenizeResponse: any) {
                        that.AuthorizeRapidPay(tokenizeResponse, totalAmount);
                    },
                    onFailure: function (error: any) {
                        that.isActivityLogName$.next('ach_token_payment');
                        that.callAchError();
                    }
                }
            });
        }
    }

    AuthorizeRapidPay(tokenizeResponse: any, totalAmount: number) {
        const authorizeRequest = {
            amount: {
                value: totalAmount,
                currency: 'USD'
            },
            payment: tokenizeResponse?.payment,
            address: tokenizeResponse?.billingAddress,
            useVerifiableResponse: false
        };
        if (this.RapidPay) {
            const that = this;
            this.RapidPay.authorize(authorizeRequest).then(function (authorization: any) {
                if (authorization.responseCode != "0" && authorization.responseCode != "DA02" && authorization.responseCode != "000") {
                    that.isActivityLogName$.next('ach_auth_payment');
                    that.callAchError();
                } else {
                    that.achResponseForPlaceOrder = {
                        achCCType: authorization?.payment?.cardType || '',
                        achCCNumber: authorization?.payment?.token || '',
                        achCCName: authorization?.payment?.cardholderName || '',
                        achAuthAmount: authorization?.amount?.value || authorization?.authorizedAmount?.value || '',
                        achAuthDate: authorization?.authorizationDate || '',
                        achCurrency: authorization?.amount?.currency || authorization?.authorizedAmount?.currency || '',
                        achAuthTime: authorization?.authorizationDate || '',
                        achAuthCCNumber: authorization?.authorizationNumber || '',
                        achAuthRefNumber: authorization?.referenceNumber || '',
                        achCCReAuthAmount: authorization?.amount?.value || authorization?.authorizedAmount?.value || '',
                        achCCAuthResponse: authorization?.responseDescription || ''
                    }
                    that.isACHPaymentMethod$.next(false);
                }
            })
        } else {
            this.callAchError();
        }
    }

    callAchError() {
        this.globalMessageService.add(
            "Sorry we're experiencing issues processing ACH payments at this time. Please try again later or choose a different payment method.",
            GlobalMessageType.MSG_TYPE_ERROR,
            5000
        );
        this.router.navigate(['checkout/PaymentMethods']);
        this.isACHPaymentMethod$.next(false);
    }

}
