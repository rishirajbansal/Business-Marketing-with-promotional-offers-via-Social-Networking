/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;

/**
 * @author Rishi
 *
 */
public interface IFavouritesSearchDAO {
	
	public boolean addFavourite(UserFavouritesData userFavouritesData) throws DataAccessException;
	
	public UserFavouritesData listFavourites(String username) throws DataAccessException;
	
	public boolean deleteFavourite(UserFavouritesData userFavouritesData) throws DataAccessException;
	
	public List<PromotionData> loadFavouritesResultsList(String username) throws DataAccessException;
	
	public PromotionData loadFavouritePromotionDetail(String promotionId) throws DataAccessException;
	
	public boolean updateUserFavouritePromotions(String promotionIds, String username) throws DataAccessException;
	
	public boolean deleteUserFavouritePromotions(String username) throws DataAccessException;

}
