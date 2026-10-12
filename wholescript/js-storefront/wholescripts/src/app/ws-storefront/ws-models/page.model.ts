export interface slotMarkup {
  slot: string;
  components: any[];
}
export enum HOMEPAGE_SLOTS {
  SECTION2 = 'PatientPractitionerSlot',
}
export const PRIMARY_SLOT = { slot: '', components: [] as any[] };

export const PRACTITIONER_SLOT = [
  'PractitionerComponent1',
  'PractitionerComponent2',
  'PractitionerComponent3',
  'PractitionerComponent4',
  'PractitionerLearnMoreLinkComponent',
  'PractitionerRegisterLinkComponent',
];
export const PATIENT_SLOT = [
  'PatientComponent1',
  'PatientComponent2',
  'PatientComponent3',
  'PatientComponent4',
  'PatientLearnMoreLinkComponent',
  'PatientRegisterLinkComponent',
  'PatientFindPractitionerLinkComponent',
  'PatientComponent5',
];
export const BRANDS_SLOT = [
  'BestBrandsParagraph1Component',
  'OneStopShopParagraph1Component',
  'OneStopShopParagraph2Component',
  'FullCatalogButtonComponent',
];
export const BRAND_ICONS_SLOT = [
  'Xymogen-Icon',
  'Biocidin-Iocn',
  'Nordic-Icon',
  'AllergyResearch-Icon',
  'QuickSilver-Icon',
  'Humann-Icon',
];
export const MEDPAX_CONTENT_SLOT = [
  'RecommendMedpaxButttonComponent',
  'MedPaxRegisterLinkComponent',
  'FindPractitionerButtonComponent',
  'MedPaxLogoComponent',
  'MedPaxLogoVerticalComponent',
  'MedpaxPractitionerComponent',
  'MedpaxPatientComponent',
  'medpaxSection3',
  'medpaxSection4'
];

export const CONDITION_SPECIFIC_MEDPAX_CONTENT_SLOT = [
  'condSpecificSection1',
  'condSpecificSection2',
  'condSpecificSection3'
];

export const ACCOUNT_SETTINGS_SLOT = [
  'PractitionerTabContent'
]

export const CONDITION_SPECIFIC_MEDPAX_CONTENT_SPECIFIC_SLOT = [
  'medpaxEnergySection1',
  'medpaxEnergySection2',
  'medpaxEnergySection3',
  'medpaxEnergySection4',
  'medpaxBeautySection1',
  'medpaxBeautySection2',
  'medpaxBeautySection3',
  'medpaxBeautySection4',
  'medpaxGutSection1',
  'medpaxGutSection2',
  'medpaxGutSection3',
  'medpaxGutSection4',
  'medpaxImmuneSection1',
  'medpaxImmuneSection2',
  'medpaxImmuneSection3',
  'medpaxImmuneSection4',
  'medpaxsleepSection1',
  'medpaxsleepSection2',
  'medpaxsleepSection3',
  'medpaxsleepSection4'
];

