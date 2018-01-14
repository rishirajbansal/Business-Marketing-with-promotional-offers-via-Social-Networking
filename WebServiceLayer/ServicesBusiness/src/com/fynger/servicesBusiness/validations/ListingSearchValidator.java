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
import com.fynger.servicesController.services.domainObjects.requests.WSSearchListingRequest;

/**
 * @author Rishi
 *
 */
public class ListingSearchValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(ListingSearchValidator.class.getName());

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSSearchListingRequest listingSearchRequest = (WSSearchListingRequest)request;	
		
		switch(requestType){
		
			case SEARCH_LISTING_RESULTS_LIST: 
				isValid = validateSearchInputs(listingSearchRequest);
				break;
				
			case SEARCH_LISTING_ADD_TO_FAVOURITE: 
				isValid = validateAddFavourites(listingSearchRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateSearchInputs(WSSearchListingRequest listingSearchRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(listingSearchRequest.getUsername());
		
		if (GenericUtility.safeTrim(listingSearchRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING) &&
				(GenericUtility.safeTrim(listingSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
						GenericUtility.safeTrim(listingSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING))){
			logger.debug("Either location or latitude/longitude is empty");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.LISTING_SEARCH_FIELD_LOCATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		if (GenericUtility.safeTrim(listingSearchRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING)){
			if (!GenericUtility.safeTrim(listingSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
				!GenericUtility.safeTrim(listingSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING)){
				
				double latitude = Double.parseDouble(listingSearchRequest.getLatitude());
				double longitude = Double.parseDouble(listingSearchRequest.getLongitude());
				
				if (latitude == 0 || longitude == 0){
					logger.debug("latitude/longitude coordinates are (0,0) and are blank coordinates");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION_LAT_LONG, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
				}
			}
		}
		
		return isValid;
	}
	
	public boolean validateAddFavourites(WSSearchListingRequest listingSearchRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(listingSearchRequest.getUsername());
		
		/* Mandatory validation check - Listing Name (Keyword) */
		if (GenericUtility.safeTrim(listingSearchRequest.getKeyword()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Keyword is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.LISTING_SEARCH_FIELD_KEYWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.LISTING_SEARCH_FIELD_KEYWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
		
	}

}
