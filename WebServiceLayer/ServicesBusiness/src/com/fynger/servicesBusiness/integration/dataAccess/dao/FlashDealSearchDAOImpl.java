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

import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FlashDealException;
import com.fynger.servicesBusiness.exception.NoFlashDealRespondedResultsFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.PromotionException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealDetailsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRequestData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRespondedData;

/**
 * @author Rishi
 *
 */
public class FlashDealSearchDAOImpl implements IFlashDealSearchDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(FlashDealSearchDAOImpl.class.getName());
	
	
	private static final String SQL_INSERT_FLASHDEAL_REQUEST 					=	"INSERT INTO flashdeal (idmreg, username, reqts, reqcat, reqloc, idbasicstore, hasresponded, status, createdon, reqtsp) VALUES ";
	
	private static final String SQL_COUNT_RESPONDED_FLASHDEAL_DETAILS 			=	"SELECT count(*) AS count from flashdeal WHERE username = ? AND hasresponded = ? AND " +
																					"idbasicstore IS NOT NULL AND STATUS = ? AND TIMESTAMPDIFF(MINUTE, reqts, respts) <= reqtsp AND TIMESTAMPDIFF(DAY, respts, NOW()) <= ?";
	
	private static final String SQL_SELECT_RESPONDED_FLASHDEAL_DETAILS 			=	"SELECT * FROM flashdeal, basicstore LEFT OUTER JOIN topcat ON basicstore.idtopcat = topcat.idtopcat " +
																					"WHERE flashdeal.idbasicstore = basicstore.idbasicstore AND username = ? AND hasresponded = ? " +
																					"AND flashdeal.idbasicstore IS NOT NULL AND STATUS = ? AND TIMESTAMPDIFF(MINUTE, reqts, respts) <= reqtsp " +
																					"AND TIMESTAMPDIFF(DAY, respts, NOW()) <= ? ORDER BY respts DESC LIMIT ?, ?";
		
	private static final String SQL_SELECT_FLASHDEALS_FIND_BY_ID 				=	"SELECT * FROM flashdeal, basicstore LEFT OUTER JOIN topcat ON basicstore.idtopcat = topcat.idtopcat " +
																					"WHERE flashdeal.idbasicstore = basicstore.idbasicstore AND idflashdeal = ?";
	
	private static final String SQL_SELECT_EXPIRED_FLASHDEALS 					=	"select TIMESTAMPDIFF(MINUTE, reqts, NOW()), reqts, reqtsp, status from flashdeal " +
																					"WHERE TIMESTAMPDIFF(MINUTE, reqts, NOW()) > reqtsp AND (hasresponded = ? OR hasresponded IS NULL) " +
																					"AND STATUS = ?";
	
	private static final String SQL_UPDATE_EXPIRED_FLASHDEALS 					=	"UPDATE flashdeal SET STATUS = 0 " +
																					"WHERE TIMESTAMPDIFF(MINUTE, reqts, NOW()) > reqtsp AND (hasresponded = ? OR hasresponded IS NULL) " +
																					"AND STATUS = ?";
	
	private static final String SQL_SELECT_NOT_EMAILED_RESPONDED_FLASHDEALS 	=	"SELECT * FROM flashdeal_response_archive, flashdeal, user_profile, user_login, " +
																					"basicstore LEFT OUTER JOIN topcat ON basicstore.idtopcat = topcat.idtopcat " +
																					"WHERE flashdeal.idbasicstore = basicstore.idbasicstore AND user_profile.login_id = user_login.login_id " +
																					"AND flashdeal_response_archive.idflashdeal = flashdeal.idflashdeal " +
																					"AND user_login.username = flashdeal_response_archive.username AND flashdeal_response_archive.emailed = ?";
	
	private static final String SQL_UPDATE_EMAILED_RESPONDED_FLASHDEALS_1		=	"UPDATE flashdeal_response_archive SET emailed = 1 WHERE idflashdeal IN ( ";
	private static final String SQL_UPDATE_EMAILED_RESPONDED_FLASHDEALS_2		=	" )";
	
	private static final String SQL_SELECT_NOT_NOTIFIED_RESPONDED_FLASHDEALS	=	"SELECT * FROM flashdeal_responded, user_mobile_auth WHERE flashdeal_responded.notified = ? AND flashdeal_responded.username = user_mobile_auth.username";
	
	private static final String SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_1		=	"UPDATE flashdeal_responded SET notified = 1 WHERE id IN ( ";
	private static final String SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_2		=	" )";
	
	private static final String SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_FAILED_1		=	"UPDATE flashdeal_responded SET notified = 2 WHERE id IN ( ";
	private static final String SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_FAILED_2		=	" )";


	@Override
	public boolean storeFlashDealRequests(List<FlashDealRequestData> alFlashDealRequestData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		Statement stmt = null;
		String sql = "";
		int rowsCount = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			stmt = con.createStatement();
			stmt.clearBatch();
			
			for (FlashDealRequestData object : alFlashDealRequestData){
				sql = SQL_INSERT_FLASHDEAL_REQUEST + "(" + object.getMerchantId() + ",'" + object.getUsername() + "'," + "NOW()" + ",'" + 
								object.getRequestCategory() + "','" + object.getRequestLocation() + "'," + 
								object.getStoreId() + "," + object.getHasResponded() + "," + object.getStatus() + "," + 
								"NOW()" + "," + object.getRequestExpiryPeriod() + ")";
				
				logger.debug("[storeFlashDealRequests()-QUERY " + rowsCount + "] : " + sql);
				
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
					throw new FlashDealException("Batch of Flash deal requests not executed successfully");
				}
			}
			else{
				logger.debug("No rows found in the batch to be executed.");
			}
			
		}
		catch(FlashDealException fdEx){
			throw fdEx;
		}
		catch(SQLException sqlEx){
			logger.error("storeFlashDealRequests", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("storeFlashDealRequests() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("storeFlashDealRequests", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("storeFlashDealRequests() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("storeFlashDealRequests() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	
	}


	@Override
	public List<FlashDealDetailsData> loadFlashDealResultsList(String username, int flashDealValidityDiff, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<FlashDealDetailsData> alFlashDealDetailsData = null;
			
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_COUNT_RESPONDED_FLASHDEAL_DETAILS);
			ps.setString(1, username);
			ps.setInt(2, DAOConstants.FLASHDEAL_REQUEST_RESPONDED_YES);
			ps.setInt(3, DAOConstants.FLASHDEAL_REQUEST_STATUS_ACTIVE);
			ps.setInt(4, flashDealValidityDiff);
			
			logger.debug("[loadFlashDealResultsList()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total flash deal responded results for user name : " + username + " are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					ps = con.prepareStatement(SQL_SELECT_RESPONDED_FLASHDEAL_DETAILS);
					ps.setString(1, username);
					ps.setInt(2, DAOConstants.FLASHDEAL_REQUEST_RESPONDED_YES);
					ps.setInt(3, DAOConstants.FLASHDEAL_REQUEST_STATUS_ACTIVE);
					ps.setInt(4, flashDealValidityDiff);
					ps.setInt(5, offset);
					ps.setInt(6, maxPageResults);
					
					logger.debug("[loadFlashDealResultsList()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					if (rs.next()){
						rs.previous();
						
						alFlashDealDetailsData = new ArrayList<FlashDealDetailsData>();
						
						while (rs.next()){
							FlashDealDetailsData object = new FlashDealDetailsData();
							
							object.setFlashDealId(rs.getInt(DAOConstants.TABLE_FLASHDEAL_COLUMN_IDFLASHDEAL));
							object.setName(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME));
							object.setAddress(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS));
							object.setLatitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE)) : 0.0 );
							object.setLongitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE)) : 0.0 );
							object.setFlashDealText(rs.getString(DAOConstants.TABLE_FLASHDEAL_COLUMN_RESPONSETEXT));
							
							object.setTotalResults(count);
							
							alFlashDealDetailsData.add(object);
						}
						
					}
					else{
						logger.debug("No flash deal results found which have been responded as of now for user name : " + username);
						throw new NoFlashDealRespondedResultsFoundException("No flash deal results found which have been responded as of now for user name : " + username);
					}
				}
			}
			else{
				logger.debug("No flash deal results found which have been responded as of now for user name : " + username);
				throw new NoFlashDealRespondedResultsFoundException("No flash deal results found which have been responded as of now for user name : " + username);
			}
			
		}
		catch(PageCountExceededException pceEx){
			throw pceEx;
		}
		catch (NoFlashDealRespondedResultsFoundException noResultsEx){
			throw noResultsEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadFlashDealResultsList", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadFlashDealResultsList() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadFlashDealResultsList", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadFlashDealResultsList() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadFlashDealResultsList() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alFlashDealDetailsData;
		
	}
	
	@Override
	public FlashDealDetailsData loadFlashDealDetail(String flashDealId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		FlashDealDetailsData daoData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_FLASHDEALS_FIND_BY_ID);
			ps.setInt(1, Integer.parseInt(flashDealId));
			
			logger.debug("[loadFlashDealDetail()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				
				daoData = new FlashDealDetailsData();
				
				daoData.setName(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME));
				daoData.setAddress(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS));
				daoData.setLatitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LATITUDE)) : 0.0 );
				daoData.setLongitude(null != rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE) ? Double.parseDouble(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_LONGITUDE)) : 0.0 );
				daoData.setFlashDealText(rs.getString(DAOConstants.TABLE_FLASHDEAL_COLUMN_RESPONSETEXT));
				daoData.setCity(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CITY));
				daoData.setState(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_STATE));
				daoData.setPhone(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CONTACT));
				daoData.setCategories(rs.getString(DAOConstants.TABLE_TOPCAT_COLUMN_CATEGORYNAME));
				
			}
			else{
				logger.error("No record found for Flash deal id : " + flashDealId);
				throw new FlashDealException("No record found for Flash deal id : " + flashDealId);
			}
		}
		catch (FlashDealException fdEx){
			throw fdEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadFlashDealDetail", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadFlashDealDetail() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadFlashDealDetail", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadFlashDealDetail() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadFlashDealDetail() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return daoData;
	}


	@Override
	public boolean validateAndUpdateFlashDealStatus() throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_EXPIRED_FLASHDEALS);
			ps.setInt(1, DAOConstants.FLASHDEAL_REQUEST_RESPONDED_NO);
			ps.setInt(2, DAOConstants.FLASHDEAL_REQUEST_STATUS_ACTIVE);
			
			logger.debug("[validateAndUpdateFlashDealStatus()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("Flash Deals are found whose validity date are expired and need to DEACITVATE");
				
				/* Update the status of promotions which are EXPIRED */
				con = DatabaseConnectionManager.getConnection();
				ps = con.prepareStatement(SQL_UPDATE_EXPIRED_FLASHDEALS);
				ps.setInt(1, DAOConstants.FLASHDEAL_REQUEST_RESPONDED_NO);
				ps.setInt(2, DAOConstants.FLASHDEAL_REQUEST_STATUS_ACTIVE);
				
				int rowsUpdated = ps.executeUpdate();
				
				logger.debug("[validateAndUpdateFlashDealStatus()-QUERY 2] : " + ps.toString());
				
				if (rowsUpdated <= 0){
					logger.error("validateAndUpdateFlashDealStatus() -> Failed to update flash deals status in database.");
					throw new PromotionException("validateAndUpdateFlashDealStatus() -> Failed to update flash deals status in database.");
				}
				else{
					logger.debug("Flash Deals status are DEACTIVATED successfully in database.");
					con.commit();
					flag = true;
				}
			}
			else{
				logger.debug("NO expired Flash Deals are found to DEACITVATE.");
				flag = true;
			}
		}
		catch(FlashDealException fdEx){
			throw fdEx;
		}
		catch(SQLException sqlEx){
			logger.error("validateAndUpdateFlashDealStatus", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("validateAndUpdateFlashDealStatus() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("validateAndUpdateFlashDealStatus", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("validateAndUpdateFlashDealStatus() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("validateAndUpdateFlashDealStatus() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	}


	@Override
	public List<FlashDealDetailsData> fetchRespondedFlashDeals() throws DataAccessException {
		
		List<FlashDealDetailsData> alFlashDealDetailsData = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_NOT_EMAILED_RESPONDED_FLASHDEALS);
			ps.setInt(1, DAOConstants.FLASHDEAL_RESPONDED_EMAILED_NO);
			
			logger.debug("[fetchRespondedFlashDeals()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				rs.previous();
				
				alFlashDealDetailsData = new ArrayList<FlashDealDetailsData>();
				
				while(rs.next()){
					FlashDealDetailsData object = new FlashDealDetailsData();
					
					object.setName(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_NAME));
					object.setAddress(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_ADDRESS));
					object.setPhone(rs.getString(DAOConstants.TABLE_BASICSTORE_COLUMN_CONTACT));
					object.setFlashDealText(rs.getString(DAOConstants.TABLE_FLASHDEAL_COLUMN_RESPONSETEXT));
					object.setFlashDealId(rs.getInt(DAOConstants.TABLE_FLASHDEAL_COLUMN_IDFLASHDEAL));
					object.setUserEmailId(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_EMAIL));
					object.setUsername(rs.getString(DAOConstants.TABLE_USER_LOGIN_COLUMN_USERNAME));
					
					alFlashDealDetailsData.add(object);
				}
			}
			else{
				logger.debug("No respoded flash deals found in DAO layer to be emailed.");
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("fetchRespondedFlashDeals", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchRespondedFlashDeals() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchRespondedFlashDeals", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchRespondedFlashDeals() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchRespondedFlashDeals() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alFlashDealDetailsData;

	}


	@Override
	public boolean updateRespondedFlashDealsEmailStatus(String flashDealIds) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		Statement stmt = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			String sql = SQL_UPDATE_EMAILED_RESPONDED_FLASHDEALS_1 + flashDealIds + SQL_UPDATE_EMAILED_RESPONDED_FLASHDEALS_2;
			stmt = con.createStatement();
			
			logger.debug("[updateRespondedFlashDealsEmailStatus()-QUERY] : " + sql);
			
			int rowsUpdated = stmt.executeUpdate(sql);
			
			if (rowsUpdated <= 0){
				logger.error("updateRespondedFlashDealsEmailStatus() -> Failed to update flash deals email status in database.");
				throw new DataAccessException("updateRespondedFlashDealsEmailStatus() -> Failed to update flash deals email status in database.");
			}
			else{
				logger.debug("Flash Deals Email status are updated successfully in database.");
				con.commit();
				flag = true;
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateRespondedFlashDealsEmailStatus", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsEmailStatus() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateRespondedFlashDealsEmailStatus", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsEmailStatus() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateRespondedFlashDealsEmailStatus() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}


	@Override
	public List<FlashDealRespondedData> fetchMobileNotifiableRespondedFlashDeals() throws DataAccessException {
		
		List<FlashDealRespondedData> alFlashDealRespondedData = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_NOT_NOTIFIED_RESPONDED_FLASHDEALS);
			ps.setInt(1, DAOConstants.FLASHDEAL_RESPONDED_NOTIFIED_NO);
			
			logger.debug("[fetchMobileNotifiableRespondedFlashDeals()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				rs.previous();
				
				alFlashDealRespondedData = new ArrayList<FlashDealRespondedData>();
				
				while(rs.next()){
					FlashDealRespondedData object = new FlashDealRespondedData();
					
					object.setFlashDealRespondedDataId(rs.getInt(DAOConstants.TABLE_FLASHDEAL_RESPONDED_COLUMN_ID));
					object.setFlashDealId(rs.getInt(DAOConstants.TABLE_FLASHDEAL_RESPONDED_COLUMN_IDFLASHDEAL));
					object.setUsername(rs.getString(DAOConstants.TABLE_FLASHDEAL_RESPONDED_COLUMN_USERNAME));
					object.setResponseText(rs.getString(DAOConstants.TABLE_FLASHDEAL_RESPONDED_COLUMN_RESPONSETEXT));
					object.setMerchantName(rs.getString(DAOConstants.TABLE_FLASHDEAL_RESPONDED_COLUMN_MERCHANTNAME));
					
					object.setDeviceRegId(rs.getString(DAOConstants.TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_REGISTRATION_ID));
					object.setDeviceType(rs.getString(DAOConstants.TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_TYPE));
					
					alFlashDealRespondedData.add(object);
				}
			}
			else{
				logger.debug("No respoded flash deals found in DAO layer to be notified.");
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("fetchMobileNotifiableRespondedFlashDeals", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchMobileNotifiableRespondedFlashDeals() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchMobileNotifiableRespondedFlashDeals", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchMobileNotifiableRespondedFlashDeals() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchMobileNotifiableRespondedFlashDeals() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alFlashDealRespondedData;
	}


	@Override
	public boolean updateRespondedFlashDealsNotifiedStatus(String flashDealRespondedIds) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		Statement stmt = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			String sql = SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_1 + flashDealRespondedIds + SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_2;
			stmt = con.createStatement();
			
			logger.debug("[updateRespondedFlashDealsNotifiedStatus()-QUERY] : " + sql);
			
			int rowsUpdated = stmt.executeUpdate(sql);
			
			if (rowsUpdated <= 0){
				logger.error("updateRespondedFlashDealsNotifiedStatus() -> Failed to update flash deals notified status in database.");
				throw new DataAccessException("updateRespondedFlashDealsNotifiedStatus() -> Failed to update flash deals notified status in database.");
			}
			else{
				logger.debug("Flash Deals notified status are updated successfully in database.");
				con.commit();
				flag = true;
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateRespondedFlashDealsNotifiedStatus", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsNotifiedStatus() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateRespondedFlashDealsNotifiedStatus", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsNotifiedStatus() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateRespondedFlashDealsNotifiedStatus() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}
	
	@Override
	public boolean updateRespondedFlashDealsNotifiedFailedStatus(String flashDealRespondedIds) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		Statement stmt = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			String sql = SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_FAILED_1 + flashDealRespondedIds + SQL_UPDATE_NOTIFIED_RESPONDED_FLASHDEALS_FAILED_2;
			stmt = con.createStatement();
			
			logger.debug("[updateRespondedFlashDealsNotifiedFailedStatus()-QUERY] : " + sql);
			
			int rowsUpdated = stmt.executeUpdate(sql);
			
			if (rowsUpdated <= 0){
				logger.error("updateRespondedFlashDealsNotifiedFailedStatus() -> Failed to update flash deals notified status in database.");
				throw new DataAccessException("updateRespondedFlashDealsNotifiedFailedStatus() -> Failed to update flash deals notified status in database.");
			}
			else{
				logger.debug("Flash Deals notified status are updated successfully in database.");
				con.commit();
				flag = true;
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateRespondedFlashDealsNotifiedFailedStatus", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsNotifiedFailedStatus() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateRespondedFlashDealsNotifiedFailedStatus", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateRespondedFlashDealsNotifiedFailedStatus() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateRespondedFlashDealsNotifiedFailedStatus() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}
	

}
