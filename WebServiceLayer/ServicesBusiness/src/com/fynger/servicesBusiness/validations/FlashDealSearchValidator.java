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
import com.fynger.servicesController.services.domainObjects.requests.WSSearchFlashDealRequest;

/**
 * @author Rishi
 *
 */
public class FlashDealSearchValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(FlashDealSearchValidator.class.getName());

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
				
		WSSearchFlashDealRequest wsSearchFlashDealRequest = (WSSearchFlashDealRequest)request;	
		
		switch(requestType){
		
			case SEARCH_FLASHDEAL_REQUEST: 
				isValid = validateFlashDealRequest(wsSearchFlashDealRequest);
				break;
				
			case SEARCH_FLASHDEAL_RESULTS_LIST: 
				isValid = validateFlashDealSearchResutls(wsSearchFlashDealRequest);
				break;
				
			case SEARCH_FLASHDEAL_DETAIL: 
				isValid = validateFlashDealDetail(wsSearchFlashDealRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
	
		return isValid;

	}
	
	public boolean validateFlashDealRequest(WSSearchFlashDealRequest wsSearchFlashDealRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFlashDealRequest.getUsername());
		
		/* Mandatory validation check - Search Text */
		validateSearchText(wsSearchFlashDealRequest.getSearchText());
		
		/* Mandatory validation check - Location string or Latitude/Longitude */
		if (GenericUtility.safeTrim(wsSearchFlashDealRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING) &&
				(GenericUtility.safeTrim(wsSearchFlashDealRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
						GenericUtility.safeTrim(wsSearchFlashDealRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING))){
			logger.debug("Either location or latitude/longitude is empty");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_LOCATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		if (GenericUtility.safeTrim(wsSearchFlashDealRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING)){
			if (!GenericUtility.safeTrim(wsSearchFlashDealRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
				!GenericUtility.safeTrim(wsSearchFlashDealRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING)){
				
				double latitude = Double.parseDouble(wsSearchFlashDealRequest.getLatitude());
				double longitude = Double.parseDouble(wsSearchFlashDealRequest.getLongitude());
				
				if (latitude == 0 || longitude == 0){
					logger.debug("latitude/longitude coordinates are (0,0) and are blank coordinates");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION_LAT_LONG, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
				}
			}
		}
		
		/* Mandatory validation check - Expiry Period */
		if (GenericUtility.safeTrim(wsSearchFlashDealRequest.getExpiryPeriod()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Expiry Date is empty");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FLASHDEAL_SEARCH_FIELD_EXPIRYPERIOD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FLASHDEAL_SEARCH_FIELD_EXPIRYPERIOD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateFlashDealSearchResutls(WSSearchFlashDealRequest wsSearchFlashDealRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFlashDealRequest.getUsername());
		
		return isValid;
	}
	
	public boolean validateFlashDealDetail(WSSearchFlashDealRequest wsSearchFlashDealRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - Flash Deal Id */
		if (GenericUtility.safeTrim(wsSearchFlashDealRequest.getFlashDealId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Flash Deal Id is empty");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FLASHDEAL_SEARCH_FIELD_FLASHDEALID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FLASHDEAL_SEARCH_FIELD_FLASHDEALID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}

}
