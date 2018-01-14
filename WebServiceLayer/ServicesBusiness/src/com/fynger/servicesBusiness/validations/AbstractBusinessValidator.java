/**
 * 
 */
package com.fynger.servicesBusiness.validations;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;

/**
 * @author Rishi
 *
 */
public abstract class AbstractBusinessValidator implements IBusinessValidator{
	
	public static LoggerManager logger = GenericUtility.getLogger(AbstractBusinessValidator.class.getName());
	
	public abstract boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException;
	
	public void validateUserName(String username){
		
		/* Mandatory validation check - user name */
		if (GenericUtility.safeTrim(username).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("User Name is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_PROFILE_FIELD_USERNAME, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.USER_PROFILE_FIELD_USERNAME, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
	}
	
	public void validateSearchText(String searchText){
		
		/* Mandatory validation check - user name */
		if (GenericUtility.safeTrim(searchText).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Search Text is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SEARCH_FIELD_SEARCHTEXT, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SEARCH_FIELD_SEARCHTEXT, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
	}
	
}	