/**
 * 
 */
package com.fynger.servicesBusiness.validations;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLogoutRequest;

/**
 * @author Rishi
 *
 */
public class LogoutValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(LogoutValidator.class.getName());


	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSUserLogoutRequest wsUserLogoutRequest = (WSUserLogoutRequest)request;
		
		switch(requestType){
		
			case USER_LOGOUT:
				isValid = validateLogout(wsUserLogoutRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateLogout(WSUserLogoutRequest wsUserLogoutRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsUserLogoutRequest.getUsername());
		
		return isValid;
	}

}
