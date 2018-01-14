/**
 * 
 */
package com.fynger.servicesController.services.resourceHandlers;

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
import com.fynger.servicesController.services.domainObjects.requests.WSUserLogoutRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.utilities.ServiceUtility;

/**
 * @author Rishi
 *
 */
@Path("/logout")
public class UserLogoutService {
	
	public static LoggerManager logger = GenericUtility.getLogger(UserLogoutService.class.getName());
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse logout(WSUserLogoutRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_LOGOUT);
				response = action.execute(request, ERequestType.USER_LOGOUT);
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
			logger.debug("|~| User Logout Service |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| User Logout Service |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| User Logout Service |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| User Logout Service |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| User Logout Service |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	

}
