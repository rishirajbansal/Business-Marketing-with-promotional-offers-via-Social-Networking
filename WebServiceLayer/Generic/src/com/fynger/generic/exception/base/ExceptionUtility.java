/**
 * 
 */
package com.fynger.generic.exception.base;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.exception.PropertyManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.LocationNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.servicesBusiness.exception.ActionException;
import com.fynger.servicesBusiness.exception.AddFavouritesException;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.DeleteFavouritesException;
import com.fynger.servicesBusiness.exception.FBLoginException;
import com.fynger.servicesBusiness.exception.FavouritesException;
import com.fynger.servicesBusiness.exception.FileUploadFailedException;
import com.fynger.servicesBusiness.exception.FlashDealException;
import com.fynger.servicesBusiness.exception.ForgotPasswordVerificationFailedException;
import com.fynger.servicesBusiness.exception.IncorrectPasswordException;
import com.fynger.servicesBusiness.exception.ListingIdNotFoundException;
import com.fynger.servicesBusiness.exception.MonitorFlashDealValidityThreadException;
import com.fynger.servicesBusiness.exception.MonitorPromotionValidityThreadException;
import com.fynger.servicesBusiness.exception.NoFavouritesFoundException;
import com.fynger.servicesBusiness.exception.NoFavouritesResultsFoundException;
import com.fynger.servicesBusiness.exception.NoFlashDealRespondedResultsFoundException;
import com.fynger.servicesBusiness.exception.NoReviewsFoundException;
import com.fynger.servicesBusiness.exception.NoSearchResultsFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.PromotionException;
import com.fynger.servicesBusiness.exception.PromotionIdNotFoundException;
import com.fynger.servicesBusiness.exception.RefreshUserFavouritesSearchResultsThreadException;
import com.fynger.servicesBusiness.exception.ShoutGroupException;
import com.fynger.servicesBusiness.exception.ThreadManagerException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesController.exception.BadRequestException;
import com.fynger.servicesController.exception.InitException;
import com.fynger.servicesController.exception.ServicesException;

/**
 * @author Rishi
 *
 */
public class ExceptionUtility {
	
	public static LoggerManager logger = GenericUtility.getLogger(ExceptionUtility.class.getName());
	
	
	
