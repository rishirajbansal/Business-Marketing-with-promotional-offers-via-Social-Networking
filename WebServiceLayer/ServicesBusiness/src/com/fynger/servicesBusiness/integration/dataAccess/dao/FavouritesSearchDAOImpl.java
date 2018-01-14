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
import com.fynger.servicesBusiness.exception.AddFavouritesException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.DeleteFavouritesException;
import com.fynger.servicesBusiness.exception.FavouriteAlreadyExistsException;
import com.fynger.servicesBusiness.exception.FavouritesException;
import com.fynger.servicesBusiness.exception.NoFavouritesFoundException;
import com.fynger.servicesBusiness.exception.NoFavouritesResultsFoundException;
import com.fynger.servicesBusiness.exception.PromotionIdNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOUtility;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;

/**
 * @author Rishi
 *
 */
public class FavouritesSearchDAOImpl implements IFavouritesSearchDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(FavouritesSearchDAOImpl.class.getName());
	
	
	private static final String SQL_SELECT_USER_FAVOURITES = "SELECT * FROM user_favourites WHERE username = ?";
	
	private static final String SQL_INSERT_USER_FAVOURITES  = "INSERT INTO user_favourites (username, categories, brands, places, created_on, last_updated) " +
																"VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_UPDATE_USER_FAVOURITES = "UPDATE user_favourites SET categories = ?, brands = ?, places = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_SELECT_USER_FAVOURITES_PROMOTIONS = "SELECT * FROM user_favourites_promotions WHERE username = ?";
	
	private static final String SQL_SELECT_FAVOURITES_RESULTS_LIST_1 = "SELECT * FROM promobasic, promotion, basicstore " +
																		"LEFT OUTER JOIN topcat ON basicstore.idtopcat = topcat.idtopcat " +
																		"WHERE promotion.idpromobasic = promobasic.idpromobasic " +
																		"AND promotion.idbasicstore = basicstore.idbasicstore " +
																		"AND promotion.idpromotion IN ( " ;
	private static final String SQL_SELECT_FAVOURITES_RESULTS_LIST_2 = " )";
	
	private static final String SQL_INSERT_USER_FAVOURITES_PROMOTIONS = "INSERT INTO user_favourites_promotions (username, promotion_ids, created_on, last_updated) VALUES (?, ?, NOW(), NOW())";
	
	private static final String SQL_UPDATE_USER_FAVOURITES_PROMOTIONS = "UPDATE user_favourites_promotions SET promotion_ids = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_DELETE_USER_FAVOURITES_PROMOTIONS = "DELETE FROM user_favourites_promotions WHERE username = ?";
																			

	@Override
	public boolean addFavourite(UserFavouritesData userFavouritesData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_FAVOURITES);
			ps.setString(1, userFavouritesData.getUsername());
			
			logger.debug("[addFavourite()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("Record exists for username : " + userFavouritesData.getUsername() + " in the database table. Record will be updated for new search");
				
				String categories = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_CATEGORIES);
				String brands = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_BRANDS);
				String places = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_PLACES);
				
				if (!GenericUtility.safeTrim(userFavouritesData.getCategoriesList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					
					if (GenericUtility.safeTrim(categories).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
						categories = userFavouritesData.getCategoriesList();
					}
					else{
						if (categories.indexOf(userFavouritesData.getCategoriesList()) != -1){
							logger.debug("Favourite category already exists in the database with the name : " + userFavouritesData.getCategoriesList());
							throw new FavouriteAlreadyExistsException("Favourite category already exists in the database with the name : " + userFavouritesData.getCategoriesList());
						}
						else{
							categories = categories + GenericConstants.PIPE_SEPARATOR + userFavouritesData.getCategoriesList();
						}
					}
				}
				else if (!GenericUtility.safeTrim(userFavouritesData.getBrandsList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					
					if (GenericUtility.safeTrim(brands).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
						brands = userFavouritesData.getBrandsList();
					}
					else{
						if (brands.indexOf(userFavouritesData.getBrandsList()) != -1){
							logger.debug("Favourite brand already exists in the database with the name : " + userFavouritesData.getBrandsList());
							throw new FavouriteAlreadyExistsException("Favourite brand already exists in the database with the name : " + userFavouritesData.getBrandsList());
						}
						else{
							brands = brands + GenericConstants.PIPE_SEPARATOR + userFavouritesData.getBrandsList();
						}
					}
					
				}
				else if (!GenericUtility.safeTrim(userFavouritesData.getPlacesList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					
					if (GenericUtility.safeTrim(places).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
						places = userFavouritesData.getPlacesList();
					}
					else{
						if (places.indexOf(userFavouritesData.getPlacesList()) != -1){
							logger.debug("Favourite place already exists in the database with the name : " + userFavouritesData.getPlacesList());
							throw new FavouriteAlreadyExistsException("Favourite place already exists in the database with the name : " + userFavouritesData.getPlacesList());
						}
						else{
							places = places + GenericConstants.PIPE_SEPARATOR + userFavouritesData.getPlacesList();
						}
					}
					
				}
				
				/* Update the user favourites table */
				ps = con.prepareStatement(SQL_UPDATE_USER_FAVOURITES);
				ps.setString(1, categories);
				ps.setString(2, brands);
				ps.setString(3, places);
				ps.setString(4, userFavouritesData.getUsername());
				
				logger.debug("[addFavourite()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("addFavourite() -> Failed to update USER FAVOURITES for new ADD request in database.");
					throw new AddFavouritesException("addFavourite() -> Failed to update USER FAVOURITES for new ADD request in database.");
				}
				else{
					logger.debug("USER FAVOURITES are updated for new ADD request successfully in database.");
					con.commit();
					flag = true;
				}
			}
			else{
				logger.debug("No Record exists for username : " + userFavouritesData.getUsername() + " in the database table. New Record will be created for new search");
				
				/* Insert into user favourites table */
				ps = con.prepareStatement(SQL_INSERT_USER_FAVOURITES);
				ps.setString(1, userFavouritesData.getUsername());
				ps.setString(2, userFavouritesData.getCategoriesList());
				ps.setString(3, userFavouritesData.getBrandsList());
				ps.setString(4, userFavouritesData.getPlacesList());
				
				logger.debug("[addFavourite()-QUERY 3] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("addFavourite() -> Failed to insert USER FAVOURITES for new ADD request in database.");
					throw new AddFavouritesException("addFavourite() -> Failed to insert USER FAVOURITES for new ADD request in database.");
				}
				else{
					logger.debug("USER FAVOURITES are Inserted for new ADD request successfully in database.");
					con.commit();
					flag = true;
				}
			}
		}
		catch(AddFavouritesException afEx){
			throw afEx;
		}
		catch(FavouriteAlreadyExistsException faeEx){
			throw faeEx;
		}
		catch(SQLException sqlEx){
			logger.error("addFavourite", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("addFavourite() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("addFavourite", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("addFavourite() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("addFavourite() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

	@Override
	public UserFavouritesData listFavourites(String username) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		UserFavouritesData userFavouritesData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_FAVOURITES);
			ps.setString(1, username);
			
			logger.debug("[listFavourites()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				userFavouritesData = new UserFavouritesData();
				
				userFavouritesData.setCategoriesList(rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_CATEGORIES));
				userFavouritesData.setBrandsList(rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_BRANDS));
				userFavouritesData.setPlacesList(rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_PLACES));
			}
			else{
				logger.debug("No Favourites found for the user : " + username + " in database.");
				throw new NoFavouritesFoundException("No Favourites found for the user : " + username + " in database.");
			}
		}
		catch(NoFavouritesFoundException nffEx){
			throw nffEx;
		}
		catch(SQLException sqlEx){
			logger.error("listFavourites", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("listFavourites() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("listFavourites", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("listFavourites() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("listFavourites() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return userFavouritesData;
		
	}

	@Override
	public boolean deleteFavourite(UserFavouritesData userFavouritesData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_FAVOURITES);
			ps.setString(1, userFavouritesData.getUsername());
			
			logger.debug("[deleteFavourite()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				String categories = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_CATEGORIES);
				String brands = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_BRANDS);
				String places = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_COLUMN_PLACES);
				
				if (!GenericUtility.safeTrim(userFavouritesData.getCategoriesList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					int index = GenericUtility.safeTrim(categories).indexOf(userFavouritesData.getCategoriesList());
					if (index != -1){
						categories = categories.replaceAll(userFavouritesData.getCategoriesList()+"[\\|]*", "");
						if (categories.endsWith("|")){
							categories = categories.substring(0, categories.lastIndexOf("|"));
						}
						categories = categories.trim();
					}
				}
				else if (!GenericUtility.safeTrim(userFavouritesData.getBrandsList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					int index = GenericUtility.safeTrim(brands).indexOf(userFavouritesData.getBrandsList());
					
					if (index != -1){
						brands = brands.replaceAll(userFavouritesData.getBrandsList()+"[\\|]*", "");
						if (brands.endsWith("|")){
							brands = categories.substring(0, brands.lastIndexOf("|"));
						}
						brands = brands.trim();
					}
				}
				else if (!GenericUtility.safeTrim(userFavouritesData.getPlacesList()).equalsIgnoreCase(GenericConstants.EMPTY_STRING)){
					int index = GenericUtility.safeTrim(places).indexOf(userFavouritesData.getPlacesList());
					if (index != -1){
						places = places.replaceAll(userFavouritesData.getPlacesList()+"[\\|]*", "");
						if (places.endsWith("|")){
							places = places.substring(0, places.lastIndexOf("|"));
						}
						places = places.trim();
					}
				}
				
				/* Update the user favourites table */
				ps = con.prepareStatement(SQL_UPDATE_USER_FAVOURITES);
				ps.setString(1, categories);
				ps.setString(2, brands);
				ps.setString(3, places);
				ps.setString(4, userFavouritesData.getUsername());
				
				logger.debug("[deleteFavourite()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("deleteFavourite() -> Failed to delete favourite from USER FAVOURITES table in database.");
					throw new DeleteFavouritesException("deleteFavourite() -> Failed to delete favourite from USER FAVOURITES table in database.");
				}
				else{
					logger.debug("Selected favourite is deleted from the USER FAVOURITES table successfully from database.");
					con.commit();
					flag = true;
				}
			}
			else{
				logger.debug("No Favourites found for the user : " + userFavouritesData.getUsername() + " in database.");
				throw new NoFavouritesFoundException("No Favourites found for the user : " + userFavouritesData.getUsername() + " in database.");
			}
		}
		catch(DeleteFavouritesException dfEx){
			throw dfEx;
		}
		catch(NoFavouritesFoundException nffEx){
			throw nffEx;
		}
		catch(SQLException sqlEx){
			logger.error("deleteFavourite", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("deleteFavourite() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("deleteFavourite", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("deleteFavourite() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("deleteFavourite() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}

	@Override
	public List<PromotionData> loadFavouritesResultsList(String username) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		List<PromotionData> alPromotionsList = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_FAVOURITES_PROMOTIONS);
			ps.setString(1, username);
			
			logger.debug("[loadFavouritesResultsList()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				String promotionIds = rs.getString(DAOConstants.TABLE_USER_FAVOURITES_PROMOTIONS_COLUMN_PROMOTIONSIDS);
				
				promotionIds = promotionIds.replaceAll("\\|", ",");
		
				String sql = SQL_SELECT_FAVOURITES_RESULTS_LIST_1 + promotionIds + SQL_SELECT_FAVOURITES_RESULTS_LIST_2;
				stmt = con.createStatement();
				
				logger.debug("[loadFavouritesResultsList()-QUERY 2] : " + sql);
				
				stmt.execute(sql);
				
				rs = stmt.getResultSet();
				
				if (rs.next()){
					rs.previous();
					
					alPromotionsList = new ArrayList<PromotionData>();
					
					while (rs.next()){
						PromotionData data = new PromotionData();
						
						data.setName(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME));
						data.setAddress(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS));
						data.setPromotionText(rs.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_SUMMARY));
						data.setValidityPeriod(DAOUtility.promotionValidityStringFormat(rs.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_VALIDTILL)));
						data.setLatitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE)) : 0.0);
						data.setLongitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE)) : 0.0);
						data.setPromotionId(rs.getInt(DAOConstants.TABLE_PROMOTION_COLUMN_IDPROMOTION));
						
						alPromotionsList.add(data);						
					}
				}
				else{
					logger.debug("No Favourites Results found for the user : " + username + " in database.");
					throw new NoFavouritesResultsFoundException("No Favourites Results found for the user : " + username + " in database.");
				}
			}
			else{
				logger.debug("No Favourites Results found for the user : " + username + " in database.");
				throw new NoFavouritesResultsFoundException("No Favourites Results found for the user : " + username + " in database.");
			}
		}
		catch(NoFavouritesResultsFoundException nffEx){
			throw nffEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadFavouritesResultsList", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadFavouritesResultsList() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadFavouritesResultsList", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadFavouritesResultsList() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadFavouritesResultsList() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alPromotionsList;
		
	}

	@Override
	public PromotionData loadFavouritePromotionDetail(String promotionId) throws DataAccessException {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		PromotionData data = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			String sql = SQL_SELECT_FAVOURITES_RESULTS_LIST_1 + promotionId + SQL_SELECT_FAVOURITES_RESULTS_LIST_2;
			stmt = con.createStatement();
			stmt.execute(sql);
			
			logger.debug("[loadFavouritePromotionDetail()-QUERY] : " + sql);
			
			rs = stmt.getResultSet();
			
			if (rs.next()){
				
				data = new PromotionData();
				
				data.setName(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME));
				data.setAddress(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS));
				data.setPromotionText(rs.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_SUMMARY));
				data.setValidityPeriod(DAOUtility.promotionValidityStringFormat(rs.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_VALIDTILL)));
				data.setLatitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE)) : 0.0);
				data.setLongitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE)) : 0.0);
				data.setPromotionId(rs.getInt(DAOConstants.TABLE_PROMOTION_COLUMN_IDPROMOTION));
				data.setCity(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CITY));
				data.setState(rs.getString("basicstore."+DAOConstants.TABLE_BASICSTORE_COLUMN_STATE));
				data.setPhone(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CONTACT));
				data.setCategories(rs.getString(DAOConstants.TABLE_TOPCAT_COLUMN_CATEGORYNAME));
				
				if (rs.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMTYPE) == 0){
					data.setMultimediaType(DAOConstants.PROMOTION_BASIC_MULTIMEDIA_TYPE_IMAGE);
				}
				else if (rs.getInt(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMTYPE) == 1) {
					data.setMultimediaType(DAOConstants.PROMOTION_BASIC_MULTIMEDIA_TYPE_VIDEO);
				}
				
				data.setMultimediaPath(rs.getString(DAOConstants.TABLE_PROMOBASIC_COLUMN_MMURL));
			}
			else{
				logger.debug("Promotion id not found in the database.");
				throw new PromotionIdNotFoundException("Promotion id not found in the database.");
			}
			
		}
		catch(PromotionIdNotFoundException pinfEx){
			throw pinfEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadFavouritePromotionDetail", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadFavouritePromotionDetail() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadFavouritePromotionDetail", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadFavouritePromotionDetail() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(rs);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadFavouritePromotionDetail() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return data;
	}

	@Override
	public boolean updateUserFavouritePromotions(String promotionIds, String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_FAVOURITES_PROMOTIONS);
			ps.setString(1, username);
			
			logger.debug("[updateUserFavouritePromotions()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				/* Update existing record for User favourite Promotions list */
				
				ps = con.prepareStatement(SQL_UPDATE_USER_FAVOURITES_PROMOTIONS);
				ps.setString(1, promotionIds);
				ps.setString(2, username);
				
				logger.debug("[updateUserFavouritePromotions()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("updateUserFavouritePromotions() -> Failed to update USER FAVOURITES PROMOTIONS for existing request in database.");
					throw new FavouritesException("updateUserFavouritePromotions() -> Failed to update USER FAVOURITES PROMOTIONS for existing request in database.");
				}
				else{
					logger.debug("USER FAVOURITES PROMOTIONS are updated for existing request successfully in database.");
					con.commit();
					flag = true;
				}
			}
			else{
				/* Insert new record for User favourite Promotions list */
				
				ps = con.prepareStatement(SQL_INSERT_USER_FAVOURITES_PROMOTIONS);
				ps.setString(1, username);
				ps.setString(2, promotionIds);
				
				logger.debug("[updateUserFavouritePromotions()-QUERY 3] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("updateUserFavouritePromotions() -> Failed to insert USER FAVOURITES PROMOTIONS for new request in database.");
					throw new FavouritesException("updateUserFavouritePromotions() -> Failed to insert USER FAVOURITES PROMOTIONS for new request in database.");
				}
				else{
					logger.debug("USER FAVOURITES PROMOTIONS are Inserted for new request successfully in database.");
					con.commit();
					flag = true;
				}
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateUserFavouritePromotions", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateUserFavouritePromotions() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateUserFavouritePromotions", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateUserFavouritePromotions() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateUserFavouritePromotions() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	
	}
	
	@Override
	public boolean deleteUserFavouritePromotions(String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_DELETE_USER_FAVOURITES_PROMOTIONS);
			ps.setString(1, username);
			
			logger.debug("[deleteUserFavouritePromotions()-QUERY] : " + ps.toString());
			
			int rowsUpdated = ps.executeUpdate();
			
			if (rowsUpdated <= 0){
				logger.debug("deleteUserFavouritePromotions() -> No record exists in USER FAVOURITES PROMOTIONS for user name : " + username);
			}
			else{
				logger.debug("Record from USER FAVOURITES PROMOTIONS is deleted successfully from database for user name : " + username);
				con.commit();
				flag = true;
			}
		}
		catch(SQLException sqlEx){
			logger.error("deleteUserFavouritePromotions", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("deleteUserFavouritePromotions() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("deleteUserFavouritePromotions", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("deleteUserFavouritePromotions() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("deleteUserFavouritePromotions() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
		
	}

}
