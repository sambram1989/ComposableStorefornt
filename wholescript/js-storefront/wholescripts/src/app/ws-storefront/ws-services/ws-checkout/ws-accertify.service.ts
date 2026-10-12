import { Injectable } from '@angular/core';
import { selectedEnv } from '../../ws-env/environment';

declare let window: any;
window.CollectorJsData = {
    deviceTransactionId: null,
    ubaId: null,
    pageId: null,
    ubaEvents: null,
    sessionId: '',
  };

@Injectable({
  providedIn: 'root',
})
export class WsAccertifyService {
  private readonly DATA_COLLECTOR_URL = selectedEnv.accertify.url;
 

  generateSessionId(): string {
    const sessionId = 'sess-' + Math.random().toString(36).substr(2, 16);
    window.CollectorJsData.sessionId = sessionId;
    return sessionId;
  }

  loadAccertifyCollectorScript(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (document.getElementById('bcn')) {
        console.log('Accertify script already loaded.');
        resolve();
        return;
      }

      window.CollectorJsData = {
        deviceTransactionId: null,
        ubaId: null,
        pageId: null,
        ubaEvents: null,
        sessionId: '',
      };
  
      const script = document.createElement('script');
      script.id = 'bcn';
      script.type = 'text/javascript';
      script.src = this.DATA_COLLECTOR_URL;
      script.async = true;
      script.setAttribute('dvct', '500');
      script.setAttribute('sid', this.generateSessionId());
      script.setAttribute('dvc', 'a');

      script.onload = () => {
        if (window._bcn?.dvc) {
          window._bcn.dvc.setSubmissionCallback(this.accertifyDataCollectorCallback);
          resolve();
        } 
      };
  
      script.onerror = (error) => reject(error);;
  
      document.head.appendChild(script);
    });
  }


  private accertifyDataCollectorCallback(): void {
    if (window.hasOwnProperty('_bcn') && window._bcn.hasOwnProperty('dvc')) {
      window.CollectorJsData.deviceTransactionId = window?._bcn?.dvc?.getTID().toString() || null;
    }
  }

  getCollectorJsData(): any {
    if (window.hasOwnProperty('_bcn')) {
      window._bcn.flush();

      window.CollectorJsData.ubaId = window?._bcn?.getToken() || null;
      window.CollectorJsData.pageId = window?._bcn?.getPageId() || null;
      window.CollectorJsData.ubaEvents = window?._bcn?.getEvents() || null;
      window.CollectorJsData.sessionId = window?.CollectorJsData?.sessionId || null;   
    }
    return window.CollectorJsData;
  }
}
