import { RoutingConfig } from '@spartacus/core';

export const wsRecommendationRoutingConfig: RoutingConfig = {
  routing: {
    routes: {
      wsRecommendationReview: {
        paths: ['Recommendation/RecommendationReview/:id'],
        paramsMapping: { id: 'id' },
      },
      wsRecommendationComplete: {
        paths: ['Recommendation/RecommendationComplete/:id'],
        paramsMapping: { id: 'id' },
      },
      wsBasicRecommendation: {
        paths: ['Recommendation/BasicRecommendation/:id'],
        paramsMapping: { id: 'id' },
      },
    },
  },
};
