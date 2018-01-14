/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.threadManager.RefreshUserFavouritesSearchResults;
import com.fynger.servicesBusiness.business.threadManager.ThreadManager;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FBLoginException;
import com.fynger.servicesBusiness.exception.IncorrectPasswordException;
import com.fynger.servicesBusiness.exception.OpenfireUserRegistrationException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserDeviceAuthData;
import com.fynger.servicesBusiness.integration.openfire.OpenfireManager;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.LoginValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLoginRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLoginResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSUserDeviceAuth;

/**
 * @author Rishi
 *
 */
public class LoginAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(LoginAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public LoginAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	

	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSUserLoginRequest wsUserLoginRequest = (WSUserLoginRequest)request;
		
		WSUserLoginResponse wsUserLoginResponse = new WSUserLoginResponse();
		
		try{
			switch(requestType){
			
				case USER_LOGIN: 
					login(wsUserLoginRequest, wsUserLoginResponse, requestType);
					break;
					
				case USER_LOGIN_FACEBOOK: 
					loginFacebook(wsUserLoginRequest, wsUserLoginResponse, requestType);
					break;
					
				case USER_LOGIN_FACEBOOK_CREATE: 
					createLoginFacebook(wsUserLoginRequest, wsUserLoginResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Login service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(IncorrectPasswordException ipEx){
			logger.debug("IncorrectPasswordException occurred in DAO layer due to the password mismatch in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_INCORRECT_PASSWORD, ExceptionConstants.USERMESSAGE_INCORRECT_PASSWORD, ExceptionConstants.ERRORMESSAGE_INCORRECT_PASSWORD, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FBLoginException fbEx){
			logger.debug("FBLoginException occurred : " + fbEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FB_LOGIN_EXCEPTION, ExceptionConstants.USERMESSAGE_FB_LOGIN_EXCEPTION, fbEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(OpenfireUserRegistrationException openfireEx){
			logger.debug("OpenfireUserRegistrationException occurred during the registration with openfire : "+ openfireEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_OPEN_REGISTRATION_EXCEPTION, ExceptionConstants.USERMESSAGE_OPEN_REGISTRATION_EXCEPTION, openfireEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_DATA_ACCESS_EXCEPTION, ExceptionConstants.USERMESSAGE_DATA_ACCESS_EXCEPTION, daEx.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
			
		return wsUserLoginResponse;
	}
	
	public void login(WSUserLoginRequest wsUserLoginRequest, WSUserLoginResponse wsUserLoginResponse, ERequestType requestType){
		
		IBusinessValidator validator = new LoginValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsUserLoginRequest, requestType);
		
		if (isValid){
			IUserDAO userDao = daoFactory.getUserDAO();
			
			UserData userData = populateDAOData(wsUserLoginRequest);
			boolean flag = userDao.login(userData);
			
			if (flag){
				logger.debug("User login credenetials are valid.");
				
				/* Update user device auth details */
				WSUserDeviceAuth wsUserDeviceAuth = wsUserLoginRequest.getDeviceAuth();				
				if (null != wsUserDeviceAuth){
					logger.debug("Updating user device auth details");
					flag = updateUserMobileAuthDetails(wsUserLoginRequest, userDao);
					
					if (flag){
						logger.debug("User Device Auth details are updated successfully.");
					}
					else{
						logger.error("User Device Auth details are failed to update successfully.");
					}
				}
				
				spawnUserFavouritePromotionsThread(wsUserLoginRequest.getUsername());
				
				generateSuccessResponse(wsUserLoginResponse);
			}
			else{
				logger.debug("User login info is invalid.");
				throw new BusinessException("User login info is invalid.");
			}
		}
		else{
			logger.debug("User login validations failed.");
			throw new BusinessException("User login validations failed.");
		}
		
	}
	
	public void loginFacebook(WSUserLoginRequest wsUserLoginRequest, WSUserLoginResponse wsUserLoginResponse, ERequestType requestType){
		
		IBusinessValidator validator = new LoginValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsUserLoginRequest, requestType);
		
		if (isValid){
			IUserDAO userDao = daoFactory.getUserDAO();
			
			String username = userDao.loginFacebook(wsUserLoginRequest.getFBEmail());
			
			if (!GenericUtility.safeTrim(username).equals(GenericConstants.EMPTY_STRING)){
				logger.debug("User entry found in the database for the FB user based on the email search. User name : " + username);
				
				wsUserLoginResponse.setUsername(username);
				spawnUserFavouritePromotionsThread(username);
				
				/* Update user location coordinates */
				if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
					String coor = wsUserLoginRequest.getLocationCoordinates();
					String[] split = coor.split("\\|");
					
					double latitude = Double.parseDouble(split[0]);
					double longitude = Double.parseDouble(split[1]);
					
					if (latitude != 0 && longitude != 0){
						boolean flag = userDao.updateUserLocationCoordinates(wsUserLoginRequest.getLocationCoordinates(), username);
						
						if (flag){
							logger.debug("User coordinates updated successfully in the database for username : " + username);
						}
						else{
							logger.error("User coordinates failed to update successfully in the database for username : " + username);
						}
					}
				}
				
				/* Update user device auth details */
				WSUserDeviceAuth wsUserDeviceAuth = wsUserLoginRequest.getDeviceAuth();				
				if (null != wsUserDeviceAuth){
					logger.debug("Updating user device auth details");
					wsUserLoginRequest.setUsername(username);
					boolean flag = updateUserMobileAuthDetails(wsUserLoginRequest, userDao);
					
					if (flag){
						logger.debug("User Device Auth details are updated successfully.");
					}
					else{
						logger.error("User Device Auth details are failed to update successfully.");
					}
				}
				
				generateSuccessResponse(wsUserLoginResponse);
				
			}
			else{
				logger.debug("User entry NOT found in the database for the FB user based on the email search. Email : " + wsUserLoginRequest.getFBEmail());
				
				generateFalseResponse(wsUserLoginResponse);
			}
		}
		else{
			logger.debug("User login - Facebook Login validations failed.");
			throw new BusinessException("User login - Facebook Login validations failed.");
		}
		
	}
	
	public void createLoginFacebook(WSUserLoginRequest wsUserLoginRequest, WSUserLoginResponse wsUserLoginResponse, ERequestType requestType){
		
		IBusinessValidator validator = new LoginValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsUserLoginRequest, requestType);
		
		if (isValid){
			if (GenericUtility.safeTrim(wsUserLoginRequest.getFyngerUser()).equals(GenericConstants.STRING_TRUE)){
				logger.debug("FB user is also a Fynger user");
				
				IUserDAO userDao = daoFactory.getUserDAO();
				
				UserData userData = populateDAOData(wsUserLoginRequest);
				boolean flag = userDao.login(userData);
				
				if (flag){
					logger.debug("User login credenetials are valid.");
					
					flag = userDao.updateFBLogin(wsUserLoginRequest.getFBEmail(), wsUserLoginRequest.getUsername());
					
					if (flag){
						logger.debug("FB User's Email is successfully updated in the database.");
						
						/* Update user location coordinates */
						if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
							String coor = wsUserLoginRequest.getLocationCoordinates();
							String[] split = coor.split("\\|");
							
							double latitude = Double.parseDouble(split[0]);
							double longitude = Double.parseDouble(split[1]);
							
							if (latitude != 0 && longitude != 0){
								flag = userDao.updateUserLocationCoordinates(wsUserLoginRequest.getLocationCoordinates(), wsUserLoginRequest.getUsername());
								
								if (flag){
									logger.debug("User coordinates updated successfully in the database for username : " + wsUserLoginRequest.getUsername());
								}
								else{
									logger.error("User coordinates failed to update successfully in the database for username : " + wsUserLoginRequest.getUsername());
								}
							}
						}
						
						/* Update user device auth details */
						WSUserDeviceAuth wsUserDeviceAuth = wsUserLoginRequest.getDeviceAuth();				
						if (null != wsUserDeviceAuth){
							logger.debug("Updating user device auth details");
							flag = updateUserMobileAuthDetails(wsUserLoginRequest, userDao);
							
							if (flag){
								logger.debug("User Device Auth details are updated successfully.");
							}
							else{
								logger.error("User Device Auth details are failed to update successfully.");
							}
						}
						
						spawnUserFavouritePromotionsThread(wsUserLoginRequest.getUsername());
						
						generateSuccessResponse(wsUserLoginResponse);
						wsUserLoginResponse.setUsername(wsUserLoginRequest.getUsername());
						
						/* Register the user with Openfire server */
						
						OpenfireManager openfireManager = new OpenfireManager();
						boolean openfireFlag = openfireManager.registerUser(wsUserLoginRequest.getUsername(), wsUserLoginRequest.getPassword(), wsUserLoginRequest.getFBEmail(), null);
						
						if (openfireFlag){
							logger.debug("FB User is registered successfully with openfire for username : " + wsUserLoginRequest.getUsername());
						}
						else{
							logger.debug("Failed to register FB user with openfire for username : " + wsUserLoginRequest.getUsername());
						}
						
					}
					else{
						logger.error("FB User's Email failed to update in the database.");
						throw new FBLoginException("FB User's Email failed to update in the database.");
					}
				}
				else{
					logger.debug("User login info is invalid.");
					throw new FBLoginException("User login info is invalid.");
				}
			}
			else{
				logger.debug("FB user is NOT a Fynger user. New record will be created for him");
				
				IUserDAO userDao = daoFactory.getUserDAO();
				
				String fbUsername = wsUserLoginRequest.getFBEmail().substring(0, wsUserLoginRequest.getFBEmail().indexOf("@"));
				
				boolean flag = userDao.createFBLogin(wsUserLoginRequest.getFBEmail(), fbUsername);
				
				if (flag){
					logger.debug("User record is saved successfully in the database.");
					
					String fbPassword = Utility.generateFBPassword();
					
					/* Update user location coordinates */
					if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
						String coor = wsUserLoginRequest.getLocationCoordinates();
						String[] split = coor.split("\\|");
						
						double latitude = Double.parseDouble(split[0]);
						double longitude = Double.parseDouble(split[1]);
						
						if (latitude != 0 && longitude != 0){
							flag = userDao.updateUserLocationCoordinates(wsUserLoginRequest.getLocationCoordinates(), fbUsername);
							
							if (flag){
								logger.debug("User coordinates updated successfully in the database for username : " + fbUsername);
							}
							else{
								logger.error("User coordinates failed to update successfully in the database for username : " + fbUsername);
							}
						}
					}
					
					/* Update user device auth details */
					WSUserDeviceAuth wsUserDeviceAuth = wsUserLoginRequest.getDeviceAuth();				
					if (null != wsUserDeviceAuth){
						logger.debug("Updating user device auth details");
						wsUserLoginRequest.setUsername(fbUsername);
						flag = updateUserMobileAuthDetails(wsUserLoginRequest, userDao);
						
						if (flag){
							logger.debug("User Device Auth details are updated successfully.");
						}
						else{
							logger.error("User Device Auth details are failed to update successfully.");
						}
					}
					
					spawnUserFavouritePromotionsThread(fbUsername);
					
					generateSuccessResponse(wsUserLoginResponse);
					wsUserLoginResponse.setUsername(fbUsername);
					wsUserLoginResponse.setFbPassword(fbPassword);
					
					/* Register the user with Openfire server */
					
					OpenfireManager openfireManager = new OpenfireManager();
					boolean openfireFlag = openfireManager.registerUser(fbUsername, fbPassword, wsUserLoginRequest.getFBEmail(), null);
					
					if (openfireFlag){
						logger.debug("FB User is registered successfully with openfire for username : " + wsUserLoginRequest.getUsername());
					}
					else{
						logger.debug("Failed to register FB user with openfire for username : " + wsUserLoginRequest.getUsername());
					}
				}
				else{
					logger.debug("User record is failed to save in the database.");
					throw new FBLoginException("User record is failed to save in the database.");
				}
			}
		}
		else{
			logger.debug("User login - Create Facebook Login validations failed.");
			throw new FBLoginException("User login - Create Facebook Login validations failed.");
		}
		
	}
	
	public UserData populateDAOData(WSUserLoginRequest wsUserLoginRequest){
		UserData userData = new UserData();
		
		userData.setUsername(wsUserLoginRequest.getUsername());
		userData.setPassword(wsUserLoginRequest.getPassword());
		userData.setLocationCoordinates(wsUserLoginRequest.getLocationCoordinates());
		
		return userData;
		
	}
	
	private void spawnUserFavouritePromotionsThread(String username){
		
		/* Spawn thread to refresh user favourites promotions on configurable time interval */
		logger.info("Calling Thread Manager to spawn Refresh User Favourite search results thread ...");
		ThreadManager threadManager = ThreadManager.createInstance();
		
		RefreshUserFavouritesSearchResults thread = threadManager.getRefreshUserFavPromotionsThread(username);
		
		if (null == thread){
			logger.debug("Spawning thread for username : " + username);
			threadManager.spawnRefreshUserFavouriteSearchThread(username);
			
			logger.debug("Starting thread for username : " + username);
			threadManager.startRefreshUserFavouriteSearchThread(username);
		}
		else{
			if (!thread.getStatus().equals(ThreadManager.THREAD_STATUS_RUNNING)){
				logger.debug("Starting thread for username : " + username);
				threadManager.startRefreshUserFavouriteSearchThread(username);
			}
		}
	}
	
	private boolean updateUserMobileAuthDetails(WSUserLoginRequest wsUserLoginRequest, IUserDAO userDao){
		
		WSUserDeviceAuth wsUserDeviceAuth = wsUserLoginRequest.getDeviceAuth();
		
		UserDeviceAuthData daoData = new UserDeviceAuthData();
		daoData.setUsername(wsUserLoginRequest.getUsername());
		daoData.setDeviceRegId(wsUserDeviceAuth.getDeviceRegistrationId());
		daoData.setDeviceType(wsUserDeviceAuth.getDeviceType());
		
		boolean flag = userDao.updateUserDeviceAuthDetails(daoData);
		
		return flag;
		
	}
	
	public void generateSuccessResponse(WSUserLoginResponse wsUserLoginResponse){
		
		wsUserLoginResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}
	
	public void generateFalseResponse(WSUserLoginResponse wsUserLoginResponse){
		
		wsUserLoginResponse.setResponse(BusinessConstants.RESPONSE_FALSE);
		
	}

}
