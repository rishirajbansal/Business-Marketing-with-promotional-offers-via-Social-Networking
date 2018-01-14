/**
 * 
 */
package com.fynger.servicesBusiness.validations;

import java.util.regex.Pattern;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLoginRequest;

/**
 * @author Rishi
 *
 */
public class LoginValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(LoginValidator.class.getName());

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSUserLoginRequest loginRequest = (WSUserLoginRequest)request;
		
		switch(requestType){
		
			case USER_LOGIN: 
				isValid = validateLogin(loginRequest);
				break;
				
			case USER_LOGIN_FACEBOOK: 
				isValid = validateLoginFacebook(loginRequest);
				break;
				
			case USER_LOGIN_FACEBOOK_CREATE: 
				isValid = validateCreateLoginFacebook(loginRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateLogin(WSUserLoginRequest wsUserLoginRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsUserLoginRequest.getUsername());
		
		/* Mandatory validation check - Location Coordinates */
		/*if (GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Location Coordinates are empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_LOCATIONCOORDINATES, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_LOCATIONCOORDINATES, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}*/
		
		/* Syntax validation check - Location Coordinates */
		if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
			if (!Pattern.matches(BusinessConstants.LOCATION_COORDINATES_SYNTAX_REGEX, wsUserLoginRequest.getLocationCoordinates())){
				logger.debug("Syntax validation for location coordinates failed. Valid format is : latitude|longitude");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_LOCATION_COORDINATES, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		/*if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
			String coor = wsUserLoginRequest.getLocationCoordinates();
			String[] split = coor.split("\\|");
			
			double latitude = Double.parseDouble(split[0]);
			double longitude = Double.parseDouble(split[1]);
			
			if (latitude == 0 || longitude == 0){
				logger.debug("latitude/longitude coordinates are (0,0) and are blank coordinates");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_LOCATION_COORDINATES, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION_LAT_LONG, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}*/
		
		return isValid;
	}
	
	public boolean validateLoginFacebook(WSUserLoginRequest wsUserLoginRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - FB Email */
		if (GenericUtility.safeTrim(wsUserLoginRequest.getFBEmail()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("FB Email is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FBEMAIL, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FBEMAIL, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateCreateLoginFacebook(WSUserLoginRequest wsUserLoginRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - FB Email */
		if (GenericUtility.safeTrim(wsUserLoginRequest.getFBEmail()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("FB Email is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FBEMAIL, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FBEMAIL, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Location Coordinates 
		if (GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Location Coordinates are empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_LOCATIONCOORDINATES, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_LOCATIONCOORDINATES, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Syntax validation check - Location Coordinates */
		if (!GenericUtility.safeTrim(wsUserLoginRequest.getLocationCoordinates()).equals(GenericConstants.EMPTY_STRING)){
			if (!Pattern.matches(BusinessConstants.LOCATION_COORDINATES_SYNTAX_REGEX, wsUserLoginRequest.getLocationCoordinates())){
				logger.debug("Syntax validation for location coordinates failed. Valid format is : latitude|longitude");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_LOCATION_COORDINATES, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		/* Mandatory validation check - Fynger User Flag */
		if (GenericUtility.safeTrim(wsUserLoginRequest.getFyngerUser()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Fynger User Flag is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		else{
			if (GenericUtility.safeTrim(wsUserLoginRequest.getFyngerUser()).equals(GenericConstants.STRING_TRUE)){
				
				/* Mandatory validation check - Fynger user name */
				if (GenericUtility.safeTrim(wsUserLoginRequest.getUsername()).equals(GenericConstants.EMPTY_STRING)){
					logger.debug("Fynger User Name is empty.");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER_NAME, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER_NAME, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
				}
				
				/* Mandatory validation check - Fynger user Password */
				if (GenericUtility.safeTrim(wsUserLoginRequest.getPassword()).equals(GenericConstants.EMPTY_STRING)){
					logger.debug("Fynger User Password is empty.");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER_PASSWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_LOGIN_FIELD_FYNGERUSER_PASSWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
				}
				
			}
		}
		
		return isValid;
		
	}

}
