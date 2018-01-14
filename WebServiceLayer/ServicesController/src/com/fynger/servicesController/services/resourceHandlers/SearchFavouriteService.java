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
import com.fynger.servicesController.services.domainObjects.requests.WSSearchFavouritesRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.utilities.ServiceUtility;

/**
 * @author Rishi
 *
 */
@Path("/favourite")
public class SearchFavouriteService {
	
	public static LoggerManager logger = GenericUtility.getLogger(SearchFavouriteService.class.getName());
	
	
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse addFavourite(WSSearchFavouritesRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_FAVOURITESSEARCH);
				response = action.execute(request, ERequestType.SEARCH_FAVOURITES_ADD);
				
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
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Favourites Search - Add Favourite Request |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Favourites Search - Add Favourite Request |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Add Favourite Request |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Add Favourite Request |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Add Favourite Request |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	
	@Path("/list")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse listFavourite(WSSearchFavouritesRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_FAVOURITESSEARCH);
				response = action.execute(request, ERequestType.SEARCH_FAVOURITES_LIST);
				
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
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Favourites Search - List Favourite Request |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Favourites Search - List Favourite Request |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - List Favourite Request |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - List Favourite Request |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - List Favourite Request |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	
	@Path("/delete")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse deleteFavourite(WSSearchFavouritesRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_FAVOURITESSEARCH);
				response = action.execute(request, ERequestType.SEARCH_FAVOURITES_DELETE);
				
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
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Favourites Search - Delete Favourite Request |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Favourites Search - Delete Favourite Request |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Delete Favourite Request |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Delete Favourite Request |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Delete Favourite Request |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	
	@Path("/results")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse searchResultsListFavourite(WSSearchFavouritesRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_FAVOURITESSEARCH);
				response = action.execute(request, ERequestType.SEARCH_FAVOURITES_RESULTS_LIST);
				
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
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Favourites Search - Search Results List Favourite Request |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Favourites Search - Search Results List Favourite Request |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Search Results List Favourite Request |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Search Results List Favourite Request |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Search Results List Favourite Request |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}
	
	@Path("/detail")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse favouriteDetail(WSSearchFavouritesRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_FAVOURITESSEARCH);
				response = action.execute(request, ERequestType.SEARCH_FAVOURITES_DETAIL);
				
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
		catch (BadRequestException brEx){
			brEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BAD_REQUEST_EXCEPTION);
			logger.debug("|~| Favourites Search - Favourite Detail Request |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Favourites Search - Favourite Detail Request |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Favourite Detail Request |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Favourite Detail Request |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Favourites Search - Favourite Detail Request |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}

}
