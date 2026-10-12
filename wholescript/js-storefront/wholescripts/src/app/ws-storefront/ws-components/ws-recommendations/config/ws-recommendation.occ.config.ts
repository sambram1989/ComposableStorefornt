export const wsRecommendationOccconfig = {
    backend: {
        occ: {
            endpoints: {
                fetchWholesaleRecommendations: 'users/${userId}/recommendation',
                fetchRecommendationsForReview: 'users/${userId}/recommendation/${recoId}',
                sendRecommendationsForReview: 'users/${userId}/recommendation/${recoId}/share'
            }
        }
    }
}