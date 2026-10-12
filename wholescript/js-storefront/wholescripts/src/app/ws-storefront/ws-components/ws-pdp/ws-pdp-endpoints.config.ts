export const wsPDPOccConfig = {
  backend: {
      occ: {
          endpoints: {
            product: {
              promotions: 'products/${productCode}?fields=potentialPromotions(FULL)',
              details:
            'products/${productCode}?fields=code,purchasable,name,summary,price(formattedValue,DEFAULT),images(galleryIndex,FULL),baseProduct,DEFAULT,averageRating,classifications,manufacturer,numberOfReviews,categories(FULL),baseOptions,variantOptions,variantType,stock(DEFAULT),description,availableForPickup,url,priceRange,multidimensional,tags,potentialPromotions(FULL)',
            }
          }
      }
  }
}

