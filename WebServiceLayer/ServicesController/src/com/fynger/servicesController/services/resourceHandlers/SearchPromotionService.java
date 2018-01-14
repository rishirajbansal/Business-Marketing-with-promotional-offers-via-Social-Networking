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
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.servicesBusiness.business.base.ActionDirectory;
import com.fynger.servicesBusiness.business.base.ActionFactory;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.base.IBusinessAction;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesController.constants.ExceptionConstants;
import com.fynger.servicesController.exception.BadRequestException;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchPromotionRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.utilities.ServiceUtility;

/**
 * @author Rishi
 *
 */

@Path("/promotions")
public class SearchPromotionService {
	
	public static LoggerManager logger = GenericUtility.getLogger(SearchPromotionService.class.getName());
	
	
	@Path("/resultsList")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse searchResults(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_RESULTS_LIST);
				
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
			logger.debug("|~| Promotions Search - Search results List |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Search results List |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(SearchEngineException searchEx){
			searchEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Search results List |~| Search Engine Exception occurred during the serivce execution : " + searchEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + searchEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(searchEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Search results List |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Search results List |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Search results List |~| Throwable occurred during the serivce execution : " + th.getMessage());
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
	public WSBaseResponse promotionDetail(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_DETAIL);
				
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
			logger.debug("|~| Promotions Search - Detail |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Detail |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Detail |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Detail |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Detail |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
		
	}

	@Path("/rating")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse rateMerchantStore(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_RATING);
				
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
			logger.debug("|~| Promotions Search - Rate Merchant Store |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Rate Merchant Store |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Rate Merchant Store |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Rate Merchant Store |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Rate Merchant Store |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("/review")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse reviewMerchantStore(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_REVIEW);
				
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
			logger.debug("|~| Promotions Search - Review Merchant Store |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Review Merchant Store |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Review Merchant Store |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Review Merchant Store |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Review Merchant Store |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("/loadReviews")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse loadReviews(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_LOAD_REVIEWS);
				
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
			logger.debug("|~| Promotions Search - Load Reviews |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Load Reviews |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Load Reviews |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Load Reviews |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Load Reviews |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}
	
	@Path("/like")
	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	public WSBaseResponse addToFavourite(WSSearchPromotionRequest request){
		
		WSBaseResponse response = null;
		
		try{
			if (null != request){
				
				IBusinessAction action = ActionFactory.getActionInstance(ActionDirectory.ACTION_PROMOTIONSSEARCH);
				response = action.execute(request, ERequestType.SEARCH_PROMOTION_ADD_TO_FAVOURITE);
				
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
			logger.debug("|~| Promotions Search - Add to Favourites |~| Bad Request Exception occurred during the serivce execution : " + brEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + brEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(brEx.getExceptionDetail());
		}
		catch(BusinessException bEx){
			bEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_BUSINESS_EXCEPTION);
			logger.debug("|~| Promotions Search - Add to Favourites |~| Business Exception occurred during the serivce execution : " + bEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + bEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(bEx.getExceptionDetail());
		}
		catch(ApplicationException aEx){
			aEx.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Add to Favourites |~| Application Exception occurred during the serivce execution : " + aEx.getMessage());
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + aEx.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(aEx.getExceptionDetail());
		}
		catch(Exception ex){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Add to Favourites |~| Exception occurred during the serivce execution : " + ex.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_EXCEPTION);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		catch(Throwable th){
			ApplicationException exception = ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_EXCEPTION, ExceptionConstants.USERMESSAGE_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
			logger.debug("|~| Promotions Search - Add to Favourites |~| Throwable occurred during the serivce execution : " + th.getMessage());
			exception.getExceptionDetail().setStatus(ExceptionConstants.SERVICE_STATUS_THROWABLE);
			logger.debug("An error response object will be sent to the client with error details. ExceptionDetail :" + exception.getExceptionDetail());
			return ServiceUtility.generateErrorResponse(exception.getExceptionDetail());
		}
		
		return response;
	}

}
