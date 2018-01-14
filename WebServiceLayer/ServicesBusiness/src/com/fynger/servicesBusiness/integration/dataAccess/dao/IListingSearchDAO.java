/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;
import java.util.Map;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingReviewsData;

/**
 * @author Rishi
 *
 */
public interface IListingSearchDAO {
	
	public Map<Integer, ListingRatingLikesData> loadListingsRatingAndLikes(String listingIds) throws DataAccessException;
	
	public Map<Integer, Integer> loadListingsReviews(String listingIds) throws DataAccessException;
	
	public boolean rateListing(ListingRatingLikesData listingRatingLikesData) throws DataAccessException;
	
	public boolean reviewListing(ListingReviewsData listingReviewsData) throws DataAccessException;
	
	public List<ListingReviewsData> loadReviews(String listingId) throws DataAccessException;
	
	public boolean updateLikesCount(int listingId) throws DataAccessException;

}
