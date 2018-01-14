/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserDeviceAuthData;

/**
 * @author Rishi
 *
 */
public interface IUserDAO {
	
	public boolean login(UserData userData) throws DataAccessException;
	
	public boolean registration(UserData userData) throws DataAccessException;
	
	public UserData loadProfile(String username) throws DataAccessException;
	
	public boolean updateProfile(UserData userData) throws DataAccessException;
	
	public boolean forgotPasswordVerification(String username, String dateOfBirth) throws DataAccessException;
	
	public boolean changePassword(String username, String password) throws DataAccessException;
	
	public boolean isUserNameAvailable(String username) throws DataAccessException;
	
	public boolean updateUserLocationCoordinates(String locationCoordinates, String username) throws DataAccessException;
	
	public String fetchUserLocationCoordinates(String username) throws DataAccessException;
	
	public boolean updateUserPicturePath(String picturePath, String username) throws DataAccessException;
	
	public String loginFacebook(String fbEmail) throws DataAccessException;
	
	public boolean updateFBLogin(String fbEmail, String username) throws DataAccessException;
	
	public boolean createFBLogin(String fbEmail, String username) throws DataAccessException;
	
	public String getUserPicturePath(String username) throws DataAccessException;
	
	public boolean updateUserDeviceAuthDetails(UserDeviceAuthData userDeviceAuthData) throws DataAccessException;
	
	public boolean clearUserDeviceAuthDetails(String username) throws DataAccessException;
	
	public UserDeviceAuthData fetchUserDeviceAuthDetails(String username) throws DataAccessException;

}
