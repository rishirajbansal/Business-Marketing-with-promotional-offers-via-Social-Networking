/**
 * 
 */
package com.fynger.servicesBusiness.validations;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchFavouritesRequest;

/**
 * @author Rishi
 *
 */
public class FavouritesSearchValidator extends AbstractBusinessValidator {

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSSearchFavouritesRequest wsSearchFavouritesRequest = (WSSearchFavouritesRequest)request;	
		
		switch(requestType){
		
			case SEARCH_FAVOURITES_ADD: 
				isValid = validateAddFavourites(wsSearchFavouritesRequest);
				break;
				
			case SEARCH_FAVOURITES_LIST: 
				isValid = validateListFavourites(wsSearchFavouritesRequest);
				break;
				
			case SEARCH_FAVOURITES_DELETE: 
				isValid = validateDeleteFavourites(wsSearchFavouritesRequest);
				break;
				
			case SEARCH_FAVOURITES_RESULTS_LIST: 
				isValid = validateSearchResultsListInputs(wsSearchFavouritesRequest);
				break;
				
			case SEARCH_FAVOURITES_DETAIL: 
				isValid = validateFavouriteDetail(wsSearchFavouritesRequest);
				break;
		
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}

		return isValid;
		
	}
	
	public boolean validateAddFavourites(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFavouritesRequest.getUsername());
		
		/* Mandatory validation check - Group Type */
		if (GenericUtility.safeTrim(wsSearchFavouritesRequest.getGroupType()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Group Type is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_GROUPTYPE, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_GROUPTYPE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Keyword */
		if (GenericUtility.safeTrim(wsSearchFavouritesRequest.getKeyword()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Keyword is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_KEYWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_KEYWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
		
	}
	
	public boolean validateListFavourites(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFavouritesRequest.getUsername());
		
		return isValid;
		
	}
	
	public boolean validateDeleteFavourites(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFavouritesRequest.getUsername());
		
		/* Mandatory validation check - Group Type */
		if (GenericUtility.safeTrim(wsSearchFavouritesRequest.getGroupType()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Group Type is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_GROUPTYPE, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_GROUPTYPE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Keyword */
		if (GenericUtility.safeTrim(wsSearchFavouritesRequest.getKeyword()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Keyword is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_KEYWORD, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_KEYWORD, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
		
	}
	
	public boolean validateSearchResultsListInputs(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFavouritesRequest.getUsername());
		
		return isValid;
		
	}
	
	public boolean validateFavouriteDetail(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsSearchFavouritesRequest.getUsername());
		
		/* Mandatory validation check - Promotion Id */
		if (GenericUtility.safeTrim(wsSearchFavouritesRequest.getPromotionId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Promotion Id is empty.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_PROMOTIONID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.FAVOURITES_SEARCH_FIELD_PROMOTIONID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
		
	}

}
