/**
 * 
 */
package com.fynger.servicesController.services.resourceHandlers;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.ActionDirectory;
import com.fynger.servicesBusiness.business.base.ActionFactory;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.base.IBusinessAction;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesController.constants.ExceptionConstants;
import com.fynger.servicesController.exception.BadRequestException;
import com.fynger.servicesController.services.domainObjects.requests.WSShoutGroupRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPost;
import com.fynger.servicesController.utilities.ServiceUtility;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author Rishi
 *
 */
@Path("/shout")
public class ShoutGroupService {
	
	public static LoggerManager logger = GenericUtility.getLogger(ShoutGroupService.class.getName());
	
	
	@Path("loadDetails")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse loadDetails(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_LOAD_DETAILS);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Load Details Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Load Details Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Load Details Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Load Details Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Load Details Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	
	@Path("viewPostResponses")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse viewPostResponses(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_VIEW_POST_RESPONSES);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - View Post Responses Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - View Post Responses Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - View Post Responses Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - View Post Responses Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - View Post Responses Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("createPost")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse createPost(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_CREATE_POST);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Create Post Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Create Post Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Post Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Post Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Post Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("uploadPost")
	@POST
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse uploadPost(@FormDataParam("file") InputStream pictureStream,
										@FormDataParam("file") FormDataContentDisposition pictureDetail,
										@FormDataParam("username") String username,
										@FormDataParam("shoutGroupId") String shoutGroupId,
										@FormDataParam("postText") String postText,
										@FormDataParam("latitude") String latitude,
										@FormDataParam("longitude") String longitude){
		
		WSBaseResponse response = null;
		
		WSShoutGroupRequest wsShoutGroupRequest = new WSShoutGroupRequest();
		
		try{
			String fileExt = pictureDetail.getFileName().substring(pictureDetail.getFileName().lastIndexOf(".") + 1, pictureDetail.getFileName().length());
			
			WSShoutGroupPost wsShoutGroupPost = new WSShoutGroupPost();
			
			wsShoutGroupPost.setPictureExt(fileExt);
			wsShoutGroupPost.setPictureStream(pictureStream);
			wsShoutGroupPost.setPictureFileName(pictureDetail.getFileName());
			wsShoutGroupPost.setShoutGroupId(shoutGroupId);
			wsShoutGroupPost.setPostText(postText);
			
			wsShoutGroupRequest.setUsername(username);
			wsShoutGroupRequest.setLatitude(latitude);
			wsShoutGroupRequest.setLongitude(longitude);
			wsShoutGroupRequest.setShoutGroupPost(wsShoutGroupPost);
			
			IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
			response = action.execute(wsShoutGroupRequest, ERequestType.SHOUT_CREATE_POST_UPLOAD);
			
			if (null != response) {
				response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
			}
			else{
				throw new ApplicationException("Response returned by Business layer found null.");
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Upload Post Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Upload Post Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Upload Post Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Upload Post Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Upload Post Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("createPoll")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse createPoll(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_CREATE_POLL);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Create Poll Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Create Poll Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Poll Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Poll Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Create Poll Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("replyPost")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse replyPost(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_REPLY_POST);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Post Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Post Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Post Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Post Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Post Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("replyPoll")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse replyPoll(WSShoutGroupRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_SHOUTGROUP);
				response = action.execute(request, ERequestType.SHOUT_REPLY_POLL);
				
				if (null != response) {
					response.setStatus(ExceptionConstants.SERVICE_STATUS_SUCCESS);
				}
				else{
					throw new ApplicationException("Response returned by Business layer found null.");
				}
			}
			else{
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BAD_REQUEST_EXCEPTION, ExceptionConstants.USERMESSAGE_BAD_REQUEST_1, ExceptionConstants.ERRORMESSAGE_BAD_REQUEST_1, EExceptionTypes.BAD_REQUEST_EXCEPTION);
			}
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Poll Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Poll Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Poll Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Poll Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Shout Group - Reply Poll Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}

}
