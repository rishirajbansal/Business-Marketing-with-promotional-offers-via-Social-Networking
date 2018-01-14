/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.ArchiveDataException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.NoReviewsFoundException;
import com.fynger.servicesBusiness.exception.PromotionException;
import com.fynger.servicesBusiness.exception.PromotionIdNotFoundException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOUtility;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantReviewsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionClickArchiveData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionViewArchiveData;

/**
 * @author Rishi
 *
 */
public class PromotionsSearchDAOImpl implements IPromotionsSearchDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(PromotionsSearchDAOImpl.class.getName());
	
	
	private static final String SQL_SELECT_FIND_PROMOTION_BY_ID 				= 	"SELECT * FROM promotion WHERE idpromotion = ?";
	
	private static final String SQL_SELECT_PROMOBASIC_BY_ID 					= 	"SELECT * FROM promobasic WHERE idpromobasic = ?";
	
	private static final String SQL_JOIN_SELECT_BASICSTORE_AND_CATEGORY 		= 	"SELECT * FROM basicstore LEFT OUTER JOIN topcat ON basicstore.idtopcat = topcat.idtopcat WHERE idbasicstore = ?";
	
	private static final String SQL_SELECT_EVENTTAB_BY_ID 						= 	"SELECT * FROM eventtab WHERE ideventtab = ?";
	
	private static final String SQL_SELECT_MERCHANT_STORE_LOAD_RATING_LIKES 	= 	"SELECT * from merchant_rating_likes WHERE merchant_store_id = ?";
	
	private static final String SQL_SELECT_MERCHANT_STORE_REVIEWS_COUNT 		= 	"SELECT merchant_store_id, COUNT(*) as reviews_count FROM merchant_reviews WHERE merchant_store_id = ?  GROUP BY merchant_store_id";
	
	private static final String SQL_SELECT_FIND_LISTING_BY_STOREID 				= 	"SELECT * FROM listing WHERE merchant_store_id = ?";
	
	private static final String SQL_UPDATE_MERCHANT_STORE_RATING 				= 	"UPDATE merchant_rating_likes SET rating = ?, listing_association = ?, last_updated = NOW() WHERE merchant_store_id = ?";
	
	private static final String SQL_INSERT_MERCHANT_STORE_RATING 				= 	"INSERT INTO merchant_rating_likes (merchant_store_id, rating, listing_association, created_on, last_updated) VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_LISTING_LOAD_RATING_LIKES 			= 	"SELECT * from listing_rating_likes WHERE listing_id = ?";
	
	private static final String SQL_UPDATE_LISTING_RATING 						= 	"UPDATE listing_rating_likes SET rating = ?, merchant_association = ?, last_updated = NOW() WHERE listing_id = ?";
	
	private static final String SQL_INSERT_LISTING_RATING 						= 	"INSERT INTO listing_rating_likes (listing_id, rating, merchant_association, created_on, last_updated) VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_USER_PROFILE 						= 	"SELECT picture_path FROM user_profile, user_login WHERE user_login.username = ?";
	
	private static final String SQL_INSERT_MERCHANT_STORE_REVIEW				= 	"INSERT INTO merchant_reviews (merchant_store_id, username, review_text, listing_association, created_on, last_updated) VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_LISTING_REVIEW 						= 	"INSERT INTO listing_reviews (listing_id, username, review_text, merchant_association, created_on, last_updated) VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_LOAD_MERCHANT_STORE_REVIEWS 			= 	"SELECT * FROM merchant_reviews, user_login, user_profile " +
																					"WHERE user_login.login_id = user_profile.login_id " +
																					"AND merchant_reviews.username = user_login.username " +
																					"AND merchant_store_id = ?";
	
	private static final String SQL_SELECT_EXPIRED_PROMOTIONS 					= 	"SELECT * FROM promobasic WHERE TIMESTAMPDIFF(MINUTE, NOW(), validtill)  <= ? AND state = ? ";
	
	private static final String SQL_UPDATE_EXPIRED_PROMOTIONS 					= 	"UPDATE promobasic SET state = ? WHERE TIMESTAMPDIFF(MINUTE, NOW(), validtill)  <= ?";
	
	private static final String SQL_UPDATE_MERCHANT_STORE_LIKES_COUNT 			= 	"UPDATE merchant_rating_likes SET likes_count = ?, last_updated = NOW() WHERE merchant_store_id = ?";
	
	private static final String SQL_INSERT_MERCHANT_STORE_LIKES_COUNT 			= 	"INSERT INTO merchant_rating_likes (merchant_store_id, likes_count, created_on, last_updated) VALUES (?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_ARCHIVE_PROMOTION_VIEW				=	"INSERT INTO promotion_view_archive (entity_id, entity_type, username, created_on) VALUES ";
	
	private static final String SQL_INSERT_ARCHIVE_PROMOTION_CLICK				=	"INSERT INTO promotion_click_archive (entity_id, entity_type, username, created_on) VALUES (?, ?, ?, NOW()) ";

	

	@Override
	public PromotionData loadPromotionDetail(String entityId, String entityType) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		PromotionData promotionData = null;
		
		boolean setPromotionData = false;
		
		String promotionText = null;
		String multimediaPath = null;
		int multimediaType;
		String validityStart = null;
		String validityEnd = null;
		String name = null;
		String address = null;
		String city = null;
	    String state = null;
	    String phone = null;
	    String latitude = null;
	    String longitude = null;
	    String categories = null;
	    
	    int basicStoreId = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			if (GenericUtility.safeTrim(entityType).equalsIgnoreCase(DAOConstants.PROMOTION_BASIC_PROMO_TYPE_PROMOTION_STRING)){
				
				/* Fetch the Promotion basic & store ids based on Promotion Id */
				ps1 = con.prepareStatement(SQL_SELECT_FIND_PROMOTION_BY_ID);
				ps1.setString(1, entityId);
				
				logger.debug("[loadPromotionDetail()-QUERY 1] : " + ps1.toString());
				
				rs1 = ps1.executeQuery();
				
				if (rs1.next()){
					int promoBasicId = rs1.getInt(DAOConstants.TABLE_PROMOTION_COLUMN_IDPROMOBASIC);
					basicStoreId = rs1.getInt(DAOConstants.TABLE_PROMOTION_COLUMN_IDBASICSTORE);
					
					/* Fetch the Promotion basic details */
					ps1 = con.prepareStatement(SQL_SELECT_PROMOBASIC_BY_ID);
					ps1.setInt(1, promoBasicId);
					
					logger.debug("[loadPromotionDetail()-QUERY 2] : " + ps1.toString());
					
					rs1 = ps1.executeQuery();
					
					if (rs1.next()){
						int promoType = rs1.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_PROMOTYPE);
						promotionText = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_SUMMARY);
						multimediaPath = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMURL);
						multimediaType = rs1.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMTYPE);
						validityStart = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_STARTSAT);
						validityEnd = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_VALIDTILL);
						
						/* Fetch the Basic Store details */
						ps1 = con.prepareStatement(SQL_JOIN_SELECT_BASICSTORE_AND_CATEGORY);
						ps1.setInt(1, basicStoreId);
						
						logger.debug("[loadPromotionDetail()-QUERY 3] : " + ps1.toString());
						
						rs1 = ps1.executeQuery();
						
						if (rs1.next()){
							name = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME);
							address = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS);
							city = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CITY);
						    state = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_STATE);
						    phone = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CONTACT);
						    latitude = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE);
						    longitude = rs1.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE);
						    categories = rs1.getString(DAOConstants.TABLE_TOPCAT_COLUMN_CATEGORYNAME);
						    
						    setPromotionData = true;
						}
						else{
							logger.debug("Basic Store id not found in the 'basicstore' table in database for promotion id : " + entityId);
							throw new PromotionException("Basic Store id not found in the 'basicstore' table in database for promotion id : " + entityId);
						}
					}
					else{
						logger.debug("Promotion basic id not found in the 'promobasic' table in database for promotion id : " + entityId);
						throw new PromotionException("Promotion basic id not found in the 'promobasic' table in database for promotion id : " + entityId);
					}
				}
				else{
					logger.debug("Promotion id not found in the database.");
					throw new PromotionIdNotFoundException("Promotion id not found in the database.");
				}
			}
			else if (GenericUtility.safeTrim(entityType).equalsIgnoreCase(DAOConstants.PROMOTION_BASIC_PROMO_TYPE_EVENT_STRING)){
				
				/* Fetch the Event details */
				ps1 = con.prepareStatement(SQL_SELECT_EVENTTAB_BY_ID);
				ps1.setInt(1, Integer.parseInt(entityId));
				
				logger.debug("[loadPromotionDetail()-QUERY 4] : " + ps1.toString());
				
				rs1 = ps1.executeQuery();
				
				if (rs1.next()){
					address = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_ADDRESS);
					city = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_CITY);
				    state = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_STATE);
				    phone = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_CONTACT);
				    latitude = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_LATITUDE);
				    longitude = rs1.getString(DAOConstants.TABLE_EVENTAB_COLUMN_LONGITUDE);
				    
				    int promoBasicId = rs1.getInt(DAOConstants.TABLE_EVENTAB_COLUMN_IDPROMOBASIC);
				    
				    /* Fetch the Promotion basic details */
					ps1 = con.prepareStatement(SQL_SELECT_PROMOBASIC_BY_ID);
					ps1.setInt(1, promoBasicId);
					
					logger.debug("[loadPromotionDetail()-QUERY 5] : " + ps1.toString());
					
					rs1 = ps1.executeQuery();
					
					if (rs1.next()){
						int promoType = rs1.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_PROMOTYPE);
						promotionText = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_SUMMARY);
						multimediaPath = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMURL);
						multimediaType = rs1.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMTYPE);
						validityStart = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_STARTSAT);
						validityEnd = rs1.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_VALIDTILL);
						
						setPromotionData = true;
					}
					else{
						logger.debug("Promotion basic id not found in the 'promobasic' table in database for event id : " + entityId);
						throw new PromotionException("Promotion basic id not found in the 'promobasic' table in database for event id : " + entityId);
					}
				}
				else{
					logger.debug("No record found in the 'eventtab' table in database for event id : " + entityId);
					throw new PromotionException("No record found in the 'eventtab' table in database for event id : " + entityId);
				}
			}
			else {
				logger.debug("Invalid promo type in DAO request.");
				throw new PromotionException("Invalid promo type in DAO request.");
			}
			
			if (setPromotionData){
				/* Set the Promotion details in DAO object */
				promotionData = new PromotionData();
				
				promotionData.setName(name);
				promotionData.setAddress(address);
				promotionData.setPromotionText(promotionText);
				promotionData.setValidityPeriod(DAOUtility.promotionValidityStringFormat(validityEnd));
				promotionData.setCity(city);
				promotionData.setState(state);
				promotionData.setPhone(phone);
				promotionData.setCategories(categories);
				promotionData.setLatitude(null != latitude ? Double.parseDouble(latitude) : 0.0);
				promotionData.setLongitude(null != longitude ? Double.parseDouble(longitude) : 0.0);
				promotionData.setMultimediaPath(multimediaPath);
				
				if (multimediaType == 0){
					promotionData.setMultimediaType(DAOConstants.PROMOTION_BASIC_MULTIMEDIA_TYPE_IMAGE);
				}
				else if (multimediaType == 1) {
					promotionData.setMultimediaType(DAOConstants.PROMOTION_BASIC_MULTIMEDIA_TYPE_VIDEO);
				}
				
				promotionData.setStoreId(Integer.toString(basicStoreId));
			}
					
		}
		catch(PromotionIdNotFoundException pinfEx){
			throw pinfEx;
		}
		catch(PromotionException pEx){
			throw pEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadPromotionDetail", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadPromotionDetail() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadPromotionDetail", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadPromotionDetail() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps1, rs1);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadPromotionDetail() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return promotionData;
		
	}
	
	public MerchantRatingLikesData loadStoreRatingsAndLikes(String merchantStoreId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		MerchantRatingLikesData merchantRatingLikesData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_MERCHANT_STORE_LOAD_RATING_LIKES);
			ps.setString(1, merchantStoreId);
			
			logger.debug("[loadStoreRatingsAndLikes()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				merchantRatingLikesData = new MerchantRatingLikesData();
				
				merchantRatingLikesData.setRating(rs.getInt(DAOConstants.TABLE_MERCHANT_RATING_LIKES_COLUMN_RATING));
				merchantRatingLikesData.setLikesCount(rs.getInt(DAOConstants.TABLE_MERCHANT_RATING_LIKES_COLUMN_LIKESCOUNT));
				merchantRatingLikesData.setMerchantAssociation((rs.getInt(DAOConstants.TABLE_MERCHANT_RATING_LIKES_COLUMN_LISTING_ASSOCIATION) == 1) ? true : false);
			}
			else{
				logger.debug("No record exists for rating & likes for Merchant Store id : " + merchantStoreId);
			}
		}
		catch(SQLException sqlEx){
			logger.error("loadStoreRatingsAndLikes", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadStoreRatingsAndLikes() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadStoreRatingsAndLikes", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadStoreRatingsAndLikes() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadStoreRatingsAndLikes() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return merchantRatingLikesData;
		
	}

	@Override
	public int storeReviewscount(String merchantStoreId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int reviewsCount = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_MERCHANT_STORE_REVIEWS_COUNT);
			ps.setString(1, merchantStoreId);
			
			logger.debug("[storeReviewscount()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				reviewsCount = rs.getInt("reviews_count");
			}
			else{
				logger.debug("No record exists for reviews for Merchant Store id : " + merchantStoreId);
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("storeReviewscount", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("storeReviewscount() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("storeReviewscount", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("storeReviewscount() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("storeReviewscount() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return reviewsCount;
	}

	@Override
	public boolean rateMerchantStore(MerchantRatingLikesData merchantRatingLikesData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int storeId = merchantRatingLikesData.getMerchantStoreId();
		boolean isListingAssociation = false;
		int listingId = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Find out if there is any association with Listing */
			ps = con.prepareStatement(SQL_SELECT_FIND_LISTING_BY_STOREID);
			ps.setInt(1, storeId);
			
			logger.debug("[rateMerchantStore()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				//int fyngerRegistered = rs.getInt("Registered_With_Fynger");
				isListingAssociation = rs.getInt("Registered_With_Fynger") == 0 ? false : true ;
				listingId = rs.getInt("idlisting");
			}
			else{
				logger.debug("No association found with Listing for store id : " + storeId);
			}
			
			/* Check if there is any record already exists for the store id in rating table */
			ps = con.prepareStatement(SQL_SELECT_MERCHANT_STORE_LOAD_RATING_LIKES);
			ps.setInt(1, storeId);
			
			logger.debug("[rateMerchantStore()-QUERY 2] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				int currentRating = rs.getInt(DAOConstants.TABLE_MERCHANT_RATING_LIKES_COLUMN_RATING);
				int rating = (currentRating + merchantRatingLikesData.getRating()) / 2;
				
				/* Update the merchant's store rating */
				ps = con.prepareStatement(SQL_UPDATE_MERCHANT_STORE_RATING);
				ps.setInt(1, rating);
				ps.setInt(2, isListingAssociation ? 1 :0);
				ps.setInt(3, storeId);
				
				logger.debug("[rateMerchantStore()-QUERY 3] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					throw new DataAccessException("rateMerchantStore() -> Failed to update merchant's store rating in database.");
				}
				else{
					logger.debug("Merchant's Store Rating is updated successfully in database.");
				}
			}
			else{
				/* Insert the merchant's store rating */
				ps = con.prepareStatement(SQL_INSERT_MERCHANT_STORE_RATING);
				ps.setInt(1, storeId);
				ps.setInt(2, merchantRatingLikesData.getRating());
				ps.setInt(3, isListingAssociation ? 1 :0);
				
				logger.debug("[rateMerchantStore()-QUERY 4] : " + ps.toString());
				
				int rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					throw new DataAccessException("rateMerchantStore() -> Failed to insert merchant's store rating in database.");
				}
				else{
					logger.debug("Merchant's store Rating is inserted successfully in database.");
				}
			}
			
			if (isListingAssociation){
				/* Check if there is any record already exists for the listing id in rating table */
				ps = con.prepareStatement(SQL_SELECT_LISTING_LOAD_RATING_LIKES);
				ps.setInt(1, listingId);

				logger.debug("[rateMerchantStore()-QUERY 5] : " + ps.toString());
				
				rs = ps.executeQuery();
				
				if (rs.next()){
					int currentRating = rs.getInt("rating");
					int rating = (currentRating + merchantRatingLikesData.getRating()) / 2;
					
					/* Update the listing rating */
					ps = con.prepareStatement(SQL_UPDATE_LISTING_RATING);
					ps.setInt(1, rating);
					ps.setInt(2, 1);
					ps.setInt(3, listingId);
					
					int rowsUpdated = ps.executeUpdate();
					
					logger.debug("[rateMerchantStore()-QUERY 6] : " + ps.toString());
					
					if (rowsUpdated <= 0){
						throw new DataAccessException("rateMerchantStore() -> Failed to update listing rating in database.");
					}
					else{
						logger.debug("Listing Rating is updated successfully in database.");
					}
				}
				else{
					/* Insert the listing rating */
					ps = con.prepareStatement(SQL_INSERT_LISTING_RATING);
					ps.setInt(1, listingId);
					ps.setInt(2, merchantRatingLikesData.getRating());
					ps.setInt(3, 1);
					
					int rowsInserted = ps.executeUpdate();
					
					logger.debug("[rateMerchantStore()-QUERY 7] : " + ps.toString());
					
					if (rowsInserted <= 0){
						throw new DataAccessException("rateMerchantStore() -> Failed to insert listing rating in database.");
					}
					else{
						logger.debug("Listing Rating is inserted successfully in database.");
					}
				}
			}
			
			flag = true;
			con.commit();
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("rateMerchantStore() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateMerchantStore() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("rateMerchantStore() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateMerchantStore() -> SQLException occurred during rollback.");
			}
			logger.error("rateMerchantStore", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("rateMerchantStore() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("rateMerchantStore() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateMerchantStore() -> SQLException occurred during rollback.");
			}
			logger.error("rateMerchantStore", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("rateMerchantStore() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("rateMerchantStore() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	}

	@Override
	public boolean reviewMerchantStore(MerchantReviewsData merchantReviewsData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int storeId = merchantReviewsData.getMerchantStoreId();
		boolean isListingAssociation = false;
		int listingId = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Find out if there is any association with Listing */
			ps = con.prepareStatement(SQL_SELECT_FIND_LISTING_BY_STOREID);
			ps.setInt(1, storeId);
			
			logger.debug("[reviewMerchantStore()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				isListingAssociation = rs.getInt("Registered_With_Fynger") == 0 ? false : true ;
				listingId = rs.getInt("idlisting");
			}
			else{
				logger.debug("No association found with Listing for store id : " + storeId);
			}
			
			/* Select the user picture path */
			String userPicturePath = "";
			ps = con.prepareStatement(SQL_SELECT_USER_PROFILE);
			ps.setString(1, merchantReviewsData.getUsername());
			
			rs = ps.executeQuery();
			
			logger.debug("[reviewMerchantStore()-QUERY 2] : " + ps.toString());
			
			if (rs.next()){
				userPicturePath = rs.getString("picture_path");
			}
			else{
				logger.debug("User name not found in the database while updating the reviews of the merchant's store.");
				throw new UserNameNotFoundException("User name not found in the database while updating the reviews of the merchant's store.");
			}
			
			/* Insert a record for the Merchant store id in merchant review table */
			ps = con.prepareStatement(SQL_INSERT_MERCHANT_STORE_REVIEW);
			ps.setInt(1, merchantReviewsData.getMerchantStoreId());
			ps.setString(2, merchantReviewsData.getUsername());
			ps.setString(3, merchantReviewsData.getReviewText());
			ps.setInt(4, isListingAssociation ? 1 : 0);
			
			int rowsInserted = ps.executeUpdate();
			
			logger.debug("[reviewMerchantStore()-QUERY 3] : " + ps.toString());
			
			if (rowsInserted <= 0){
				throw new DataAccessException("reviewMerchantStore() -> Failed to insert Merchant's store review in database.");
			}
			else{
				logger.debug("Merchant's store Review is inserted successfully in database.");
			}
			
			if (isListingAssociation){
				/* Insert a record for the merchant in listing table */
				ps = con.prepareStatement(SQL_INSERT_LISTING_REVIEW);
				ps.setInt(1, listingId);
				ps.setString(2, merchantReviewsData.getUsername());
				ps.setString(3, merchantReviewsData.getReviewText());
				ps.setInt(4, 1);
				
				rowsInserted = ps.executeUpdate();
				
				logger.debug("[reviewMerchantStore()-QUERY 4] : " + ps.toString());
				
				if (rowsInserted <= 0){
					logger.error("reviewMerchantStore() -> Failed to insert Listing review in database.");
					throw new DataAccessException("reviewMerchantStore() -> Failed to insert Listing review in database.");
				}
				else{
					logger.debug("Listing Review is inserted successfully in database.");
				}
			}
			
			flag = true;
			con.commit();
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("reviewMerchantStore() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewMerchantStore() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("reviewMerchantStore() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewMerchantStore() -> SQLException occurred during rollback.");
			}
			logger.error("reviewMerchantStore", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("reviewMerchantStore() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("reviewMerchantStore() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewMerchantStore() -> SQLException occurred during rollback.");
			}
			logger.error("reviewMerchantStore", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("reviewMerchantStore() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("reviewMerchantStore() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	}

	@Override
	public List<MerchantReviewsData> loadReviews(String merchantStoreId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MerchantReviewsData> alMerchantReviewsData = null;

		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_LOAD_MERCHANT_STORE_REVIEWS);
			ps.setString(1, merchantStoreId);
			
			logger.debug("[loadReviews()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				rs.previous();
				
				alMerchantReviewsData = new ArrayList<MerchantReviewsData>();
				
				while (rs.next()){
					
					MerchantReviewsData merchantReviewsData = new MerchantReviewsData();
					merchantReviewsData.setUsername(rs.getString(DAOConstants.TABLE_MERCHANT_REVIEWS_COLUMN_USERNAME));
					merchantReviewsData.setReviewText(rs.getString(DAOConstants.TABLE_MERCHANT_REVIEWS_COLUMN_REVIEW_TEXT));
					merchantReviewsData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
					
					alMerchantReviewsData.add(merchantReviewsData);
				}
			}
			else{
				logger.debug("No Reviews found for the Merchant store id in the database.");
				throw new NoReviewsFoundException("No Reviews found for the Merchant store id in the database.");
			}
		}
		catch(NoReviewsFoundException nrfEx){
			throw nrfEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadReviews", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadReviews() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadReviews", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadReviews() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadReviews() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alMerchantReviewsData;
	
	}

	@Override
	public boolean validateAndUpdatePromotionStatus(int validityDiff) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_EXPIRED_PROMOTIONS);
			ps.setInt(1, validityDiff);
			ps.setInt(2, DAOConstants.PROMOTION_BASIC_STATUS_ACTIVE);
			
			logger.debug("[validateAndUpdatePromotionStatus()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("Promotions are found whose validity date are expired and need to DEACITVATE");
				
				/* Update the status of promotions which are EXPIRED */
				con = DatabaseConnectionManager.getConnection();
				ps = con.prepareStatement(SQL_UPDATE_EXPIRED_PROMOTIONS);
				ps.setInt(1, DAOConstants.PROMOTION_BASIC_STATUS_INACTIVE);
				ps.setInt(2, validityDiff);
				
				int rowsUpdated = ps.executeUpdate();
				
				logger.debug("[validateAndUpdatePromotionStatus()-QUERY 2] : " + ps.toString());
				
				if (rowsUpdated <= 0){
					logger.error("validateAndUpdatePromotionStatus() -> Failed to update promotions status in database.");
					throw new PromotionException("validateAndUpdatePromotionStatus() -> Failed to update promotions status in database.");
				}
				else{
					logger.debug("Promotions status are DEACTIVATED successfully in database.");
					con.commit();
					flag = true;
				}
			}
			else{
				logger.debug("NO expired Promotions are found to DEACITVATE.");
				flag = true;
			}
		}
		catch(PromotionException pEx){
			throw pEx;
		}
		catch(SQLException sqlEx){
			logger.error("validateAndUpdatePromotionStatus", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("validateAndUpdatePromotionStatus() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("validateAndUpdatePromotionStatus", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("validateAndUpdatePromotionStatus() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("validateAndUpdatePromotionStatus() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}	
	
	public boolean updateLikesCount(int merchantStoreId) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Check if there is any record already exists for the merchant Store id in rating & Likes table */
			ps = con.prepareStatement(SQL_SELECT_MERCHANT_STORE_LOAD_RATING_LIKES);
			ps.setInt(1, merchantStoreId);
			
			rs = ps.executeQuery();
			
			logger.debug("[updateLikesCount()-QUERY 1] : " + ps.toString());
			
			if (rs.next()){
				int currentLikesCount = rs.getInt("likes_count");
				
				/* Update the listing rating */
				ps = con.prepareStatement(SQL_UPDATE_MERCHANT_STORE_LIKES_COUNT);
				ps.setInt(1, currentLikesCount + 1);
				ps.setInt(2, merchantStoreId);
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[updateLikesCount()-QUERY 2] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("updateLikesCount() -> Failed to update merchant store's likes count in database.");
				}
				else{
					logger.debug("Merchant store's Likes count is updated successfully in database.");
					flag = true;
					con.commit();
				}
			}
			else{
				/* Insert the listing likes count */
				ps = con.prepareStatement(SQL_INSERT_MERCHANT_STORE_LIKES_COUNT);
				ps.setInt(1, merchantStoreId);
				ps.setInt(2, 1);
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[updateLikesCount()-QUERY 3] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("updateLikesCount() -> Failed to insert  merchant store's likes count in database.");
				}
				else{
					logger.debug("Merchant store's likes count is inserted successfully in database.");
					flag = true;
					con.commit();
				}
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateLikesCount", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateLikesCount() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateLikesCount", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateLikesCount() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateLikesCount() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}

	@Override
	public boolean archiveOnPromotionSearchResults(List<PromotionViewArchiveData> alPromotionViewArchiveData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		int rowsCount = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			stmt = con.createStatement();
			stmt.clearBatch();
			
			for (PromotionViewArchiveData object : alPromotionViewArchiveData){
				sql = SQL_INSERT_ARCHIVE_PROMOTION_VIEW + "(" + object.getEntityId() + ",'" + object.getEntityType() + "','" + 
								object.getUsername() + "'," + "NOW()" + ")";
				
				logger.debug("[archiveOnPromotionSearchResults()-QUERY " + rowsCount + "] : " + sql);
				
				stmt.addBatch(sql);
				
				++rowsCount;
			}
			
			//Execute the Batch in DB, if founds any records from above 
			if (rowsCount > 0){
				int sum = 0;
				
				int rows[] = stmt.executeBatch();
				int n = rows.length;
				for (int i=0; i < n; i++){
					sum += rows[i];
				}
				
				if (sum == rowsCount){
					logger.debug("Batch executed succesfully.");
					con.commit();
					flag = true;
				}
				else{
					logger.error("Batch not executed succesfully.");
					con.rollback();
					throw new ArchiveDataException("Batch of Archive Promotion View requests not executed successfully");
				}
			}
			else{
				logger.debug("No rows found in the batch to be executed.");
			}
		}
		catch(ArchiveDataException adEx){
			throw adEx;
		}
		catch(SQLException sqlEx){
			logger.error("archiveOnPromotionSearchResults", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new ArchiveDataException("archiveOnPromotionSearchResults() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("archiveOnPromotionSearchResults", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new ArchiveDataException("archiveOnPromotionSearchResults() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new ArchiveDataException("archiveOnPromotionSearchResults() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

	@Override
	public boolean archiveOnPromotionDetail(PromotionClickArchiveData promotionClickArchiveData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_ARCHIVE_PROMOTION_CLICK);
			ps.setInt(1, promotionClickArchiveData.getEntityId());
			ps.setString(2, promotionClickArchiveData.getEntityType());
			ps.setString(3, promotionClickArchiveData.getUsername());
			
			logger.debug("[archiveOnPromotionDetail()-QUERY] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				throw new DataAccessException("archiveOnPromotionDetail() -> Failed to insert archive Promotion click record in database.");
			}
			else{
				logger.debug("Record is successfully inserted in the database for archive Promotion click.");
				flag = true;
				con.commit();
			}
		}
		catch(ArchiveDataException adEx){
			throw adEx;
		}
		catch(SQLException sqlEx){
			logger.error("archiveOnPromotionDetail", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new ArchiveDataException("archiveOnPromotionDetail() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("archiveOnPromotionDetail", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new ArchiveDataException("archiveOnPromotionDetail() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new ArchiveDataException("archiveOnPromotionDetail() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}
	
}
