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
import com.fynger.servicesController.services.domainObjects.requests.WSSearchPromotionRequest;

/**
 * @author Rishi
 *
 */
public class PromotionsSearchValidator extends AbstractBusinessValidator {
	
	public static LoggerManager logger = GenericUtility.getLogger(PromotionsSearchValidator.class.getName());

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSSearchPromotionRequest promotionSearchRequest = (WSSearchPromotionRequest)request;	
		
		switch(requestType){
		
			case SEARCH_PROMOTION_RESULTS_LIST: 
				isValid = validateSearchInputs(promotionSearchRequest);
				break;
				
			case SEARCH_PROMOTION_DETAIL: 
				isValid = validatePromotionDetail(promotionSearchRequest);
				break;
				
			case SEARCH_PROMOTION_ADD_TO_FAVOURITE: 
				isValid = validateAddFavourites(promotionSearchRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;
	}
	
	public boolean validateSearchInputs(WSSearchPromotionRequest promotionSearchRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(promotionSearchRequest.getUsername());
		
		if (GenericUtility.safeTrim(promotionSearchRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING) &&
				(GenericUtility.safeTrim(promotionSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
						GenericUtility.safeTrim(promotionSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING))){
			logger.debug("Either location or latitude/longitude is empty");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_LOCATION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		if (GenericUtility.safeTrim(promotionSearchRequest.getLocationString()).equals(GenericConstants.EMPTY_STRING)){
			if (!GenericUtility.safeTrim(promotionSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) || 
				!GenericUtility.safeTrim(promotionSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING)){
				
				double latitude = Double.parseDouble(promotionSearchRequest.getLatitude());
				double longitude = Double.parseDouble(promotionSearchRequest.getLongitude());
				
				if (latitude == 0 || longitude == 0){
					logger.debug("latitude/longitude coordinates are (0,0) and are blank coordinates");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_SYNTAX_FIELD_VALIDATION_LAT_LONG, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
				}
			}
		}
		
		return isValid;
	}
	
	public boolean validatePromotionDetail(WSSearchPromotionRequest promotionSearchRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(promotionSearchRequest.getUsername());
		
		/* Mandatory validation check - Entity Id */
		if (GenericUtility.safeTrim(promotionSearchRequest.getEntityId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Entity id is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_ENTITYID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_ENTITYID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Entity Type */
		if (GenericUtility.safeTrim(promotionSearchRequest.getEntityType()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Entity Type is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_ENTITYTYPE, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_ENTITYTYPE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateAddFavourites(WSSearchPromotionRequest promotionSearchRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(promotionSearchRequest.getUsername());
		
		/* Mandatory validation check - Listing Name (Keyword) */
		if (GenericUtility.safeTrim(promotionSearchRequest.getKeyword()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Keyword is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_KEYWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PROMOTION_SEARCH_FIELD_KEYWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
		
	}

}