export const TESTIMONIALS_SLOT = ['TestimonialsCarouselListComponent'];
export interface FACET_FILTER {
  title: string;
  filters: { code: string; value: string }[];
  config: {
    isOpen: boolean;
    viewMore: boolean;
  };
}
export const LEFT_NAV_PATIENT = [
  { link: 'Order History', url: '/my-account/orders', icon: 'fa-box-open' },
  { link: 'My Favorites', url: '/my-account/favourites', icon: 'fa-heart' },
  { link: 'Recommendations', url: '/Recommendation/MyRecommendations', icon: 'fa-prescription-bottle' },
  { link: 'My Practitioner', url: '/my-account/practitioner', icon: 'fa-user-md' },
  { link: 'Practitioner Protocols', url: '/my-account/practitioner-protocols', icon: 'fa-solid fa-clipboard' },
  { link: 'My Autoships', url: '/my-account/my-auto-ships', icon: 'fa-redo-alt' },
  { link: 'Special offers', url: '/my-account/special-offers', icon: 'fa-tag' },
  { link: 'My Addresses', url: '/my-account/addresses', icon: 'fa-address-card' },
  { link: 'Manage Payments', url: '/my-account/manage-Payment', icon: 'fa-credit-card' },
  { link: 'Account Settings', url: '/my-account/account-setting', icon: 'fa-sign-in' },
];
export const API_NAV_LINKS = {
  intro: 'Introduction',
  link1: 'External Order Submission',
  link2: 'EMR Recommendations'
}
export const NAV_LINKS = {
  intro: 'Introduction',
  link1: 'New Brand Onboarding',
  link2: 'Promo Banners'
}
export const EMR_RECOMMEDATIONS = [
  'Authorization Code',
  'Token',
  'Logout',
  'BrandList',
  'CategoryList',
  'ProductList',
  'Product',
  'ProductImage',
  'ProductDRS',
  'PractitionerReferralCodes',
  'PractitionerPatients',
  'PrivateLabelProductList',
  'Recommendations (POST)',
  'Recommendations (GET)',
  'Recommendation (GET)',
];
export const EXTERNAL_ORDERS = [
  'BrandList',
  'CategoryList',
  'Product List',
  'PrivateLabelProductList',
  'Submit',
  'Status',
  'Cancel'
];
export const EMR_RECOMMEDATIONS_CONTENT = {
  sections: [
    {
      heading: 'Authorization Code',
      requestValues: [
        'client_id',
        'redirect_uri',
        'State'
      ]
    },
    {
      heading: 'Token',
      requestValues: [
        'grant_type',
        'username',
        'password',
        'code',
        'refresh_token',
      ],
    },
    {
      heading: 'ProductList',
      requestValues: [
        'search',
        'instockonly',
        'brandname'
      ],
    },
    {
      heading: 'ProductList',
      requestValues: [
        'search',
        'instockonly',
        'brandname'
      ],
      responseValues: [
        'ProductName',
        'Sku',
        'MedPaxSku',
        'MedPaxDetails',
        'Categories',
        'Quantity',
        'ReleaseDate',
        'WholesalePrice',
        'RetailPrice',
        'OnlineMAP',
        'UPC',
        'Count',
        'Flavor',
        'DescriptionShort',
        'DescriptionFull',
        'Brand',
        'Weight',
        'ProductImage',
        'SupplementFactsHTML',
        'DefaultDosing',
        'DRS',
        'CountUnit',
        'WeightUnit',
        'Warnings'
      ],
    },
    {
      heading: 'PractitionerReferralCodes',
      responseValues: [
        'ref_code',
        'ref_name',
        'discount',
        'access_level'
      ],
    },
    {
      heading: 'PractitionerPatients',
      responseValues: [
        'Patients',
        'Count',
        'Success',
        'Msg'
      ],
    },
    {
      heading: 'PrivateLabelProductList',
      responseValues: [
        'privateLabelProducts',
        'medPaxPills',
        'privateLabelCartons'
      ]
    },
    {
      heading: 'Recommendations (POST)',
      requestValues: [
        'PractitionerEmail',
        'EmailTo',
        'PatientFirstName',
        'PatientLastName',
        'Notes',
        'ref_code',
        'ref_name',
        'Items',
        'SendEmail'
      ]
    }
  ],
};

export const EXTERNAL_RECOMMEDATIONS_CONTENT = {
  sections: [
    {
      heading: 'PrivateLabelProductList',
      responseValues: [
        'privateLabelProducts',
        'medPaxPills',
        'privateLabelCartons'
      ]
    },
    {
      heading: 'Cancel',
      responseValues: [
        'Success',
        'Msg',
      ]
    },
    {
      heading: 'Submit',
      requestValues: [
        'ShippingMethod',
        'ShippingAddress',
        'Items'
      ],
      responseValues: [
        'OrderNumber',
        'Success',
        'Msg'
      ]
    },
    {
      heading: 'Status',
      responseValues: [
        'OrderNumber',
        'OrderDate',
        'SalesOrder',
        'Status',
        'Tracking',
        'Message',
        'SubTotal',
        'ShipMethod',
        'ShipCharge',
        'Discount',
        'Tax',
        'ServiceFee',
        'OrderTotal'
      ]
    },
    {
      heading: 'Product List',
      requestValues: [
       'search',
        'instockonly',
        'brandname'
      ],
      responseValues: [
        'ProductName',
        'Sku',
        'MedPaxSku',
        'MedPaxDetails',
        'Categories',
        'Quantity',
        'ReleaseDate',
        'WholesalePrice',
        'RetailPrice',
        'OnlineMAP',
        'UPC',
        'Count',
        'Flavor',
        'DescriptionShort',
        'DescriptionFull',
        'Brand',
        'Weight',
        'ProductImage',
        'SupplementFactsHTML',
        'DefaultDosing',
        'DRS',
        'CountUnit',
        'WeightUnit',
        'Warnings'
      ],
    },
  ]
}
