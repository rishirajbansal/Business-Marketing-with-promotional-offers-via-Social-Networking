/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.handlers.SearchQueryHandler;
import com.fynger.searchEngine.requests.vo.PromotionsSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.PromotionsSearchResponseVO;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.enums.EFavouriteGroupTypes;
import com.fynger.servicesBusiness.business.threadManager.RefreshUserFavouritesSearchResults;
import com.fynger.servicesBusiness.business.threadManager.ThreadManager;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.AddFavouritesException;
import com.fynger.servicesBusiness.exception.ArchiveDataException;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FavouriteAlreadyExistsException;
import com.fynger.servicesBusiness.exception.NoReviewsFoundException;
import com.fynger.servicesBusiness.exception.NoSearchResultsFoundException;
import com.fynger.servicesBusiness.exception.PromotionException;
import com.fynger.servicesBusiness.exception.PromotionIdNotFoundException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FavouritesSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFavouritesSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPromotionsSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.PromotionsSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.MerchantReviewsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionClickArchiveData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionViewArchiveData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;
import com.fynger.servicesBusiness.integration.searchEngine.facade.SearchFacade;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.PromotionsSearchValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchPromotionRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchPromotionResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;

/**
 * @author Rishi
 *
 */
public class PromotionsSearchAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(PromotionsSearchAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public PromotionsSearchAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	
	
	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSSearchPromotionRequest wsSearchPromotionRequest = (WSSearchPromotionRequest)request;
		
		WSSearchPromotionResponse wsSearchPromotionResponse = new WSSearchPromotionResponse();
		
		try{
			switch(requestType){
			
				case SEARCH_PROMOTION_RESULTS_LIST: 
					searchResultsList(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
					
				case SEARCH_PROMOTION_DETAIL: 
					promotionDetail(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
					
				case SEARCH_PROMOTION_RATING: 
					rateMerchantStore(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
					
				case SEARCH_PROMOTION_REVIEW: 
					reviewMerchantStore(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
					
				case SEARCH_PROMOTION_LOAD_REVIEWS: 
					loadReviews(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
					
				case SEARCH_PROMOTION_ADD_TO_FAVOURITE:
					addToFavourite(wsSearchPromotionRequest, wsSearchPromotionResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Promotion Search service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(GeoCoordinatesNotFoundException gcnfEx){
			logger.debug("GeoCoordinatesNotFoundException recevied from Search Engine : " + gcnfEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_GEO_COORDINATES_NOT_FOUND, ExceptionConstants.USERMESSAGE_GEO_COORDINATES_NOT_FOUND, gcnfEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(SearchEngineException searchEx){
			logger.debug("SearchEngineException recevied from Search Engine : " + searchEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SEARCH_ENGINE_EXCEPTION, ExceptionConstants.USERMESSAGE_SEARCH_ENGINE_EXCEPTION, searchEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PromotionIdNotFoundException pinfEx){
			logger.debug("PromotionIdNotFoundException occurred in DAO layer as Promotion id not found in the database.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PROMOTION_ID_NOT_FOUND, ExceptionConstants.USERMESSAGE_PROMOTION_ID_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_PROMOTION_ID_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PromotionException pEx){
			logger.debug("PromotionException occurred : " + pEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PROMOTION_EXCEPTION, ExceptionConstants.USERMESSAGE_PROMOTION_EXCEPTION, pEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoSearchResultsFoundException nsrfEx){
			logger.debug("NoSearchResultsFoundException occurred as Search engine returned no results.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.USERMESSAGE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.ERRORMESSAGE_NO_SEARCH_RESULTS_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(AddFavouritesException afEx){
			logger.debug("AddFavouritesException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_ADD_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_ADD_FAVOURITES_EXCEPTION, afEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FavouriteAlreadyExistsException afEx){
			logger.debug("FavouriteAlreadyExistsException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.USERMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.ERRORMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoReviewsFoundException nsrfEx){
			logger.debug("NoReviewsFoundException occurred as Search engine returned no results.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_REVIEWS_FOUND, ExceptionConstants.USERMESSAGE_NO_REVIEW_FOUND, ExceptionConstants.ERRORMESSAGE_NO_REVIEW_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_DATA_ACCESS_EXCEPTION, ExceptionConstants.USERMESSAGE_DATA_ACCESS_EXCEPTION, daEx.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.toString());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
			
		return wsSearchPromotionResponse;
		
	}
	
	public void searchResultsList(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IBusinessValidator validator = new PromotionsSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchPromotionRequest, requestType);
		
		if (isValid){
			
			IUserDAO userDao = daoFactory.getUserDAO();
			
			double latitude = GenericUtility.safeTrim(wsSearchPromotionRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchPromotionRequest.getLatitude());
			double longitude = GenericUtility.safeTrim(wsSearchPromotionRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchPromotionRequest.getLongitude());
			
			/* Verify if the request is containing coordinates */
			if (latitude != 0 && longitude != 0){
				wsSearchPromotionRequest.setLocationCoordinates(wsSearchPromotionRequest.getLatitude() + "|" + wsSearchPromotionRequest.getLongitude());
				
				/* Update the coordinates in User login table */
				
				boolean flag = userDao.updateUserLocationCoordinates(wsSearchPromotionRequest.getLocationCoordinates(), wsSearchPromotionRequest.getUsername());
				
				if (flag){
					logger.debug("User coordinates updated successfully in the database for username : " + wsSearchPromotionRequest.getUsername());
				}
				else{
					logger.error("User coordinates failed to update successfully in the database for username : " + wsSearchPromotionRequest.getUsername());
				}
			}
			else{
				/* Fetch user coordinates from the database */
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchPromotionRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					wsSearchPromotionRequest.setLatitude(coordArr[0]);
					wsSearchPromotionRequest.setLongitude(coordArr[1]);
				}
			}
			
			
			/* Fetch the search results from Search Engine */

			SearchQueryHandler queryHandler = SearchFacade.getPromotionSearchHandler();
			
			PromotionsSearchRequestVO requestVO = populateSearchQueryHandlerRequest(wsSearchPromotionRequest);
			
			queryHandler.setQueryType(SearchQueryTypes.Promotion_resultset);
			
			queryHandler.setQueryRequest(requestVO);
			
			queryHandler.handleQuery();
			
			List<BaseResponseVO> alPromotionsResultSet = queryHandler.getQueryResponseResultSet();
			
			if (null != alPromotionsResultSet && alPromotionsResultSet.size() > 0){
				
				wsSearchPromotionResponse.setTotalPages(Integer.toString(queryHandler.getTotalPages()));
				wsSearchPromotionResponse.setTotalResults(Integer.toString(queryHandler.getTotalResults()));
				
				List<PromotionViewArchiveData> alPromotionViewArchiveData = new ArrayList<PromotionViewArchiveData>();
				
				populatePromotionsSearchResultsResponse(wsSearchPromotionRequest, wsSearchPromotionResponse, alPromotionsResultSet, alPromotionViewArchiveData);
				
				/* Archive the Promotion details for analytics purposes */
				
				try{
					IPromotionsSearchDAO promotionSearchDAO = daoFactory.getPromotionsSearchDAO();
					
					boolean flag = promotionSearchDAO.archiveOnPromotionSearchResults(alPromotionViewArchiveData);
					
					if (flag){
						logger.debug("Data set is archived successfully on Promotion Search Results event");
					}
					else{
						logger.error("Failed to archive Data set on Promotion Search Results event");
					}
				}
				catch(ArchiveDataException adEx){
					//TODO: This error should be reported
					logger.error("ArchiveDataException occurred : " + adEx.getMessage());
					logger.error("Failed to archive Data set on Promotion Search Results event");
				}
			}
			else{
				logger.debug("Search Engine returned blank response for Promotion Search.");
				throw new NoSearchResultsFoundException("Search Engine returned blank response for Promotion Search.");
			}
		}
		else{
			logger.debug("Promotion Search validations failed.");
			throw new PromotionException("Promotion Search validations failed.");
		}
	}
	
	public void promotionDetail(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IBusinessValidator validator = new PromotionsSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchPromotionRequest, requestType);
		
		if (isValid){
			IPromotionsSearchDAO promotionSearchDAO = daoFactory.getPromotionsSearchDAO();
			
			PromotionData promotionData = promotionSearchDAO.loadPromotionDetail(wsSearchPromotionRequest.getEntityId(), wsSearchPromotionRequest.getEntityType());
			
			if (null != promotionData){
				MerchantRatingLikesData merchantRatingLikesData = promotionSearchDAO.loadStoreRatingsAndLikes(promotionData.getStoreId());
				
				int reviewsCount = promotionSearchDAO.storeReviewscount(promotionData.getStoreId());
				
				/* Fetch user coordinates from the database to calculate distance */
				IUserDAO userDao = daoFactory.getUserDAO();
				/* Fetch user coordinates from the database */
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchPromotionRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					wsSearchPromotionRequest.setLatitude(coordArr[0]);
					wsSearchPromotionRequest.setLongitude(coordArr[1]);
				}
				
				populatePromotionDetailResponse(wsSearchPromotionRequest, wsSearchPromotionResponse, promotionData, merchantRatingLikesData, reviewsCount);
				
				/* Archive the Promotion details for analytics purposes */
				
				try{
					PromotionClickArchiveData promotionClickArchiveData = new PromotionClickArchiveData();
					promotionClickArchiveData.setEntityId(Integer.parseInt(wsSearchPromotionRequest.getEntityId()));
					promotionClickArchiveData.setEntityType(wsSearchPromotionRequest.getEntityType());
					promotionClickArchiveData.setUsername(wsSearchPromotionRequest.getUsername());
					
					boolean flag = promotionSearchDAO.archiveOnPromotionDetail(promotionClickArchiveData);
					
					if (flag){
						logger.debug("Data set is archived successfully on Promotion Detail event");
					}
					else{
						logger.error("Failed to archive Data set on Promotion Detail event");
					}
				}
				catch(ArchiveDataException adEx){
					//TODO: This error should be reported
					logger.error("ArchiveDataException occurred : " + adEx.getMessage());
					logger.error("Failed to archive Data set on Promotion Detail event");
				}
			}
			else{
				logger.debug("Failed to load Promotion details from the database.");
				throw new PromotionException("Failed to load Promotion details from the database.");
			}
		}
		else{
			logger.debug("Promotion Search - Detail validations failed.");
			throw new PromotionException("Promotion Search - Detail validations failed.");
		}
	}
	
	public void rateMerchantStore(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IPromotionsSearchDAO promotionSearchDAO = new PromotionsSearchDAOImpl();
		
		MerchantRatingLikesData daoObject = new MerchantRatingLikesData();
		daoObject.setMerchantStoreId(Integer.parseInt(wsSearchPromotionRequest.getStoreId()));
		daoObject.setRating(wsSearchPromotionRequest.getRating());
		
		boolean flag = promotionSearchDAO.rateMerchantStore(daoObject);
		
		if (flag){
			logger.debug("Rating saved successfully in the database.");
			
			generateSuccessResponse(wsSearchPromotionResponse);
		}
		else{
			logger.debug("Rating failed to save in the database.");
			throw new PromotionException("Rating failed to save in the database.");
		}
		
	}
	
	public void reviewMerchantStore(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IPromotionsSearchDAO promotionSearchDAO = daoFactory.getPromotionsSearchDAO();
		
		MerchantReviewsData daoObject = new MerchantReviewsData();
		daoObject.setMerchantStoreId(Integer.parseInt(wsSearchPromotionRequest.getStoreId()));
		daoObject.setUsername(wsSearchPromotionRequest.getUsername());
		daoObject.setReviewText(wsSearchPromotionRequest.getReviewText());
		
		boolean flag = promotionSearchDAO.reviewMerchantStore(daoObject);
		
		if (flag){
			logger.debug("Review saved successfully in the database.");
			
			generateSuccessResponse(wsSearchPromotionResponse);
		}
		else{
			logger.debug("Review failed to save in the database.");
			throw new PromotionException("Review failed to save in the database.");
		}
		
	}
	
	public void loadReviews(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IPromotionsSearchDAO promotionSearchDAO = daoFactory.getPromotionsSearchDAO();
		
		List<MerchantReviewsData> alMerchantReviewsData = promotionSearchDAO.loadReviews(wsSearchPromotionRequest.getStoreId());
		
		if (null != alMerchantReviewsData){
			logger.debug("Merchant's store Reviews are loaded succssfully from the database.");
			populatePromotionSearchLoadReviewsResponse(wsSearchPromotionResponse, alMerchantReviewsData);
		}
		else{
			logger.debug("No Reviews found for the Merchant's store id in the database.");
			//generateSuccessResponse(promotionSearchResponse);
			//throw new BusinessException("No Reviews found for the Listing id in the database.");
		}
		
	}
	
	public void addToFavourite(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, ERequestType requestType){
		
		IBusinessValidator validator = new PromotionsSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchPromotionRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			UserFavouritesData userFavouritesData = populateUserFavouriteDAOData(wsSearchPromotionRequest);
			
			boolean flag = favouritesSearchDAO.addFavourite(userFavouritesData);
			
			if (flag){
				IPromotionsSearchDAO promotionSearchDAO = new PromotionsSearchDAOImpl();
				
				flag = promotionSearchDAO.updateLikesCount(Integer.parseInt(wsSearchPromotionRequest.getStoreId()));
				
				if (flag){
					
					/* Notify the user favourite promotion thread to refresh the results for new favourite */
					logger.debug("Refresh user favourite search results list by invoking the user's thread...");
					ThreadManager threadManager = ThreadManager.createInstance();
					RefreshUserFavouritesSearchResults thread = threadManager.getRefreshUserFavPromotionsThread(wsSearchPromotionRequest.getUsername());
					
					if (null != thread){
						try{
							synchronized (thread) {
								thread.notify();
							}
						}
						catch(Exception ex){
							logger.error("An exception occured while notifying user favourite search results thread for user name : " + wsSearchPromotionRequest.getUsername());
							//TODO: This error should be reported
						}
					}
					else{
						logger.error("NO thread exists for refresh user favourite promotions");
						//TODO: This error should be reported
					}
					
					generateSuccessResponse(wsSearchPromotionResponse);
				}
				else{
					logger.debug("Add to Favourite request is FAILED to update successfully in the database while updatig likes count.");
					throw new AddFavouritesException("Add to Favourite request is FAILED to update successfully in the database while updatig likes count.");
				}
			}
			else{
				logger.debug("Add to Favourite request is FAILED to update successfully in the database.");
				throw new AddFavouritesException("Add to Favourite request is FAILED to update successfully in the database.");
			}
		}
		else{
			logger.debug("Promotions Search - Add to Favourites validations failed.");
			throw new BusinessException("Promotions Search - Add to Favourites validations failed.");
		}
	}
	
	
	private PromotionsSearchRequestVO populateSearchQueryHandlerRequest(WSSearchPromotionRequest wsSearchPromotionRequest){
		
		PromotionsSearchRequestVO requestVO = new PromotionsSearchRequestVO();
		
		requestVO.setKeyword(wsSearchPromotionRequest.getSearchText());
		requestVO.setLatitude(GenericUtility.safeTrim(wsSearchPromotionRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchPromotionRequest.getLatitude()));
		requestVO.setLongitude(GenericUtility.safeTrim(wsSearchPromotionRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchPromotionRequest.getLongitude()));
		requestVO.setLocation(wsSearchPromotionRequest.getLocationString());
		requestVO.setResultSetCount(wsSearchPromotionRequest.getPageCount() <= 0 ? 1: wsSearchPromotionRequest.getPageCount());
		
		return requestVO;
	}
	
	private void populatePromotionsSearchResultsResponse(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, List<BaseResponseVO> alPromotionsResultSet, List<PromotionViewArchiveData> alPromotionViewArchiveData){
		
		PromotionsSearchResponseVO promotionsResponseVO = null;
		
		List<WSPromotion> alPromotionsData = new ArrayList<WSPromotion>();
		
		for (BaseResponseVO responseVO : alPromotionsResultSet){
			promotionsResponseVO = (PromotionsSearchResponseVO)responseVO;
			
			WSPromotion promotion = new WSPromotion();
			
			promotion.setName(promotionsResponseVO.getName());
			promotion.setAddress(promotionsResponseVO.getAddress());
			promotion.setPromotionText(promotionsResponseVO.getPromotionText());
			promotion.setValidityPeriod(promotionsResponseVO.getValidityPeriod());
			promotion.setEntityId(Integer.toString(promotionsResponseVO.getEntityId()));
			promotion.setEntityType(promotionsResponseVO.getPromotionType());
			promotion.setLatitude(Double.toString(promotionsResponseVO.getLatitude()));
			promotion.setLongitude(Double.toString(promotionsResponseVO.getLongitude()));
			
			/* Calculate distance between the source & destination coordinates */
			if ((Double.parseDouble(wsSearchPromotionRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchPromotionRequest.getLongitude()) != 0)){
				double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchPromotionRequest.getLatitude()), Double.parseDouble(wsSearchPromotionRequest.getLongitude()), promotionsResponseVO.getLatitude(), promotionsResponseVO.getLongitude());
				promotion.setDistance(Double.toString(distance));
			}
			
			alPromotionsData.add(promotion);
			
			/* For archiving purposes */
			PromotionViewArchiveData archiveData = new PromotionViewArchiveData();
			archiveData.setEntityId(promotionsResponseVO.getEntityId());
			archiveData.setEntityType(promotionsResponseVO.getPromotionType());
			archiveData.setUsername(wsSearchPromotionRequest.getUsername());
			alPromotionViewArchiveData.add(archiveData);
			
		}
		
		wsSearchPromotionResponse.setPromotionsData(alPromotionsData);
		
	}
	
	private void populatePromotionDetailResponse(WSSearchPromotionRequest wsSearchPromotionRequest, WSSearchPromotionResponse wsSearchPromotionResponse, PromotionData promotionData, MerchantRatingLikesData merchantRatingLikesData, int reviewsCount){
		
		WSPromotion promotion = new WSPromotion();
		
		promotion.setName(promotionData.getName());
		promotion.setAddress(promotionData.getAddress());
		promotion.setPromotionText(promotionData.getPromotionText());
		promotion.setValidityPeriod(promotionData.getValidityPeriod());
		promotion.setCity(promotionData.getCity());
		promotion.setState(promotionData.getState());
		promotion.setPhone(promotionData.getPhone());
		promotion.setCategories(promotionData.getCategories());
		promotion.setLatitude(Double.toString(promotionData.getLatitude()));
		promotion.setLongitude(Double.toString(promotionData.getLongitude()));
		promotion.setMultimediaPath(promotionData.getMultimediaPath());
		promotion.setMultimediaType(promotionData.getMultimediaType());
		promotion.setStoreId(promotionData.getStoreId());
		
		/* Calculate distance between the source & destination coordinates */
		if ((Double.parseDouble(wsSearchPromotionRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchPromotionRequest.getLongitude()) != 0)){
			double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchPromotionRequest.getLatitude()), Double.parseDouble(wsSearchPromotionRequest.getLongitude()), promotionData.getLatitude(), promotionData.getLongitude());
			promotion.setDistance(Double.toString(distance));
		}
		
		if (null != merchantRatingLikesData){
			promotion.setRating(Integer.toString(merchantRatingLikesData.getRating()));
			promotion.setFavouritesCount(Integer.toString(merchantRatingLikesData.getLikesCount()));
		}
		
		promotion.setReviewsCount(Integer.toString(reviewsCount));
		
		wsSearchPromotionResponse.setPromotionDetail(promotion);
	}
	
	private void populatePromotionSearchLoadReviewsResponse(WSSearchPromotionResponse wsSearchPromotionResponse, List<MerchantReviewsData> alMerchantReviewsData){
		
		List<WSReview> reviewData = new ArrayList<WSReview>();
		
		for (MerchantReviewsData merchantReviewsData : alMerchantReviewsData){
			 
			WSReview review = new WSReview();
			review.setUserName(merchantReviewsData.getUsername());
			review.setReviewText(merchantReviewsData.getReviewText());
			review.setUserPicturePath(merchantReviewsData.getUserPicturePath());
			
			reviewData.add(review);
		}
		
		wsSearchPromotionResponse.setReviewData(reviewData);
		
	}
	
	private UserFavouritesData populateUserFavouriteDAOData(WSSearchPromotionRequest wsSearchPromotionRequest){
		
		UserFavouritesData userFavouritesData = new UserFavouritesData();
		
		userFavouritesData.setUsername(wsSearchPromotionRequest.getUsername());
		
		if (EFavouriteGroupTypes.valueOf(wsSearchPromotionRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.CATEGORIES)){
			userFavouritesData.setCategoriesList(wsSearchPromotionRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(wsSearchPromotionRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.BRANDS)){
			userFavouritesData.setBrandsList(wsSearchPromotionRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(wsSearchPromotionRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.PLACES)){
			userFavouritesData.setPlacesList(wsSearchPromotionRequest.getKeyword());
		}
		else {
			userFavouritesData.setPlacesList(wsSearchPromotionRequest.getKeyword());
		}
		
		return userFavouritesData;
		
	}
	
	public void generateSuccessResponse(WSSearchPromotionResponse wsSearchPromotionResponse){
		
		wsSearchPromotionResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}


}
