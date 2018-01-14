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
import com.fynger.servicesBusiness.utilities.DateValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserProfileRequest;

/**
 * @author Rishi
 *
 */
public class ProfileValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(ProfileValidator.class.getName());


	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSUserProfileRequest profileRequest = (WSUserProfileRequest)request;	
		
		switch(requestType){
		
			case USER_PROFILE_REGISTRATION: 
				isValid = validateProfile(profileRequest);
				break;
				
			case USER_PROFILE_UPDATE: 
				isValid = validateProfile(profileRequest);
				break;
				
			case USER_PROFILE_FORGOT_PASSWORD_UPDATE: 
				isValid = validatePassword(profileRequest);
				break;
				
			case USER_PROFILE_FILE_UPLOAD: 
				isValid = validateUpload(profileRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateProfile(WSUserProfileRequest profileRequest){
		
		boolean isValid = true;
		
		/* Mandatory fields validation check */
		if (!mandatoryFieldValidation(profileRequest)){
			return false;
		}
		
		/* Length validation check - User name */
		if (profileRequest.getUsername().length() > BusinessConstants.USER_PROFILE_FIELD_USERNAME_LENGTH_VALIDATION){
			logger.debug("Invalid length for field Username");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LENGTH_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LENGTH_FIELD_VALIDATION_USERNAME, ExceptionConstants.ERRORMESSAGE_LENGTH_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Length validation check - Password */
		if (profileRequest.getPassword().length() > BusinessConstants.USER_PROFILE_FIELD_PASSWORD_LENGTH_VALIDATION){
			logger.debug("Invalid length for field password");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LENGTH_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LENGTH_FIELD_VALIDATION_PASSWORD, ExceptionConstants.ERRORMESSAGE_LENGTH_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
					
		/* Length validation check - Phone */
		if (!GenericUtility.safeTrim(profileRequest.getPhone()).equals(GenericConstants.EMPTY_STRING)){
			if (profileRequest.getPhone().length() > BusinessConstants.USER_PROFILE_FIELD_PHONE_LENGTH_VALIDATION){
				logger.debug("Invalid length for field Phone");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LENGTH_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LENGTH_FIELD_VALIDATION_PHONE, ExceptionConstants.ERRORMESSAGE_LENGTH_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		/* Syntax validation check - User name */
		if (!Pattern.matches(BusinessConstants.ALPHANUMERIC_SYNTAX_REGEX, profileRequest.getUsername())){
			logger.debug("Syntax validation for user name failed. Not alphanumeric.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_USERNAME, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Syntax validation check - Phone */
		if (!GenericUtility.safeTrim(profileRequest.getPhone()).equals(GenericConstants.EMPTY_STRING)){
			if (profileRequest.getPhone().startsWith("0")){
				logger.debug("Syntax validation for phone failed. Phone no. start with '0'.");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_PHONE, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		/* Syntax validation check - Email */
		if (!GenericUtility.safeTrim(profileRequest.getEmail()).equals(GenericConstants.EMPTY_STRING)){
			if (!Pattern.matches(BusinessConstants.EMAIL_SYNTAX_REGEX, profileRequest.getEmail())){
				logger.debug("Syntax validation for email failed.");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_EMAIL, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		/* Syntax validation check - Date of Birth */
		if (!GenericUtility.safeTrim(profileRequest.getDateOfBirth()).equals(GenericConstants.EMPTY_STRING)){
			if (!DateValidator.validate(profileRequest.getDateOfBirth(), DateValidator.FORMAT_MM_DD_YYYY)){
				logger.debug("Syntax validation for date of birth failed.");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SYNTAX_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_SYNTAX_FIELD_VALIDATION_DOB, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
			}
		}
		
		return isValid;
	}
	
	public boolean validatePassword(WSUserProfileRequest profileRequest){
		
		boolean isValid = true;
		
		if (GenericUtility.safeTrim(profileRequest.getPassword()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Password is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_PROFILE_FIELD_PASSWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_PROFILE_FIELD_PASSWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Length validation check - Password */
		if (profileRequest.getPassword().length() > BusinessConstants.USER_PROFILE_FIELD_PASSWORD_LENGTH_VALIDATION){
			logger.debug("Invalid length for field password");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LENGTH_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LENGTH_FIELD_VALIDATION_PASSWORD, ExceptionConstants.ERRORMESSAGE_LENGTH_FIELD_VALIDATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean mandatoryFieldValidation(WSUserProfileRequest profileRequest){
		
		boolean mandatoryCheck = true;
		
		String username = profileRequest.getUsername();
		String password = profileRequest.getPassword();
		String dateOfBirth = profileRequest.getDateOfBirth();
		String email = profileRequest.getEmail();
		
		String[] mandatoryFields = new String[]{username, password, dateOfBirth, email};
		
		String[] mandatoryFieldNames = new String[]{BusinessConstants.USER_PROFILE_FIELD_USERNAME, 
												BusinessConstants.USER_PROFILE_FIELD_PASSWORD,
												BusinessConstants.USER_PROFILE_FIELD_DATEOFBIRTH,
												BusinessConstants.USER_PROFILE_FIELD_EMAIL};
		
		StringBuffer emptyFields = new StringBuffer();
		
		for (int i = 0; i< mandatoryFields.length; i++){
			String fieldValue = mandatoryFields[i];
			
			if (GenericUtility.safeTrim(fieldValue).equals(GenericConstants.EMPTY_STRING)){
				emptyFields.append(mandatoryFieldNames[i] + ", ");
				mandatoryCheck = false;
			}
		}
		
		//Code to remove last comma (,) from the emptyFields string
		if (emptyFields.lastIndexOf(", ") > 0) {
			emptyFields = new StringBuffer((emptyFields.substring(0, emptyFields.lastIndexOf(", "))));
		}
		
		if (!mandatoryCheck){
			logger.debug("User Profile registration Service : Mandatory validations failed for : " + username + " >> Empty Fields: " + emptyFields);
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + emptyFields, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + emptyFields, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		else{
			logger.debug("Mandatory fields validation passed for : " + username);
		}
		
		return mandatoryCheck;
	}
	
	public boolean validateUpload(WSUserProfileRequest profileRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(profileRequest.getUsername());
		
		return isValid;
	}
	
}
