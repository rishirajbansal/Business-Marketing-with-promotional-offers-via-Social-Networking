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
import com.fynger.servicesController.services.domainObjects.requests.WSPlaceGroupRequest;

/**
 * @author Rishi
 *
 */
public class PlaceGroupValidator extends AbstractBusinessValidator {


	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSPlaceGroupRequest wsPlaceGroupRequest = (WSPlaceGroupRequest)request;	
		
		switch(requestType){
		
			case PLACE_LOAD_DETAILS: 
				isValid = validateloadDetails(wsPlaceGroupRequest);
				break;
				
			case PLACE_VIEW_POST_RESPONSES: 
				isValid = validateViewPostResponses(wsPlaceGroupRequest);
				break;
				
			case PLACE_CREATE_POST: 
				isValid = validateCreatePost(wsPlaceGroupRequest);
				break;
				
			case PLACE_CREATE_POST_UPLOAD: 
				isValid = validateCreatePostAndUpload(wsPlaceGroupRequest);
				break;
				
			case PLACE_CREATE_POLL: 
				isValid = validateCreatePoll(wsPlaceGroupRequest);
				break;
				
			case PLACE_REPLY_POST:
				isValid = validateReplyPost(wsPlaceGroupRequest);
				break;
				
			case PLACE_REPLY_POLL: 
				isValid = validateReplyPoll(wsPlaceGroupRequest);
				break;
				
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
	
		return isValid;

	}
	
	public boolean validateloadDetails(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Latitude */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsPlaceGroupRequest.getLatitude()) == 0){
			logger.debug("Latitude is empty or (0,0) in 'Place Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_LATITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Longitude */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsPlaceGroupRequest.getLongitude()) == 0){
			logger.debug("Longitude is empty in 'Place Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_LONGITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Place Name */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceName()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Place Name is empty or (0,0) in 'Place Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_NAME, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_NAME, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Place Map Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceMapId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Place Map Id is empty in 'Place Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_MAP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_MAP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateViewPostResponses(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPostId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Id is empty in 'Place Group - View Post Responses Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
				
		return isValid;
	}
	
	public boolean validateCreatePost(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Text */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPostText()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Text is empty in 'Place Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTTEXT, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTTEXT, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Place Group Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPlaceGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Place Group Id is empty in 'Place Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateCreatePostAndUpload(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Text */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPostText()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Text is empty in 'Place Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTTEXT, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTTEXT, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Place Group Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPlaceGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Place Group Id is empty in 'Place Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateCreatePoll(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Poll Question */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPoll().getPollQuestion()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Poll Question is empty in 'Place Group - Create Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POLL_QUESTION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POLL_QUESTION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Place Group Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPoll().getPlaceGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Place Group Id is empty in 'Place Group - Create Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_PLACE_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateReplyPost(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPostId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Id is empty in 'Place Group - Reply Post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POSTID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Post Reply Text */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPost().getPostReply()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Reply Text is empty in 'Place Group - Reply Post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POST_REPLY, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POST_REPLY, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateReplyPoll(WSPlaceGroupRequest wsPlaceGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsPlaceGroupRequest.getUsername());
		
		/* Mandatory validation check - Poll Id */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPoll().getPollId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Poll Id is empty in 'Place Group - Reply Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POLLID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_POLLID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Poll Selected Option */
		if (GenericUtility.safeTrim(wsPlaceGroupRequest.getPlaceGroupPoll().getSelectedPollOption()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Selected Option is empty in 'Place Group - Reply Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_SELECTED_POLLOPTION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.PLACE_GROUP_FIELD_SELECTED_POLLOPTION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}

}
