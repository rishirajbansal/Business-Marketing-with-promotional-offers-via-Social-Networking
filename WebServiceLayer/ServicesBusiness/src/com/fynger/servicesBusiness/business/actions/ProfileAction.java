/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FileUploadFailedException;
import com.fynger.servicesBusiness.exception.ForgotPasswordVerificationFailedException;
import com.fynger.servicesBusiness.exception.OpenfireUserRegistrationException;
import com.fynger.servicesBusiness.exception.UserNameExistException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserData;
import com.fynger.servicesBusiness.integration.openfire.OpenfireManager;
import com.fynger.servicesBusiness.objects.MailContent;
import com.fynger.servicesBusiness.utilities.FileUploader;
import com.fynger.servicesBusiness.utilities.MailDispatcher;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.ProfileValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserProfileRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserProfileResponse;

/**
 * @author Rishi
 *
 */
public class ProfileAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(ProfileAction.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	DAOFactory daoFactory = null;
	
	private static String mailContentType;
	private static String registrationMailSubject;
	private static String registrationMailBody;
	
	public ProfileAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	
	static{
		mailContentType 			= propertyManager.getProperty(GenericConstants.EMAIL_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONFIG_CONTENT_TYPE);
		registrationMailSubject 	= propertyManager.getProperty(GenericConstants.EMAIL_CONTENT_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONTENT_REGISTRATION_SUBJECT);
		registrationMailBody 		= propertyManager.getProperty(GenericConstants.EMAIL_CONTENT_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONTENT_REGISTRATION_BODY);
	}


	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSUserProfileRequest profileRequest = (WSUserProfileRequest)request;
		
		WSUserProfileResponse profileResponse = new WSUserProfileResponse();
		
		try{
			switch(requestType){
			
				case USER_PROFILE_REGISTRATION: 
					registration(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_LOAD: 
					loadProfile(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_UPDATE: 
					updateProfile(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_FORGOT_PASSWORD_VERIFICATION: 
					forgotPasswordVerification(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_FORGOT_PASSWORD_UPDATE: 
					changePassword(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_USERNAME_AVAILABILITY: 
					isUserNameAvailable(profileRequest, profileResponse, requestType);
					break;
					
				case USER_PROFILE_FILE_UPLOAD: 
					uploadUserPicture(profileRequest, profileResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of User Profile service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameExistException uneEx){
			logger.debug("UserNameExistException occurred in DAO layer as user name already exists in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_EXISTS, ExceptionConstants.USERMESSAGE_USERNAME_EXISTS, ExceptionConstants.ERRORMESSAGE_USERNAME_EXISTS, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ForgotPasswordVerificationFailedException fpvEx){
			logger.debug("ForgotPasswordVerificationFailedException occurred in DAO layer as user's forgot password verification failed");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FORGOT_PASSWORD_VERIFICATION_FAILED, ExceptionConstants.USERMESSAGE_FORGOT_PASSWORD_VERIFICATION_FAILED, ExceptionConstants.ERRORMESSAGE_FORGOT_PASSWORD_VERIFICATION_FAILED, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FileUploadFailedException fileEx){
			logger.debug("FileUploadFailedException occurred during uploading the user picture.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FILE_UPLOAD_FAILED, ExceptionConstants.USERMESSAGE_FILE_UPLOAD_FAILED, ExceptionConstants.ERRORMESSAGE_FILE_UPLOAD_FAILED, EExceptionTypes.BUSINESS_EXCEPTION);
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
		
		return profileResponse;
		
	}
	
	public void registration(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ProfileValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)profileRequest, requestType);
		
		if (isValid){
			IUserDAO userDao = daoFactory.getUserDAO();
			
			UserData userData = populateProfileDAOData(profileRequest);
			boolean flag = userDao.registration(userData);
			
			if (flag){
				logger.debug("User record is saved successfully in the database.");
				
				generateSuccessResponse(profileResponse);
				
				/* Send the Email on successful registration on User Email id */
				
				String body = registrationMailBody;
				body = body.replaceAll(GenericConstants.MAIL_CONTENT_REGISTRATION_BODY_PLACEHOLDER_USERNAME, profileRequest.getUsername());
				body = body.replaceAll(GenericConstants.MAIL_CONTENT_REGISTRATION_BODY_PLACEHOLDER_EMAILID, profileRequest.getEmail());
				
				MailContent mailContent = new MailContent();
				
				mailContent.setToMailId(profileRequest.getEmail());
				mailContent.setEvent(BusinessConstants.EMAIL_EVENT_REGISTRATION);
				mailContent.setUsername(profileRequest.getUsername());
				mailContent.setSubject(registrationMailSubject);
				mailContent.setBody(body);
				mailContent.setContentType(mailContentType);
				
				logger.debug("Sending Mail on Registration event...");
				
				MailDispatcher mailDispatcher = new MailDispatcher();
				boolean emailFlag = mailDispatcher.dispatchMail(mailContent);
				
				if (emailFlag){
					logger.debug("Email has been dispatched successfully.");
				}
				else{
					logger.debug("Fail to dispatch Email successfully. Check the email logs in the database.");
				}
				
				/* Register the user with Openfire server */
				
				OpenfireManager openfireManager = new OpenfireManager();
				boolean openfireFlag = openfireManager.registerUser(profileRequest.getUsername(), profileRequest.getPassword(), profileRequest.getEmail(), profileRequest.getFullName());
				
				if (openfireFlag){
					logger.debug("User is registered successfully with openfire for username : " + profileRequest.getUsername());
				}
				else{
					logger.debug("Failed to register user with openfire for username : " + profileRequest.getUsername());
				}
				
			}
			else{
				logger.debug("User record is failed to save in the database.");
				throw new BusinessException("User record is failed to save in the database.");
			}
		}
		else{
			logger.debug("User Profile - Registration validations failed.");
			throw new BusinessException("User Profile - Registration validations failed.");
		}
	}
	
	public void loadProfile(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IUserDAO userDao = daoFactory.getUserDAO();
		
		UserData userData = userDao.loadProfile(profileRequest.getUsername());
		
		if (null != userData){
			logger.debug("User Profile is fetched succssfully from the database.");
			populateProfileResponseFromDAOData(userData, profileResponse);
		}
		else{
			logger.debug("Failed to load User Profile from the database.");
			throw new BusinessException("Failed to load User Profile from the database.");
		}
		
	}
	
	public void updateProfile(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ProfileValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)profileRequest, requestType);
		
		if (isValid){
			IUserDAO userDao = daoFactory.getUserDAO();
			
			UserData userData = populateProfileDAOData(profileRequest);			
			boolean flag = userDao.updateProfile(userData);
			
			if (flag){
				logger.debug("User record is updated successfully in the database.");
				
				generateSuccessResponse(profileResponse);
			}
			else{
				logger.debug("User record is failed to update in the database.");
				throw new BusinessException("User record is failed to update in the database.");
			}
		}
		else{
			logger.debug("User Profile - Update Profile validations failed.");
			throw new BusinessException("User Profile - Update Profile validations failed.");
		}
	}
	
	public void forgotPasswordVerification(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IUserDAO userDao = daoFactory.getUserDAO();
		
		boolean flag = userDao.forgotPasswordVerification(profileRequest.getUsername(), profileRequest.getDateOfBirth());
		
		if (flag){
			logger.debug("User is authorized to change the password.");
			
			generateSuccessResponse(profileResponse);
		}
		else{
			logger.debug("User is not authorized to change the password as the password verification is failed.");
			throw new BusinessException("User is not authorized to change the password as the password verification is failed.");
		}
		
	}
	
	public void changePassword(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ProfileValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)profileRequest, requestType);
		
		if (isValid){
			IUserDAO userDao = daoFactory.getUserDAO();
			
			boolean flag = userDao.changePassword(profileRequest.getUsername(), profileRequest.getPassword());
			
			if (flag){
				logger.debug("User password is updated successfully.");
				
				generateSuccessResponse(profileResponse);
			}
			else{
				logger.debug("User password is failed to update successfully.");
				throw new BusinessException("User password is failed to update successfully.");
			}
		}
		else{
			logger.debug("User Profile - Change Password validations failed.");
			throw new BusinessException("User Profile - Change Password validations failed.");
		}
		
	}
	
	public void isUserNameAvailable(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IUserDAO userDao = daoFactory.getUserDAO();
		
		boolean flag = userDao.isUserNameAvailable(profileRequest.getUsername());
		
		if (flag){
			logger.debug("User name is avaiable.");
			
			generateSuccessResponse(profileResponse);
		}
		else{
			logger.debug("User name is not available.");
			
			generateFalseResponse(profileResponse);
		}
		
	}
	
	public void uploadUserPicture(WSUserProfileRequest profileRequest, WSUserProfileResponse profileResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ProfileValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)profileRequest, requestType);
		
		if (isValid){
			
			/* Upload the picture */
			
			FileUploader fileUploader = new FileUploader();
			String uploadFilePath = fileUploader.generateUserPictureUploadFilePath(profileRequest.getUsername(), profileRequest.getPictureExt());
			String downloadFilePath = fileUploader.generateUserPictureDownloadFilePath(profileRequest.getUsername(), profileRequest.getPictureExt());
			fileUploader.uploadFile(profileRequest.getPictureStream(), uploadFilePath);
			
			profileRequest.setUserPicturePath(downloadFilePath);
			
			logger.debug("User Picture is uploaded successfully.");
			
			/* Update user's picture path in database */
			
			IUserDAO userDao = daoFactory.getUserDAO();
			
			boolean flag = userDao.updateUserPicturePath(profileRequest.getUserPicturePath(), profileRequest.getUsername());
			
			if (flag){
				logger.debug("User picture path is updated successfully in the database for username : " + profileRequest.getUsername());
				
				generateSuccessResponse(profileResponse);
			}
			else{
				logger.error("User picture path is failed to update successfully in the database for username : " + profileRequest.getUsername());
			}
		}
			else{
				logger.debug("User Profile - Upload Picture validations failed.");
				throw new BusinessException("User Profile - Upload Picture validations failed.");
			}
		
	}
	
	public UserData populateProfileDAOData(WSUserProfileRequest profileRequest){
		UserData userData = new UserData();
		
		userData.setUsername(profileRequest.getUsername());
		userData.setPassword(profileRequest.getPassword());
		userData.setFullName(profileRequest.getFullName());
		userData.setEmail(profileRequest.getEmail());
		userData.setZipcode(profileRequest.getZipcode());
		userData.setGender(profileRequest.getGender());
		userData.setDateOfBirth(profileRequest.getDateOfBirth());
		userData.setPhone(profileRequest.getPhone());
		userData.setUserPicturePath(profileRequest.getUserPicturePath());		
		
		return userData;
		
	}
	
	public void populateProfileResponseFromDAOData(UserData userData, WSUserProfileResponse profileResponse){
		
		profileResponse.setFullName(userData.getFullName());
		profileResponse.setEmail(userData.getEmail());
		profileResponse.setZipcode(userData.getZipcode());
		profileResponse.setGender(userData.getGender());
		profileResponse.setDateOfBirth(userData.getDateOfBirth());
		profileResponse.setPhone(userData.getPhone());
		profileResponse.setUserPicturePath(userData.getUserPicturePath());
		
	}
	
	public void generateSuccessResponse(WSUserProfileResponse profileResponse){
		
		profileResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}
	
	public void generateFalseResponse(WSUserProfileResponse profileResponse){
		
		profileResponse.setResponse(BusinessConstants.RESPONSE_FALSE);
		
	}

}
