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
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLocationRequest;

/**
 * @author Rishi
 *
 */
public class RefreshLocationValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(RefreshLocationValidator.class.getName());


	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSUserLocationRequest wsUserLocationRequest = (WSUserLocationRequest)request;
		
		switch(requestType){
		
			case USER_REFRESH_LOCATION:
				isValid = validateRefreshLocation(wsUserLocationRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateRefreshLocation(WSUserLocationRequest wsUserLocationRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsUserLocationRequest.getUsername());
		
		/* Mandatory validation check - Latitude */
		if (GenericUtility.safeTrim(wsUserLocationRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsUserLocationRequest.getLatitude()) == 0){
			logger.debug("Latitude is empty in Refresh Location Service.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.REFRESH_LOCATION_FIELD_LATITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Longitude */
		if (GenericUtility.safeTrim(wsUserLocationRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsUserLocationRequest.getLongitude()) == 0){
			logger.debug("Longitude is empty or (0,0) in Refresh Location Service.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.REFRESH_LOCATION_FIELD_LONGITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}

}
