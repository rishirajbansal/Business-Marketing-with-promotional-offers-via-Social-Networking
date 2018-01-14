/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantReviewsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionClickArchiveData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionViewArchiveData;

/**
 * @author Rishi
 *
 */
public interface IPromotionsSearchDAO {
	
	public PromotionData loadPromotionDetail(String entityId, String entityType) throws DataAccessException;
	
	public MerchantRatingLikesData loadStoreRatingsAndLikes(String merchantStoreId) throws DataAccessException;
	
	public int storeReviewscount(String merchantStoreId) throws DataAccessException;
	
	public boolean rateMerchantStore(MerchantRatingLikesData merchantRatingLikesData) throws DataAccessException;
	
	public boolean reviewMerchantStore(MerchantReviewsData merchantReviewsData) throws DataAccessException;
	
	public List<MerchantReviewsData> loadReviews(String merchantStoreId) throws DataAccessException;
	
	public boolean validateAndUpdatePromotionStatus(int validityDiff) throws DataAccessException;
	
	public boolean updateLikesCount(int merchantStoreId) throws DataAccessException;
	
	public boolean archiveOnPromotionSearchResults(List<PromotionViewArchiveData> alPromotionViewArchiveData) throws DataAccessException;
	
	public boolean archiveOnPromotionDetail(PromotionClickArchiveData promotionClickArchiveData) throws DataAccessException;

}