	public static ApplicationException createExceptionDetail(String errorCode, String userMessage, String errorMessage, EExceptionTypes exceptionType){
		
		ApplicationException exception = null;
		
		ExceptionDetail exDetail = new ExceptionDetail();
		exDetail.setCode(errorCode);
		exDetail.setUserMessage(userMessage);
		exDetail.setErrorMessage(errorMessage);
		
		switch(exceptionType){
		
			case ACTION_EXCEPTION:
				exception = new ActionException(exDetail);
				break;
				
			case APPLICATION_EXCEPTION:
				exception = new ApplicationException(exDetail);	
				break;
				
			case BAD_REQUEST_EXCEPTION:
				exception = new BadRequestException(exDetail);
				break;
				
			case BUSINESS_EXCEPTION:
				exception = new BusinessException(exDetail);
				break;
				
			case BUSINESS_VALIDATION_EXCEPTION:
				exception = new BusinessValidationException(exDetail);
				break;
				
			case DATABASE_CONNECTION_MANAGER_EXCEPTION:
				exception = new DatabaseConnectionManagerException(exDetail);
				break;
				
			case DATA_ACCESS_EXCEPTION:
				exception = new DataAccessException(exDetail);
				break;
				
			case INCORRECT_PASSWORD_EXCEPTION:
				exception = new IncorrectPasswordException(exDetail);
				break;
				
			case INIT_EXCEPTION:
				exception = new InitException(exDetail);
				break;
				
			case PROPERTY_MANAGER_EXCEPTION:
				exception = new PropertyManagerException(exDetail);
				break;
				
			case SERVICES_EXCEPTION:
				exception = new ServicesException(exDetail);
				break;
				
			case USER_NAME_NOT_FOUND_EXCEPTION:
				exception = new UserNameNotFoundException(exDetail);
				break;
				
			case FORGOT_PASSWORD_VERIFICATION_FAILED_EXCEPTION:
				exception = new ForgotPasswordVerificationFailedException(exDetail);
				break;
				
			case SEARCH_ENGINE_EXCEPTION:
				exception = new SearchEngineException(exDetail);
				break;
				
			case NO_SEARCH_RESULTS_FOUND_EXCEPTION:
				exception = new NoSearchResultsFoundException(exDetail);
				break;
				
			case NO_REVIEWS_FOUND_EXCEPTION:
				exception = new NoReviewsFoundException(exDetail);
				break;
				
			case LISTING_ID_NOT_FOUND_EXCEPTION:
				exception = new ListingIdNotFoundException(exDetail);
				break;
				
			case PROMOTION_ID_NOT_FOUND_EXCEPTION:
				exception = new PromotionIdNotFoundException(exDetail);
				break;
				
			case PROMOTION_EXCEPTION:
				exception = new PromotionException(exDetail);
				break;
				
			case GEO_COORDINATES_NOT_FOUND_EXCEPTION:
				exception = new GeoCoordinatesNotFoundException(exDetail);
				break;
				
			case LOCATION_NOT_FOUND_EXCEPTION:
				exception = new LocationNotFoundException(exDetail);
				break;
				
			case FLASHDEAL_EXCEPTION:
				exception = new FlashDealException(exDetail);
				break;
				
			case NO_FLASHDEAL_RESPONDED_RESULTS_FOUND_EXCEPTION:
				exception = new NoFlashDealRespondedResultsFoundException(exDetail);
				break;
				
			case FILE_UPLOAD_FAILED_EXCEPTION:
				exception = new FileUploadFailedException(exDetail);
				break;
				
			case FAVOURITES_EXCEPTION:
				exception = new FavouritesException(exDetail);
				break;
				
			case ADD_FAVOURITES_EXCEPTION:
				exception = new AddFavouritesException(exDetail);
				break;
				
			case DELETE_FAVOURITES_EXCEPTION:
				exception = new DeleteFavouritesException(exDetail);
				break;
				
			case NO_FAVOURITES_FOUND_EXCEPTION:
				exception = new NoFavouritesFoundException(exDetail);
				break;
				
			case NO_FAVOURITES_RESULTS_FOUND_EXCEPTION:
				exception = new NoFavouritesResultsFoundException(exDetail);
				break;
				
			case PAGE_COUNT_EXCEEDED_EXCEPTION:
				exception = new PageCountExceededException(exDetail);
				break;
				
			case FB_LOGIN_EXCEPTION:
				exception = new FBLoginException(exDetail);
				break;
				
			case THREAD_MANAGER_EXCEPTION:
				exception = new ThreadManagerException(exDetail);
				break;
				
			case MONITOR_PROMOTION_VALIDITY_THREAD_EXCEPTION:
				exception = new MonitorPromotionValidityThreadException(exDetail);
				break;
				
			case MONITOR_FLASH_DEAL_VALIDITY_THREAD_EXCEPTION:
				exception = new MonitorFlashDealValidityThreadException(exDetail);
				break;
				
			case REFRESH_USER_FAVOURITES_SEARCH_RESULTS_THREAD_EXCEPTION:
				exception = new RefreshUserFavouritesSearchResultsThreadException(exDetail);
				break;
				
			case SHOUT_GROUP_EXCEPTION:
				exception = new ShoutGroupException(exDetail);
				break;
				
			default:
				logger.error("Invalid exception type");
				break;
		
		}
		
		return exception;
	}
	
	public static ApplicationException createExceptionDetail(ExceptionDetail exDetail, EExceptionTypes exceptionType){
		
		return createExceptionDetail(exDetail.getCode(), exDetail.getUserMessage(), exDetail.getErrorMessage(), exceptionType);

	}

}
