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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.ListingIdNotFoundException;
import com.fynger.servicesBusiness.exception.NoReviewsFoundException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingReviewsData;

/**
 * @author Rishi
 *
 */
public class ListingSearchDAOImpl implements IListingSearchDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(ListingSearchDAOImpl.class.getName());
	
	
	private static final String SQL_SELECT_LISTING_LOAD_RATING_LIKES_IN_1 = "SELECT * from listing_rating_likes WHERE listing_id in ( " ;
	private static final String SQL_SELECT_LISTING_LOAD_RATING_LIKES_IN_2 = " )";
	
	private static final String SQL_SELECT_LISTING_LOAD_RATING_LIKES = "SELECT * from listing_rating_likes WHERE listing_id = ?";
	
	private static final String SQL_SELECT_LISTING_REVIEWS_COUNT_IN_1 = "SELECT listing_id, COUNT(*) as reviews_count FROM listing_reviews WHERE listing_id in ( " ; 
	private static final String SQL_SELECT_LISTING_REVIEWS_COUNT_IN_2 = ") GROUP BY listing_id";
	
	private static final String SQL_SELECT_FIND_LISTING = "SELECT * FROM listing WHERE idlisting = ?";
	
	private static final String SQL_UPDATE_LISTING_RATING = "UPDATE listing_rating_likes SET rating = ?, merchant_association = ?, last_updated = NOW() WHERE listing_id = ?";
	
	private static final String SQL_INSERT_LISTING_RATING = "INSERT INTO listing_rating_likes (listing_id, rating, merchant_association, created_on, last_updated) VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_MERCHANT_LOAD_RATING_LIKES = "SELECT * from merchant_rating_likes WHERE merchant_store_id = ?";
	
	private static final String SQL_UPDATE_MERCHANT_RATING = "UPDATE merchant_rating_likes SET rating = ?, listing_association = ?, last_updated = NOW() WHERE merchant_store_id = ?";
	
	private static final String SQL_INSERT_MERCHANT_RATING = "INSERT INTO merchant_rating_likes (merchant_store_id, rating, listing_association, created_on, last_updated) VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_LISTING_REVIEW = "INSERT INTO listing_reviews (listing_id, username, review_text, merchant_association, created_on, last_updated) VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_MERCHANT_REVIEW = "INSERT INTO merchant_reviews (merchant_store_id, username, review_text, listing_association, created_on, last_updated) VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_USER_PROFILE = "SELECT picture_path FROM user_profile, user_login WHERE user_login.username = ?";
	
	private static final String SQL_SELECT_LOAD_LISTING_REVIEWS = "SELECT * FROM listing_reviews, user_login, user_profile " +
																	"WHERE user_login.login_id = user_profile.login_id " +
																	"AND listing_reviews.username = user_login.username " +
																	"AND listing_id = ?";
	
	private static final String SQL_UPDATE_LISTING_LIKES_COUNT = "UPDATE listing_rating_likes SET likes_count = ?, last_updated = NOW() WHERE listing_id = ?";
	
	private static final String SQL_INSERT_LISTING_LIKES_COUNT = "INSERT INTO listing_rating_likes (listing_id, likes_count, created_on, last_updated) VALUES (?, ?, NOW(), NOW())";

	
	public Map<Integer, ListingRatingLikesData> loadListingsRatingAndLikes(String listingIds) {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Map<Integer, ListingRatingLikesData> hmListingRatingLikesData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			String sql = SQL_SELECT_LISTING_LOAD_RATING_LIKES_IN_1 + listingIds + SQL_SELECT_LISTING_LOAD_RATING_LIKES_IN_2;
			stmt = con.createStatement();
			stmt.execute(sql);
			
			logger.debug("[loadListingsRatingAndLikes()-QUERY] : " + sql);
			
			rs = stmt.getResultSet();
			
			if (rs.next()){
				hmListingRatingLikesData = new HashMap<Integer, ListingRatingLikesData>();
				rs.previous();
				
				while (rs.next()){
					ListingRatingLikesData object = new ListingRatingLikesData();
					
					object.setLikesCount(rs.getInt("likes_count"));
					object.setListingId(rs.getInt("listing_id"));
					object.setMerchantAssociation((rs.getInt("merchant_association") == 1) ? true : false);
					object.setRating(rs.getInt("rating"));
					
					hmListingRatingLikesData.put(rs.getInt("listing_id"), object);					
				}
			}
			else{
				logger.debug("Listing id not found in the database.");
				//throw new ListingIdNotFoundException("Listing id not found in the database.");
			}
		}
		catch(ListingIdNotFoundException linfEx){
			throw linfEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadListingsRatingAndLikes", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadListingsRatingAndLikes() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadListingsRatingAndLikes", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadListingsRatingAndLikes() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadListingsRatingAndLikes() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return hmListingRatingLikesData;
	}

	
	public Map<Integer, Integer> loadListingsReviews(String listingIds) {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		Map<Integer, Integer> hmListingReviewsData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			String sql = SQL_SELECT_LISTING_REVIEWS_COUNT_IN_1 + listingIds + SQL_SELECT_LISTING_REVIEWS_COUNT_IN_2;
			stmt = con.createStatement();
			stmt.execute(sql);
			
			logger.debug("[loadListingsReviews()-QUERY] : " + sql);
			
			rs = stmt.getResultSet();
			
			if (rs.next()){
				hmListingReviewsData = new HashMap<Integer, Integer>();
				rs.previous();
				
				while (rs.next()){
					hmListingReviewsData.put(rs.getInt("listing_id"), rs.getInt("reviews_count"));					
				}
			}
			else{
				logger.debug("Listing id not found in the database.");
				//throw new ListingIdNotFoundException("Listing id not found in the database.");
			}
		}
		catch(ListingIdNotFoundException linfEx){
			throw linfEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadListingsReviews", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadListingsReviews() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadListingsReviews", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadListingsReviews() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadListingsReviews() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return hmListingReviewsData;
	}
	
	public boolean rateListing(ListingRatingLikesData listingRatingLikesData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Check if the listing id exists or not and fetch the merchant's association */
			/* Need to execute this as a first query as to get the updated info of listing */
			ps = con.prepareStatement(SQL_SELECT_FIND_LISTING);
			ps.setInt(1, listingRatingLikesData.getListingId());
			
			rs = ps.executeQuery();
			
			logger.debug("[rateListing()-QUERY 1] : " + ps.toString());
			
			if (rs.next()){
				int fyngerRegistered = rs.getInt("Registered_With_Fynger");
				boolean isMerchantAssociation = rs.getInt("Registered_With_Fynger") == 0 ? false : true ;
				int merchantStoreId = rs.getInt("merchant_store_id");
				
				boolean updateMerchantRating = false;
				
				if (isMerchantAssociation && merchantStoreId != 0){
					logger.debug("Listing is associated with merchant and linked with valid store id. Merchant's Rating will also be updated.");
					updateMerchantRating = true;
				}
				else if (isMerchantAssociation && merchantStoreId == 0){
					logger.debug("Listing is associated with merchant but is NOT linked with valid store id. Merchant's Rating will NOT be updated.");
				}
				else{
					logger.debug("Listing is NOT associated with merchant. Merchant's Rating will NOT be updated.");					
				}
				
				/* Check if there is any record already exists for the listing id in rating table */
				ps = con.prepareStatement(SQL_SELECT_LISTING_LOAD_RATING_LIKES);
				ps.setInt(1, listingRatingLikesData.getListingId());
				
				rs = ps.executeQuery();
				
				logger.debug("[rateListing()-QUERY 2] : " + ps.toString());
				
				if (rs.next()){
					int currentRating = rs.getInt("rating");
					int rating = (currentRating + listingRatingLikesData.getRating()) / 2;
					
					/* Update the listing rating */
					ps = con.prepareStatement(SQL_UPDATE_LISTING_RATING);
					ps.setInt(1, rating);
					ps.setInt(2, fyngerRegistered);
					ps.setInt(3, listingRatingLikesData.getListingId());
					
					int rowsInserted = ps.executeUpdate();
					
					logger.debug("[rateListing()-QUERY 3] : " + ps.toString());
					
					if (rowsInserted <= 0){
						throw new DataAccessException("rateListing() -> Failed to update listing rating in database.");
					}
					else{
						logger.debug("Listing Rating is updated successfully in database.");
					}
				}
				else{
					/* Insert the listing rating */
					ps = con.prepareStatement(SQL_INSERT_LISTING_RATING);
					ps.setInt(1, listingRatingLikesData.getListingId());
					ps.setInt(2, listingRatingLikesData.getRating());
					ps.setInt(3, fyngerRegistered);
					
					int rowsInserted = ps.executeUpdate();
					
					logger.debug("[rateListing()-QUERY 4] : " + ps.toString());
					
					if (rowsInserted <= 0){
						throw new DataAccessException("rateListing() -> Failed to insert listing rating in database.");
					}
					else{
						logger.debug("Listing Rating is inserted successfully in database.");
					}
				}
				
				if (updateMerchantRating){
					/* Check if there is any record already exists for the merchant store id in rating table */
					ps = con.prepareStatement(SQL_SELECT_MERCHANT_LOAD_RATING_LIKES);
					ps.setInt(1, merchantStoreId);
					
					rs = ps.executeQuery();
					
					logger.debug("[rateListing()-QUERY 5] : " + ps.toString());
					
					if (rs.next()){
						int currentRating = rs.getInt("rating");
						int rating = (currentRating + listingRatingLikesData.getRating()) / 2;
						
						/* Update the merchant rating */
						ps = con.prepareStatement(SQL_UPDATE_MERCHANT_RATING);
						ps.setInt(1, rating);
						ps.setInt(2, 1);
						ps.setInt(3, merchantStoreId);
						
						int rowsInserted = ps.executeUpdate();
						
						logger.debug("[rateListing()-QUERY 6] : " + ps.toString());
						
						if (rowsInserted <= 0){
							throw new DataAccessException("rateListing() -> Failed to update merchant rating in database.");
						}
						else{
							logger.debug("Merchant Rating is updated successfully in database.");
						}
					}
					else{
						/* Insert the merchant rating */
						ps = con.prepareStatement(SQL_INSERT_MERCHANT_RATING);
						ps.setInt(1, merchantStoreId);
						ps.setInt(2, listingRatingLikesData.getRating());
						ps.setInt(3, 1);
						
						int rowsInserted = ps.executeUpdate();
						
						logger.debug("[rateListing()-QUERY 7] : " + ps.toString());
						
						if (rowsInserted <= 0){
							throw new DataAccessException("rateListing() -> Failed to insert merchant rating in database.");
						}
						else{
							logger.debug("Merchant Rating is inserted successfully in database.");
						}
					}
				}
				
			}
			else{
				logger.debug("Listing id not found in the database.");
				throw new ListingIdNotFoundException("Listing id not found in the database.");
			}
			
			flag = true;
			con.commit();
			
		}
		catch(ListingIdNotFoundException linfEx){
			throw linfEx;
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("rateListing() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateListing() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("rateListing() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateListing() -> SQLException occurred during rollback.");
			}
			logger.error("rateListing", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("rateListing() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("rateListing() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("rateListing() -> SQLException occurred during rollback.");
			}
			logger.error("rateListing", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("rateListing() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("rateListing() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}
	
	public boolean reviewListing(ListingReviewsData listingReviewsData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Check if the listing id exists or not and fetch the merchant's association */
			/* Need to execute this as a first query as to get the updated info of listing */
			ps = con.prepareStatement(SQL_SELECT_FIND_LISTING);
			ps.setInt(1, listingReviewsData.getListingId());
			
			rs = ps.executeQuery();
			
			logger.debug("[reviewListing()-QUERY 1] : " + ps.toString());
			
			if (rs.next()){
				int fyngerRegistered = rs.getInt("Registered_With_Fynger");
				boolean isMerchantAssociation = rs.getInt("Registered_With_Fynger") == 0 ? false : true ;
				int merchantStoreId = rs.getInt("merchant_store_id");
				
				boolean updateMerchantRating = false;
				
				if (isMerchantAssociation && merchantStoreId != 0){
					logger.debug("Listing is associated with merchant and linked with valid store id. Merchant's Reviews will also be updated.");
					updateMerchantRating = true;
				}
				else if (isMerchantAssociation && merchantStoreId == 0){
					logger.debug("Listing is associated with merchant but is NOT linked with valid store id. Merchant's Reviews will NOT be updated.");
				}
				else{
					logger.debug("Listing is NOT associated with merchant. Merchant's Reviews will NOT be updated.");					
				}
				
				/* Select the user picture path */
				String userPicturePath = "";
				ps = con.prepareStatement(SQL_SELECT_USER_PROFILE);
				ps.setString(1, listingReviewsData.getUsername());
				
				rs = ps.executeQuery();
				
				logger.debug("[reviewListing()-QUERY 2] : " + ps.toString());
				
				if (rs.next()){
					userPicturePath = rs.getString("picture_path");
				}
				else{
					logger.debug("User name not found in the database while updating the reviews of the listing.");
					throw new UserNameNotFoundException("User name not found in the database while updating the reviews of the listing.");
				}
				
				
				/* Insert a record for the listing id in listing review table */
				ps = con.prepareStatement(SQL_INSERT_LISTING_REVIEW);
				ps.setInt(1, listingReviewsData.getListingId());
				ps.setString(2, listingReviewsData.getUsername());
				ps.setString(3, listingReviewsData.getReviewText());
				ps.setInt(4, fyngerRegistered);
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[reviewListing()-QUERY 3] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("reviewListing() -> Failed to insert listing review in database.");
				}
				else{
					logger.debug("Listing Review is updated successfully in database.");
				}
				
				if (updateMerchantRating){
					/* Insert a record for the listing in merchant review table */
					ps = con.prepareStatement(SQL_INSERT_MERCHANT_REVIEW);
					ps.setInt(1, merchantStoreId);
					ps.setString(2, listingReviewsData.getUsername());
					ps.setString(3, listingReviewsData.getReviewText());
					ps.setInt(4, 1);
					
					rowsInserted = ps.executeUpdate();
					
					logger.debug("[reviewListing()-QUERY 4] : " + ps.toString());
					
					if (rowsInserted <= 0){
						throw new DataAccessException("reviewListing() -> Failed to insert Merchant review in database.");
					}
					else{
						logger.debug("Merchant Review is updated successfully in database.");
					}
				}
			}
			else{
				logger.debug("Listing id not found in the database.");
				throw new ListingIdNotFoundException("Listing id not found in the database.");
			}
			
			flag = true;
			con.commit();
			
		}
		catch(ListingIdNotFoundException linfEx){
			throw linfEx;
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("reviewListing() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewListing() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("reviewListing() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewListing() -> SQLException occurred during rollback.");
			}
			logger.error("reviewListing", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("reviewListing() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("reviewListing() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("reviewListing() -> SQLException occurred during rollback.");
			}
			logger.error("reviewListing", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("reviewListing() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("reviewListing() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}
	
	public List<ListingReviewsData> loadReviews(String listingId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<ListingReviewsData> alListingReviewsData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_LOAD_LISTING_REVIEWS);
			ps.setString(1, listingId);
			
			rs = ps.executeQuery();
			
			logger.debug("[loadReviews()-QUERY] : " + ps.toString());
			
			if (rs.next()){
				rs.previous();
				
				alListingReviewsData = new ArrayList<ListingReviewsData>();
				
				while (rs.next()){
					
					ListingReviewsData listingReviewsData = new ListingReviewsData();
					listingReviewsData.setUsername(rs.getString("username"));
					listingReviewsData.setReviewText(rs.getString("review_text"));
					listingReviewsData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
					
					alListingReviewsData.add(listingReviewsData);
				}
			}
			else{
				logger.debug("No Reviews found for the Listing id in the database.");
				throw new NoReviewsFoundException("No Reviews found for the Listing id in the database.");
			}
		}
		catch(ListingIdNotFoundException linfEx){
			throw linfEx;
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
		
		return alListingReviewsData;
		
	}
	
	public boolean updateLikesCount(int listingId) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Check if there is any record already exists for the listing id in rating & Likes table */
			ps = con.prepareStatement(SQL_SELECT_LISTING_LOAD_RATING_LIKES);
			ps.setInt(1, listingId);
			
			rs = ps.executeQuery();
			
			logger.debug("[updateLikesCount()-QUERY 1] : " + ps.toString());
			
			if (rs.next()){
				int currentLikesCount = rs.getInt("likes_count");
				
				/* Update the listing rating */
				ps = con.prepareStatement(SQL_UPDATE_LISTING_LIKES_COUNT);
				ps.setInt(1, currentLikesCount + 1);
				ps.setInt(2, listingId);
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[updateLikesCount()-QUERY 2] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("updateLikesCount() -> Failed to update listing likes count in database.");
				}
				else{
					logger.debug("Listing Likes count is updated successfully in database.");
					flag = true;
					con.commit();
				}
			}
			else{
				/* Insert the listing likes count */
				ps = con.prepareStatement(SQL_INSERT_LISTING_LIKES_COUNT);
				ps.setInt(1, listingId);
				ps.setInt(2, 1);
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[updateLikesCount()-QUERY 3] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("updateLikesCount() -> Failed to insert listing likes count in database.");
				}
				else{
					logger.debug("Listing likes count is inserted successfully in database.");
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

}
