export type ShopCategory  = {
  popularByCategory: PopularByCategory[];
  popularCategories: PopularCategory[];
};

export type PopularCategory = {
  code: string;
  name?: string;
  url?: string;
  categoryUrl?: string,
  categoryImage?: ImageUrl;
  image?: Image;
}

export type PopularByCategory = {
  code: string;
  name?: string;
  products: Product[];
  url?: string;
  categoryUrl?: string,
  categoryImage?: ImageUrl;
  selected?: boolean;
}

export type ImageUrl = {
  url: string;
}

export type Product = {
  baseOptions: any[];
  code: string;
  configurable: boolean;
  images?: Image[];
  manufacturer: string;
  name: string;
  purchasable: boolean;
  url: string;
}

export type Image = {
  altText?: string;
  format?: string;
  imageType?: string;
  url?: string;
}

export type ShopTopBrand = {
  topBrands: TopBrandItems[];
}

export type TopBrandItems =  {
  brandImage?: BrandImage;
  brandLogo?: BrandImage;
  brandUrl?: string;
  code?: string;
  description?: string;
  shortDescription?: string;
  name?: string;
}

export type BrandImage = {
  url: string;
}
