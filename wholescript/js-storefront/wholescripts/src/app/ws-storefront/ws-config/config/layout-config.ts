import { LayoutConfig } from '@spartacus/storefront';

export const wsLayoutConfig: LayoutConfig = {
  layoutSlots: {
    header: {
      lg: {
        slots: [
          'PreHeader',
          'SiteContext',
          'SiteLinks',
          'SiteLogo',
          'NavigationBar',
          'SearchBox',
          'SiteLogin',
          'wsMyFavoriteIcon',
          'MiniCart'
        ],
      },

      slots: ['PreHeader', 'mobileSiteLogo', 'SearchBox', 'SiteLogin'],
    },
    navigation: {
      lg: { slots: [] },
      slots: ['SiteLogin', 'NavigationBar', 'SiteContext', 'SiteLinks'],
    },
    footer: {
      slots: ['JoinNewsLetterSlot', 'Footer', 'WholeScriptsScheduleADemo'],
    },
    GuestHomePageTemplate: {
      slots: [
        'TopLeftSlot',
        'TopBodySlot',
        'PatientPractitionerSlot',
        'BestBrands',
        'Testimonials',
        'MedPax',
        'WholeHealth',
      ],
    },
    PractiotionerRecommendationTemplate: {
      slots: ['myRecommendationListBody'],
    },
    PatientAccountTemplate: {
      slots: ['BodyConentSlot'],
    },
    PractiotionerRecommendationDetailTemplate: {
      slots: ['section2'],
    },
    PractiotionerRecommendationEditTemplate: {
      slots: ['section2'],
    },
    ContentPage1Template: {
      slots: ['Section2A', 'Section2B'],
    },
    CategoryPageTemplate: {
      pageFold: 'Section2',
      slots: ['Section1', 'Section2', 'Section3'],
    },
    ProductListPageTemplate: {
      slots: ['ProductListingCategoryHeader','ProductLeftRefinements', 'ProductListSlot'],
    },
    ProductGridPageTemplate: {
      slots: ['ProductLeftRefinements', 'ProductGridSlot'],
    },
    // SearchResultsListPageTemplate: {
    //   slots: [
    //     'Section2',
    //     'ProductLeftRefinements',
    //     'SearchResultsListSlot',
    //     'Section4',
    //   ],
    // },
    SearchResultsGridPageTemplate: {
      slots: [
        'Section2',
        'ProductLeftRefinements',
        'SearchResultsGridSlot',
        'Section4',
      ],
    },
    ProductDetailsPageTemplate: {
      lg: {
        pageFold: 'UpSelling',
      },
      pageFold: 'Summary',
      slots: [
        'Summary',
        'wsPDPPageProductOverview',
        'wsPDPPageProductFamilyReferrence',
        'wsPDPPageProductFamily',
        'UpSelling',
        'CrossSelling',
        'Tabs',
        'PlaceholderContentSlot',
      ],
    },
    CartPageTemplate: {
      slots: ['TopContent', 'CenterRightContentSlot', 'EmptyCartMiddleContent'],
    },
    AccountPageTemplate: {
      slots: [
        'wsOrderDetailsRightBodyFlex',
        'wsOrderHistoryFullBodyFlex',
        'SideContent'
      ],
    },
    PatientAccountSettingTemplate: {
      slots: ['PatientPayment'],
    },
    LoginPageTemplate: {
      slots: ['LeftContentSlot', 'RightContentSlot'],
    },
    ErrorPageTemplate: {
      slots: ['TopContent', 'MiddleContent', 'BottomContent'],
    },
    OrderConfirmationPageTemplate: {
        slots: [
          'BodyContent'
        ]
    },
    MultiStepCheckoutSummaryPageTemplate: {
      slots: ['TopContent', 'BodyContent', 'SideContent', 'BottomContent'],
    },
    CheckoutLoginPageTemplate: {
      slots: ['RightContentSlot'],
    },
    MyAccountViewPageTemplate: {
      slots: ['LeftContentSlot', 'RightContentSlot'],
    },
    PractitionerMyPatientsPageTemplate: {
      slots: [
        'practitionerMyPatientsPageBodySlot',
        'recommendationMyPatientsPageBodySlot',
      ],
    },

    myProtocolPageTemplate: {
      slots: [
        'myProtocolListBody',
        'myProtocolEditPageBody',
        'myProtocolDetailPageBody',
        'myProtocolCompletePageBody',
        'myProtocolReviewPageBody',
        'shareProtocolSuccessBody',
        'shareProtocolPageBody',
        'wsProtocolListPageBody',
      ],
    },
    AdvertisingTemplate: {
      slots: [
        'HeaderPara',
        'AdvertisingNav',
        'Advertising',
        'NewBrand',
        'PromoBanner',
      ],
    },
    APIResourcesTemplate: {
      slots: [
        'BodyHeader',
        'BodyContent-APIResources',
        'ApiNavigation',
        'BodyContent-ExternalOrder',
        'BodyContent-EMR',
      ],
    },
    PractiotionerRecommendationReviewTemplate: {
      slots: ['section2'],
    },
    PractiotionerRecommendationConfirmationTemplate: { slots: ['section1'] },
    PatientDirectPageTemplate: {
      slots: ['BodySlot'],
    },
    ResellingPageTemplate: {
      slots: [
        'TopBannerSlot',
        'ResellCardsSlot',
        'ResellAvalaraSlot',
        'ResellProcessSlot',
      ],
    },
    PatientGuestPageTemplate: {
      slots: [
        'Section1',
        'Section2',
        'Section3',
        'Section4',
        'Section5',
        'Section6',
      ],
    },
    PractitionersPageTemplate: {
      slots: [
        'TopSlot',
        'IntroSlot',
        'XymoSlot',
        'RecommendationsSlot',
        'MedPaxSlot',
        'DispensarySlot',
        'WholehealthSlot',
      ],
    },
    PrivacyPolicyPageTemplate: {
      slots: ['PrivacyPolicyHeading', 'PrivacyPolicyContent', 'WholeHealth'],
    },
    TermsAndConditionsTemplate: {
      slots: [
        'Section1',
        'Section2',
        'Section3',
        'Section4',
        'Section5',
        'Section6',
      ],
    },
    PersonalInfoTemplate: {
      slots: [
        'DonotSellHeader',
        'BodyOne',
        'BodyTwo',
        'BodyThree',
      ],
    },
    CaliforniaPrivacyTemplate: {
      slots: [
        'CaliforniaPrivacyHeader',
        'BodyOne',
        'BodyTwo',
        'BodyThree',
      ],
    }, 
    PatientShopPageTemplate: {
      slots: [
        'Section1',
        'Section3A',
        'Section2',
        'Section3',
        'Section4',
        'Section5',
      ],
    },
    faqPageTemplate: {
      slots: [
        'faqTopBanner',
        'faqSearchText',
        'faqQuestionBody',
        'faqContactUS',
        'faqWholehealth',
      ],
    },
    wsIntegrationPageTemplate: {
      slots: [
        'wsIntegrationsHeadingslot',
        'wsIntegrationsContentSection2',
        'wsIntegrationsFlexSection3slot',
        'practiceBetter',
        'charmHealth',
        'cerbo',
        'getHealthy',
        'wellProZ',
        'shopify',
        'betterHealth',
        'storeByWrkout',
        'odxOptimalDX',
        'GeneticsComapany',
        'cellScienceSystems',
        'PracticeBetterpageHeadingTop',
        'PracticeBetterpageHeading',
        'PracticeBetterpageBody',
        'PracticeBetterBodyContentSection2',
        'PracticeBetterBodyContentSection3',
        'CharmHealthHeadingTop',
        'CharmHealthPageHeading',
        'CharmHealthBodyContent',
        'CharmHealthBodyContentSection2',
        'CharmHealthBodyContentSection3',
        'CerboHeadingTop',
        'CerbopageHeading',
        'CerbopageBody',
        'CerboBodyContentSection2',
        'CerboBodyContentSection3',
        'GetHealthyHeadingTop',
        'GetHealthypageHeading',
        'GetHealthypageBody',
        'GetHealthyBodyContentSection2',
        'GetHealthyBodyContentSection3',
        'GetHealthyBodyContentSection4',
        'GetHealthyBodyContentSection5',
        'WellProzpageHeading',
        'WellProzpageBody',
        'WellProzBodyContentSection2',
        'WellProzBodyContentSection3',
        'WellProzBodyContentSection4',
        'WellProzBodyContentSection5',
        'WRKOUTpageHeading',
        'WRKOUTpageBody',
        'GeneticsHeadingTop',
        'GeneticspageHeading',
        'GeneticspageBody',
        'CellSciencepageHeading',
        'CellScienceBodyContentSection1',
        'CellScienceBodyContentSection2',
        'CellScienceBodyContentSection3',
        'CellScienceBodyContentSection4',
        'ShopifypageHeading',
        'ShopifypageBody',
        'ShopifyBodyContentSlot2',
        'ShopifyBodyContentSlot3',
        'ShopifyBodyContentSlot4',
        'ShopifyBodyContentSlot5',
        'OptimalDXHeadingTop',
        'OptimalDXPageHeading',
        'OptimalDXBodyContent',
        'BetterHealthHeadingTop',
        'BetterHealthPageHeading',
        'BetterHealthBodyContent',
        'BetterHealthBodyContentSection2',
        'BetterHealthBodyContentSection3',
        'BetterHealthBodyContentSection4'
      ],
    },
    ProductUpdatesPageTemplate: {
      slots: ['TitleSlot', 'DescriptionSlot', 'BodySlot'],
    },
    ContactUsTemplate: {
      slots: [
        'secton1',
        // 'section2Left',
        'section2Right',
        'section3',
        'section4',
      ],
    },
    PatientMyAccountTemplate: {
      slots: ['myaccountRightBodyFlexSlot'],
    },
    MedpaxPageTemplate: {
      slots: [
        'Section2A', 
        'Section2'
        // 'Section1', 'Section2', 'Section3', 'Section4'
      ],
    },
    PractitionerAccountSettingTemplate: {
      slots: [],
    },
    PractitionerMyFavouritesListpageTemplate: {
      slots: ['MyFavoritesProductList'],
    },
    patientDirectHubTemplate: {
      slots: [
        'patientDirectHubHeadingAndNav',
        'patientDirectHubTabContainerslot',
        'patientDirectHubSection2Body',
      ],
    },
    FindPractitionerForPatientTemplate: {
      slots: []
    },

    WholeScriptsMagazineTemplate: {
      slots: [
        'WholeScriptsMagazineHeadingSection1',
        'WholeScriptsMagazineContentSection1',
        'WholeScriptsMagazineContentSection2',
        'wsMagazinePastIssuesContentSection',
        'WholeScriptsMagazineVolume4Issue3Section',
        'WholeScriptsMagazineVolume4Issue2Section',
        'WholeScriptsMagazineVolume3Issue3Section',
        'WholeScriptsMagazineVolume3Issue2Section',
        'WholeScriptsMagazineVolume3Issue1Section',
        'WholeScriptsMagazineVolume2Issue4Section',
        'WholeScriptsMagazineVolume2Issue3Section',
        'WholeScriptsMagazineVolume2Issue2Section',
        'WholeScriptsMagazineVolume1Issue1Section',
        'WholeScriptsMagazineVolume2Issue1Section',
        'WholeScriptsMagazineLotEasierContentSection',
        'wsMagazineLotEasierContentParaComponent',
      ]
    },
    WebinarPageTemplate: {
      slots: ['WebinarListBodyContent',
        'WebinarProfileBodyContent',
        'WebinarDetailsBodyContent']
    },
    SearchResultsListPageTemplate: {
      slots: [
        'SearchResultsListSlot'
      ]
    },
    AutoshipsTemplate: {
      slots: []
    },
    WSPractitionerDashboardPageTemplate:{
      slots:['BodySection']
    },
    PractitionerMyAccountTemplate:{
      slots:['Section1']
    },
    privateLabelPageTemplate: {
      slots: [
        'privateLabelBody'
      ]
    },
    IntegrationsHubTemplate: {
      slots: [
        'Section3'
      ]
    },
    SpecialOffersPageTemplate: {
      slots: [
        'Section1',
        'Section2'
      ]
    },
    ShopifyCatalogPageTemplate: {
      slots: [
        'Section2'
      ]
    },
    PatientRecommendationTemplate: {
      slots: [
        'myRecommendationListBody'
      ]
    },
    emailverifiedPageTemplate:{
      slots:['emailverifiedListBody']
    },
    AboutUsPageTemplate: {
      slots:['AboutUsSection1', 'AboutUsSection2', 'AboutUsSection3', 'AboutUsSection4']
    }
  },
};
