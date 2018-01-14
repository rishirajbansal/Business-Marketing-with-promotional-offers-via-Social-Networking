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
import com.fynger.servicesController.services.domainObjects.requests.WSShoutGroupRequest;

/**
 * @author Rishi
 *
 */
public class ShoutGroupValidator extends AbstractBusinessValidator {

	@Override
	public boolean validate(WSBaseRequest request, ERequestType requestType) throws BusinessValidationException {
		
		boolean isValid = true;
		
		WSShoutGroupRequest wsShoutGroupRequest = (WSShoutGroupRequest)request;	
		
		switch(requestType){
		
			case SHOUT_LOAD_DETAILS: 
				isValid = validateloadDetails(wsShoutGroupRequest);
				break;
				
			case SHOUT_VIEW_POST_RESPONSES: 
				isValid = validateViewPostResponses(wsShoutGroupRequest);
				break;
				
			case SHOUT_CREATE_POST: 
				isValid = validateCreatePost(wsShoutGroupRequest);
				break;
				
			case SHOUT_CREATE_POST_UPLOAD: 
				isValid = validateCreatePostAndUpload(wsShoutGroupRequest);
				break;
				
			case SHOUT_CREATE_POLL: 
				isValid = validateCreatePoll(wsShoutGroupRequest);
				break;
				
			case SHOUT_REPLY_POST:
				isValid = validateReplyPost(wsShoutGroupRequest);
				break;
				
			case SHOUT_REPLY_POLL: 
				isValid = validateReplyPoll(wsShoutGroupRequest);
				break;
				
			default: 
				throw new BusinessException("Unsupported request type.");
		
		}
		
		return isValid;

	}
	
	public boolean validateloadDetails(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Latitude */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsShoutGroupRequest.getLatitude()) == 0){
			logger.debug("Latitude is empty in 'Shout Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_LATITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Longitude */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ||
				Double.parseDouble(wsShoutGroupRequest.getLatitude()) == 0){
			logger.debug("Longitude is empty in 'Shout Group - Load Details Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_LONGITUDE, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateViewPostResponses(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getPostId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Id is empty in 'Shout Group - View Post Responses Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
				
		return isValid;
	}
	
	public boolean validateCreatePost(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Text */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getPostText()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Text is empty in 'Shout Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTTEXT, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTTEXT, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Shout Group Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getShoutGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Shout Group Id is empty in 'Shout Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateCreatePostAndUpload(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Text */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getPostText()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Text is empty in 'Shout Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTTEXT, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTTEXT, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Shout Group Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getShoutGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Shout Group Id is empty in 'Shout Group - Create post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateCreatePoll(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Poll Question */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPoll().getPollQuestion()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Poll Question is empty in 'Shout Group - Create Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POLL_QUESTION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POLL_QUESTION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Shout Group Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPoll().getShoutGroupId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Shout Group Id is empty in 'Shout Group - Create Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SHOUT_GROUP_ID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateReplyPost(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Post Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getPostId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Id is empty in 'Shout Group - Reply Post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POSTID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Post Reply Text */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPost().getPostReply()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Reply Text is empty in 'Shout Group - Reply Post Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POST_REPLY, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POST_REPLY, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
	
	public boolean validateReplyPoll(WSShoutGroupRequest wsShoutGroupRequest){
		
		boolean isValid = true;
		
		/* Mandatory validation check - user name */
		validateUserName(wsShoutGroupRequest.getUsername());
		
		/* Mandatory validation check - Poll Id */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPoll().getPollId()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Poll Id is empty in 'Shout Group - Reply Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POLLID, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_POLLID, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		/* Mandatory validation check - Poll Selected Option */
		if (GenericUtility.safeTrim(wsShoutGroupRequest.getShoutGroupPoll().getSelectedPollOption()).equals(GenericConstants.EMPTY_STRING)){
			logger.debug("Post Selected Option is empty in 'Shout Group - Reply Poll Request'.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_MANDATORY_FIELD_VALIDATION, ExceptionConstants.USERMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SELECTED_POLLOPTION, ExceptionConstants.ERRORMESSAGE_MANDATORY_FIELD_VALIDATION + BusinessConstants.SHOUT_GROUP_FIELD_SELECTED_POLLOPTION, EExceptionTypes.BUSINESS_VALIDATION_EXCEPTION);
		}
		
		return isValid;
	}
		

}
