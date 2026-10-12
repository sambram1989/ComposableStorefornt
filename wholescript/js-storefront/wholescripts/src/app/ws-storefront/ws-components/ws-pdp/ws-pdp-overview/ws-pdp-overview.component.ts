import { BreakpointObserver } from '@angular/cdk/layout';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { Config, OccEndpointsService, PageMeta } from '@spartacus/core';
import { CurrentProductService, SeoMetaService } from '@spartacus/storefront';
import { map, tap } from 'rxjs';
import { WsSeoMetaService } from '../../../ws-services/seo/ws-seo.service';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { SEO_CONSTANT_KEY } from '../../../ws-constants/ws-api-endpoint.constant';

@Component({
    selector: 'ws-ws-pdp-overview',
    templateUrl: './ws-pdp-overview.component.html',
    styleUrl: './ws-pdp-overview.component.scss',
})
export class WsPdpOverviewComponent {
    private seoService = inject(WsSeoMetaService);
    private router = inject(Router);
    private occ = inject(OccEndpointsService);
    private location = inject(Location);

    filteredFeatures: any[] = [];
    categories: any[];
    isMobile = false;
    doctorReferenceSheetURL: string;
    supplementFactsUrl: string;
    overviewExpanded = 0;
    openedAccordionId = signal<string | null>(null);

    constructor(
        private currentProductService: CurrentProductService,
        private bObserver: BreakpointObserver,
        private http: HttpClient,
        private config: Config,
        private cdr: ChangeDetectorRef
    ) {
        this.bObserver.observe('(max-width: 768px)').subscribe((result) => {
            this.isMobile = false;
            if (result.matches) {
                this.isMobile = true;
            }
            this.cdr.detectChanges(); 
        });
    }

    product$ = this.currentProductService.getProduct().pipe(
        tap((res: any) => {
            const baseUrl = this.config.backend?.occ?.baseUrl
                ? this.config.backend?.occ?.baseUrl
                : '';

            this.doctorReferenceSheetURL = `${baseUrl}${res?.doctorReferenceSheet?.url}`;
            this.supplementFactsUrl = `${baseUrl}${res?.supplementFacts?.url}`;
            // Extract the relevant classifications
            const relevantClassification = res?.classifications?.find(
                (classification: any) =>
                    classification?.code === 'pimClassificationDataClass'
            );

            if (relevantClassification) {
                // List of codes to match
                const requiredCodes = [
                    'PIMClassification/1.0/pimClassificationDataClass.directions',
                    'PIMClassification/1.0/pimClassificationDataClass.storage',
                    'PIMClassification/1.0/pimClassificationDataClass.formulatedtoexclude',
                    'PIMClassification/1.0/pimClassificationDataClass.otheringredients',
                    'PIMClassification/1.0/pimClassificationDataClass.trademarks',
                ];

                // Filter the features based on the required codes
                this.filteredFeatures = relevantClassification?.features.filter(
                    (feature: any) => requiredCodes?.includes(feature.code)
                );
            }
            this.parseSeoUpdates(res);
        })
    );

    downloadPdf() {
        this.pdf().subscribe(
            {
                next: (data: Blob) => {
                    const file = new Blob([data], { type: 'application/pdf' });
                    const fileURL = URL.createObjectURL(file);
                    var a = document.createElement('a');
                    a.href = fileURL;

                    a.download = 'DRS.pdf';
                    document.body.appendChild(a);
                    a.click();
                },
                error: (error) => {
                    console.error('error: ', error);
                }
            }
        );
    }

    pdf() {
        let headers = new HttpHeaders();
        headers = headers.set('Accept', 'application/pdf');

        return this.http.get(this.doctorReferenceSheetURL, {
            headers: headers,
            responseType: 'blob',
        });
    }

    parseSeoUpdates(response: any) {
        const refactored = response.classifications.find(
            (item: any) => item.code == SEO_CONSTANT_KEY);

        if (!refactored) {
            console.error("Refactored data not found.");
            return;
        }

        const metaData: any = {};

        const getFeatureValue = (name: string): string | undefined => {
            const feature = refactored.features.find((item: any) => item.name.includes(name));
            return feature?.featureValues?.[0]?.value;
        };
        
        const canonicalUrl = window.location.href+'/'+getFeatureValue('Search engine friendly page name') ;

        metaData.title = getFeatureValue('Title') ?? response?.name;
        metaData.description = getFeatureValue('Description');
        metaData.image = getFeatureValue('Image');
        metaData.robots = getFeatureValue('Keywords').split(',');
        metaData.keywords = getFeatureValue('Keywords');
        metaData.canonicalUrl = canonicalUrl ;
        this.seoUpdate(metaData);
    }

    seoUpdate(meta: PageMeta) {
        this.seoService.customWrapper(meta);
    }

    expandSection(type: any) {
        this.overviewExpanded = type;
    }

    toggleAccordion(id: string): void {
        this.openedAccordionId.set(this.openedAccordionId() === id ? null : id);
    }
      
    isAccordionOpen(id: string): boolean {
        return this.openedAccordionId() === id;
    }

}
