/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FBLoginException;
import com.fynger.servicesBusiness.exception.ForgotPasswordVerificationFailedException;
import com.fynger.servicesBusiness.exception.IncorrectPasswordException;
import com.fynger.servicesBusiness.exception.UserNameExistException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOUtility;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserDeviceAuthData;


/**
 * @author Rishi
 *
 */
public class UserDAOImpl implements IUserDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(UserDAOImpl.class.getName());
	
	
	private static final String SQL_SELECT_FIND_USERNAME = "SELECT * FROM user_login WHERE username = ?";
	
	private static final String SQL_SELECT_FIND_USERNAME_EXISTS = "SELECT * FROM user_login WHERE username = ?";
	
	private static final String SQL_INSERT_USER_PROFILE = "INSERT INTO user_profile (full_name, date_of_birth, email, phone, zipcode, gender, picture_path, created_on, last_updated) " +
															"values (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_USER_LOGIN = "INSERT INTO user_login (login_id, username, password, created_on, last_updated)  values(?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_LOAD_USER_PROFILE = "SELECT * FROM user_profile, user_login WHERE user_profile.login_id = user_login.login_id AND user_login.username = ?";
	
	private static final String SQL_UPDATE_USER_PROFILE = "UPDATE user_profile, user_login SET full_name = ?, date_of_birth = ?, email = ?, phone = ?, zipcode = ?, gender = ?, " +
															"user_profile.last_updated = NOW() WHERE user_profile.login_id = user_login.login_id AND user_login.username = ?";
	
	private static final String SQL_SELECT_FORGOT_PASSWORD_VERIFICATION = "SELECT * FROM user_profile, user_login WHERE user_profile.login_id = user_login.login_id AND user_login.username = ? AND user_profile.date_of_birth = ?";
	
	private static final String SQL_UPDATE_PASSWORD = "UPDATE user_login SET password = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_SELECT_FIND_USERNAME_AVAILABILITY = "SELECT * FROM user_login WHERE username = ?";
	
	private static final String SQL_UPDATE_USER_LOCATIONCOORDINATES = "UPDATE user_login SET previous_location_coordinates = location_coordinates, location_coordinates = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_SELECT_USER_LOCATIONCOORDINATES = "SELECT location_coordinates FROM user_login WHERE username = ?";
	
	private static final String SQL_UPDATE_USER_PROFILE_PICTURE = "UPDATE user_profile, user_login SET user_profile.picture_path = ?, user_profile.last_updated = NOW() WHERE user_profile.login_id = user_login.login_id AND user_login.username = ?";
	
	private static final String SQL_SELECT_FB_USER_LOGIN = "SELECT * FROM user_login WHERE fbemail = ?";
	
	private static final String SQL_UPDATE_FB_USER_LOGIN = "UPDATE user_login SET fbemail = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_INSERT_FB_USER_PROFILE = "INSERT INTO user_profile (email, created_on, last_updated) values (?, NOW(), NOW())";
	
	private static final String SQL_INSERT_FB_USER_LOGIN = "INSERT INTO user_login (login_id, username, fbemail, isfbuser, created_on, last_updated)  values(?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_USER_PICTURE_PATH = "SELECT user_profile.picture_path FROM user_profile, user_login WHERE user_profile.login_id = user_login.login_id AND user_login.username = ?";
	
	private static final String SQL_SELECT_USER_MOBILE_AUTH = "SELECT * FROM user_mobile_auth WHERE username = ?";
	
	private static final String SQL_INSERT_USER_MOBILE_AUTH = "INSERT INTO user_mobile_auth (username, device_reg_id, device_type, created_on, last_updated) VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_UPDATE_USER_MOBILE_AUTH	= "UPDATE user_mobile_auth SET device_reg_id = ?, device_type = ?, last_updated = NOW() WHERE username = ?";
	
	private static final String SQL_UPDATE_USER_MOBILE_AUTH_FLUSH	= "UPDATE user_mobile_auth SET device_reg_id = NULL, device_type = NULL, last_updated = NOW() WHERE username = ?"; 
	
	
	public UserDAOImpl() {
		
	}
	
	public boolean login(UserData userData) throws DataAccessException{
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_FIND_USERNAME);
			ps.setString(1, userData.getUsername());
			
			rs = ps.executeQuery();
			
			logger.debug("[login()-QUERY] : " + ps.toString());
			
			if (rs.next()){
				logger.debug("User name found in the database.");
				
				String dbPassword = rs.getString("password");
				String userPassword = DAOUtility.encryptPassword(userData.getPassword());
				
				if (GenericUtility.safeTrim(userPassword).equals(GenericUtility.safeTrim(dbPassword))){
					logger.debug("Password matched.");
					
					if (!GenericUtility.safeTrim(userData.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
						
						String coor = userData.getLocationCoordinates();
						String[] split = coor.split("\\|");
						
						double latitude = Double.parseDouble(split[0]);
						double longitude = Double.parseDouble(split[1]);
						
						if (latitude != 0 && longitude != 0){
							logger.debug("Location coordinates will be updated");
							updateUserLocationCoordinates(userData.getLocationCoordinates(), userData.getUsername());
						}
					}
					
					flag = true;
				}
				else{
					logger.debug("Password NOT matched.");
					throw new IncorrectPasswordException("Password not matched with the stored password in the database.");
				}
			}
			else{
				logger.debug("User name NOT found in the database.");
				throw new UserNameNotFoundException("User Name not found in the database.");
			}
		}
		catch(IncorrectPasswordException ipEx){			
			throw ipEx;
		}
		catch(UserNameNotFoundException unnfEx){
			throw unnfEx;
		}
		catch(SQLException sqlEx){
			logger.error("login", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("login() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("login", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("login() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("login() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}
	
	public boolean registration(UserData userData) throws DataAccessException{
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Check if user name already exists or not */
			ps = con.prepareStatement(SQL_SELECT_FIND_USERNAME_EXISTS);
			ps.setString(1, userData.getUsername());
			
			rs = ps.executeQuery();
			
			logger.debug("[registration()-QUERY 1] : " + ps.toString());
			
			if (rs.next()){
				logger.debug("User name already exists in the database.");
				throw new UserNameExistException("User name already exists in the database.");
			}
			else{
				/* Insert the user profile details in database */
				ps = con.prepareStatement(SQL_INSERT_USER_PROFILE, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, userData.getFullName());
				ps.setString(2, userData.getDateOfBirth());
				ps.setString(3, userData.getEmail());
				ps.setString(4, userData.getPhone());
				ps.setString(5, userData.getZipcode());
				ps.setString(6, userData.getGender());
				ps.setString(7, userData.getUserPicturePath());
				
				int rowsInserted = ps.executeUpdate();
				
				logger.debug("[registration()-QUERY 2] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("registration() -> Failed to insert record for User's registration in database.");
				}
				generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()){
					userData.setLoginId(Integer.toString(generatedKeys.getInt(1)));
				}
				else{
					throw new DataAccessException("registration() -> Failed to insert record for User's registration in database, no generated key obtained.");
				}
				
				/* Insert the user login details in database */
				ps = con.prepareStatement(SQL_INSERT_USER_LOGIN);
				ps.setString(1, userData.getLoginId());
				ps.setString(2, userData.getUsername());
				ps.setString(3, DAOUtility.encryptPassword(userData.getPassword()));
				
				rowsInserted = ps.executeUpdate();
				
				logger.debug("[registration()-QUERY 3] : " + ps.toString());
				
				if (rowsInserted <= 0){
					throw new DataAccessException("registration() -> Failed to insert record for User's Login in database. Profile details will be rollbacked.");
				}
				else{
					logger.debug("User Profile data is saved successfully in database.");
					flag = true;
					con.commit();
				}
			}
			
		}
		catch(UserNameExistException uneEx){			
			throw uneEx;
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("registration() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("registration() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("registration() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("registration() -> SQLException occurred during rollback.");
			}
			logger.error("registration", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("registration() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("registration() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("registration() -> SQLException occurred during rollback.");
			}
			logger.error("registration", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("registration() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, generatedKeys);
				DatabaseConnectionManager.clearResources(rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("registration() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}
	
	public UserData loadProfile(String username) throws DataAccessException{
		
		UserData userData = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_SELECT_LOAD_USER_PROFILE);
			ps.setString(1, username);
			
			rs = ps.executeQuery();
			
			logger.debug("[loadProfile()-QUERY] : " + ps.toString());
			
			if (rs.next()){
				userData = new UserData();
				
				userData.setFullName(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
				userData.setDateOfBirth(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_DATEOFBIRTH));
				userData.setEmail(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_EMAIL));
				userData.setPhone(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PHONE));
				userData.setZipcode(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_ZIPCODE));
				userData.setGender(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_GENDER));
				userData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
			
			}
			else{
				logger.debug("User name NOT found in the database.");
				throw new UserNameNotFoundException("User Name not found in the database.");
			}
			
		}
		catch(UserNameNotFoundException unnfEx){
			throw unnfEx;
		}
		catch(SQLException sqlEx){
			logger.error("loadProfile", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadProfile() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadProfile", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadProfile() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadProfile() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return userData;
		
	}
	
	public boolean updateProfile(UserData userData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Update the user profile details in database */
			ps = con.prepareStatement(SQL_UPDATE_USER_PROFILE);
			ps.setString(1, userData.getFullName());
			ps.setString(2, userData.getDateOfBirth());
			ps.setString(3, userData.getEmail());
			ps.setString(4, userData.getPhone());
			ps.setString(5, userData.getZipcode());
			ps.setString(6, userData.getGender());
			ps.setString(7, userData.getUsername());
			
			int rowsInserted = ps.executeUpdate();
			
			logger.debug("[updateProfile()-QUERY 1] : " + ps.toString());
			
			if (rowsInserted <= 0){
				throw new DataAccessException("updateProfile() -> Failed to update record for User's registration in database.");
			}
			else{
				logger.debug("User Profile data is updated successfully in database.");
				flag = true;
				con.commit();
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("updateProfile", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateProfile() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateProfile", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateProfile() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateProfile() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
	}
	
	public boolean forgotPasswordVerification(String username, String dateOfBirth) throws DataAccessException{
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_FORGOT_PASSWORD_VERIFICATION);
			ps.setString(1, username);
			ps.setString(2, dateOfBirth);
			
			rs = ps.executeQuery();
			
			logger.debug("[verifyUserForPasswordUpdate()-QUERY] : " + ps.toString());
			
			if (rs.next()){
				logger.debug("User verification details for Forgot password PASSED.");
				flag = true;
			}
			else{
				logger.debug("User verification details for Forgot password FAILED.");
				throw new ForgotPasswordVerificationFailedException("User verification details for Forgot password FAILED.");
			}
		}
		catch(ForgotPasswordVerificationFailedException fpvEx){
			throw fpvEx;
		}
		catch(SQLException sqlEx){
			logger.error("forgotPasswordVerification", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("forgotPasswordVerification() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("forgotPasswordVerification", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("forgotPasswordVerification() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("forgotPasswordVerification() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}
	
	public boolean changePassword(String username, String password) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Update the password in database */
			ps = con.prepareStatement(SQL_UPDATE_PASSWORD);
			ps.setString(1, DAOUtility.encryptPassword(password));
			ps.setString(2, username);
			
			int rowsUpdated = ps.executeUpdate();
			
			logger.debug("[changePassword()-QUERY 1] : " + ps.toString());
			
			if (rowsUpdated <= 0){
				throw new DataAccessException("changePassword() -> Failed to update user's password in database.");
			}
			else{
				logger.debug("User new password is updated successfully in database.");
				flag = true;
				con.commit();
			}
		}
		catch(SQLException sqlEx){
			logger.error("changePassword", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("changePassword() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("changePassword", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("changePassword() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("changePassword() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}
	
	public boolean isUserNameAvailable(String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_FIND_USERNAME_AVAILABILITY);
			ps.setString(1, username);
			
			rs = ps.executeQuery();
			
			logger.debug("[isUserNameAvailable()-QUERY] : " + ps.toString());
			
			if (rs.next()){
				logger.debug("User name exists in the database.");
			}
			else{
				logger.debug("User name does not exists in the database.");
				flag = true;
			}
		}
		catch(SQLException sqlEx){
			logger.error("isUserNameAvailable", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("userNameAvailability() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("isUserNameAvailable", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("userNameAvailability() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("isUserNameAvailable() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}

	@Override
	public boolean updateUserLocationCoordinates(String locationCoordinates, String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_UPDATE_USER_LOCATIONCOORDINATES);
			ps.setString(1, locationCoordinates);
			ps.setString(2, username);
			
			logger.debug("[updateUserLocationCoordinates()-QUERY 1] : " + ps.toString());
			
			int rowsUpdated = ps.executeUpdate();
			
			if (rowsUpdated <= 0){
				throw new DataAccessException("updateUserLocationCoordinates() -> Failed to update user's location coordinates in database for user name : " + username);
			}
			else{
				logger.debug("User location coordinates are updated successfully in database.");
				flag = true;
				con.commit();
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateUserLocationCoordinates", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateUserLocationCoordinates() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateUserLocationCoordinates", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateUserLocationCoordinates() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateUserLocationCoordinates() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
	}

	@Override
	public String fetchUserLocationCoordinates(String username) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String locationCoordinates = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_USER_LOCATIONCOORDINATES);
			ps.setString(1, username);
			
			logger.debug("[fetchUserLocationCoordinates()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				locationCoordinates = rs.getString(DAOConstants.TABLE_USER_LOGIN_COLUMN_LOCATION_COORDINATES);
			}
			else{
				logger.debug("User name NOT found in the database.");
				throw new UserNameNotFoundException("User Name not found in the database.");
			}
		}
		catch(UserNameNotFoundException unnfEx){
			throw unnfEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchUserLocationCoordinates", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchUserLocationCoordinates() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchUserLocationCoordinates", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchUserLocationCoordinates() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchUserLocationCoordinates() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return locationCoordinates;
		
	}

	@Override
	public boolean updateUserPicturePath(String picturePath, String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_UPDATE_USER_PROFILE_PICTURE);
			ps.setString(1, picturePath);
			ps.setString(2, username);
			
			logger.debug("[updateUserPicturePath()-QUERY] : " + ps.toString());
			
			int rowsUpdated = ps.executeUpdate();
			
			if (rowsUpdated <= 0){
				throw new DataAccessException("updateUserPicturePath() -> Failed to update user's picture path in database for user name : " + username);
			}
			else{
				logger.debug("User Picture Path is updated successfully in database.");
				flag = true;
				con.commit();
			}
		}
		catch(UserNameNotFoundException unnfEx){
			throw unnfEx;
		}
		catch(SQLException sqlEx){
			logger.error("updateUserPicturePath", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateUserPicturePath() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateUserPicturePath", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateUserPicturePath() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateUserPicturePath() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	}
	
	@Override
	public String loginFacebook(String fbEmail) throws DataAccessException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String username = "";
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_FB_USER_LOGIN);
			ps.setString(1, fbEmail);
			
			logger.debug("[loginFacebook()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				username = rs.getString(DAOConstants.TABLE_USER_LOGIN_COLUMN_USERNAME);
			}
			else{
				logger.debug("FB User's Email does not exist in the database.");
			}
		}
		catch(SQLException sqlEx){
			logger.error("loginFacebook", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loginFacebook() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loginFacebook", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loginFacebook() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loginFacebook() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return username;
	}
	
	@Override
	public boolean updateFBLogin(String fbEmail, String username) throws DataAccessException{
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		boolean flag = false;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_UPDATE_FB_USER_LOGIN);
			ps.setString(1, fbEmail);
			ps.setString(2, username);
			
			logger.debug("[updateFBLogin()-QUERY] : " + ps.toString());
			
			int rowsUpdated = ps.executeUpdate();
			
			if (rowsUpdated <= 0){
				throw new FBLoginException("updateFBLogin() -> Failed to update user's FB Email in database for user name : " + username);
			}
			else{
				logger.debug("FB email is updated successfully in database.");
				
				flag = true;
				con.commit();
			}
		}
		catch(FBLoginException fbEx){
			throw fbEx;
		}
		catch(SQLException sqlEx){
			logger.error("updateFBEmail", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateFBEmail() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateFBEmail", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateFBEmail() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateFBEmail() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;
	}
	
	@Override
	public boolean createFBLogin(String fbEmail, String username) throws DataAccessException{
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		int loginId = 0;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			/* Insert the user profile details in database */
			ps = con.prepareStatement(SQL_INSERT_FB_USER_PROFILE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, fbEmail);
			
			int rowsInserted = ps.executeUpdate();
			
			logger.debug("[createFBLogin()-QUERY 1] : " + ps.toString());
			
			if (rowsInserted <= 0){
				throw new FBLoginException("createFBLogin() -> Failed to insert record for FB User's registration in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				loginId = generatedKeys.getInt(1);
			}
			else{
				throw new FBLoginException("createFBLogin() -> Failed to create login for FB User in database, no generated key obtained.");
			}
			
			/* Insert the user login details in database */
			ps = con.prepareStatement(SQL_INSERT_FB_USER_LOGIN);
			ps.setInt(1, loginId);
			ps.setString(2, username);
			ps.setString(3, fbEmail);
			ps.setInt(4, DAOConstants.LOGIN_FBUSER_YES);
			
			logger.debug("[createFBLogin()-QUERY 2] : " + ps.toString());
			
			rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				throw new DataAccessException("createFBLogin() -> Failed to insert record for FB User's Login in database. Profile details will be rollbacked.");
			}
			else{
				logger.debug("FB User login data is saved successfully in database.");
				
				flag = true;
				con.commit();
			}
		}
		catch(DataAccessException daEx){
			try {
				logger.debug("createFBLogin() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createFBLogin() -> SQLException occurred during rollback.");
			}
			throw daEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("createFBLogin() -> SQLException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createFBLogin() -> SQLException occurred during rollback.");
			}
			logger.error("createFBLogin", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("createFBLogin() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("createFBLogin() -> Exception occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createFBLogin() -> SQLException occurred during rollback.");
			}
			logger.error("createFBLogin", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("createFBLogin() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, generatedKeys);
				DatabaseConnectionManager.clearResources(rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("createFBLogin() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		return flag;
		
	}
	
	public String getUserPicturePath(String username) throws DataAccessException{
		
		String userPicturePath = "";
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_SELECT_USER_PICTURE_PATH);
			ps.setString(1, username);
			
			logger.debug("[getUserPicturePath()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				userPicturePath = rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH);
			}
			else{
				logger.debug("User name NOT found in the database.");
				throw new UserNameNotFoundException("User Name not found in the database.");
			}
		}
		catch(UserNameNotFoundException unnfEx){
			throw unnfEx;
		}
		catch(SQLException sqlEx){
			logger.error("getUserPicturePath", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("getUserPicturePath() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("getUserPicturePath", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("getUserPicturePath() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("getUserPicturePath() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return userPicturePath;
	}

	@Override
	public boolean updateUserDeviceAuthDetails(UserDeviceAuthData userDeviceAuthData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_SELECT_USER_MOBILE_AUTH);
			ps.setString(1, userDeviceAuthData.getUsername());
			
			logger.debug("[updateUserDeviceAuthDetails()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("Record exists in the USER_MOBILE_AUTH table, the existing record will be updated for username : " + userDeviceAuthData.getUsername());
				
				ps = con.prepareStatement(SQL_UPDATE_USER_MOBILE_AUTH);
				ps.setString(1, userDeviceAuthData.getDeviceRegId());
				ps.setString(2, userDeviceAuthData.getDeviceType());
				ps.setString(3, userDeviceAuthData.getUsername());
				
				logger.debug("[updateUserDeviceAuthDetails()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					throw new DataAccessException("updateUserDeviceAuthDetails() -> Failed to update record for user mobile auth in database.");
				}
				else{
					logger.debug("User mobile Auth is updated successfully in database.");
					
					flag = true;
					con.commit();
				}
			}
			else{
				logger.debug("Record does not exist in the USER_MOBILE_AUTH table, new record will be created for username : " + userDeviceAuthData.getUsername());
				
				ps = con.prepareStatement(SQL_INSERT_USER_MOBILE_AUTH);
				ps.setString(1, userDeviceAuthData.getUsername());
				ps.setString(2, userDeviceAuthData.getDeviceRegId());
				ps.setString(3, userDeviceAuthData.getDeviceType());
				
				logger.debug("[updateUserDeviceAuthDetails()-QUERY 3] : " + ps.toString());
				
				int rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					throw new DataAccessException("updateUserDeviceAuthDetails() -> Failed to insert record for user mobile auth in database.");
				}
				else{
					logger.debug("User mobile Auth is saved successfully in database.");
					
					flag = true;
					con.commit();
				}
			}
		}
		catch(SQLException sqlEx){
			logger.error("updateUserDeviceAuthDetails", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("updateUserDeviceAuthDetails() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("updateUserDeviceAuthDetails", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("updateUserDeviceAuthDetails() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("updateUserDeviceAuthDetails() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

	@Override
	public boolean clearUserDeviceAuthDetails(String username) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_UPDATE_USER_MOBILE_AUTH_FLUSH);
			ps.setString(1, username);
			
			logger.debug("[clearUserDeviceAuthDetails()-QUERY] : " + ps.toString());
			
			int rowsUpdated = ps.executeUpdate();
			
			if (rowsUpdated <= 0){
				logger.debug("clearUserDeviceAuthDetails() -> No record exists in USER MOBILE AUTH for user name : " + username);
			}
			else{
				logger.debug("User mobile Auth is updated successfully in database.");
				
				flag = true;
				con.commit();
			}
		}
		catch(SQLException sqlEx){
			logger.error("clearUserDeviceAuthDetails", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("clearUserDeviceAuthDetails() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("clearUserDeviceAuthDetails", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("clearUserDeviceAuthDetails() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("clearUserDeviceAuthDetails() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

	@Override
	public UserDeviceAuthData fetchUserDeviceAuthDetails(String username) throws DataAccessException {
		
		UserDeviceAuthData userDeviceAuthData = null;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_SELECT_USER_MOBILE_AUTH);
			ps.setString(1, username);
			
			logger.debug("[fetchUserDeviceAuthDetails()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("User Device Auth data exists for user : " + username);
				
				userDeviceAuthData = new UserDeviceAuthData();
				userDeviceAuthData.setDeviceRegId(rs.getString(DAOConstants.TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_REGISTRATION_ID));
				userDeviceAuthData.setDeviceType(rs.getString(DAOConstants.TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_TYPE));
			}
			else{
				logger.debug("User Device Auth data does not exist for user : " + username);
			}
		}
		catch(SQLException sqlEx){
			logger.error("fetchUserDeviceAuthDetails", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchUserDeviceAuthDetails() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchUserDeviceAuthDetails", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchUserDeviceAuthDetails() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchUserDeviceAuthDetails() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return userDeviceAuthData;
		
	}

}
