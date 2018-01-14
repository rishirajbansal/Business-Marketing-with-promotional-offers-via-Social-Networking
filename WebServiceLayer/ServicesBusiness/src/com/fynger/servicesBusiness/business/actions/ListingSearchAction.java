/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.handlers.SearchQueryHandler;
import com.fynger.searchEngine.requests.vo.ListingSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.ListingSearchResponseVO;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.enums.EFavouriteGroupTypes;
import com.fynger.servicesBusiness.business.threadManager.RefreshUserFavouritesSearchResults;
import com.fynger.servicesBusiness.business.threadManager.ThreadManager;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.AddFavouritesException;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FavouriteAlreadyExistsException;
import com.fynger.servicesBusiness.exception.ListingIdNotFoundException;
import com.fynger.servicesBusiness.exception.NoReviewsFoundException;
import com.fynger.servicesBusiness.exception.NoSearchResultsFoundException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FavouritesSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFavouritesSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IListingSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingRatingLikesData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ListingReviewsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;
import com.fynger.servicesBusiness.integration.searchEngine.facade.SearchFacade;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.ListingSearchValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchListingRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchListingResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSListing;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;

/**
 * @author Rishi
 *
 */
public class ListingSearchAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(ListingSearchAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public ListingSearchAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}


	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSSearchListingRequest listingSearchRequest = (WSSearchListingRequest)request;
		
		WSSearchListingResponse listingSearchResponse = new WSSearchListingResponse();
		
		try{
			switch(requestType){
			
				case SEARCH_LISTING_RESULTS_LIST: 
					searchResultsList(listingSearchRequest, listingSearchResponse, requestType);
					break;
					
				case SEARCH_LISTING_RATING:
					rateListing(listingSearchRequest, listingSearchResponse, requestType);
					break;
					
				case SEARCH_LISTING_REVIEW:
					reviewListing(listingSearchRequest, listingSearchResponse, requestType);
					break;
					
				case SEARCH_LISTING_LOAD_REVIEWS:
					loadReviews(listingSearchRequest, listingSearchResponse, requestType);
					break;
					
				case SEARCH_LISTING_ADD_TO_FAVOURITE:
					addToFavourite(listingSearchRequest, listingSearchResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Listing Search service : " + bvEx.getMessage());
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
		catch(ListingIdNotFoundException sqEx){
			logger.debug("ListingIdNotFoundException occurred in DAO layer as listing id not found in the database.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LISTING_ID_NOT_FOUND, ExceptionConstants.USERMESSAGE_LISTING_ID_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_LISTING_ID_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(AddFavouritesException afEx){
			logger.debug("AddFavouritesException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_ADD_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_ADD_FAVOURITES_EXCEPTION, afEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FavouriteAlreadyExistsException afEx){
			logger.debug("FavouriteAlreadyExistsException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.USERMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.ERRORMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoSearchResultsFoundException nsrfEx){
			logger.debug("NoSearchResultsFoundException occurred as Search engine returned no results.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.USERMESSAGE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.ERRORMESSAGE_NO_SEARCH_RESULTS_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
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
			logger.error("Exception occurred : " + ex.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
			
		return listingSearchResponse;
	}
	
	public void searchResultsList(WSSearchListingRequest listingSearchRequest, WSSearchListingResponse listingSearchResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ListingSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)listingSearchRequest, requestType);
		
		if (isValid){
			
			IUserDAO userDao = daoFactory.getUserDAO();
			
			double latitude = GenericUtility.safeTrim(listingSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(listingSearchRequest.getLatitude());
			double longitude = GenericUtility.safeTrim(listingSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(listingSearchRequest.getLongitude());
			
			/* Verify if the request is containing coordinates */
			if (latitude != 0 && longitude != 0){
				listingSearchRequest.setLocationCoordinates(listingSearchRequest.getLatitude() + "|" + listingSearchRequest.getLongitude());
				
				/* Update the coordinates in User login table */
				
				boolean flag = userDao.updateUserLocationCoordinates(listingSearchRequest.getLocationCoordinates(), listingSearchRequest.getUsername());
				
				if (flag){
					logger.debug("User coordinates updated successfully in the database for username : " + listingSearchRequest.getUsername());
				}
				else{
					logger.error("User coordinates failed to update successfully in the database for username : " + listingSearchRequest.getUsername());
				}
			}
			else{
				/* Fetch user coordinates from the database */
				String locationCoordinates = userDao.fetchUserLocationCoordinates(listingSearchRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					listingSearchRequest.setLatitude(coordArr[0]);
					listingSearchRequest.setLongitude(coordArr[1]);
				}
				
			}
			
			/* Fetch the search results from Search Engine */

			SearchQueryHandler queryHandler = SearchFacade.getListingSearchHandler();
			
			ListingSearchRequestVO requestVO = populateQueryHandlerRequest(listingSearchRequest);
			
			queryHandler.setQueryType(SearchQueryTypes.Listing_resultset);
			
			/*if (requestVO.getLatitude() != 0 && requestVO.getLongitude() != 0){
				queryHandler.setQueryType(SearchQueryTypes.Listing_Coordinates_resultset);
			}
			else{
				queryHandler.setQueryType(SearchQueryTypes.Listing_Location_resultset);
			}*/
			
			queryHandler.setQueryRequest(requestVO);
			
			queryHandler.handleQuery();
			List<BaseResponseVO> alListingResultSet = queryHandler.getQueryResponseResultSet();
			
			if (null != alListingResultSet && alListingResultSet.size() > 0){
								
				listingSearchResponse.setTotalPages(Integer.toString(queryHandler.getTotalPages()));
				listingSearchResponse.setTotalResults(Integer.toString(queryHandler.getTotalResults()));
				
				IListingSearchDAO listingSearchDao = daoFactory.getListingSearchDAO();
				
				ListingSearchResponseVO listingResponseVO = null;
				StringBuffer consolidatedListingIds = new StringBuffer();
				for (BaseResponseVO searchResponse : alListingResultSet){
					listingResponseVO = (ListingSearchResponseVO)searchResponse;
					
					consolidatedListingIds.append(listingResponseVO.getListingId()).append(",");
				}
				if (consolidatedListingIds.toString().endsWith(",")) {
					consolidatedListingIds = new StringBuffer((consolidatedListingIds.substring(0, consolidatedListingIds.lastIndexOf(","))));
				}
				
				/* Fetch the Rating & favorites count from DAO */
				Map<Integer, ListingRatingLikesData> hmListingRatingLikesData = listingSearchDao.loadListingsRatingAndLikes(consolidatedListingIds.toString());
				
				/* Fetch the Reviews Count from DAO */
				Map<Integer, Integer> hmListingReviewsData = listingSearchDao.loadListingsReviews(consolidatedListingIds.toString());
				
				populateListingSearchResultsResponse(listingSearchRequest, listingSearchResponse, alListingResultSet, hmListingRatingLikesData, hmListingReviewsData);
			}
			else{
				logger.debug("Search Engine returned blank response for Listing Search.");
				throw new NoSearchResultsFoundException("Search Engine returned blank response for Listing Search.");
			}
			
		}
		else{
			logger.debug("Listing Search validations failed.");
			throw new BusinessException("Listing Search validations failed.");
		}
		
	}
	
	public void rateListing(WSSearchListingRequest listingSearchRequest, WSSearchListingResponse listingSearchResponse, ERequestType requestType){
		
		IListingSearchDAO listingSearchDao = daoFactory.getListingSearchDAO();
		
		ListingRatingLikesData daoObject = new ListingRatingLikesData();
		daoObject.setListingId(Integer.parseInt(listingSearchRequest.getListingId()));
		daoObject.setRating(listingSearchRequest.getRating());
		
		boolean flag = listingSearchDao.rateListing(daoObject);
		
		if (flag){
			logger.debug("Rating saved successfully in the database.");
			
			generateSuccessResponse(listingSearchResponse);
		}
		else{
			logger.debug("Rating failed to save in the database.");
			throw new BusinessException("Rating failed to save in the database.");
		}
		
	}
	
	public void reviewListing(WSSearchListingRequest listingSearchRequest, WSSearchListingResponse listingSearchResponse, ERequestType requestType){
		
		IListingSearchDAO listingSearchDao = daoFactory.getListingSearchDAO();
		
		ListingReviewsData daoObject = new ListingReviewsData();
		daoObject.setListingId(Integer.parseInt(listingSearchRequest.getListingId()));
		daoObject.setUsername(listingSearchRequest.getUsername());
		daoObject.setReviewText(listingSearchRequest.getReviewText());
		
		boolean flag = listingSearchDao.reviewListing(daoObject);
		
		if (flag){
			logger.debug("Review saved successfully in the database.");
			
			generateSuccessResponse(listingSearchResponse);
		}
		else{
			logger.debug("Review failed to save in the database.");
			throw new BusinessException("Review failed to save in the database.");
		}
		
	}
	
	public void loadReviews(WSSearchListingRequest listingSearchRequest, WSSearchListingResponse listingSearchResponse, ERequestType requestType){
		
		IListingSearchDAO listingSearchDao = daoFactory.getListingSearchDAO();
		
		List<ListingReviewsData> alListingReviewsData = listingSearchDao.loadReviews(listingSearchRequest.getListingId());
		
		if (null != alListingReviewsData){
			logger.debug("Listing Reviews are loaded succssfully from the database.");
			populateListingSearchLoadReviewsResponse(listingSearchResponse, alListingReviewsData);
		}
		else{
			logger.debug("No Reviews found for the Listing id in the database.");
			//generateSuccessResponse(listingSearchResponse);
			//throw new BusinessException("No Reviews found for the Listing id in the database.");
		}
		
	}
	
	public void addToFavourite(WSSearchListingRequest listingSearchRequest, WSSearchListingResponse listingSearchResponse, ERequestType requestType){
		
		IBusinessValidator validator = new ListingSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)listingSearchRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			UserFavouritesData userFavouritesData = populateUserFavouriteDAOData(listingSearchRequest);
			
			boolean flag = favouritesSearchDAO.addFavourite(userFavouritesData);
			
			if (flag){
				IListingSearchDAO listingSearchDao = daoFactory.getListingSearchDAO();
				
				flag = listingSearchDao.updateLikesCount(Integer.parseInt(listingSearchRequest.getListingId()));
				
				if (flag){
					
					/* Notify the user favourite promotion thread to refresh the results for new favourite */
					logger.debug("Refresh user favourite search results list by invoking the user's thread...");
					ThreadManager threadManager = ThreadManager.createInstance();
					RefreshUserFavouritesSearchResults thread = threadManager.getRefreshUserFavPromotionsThread(listingSearchRequest.getUsername());
					
					if (null != thread){
						try{
							synchronized (thread) {
								thread.notify();
							}
						}
						catch(Exception ex){
							logger.error("An exception occured while notifying user favourite search results thread for user name : " + listingSearchRequest.getUsername());
							//TODO: This error should be reported
						}
					}
					else{
						logger.error("NO thread exists for refresh user favourite promotions");
						//TODO: This error should be reported
					}
					
					generateSuccessResponse(listingSearchResponse);
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
			logger.debug("Listing Search - Add to Favourites validations failed.");
			throw new BusinessException("Listing Search - Add to Favourites validations failed.");
		}
	}
	
	private ListingSearchRequestVO populateQueryHandlerRequest(WSSearchListingRequest listingSearchRequest){
		
		ListingSearchRequestVO requestVO = new ListingSearchRequestVO();
		
		requestVO.setKeyword(listingSearchRequest.getSearchText());
		requestVO.setLatitude(GenericUtility.safeTrim(listingSearchRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(listingSearchRequest.getLatitude()));
		requestVO.setLongitude(GenericUtility.safeTrim(listingSearchRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(listingSearchRequest.getLongitude()));
		requestVO.setLocation(listingSearchRequest.getLocationString());
		requestVO.setResultSetCount(listingSearchRequest.getPageCount() <= 0 ? 1: listingSearchRequest.getPageCount());
		
		return requestVO;
		
	}
	
	private void populateListingSearchResultsResponse(WSSearchListingRequest listingSearchRequest,
												WSSearchListingResponse listingSearchResponse, 
												List<BaseResponseVO> alListingResultSet,
												Map<Integer, ListingRatingLikesData> hmListingRatingLikesData, 
												Map<Integer, Integer> hmListingReviewsData){
		
		ListingSearchResponseVO listingSearchResponseVO = null;
		
		List<WSListing> alListingsData = new ArrayList<WSListing>();
		
		for (BaseResponseVO responseVO : alListingResultSet){
			listingSearchResponseVO = (ListingSearchResponseVO)responseVO;
			
			WSListing listing = new WSListing();
			
			listing.setName(listingSearchResponseVO.getName());
			listing.setAddress(listingSearchResponseVO.getAddress());
			listing.setCity(listingSearchResponseVO.getCity());
			listing.setState(listingSearchResponseVO.getState());
			listing.setPinCode(listingSearchResponseVO.getPinCode());
			listing.setPhone(listingSearchResponseVO.getPhone());
			listing.setMobile(listingSearchResponseVO.getMobile());
			listing.setCategories(listingSearchResponseVO.getCategories());
			listing.setLatitude(Double.toString(listingSearchResponseVO.getLatitude()));
			listing.setLongitude(Double.toString(listingSearchResponseVO.getLongitude()));
			
			/* Calculate distance between the source & destination coordinates */
			if ((Double.parseDouble(listingSearchRequest.getLatitude()) != 0) && (Double.parseDouble(listingSearchRequest.getLongitude()) != 0)){
				double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(listingSearchRequest.getLatitude()), Double.parseDouble(listingSearchRequest.getLongitude()), listingSearchResponseVO.getLatitude(), listingSearchResponseVO.getLongitude());
				listing.setDistance(Double.toString(distance));
			}
			
			listing.setListingId(Integer.toString(listingSearchResponseVO.getListingId()));
			
			if (null != hmListingRatingLikesData && hmListingRatingLikesData.size() >= 0){
				if (hmListingRatingLikesData.containsKey(listingSearchResponseVO.getListingId())){
					listing.setRating(Integer.toString(hmListingRatingLikesData.get(listingSearchResponseVO.getListingId()).getRating()));
					listing.setFavouritesCount(Integer.toString(hmListingRatingLikesData.get(listingSearchResponseVO.getListingId()).getLikesCount()));
				}
			}
			
			if (null != hmListingReviewsData && hmListingReviewsData.size() >= 0){
				if (hmListingReviewsData.containsKey(listingSearchResponseVO.getListingId())){
					listing.setReviewsCount(Integer.toString(hmListingReviewsData.get(listingSearchResponseVO.getListingId())));
				}
			}
			
			alListingsData.add(listing);
		}
		
		listingSearchResponse.setListingsData(alListingsData);
		
		return;
		
	}
	
	private void populateListingSearchLoadReviewsResponse(WSSearchListingResponse listingSearchResponse, List<ListingReviewsData> alListingReviewsData){
		
		List<WSReview> reviewData = new ArrayList<WSReview>();
		
		for (ListingReviewsData listingReviewsData : alListingReviewsData){
			 
			WSReview review = new WSReview();
			review.setUserName(listingReviewsData.getUsername());
			review.setReviewText(listingReviewsData.getReviewText());
			review.setUserPicturePath(listingReviewsData.getUserPicturePath());
			
			reviewData.add(review);
		}
		
		listingSearchResponse.setReviewData(reviewData);
		
	}
	
	private UserFavouritesData populateUserFavouriteDAOData(WSSearchListingRequest listingSearchRequest){
		
		UserFavouritesData userFavouritesData = new UserFavouritesData();
		
		userFavouritesData.setUsername(listingSearchRequest.getUsername());
		
		if (EFavouriteGroupTypes.valueOf(listingSearchRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.CATEGORIES)){
			userFavouritesData.setCategoriesList(listingSearchRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(listingSearchRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.BRANDS)){
			userFavouritesData.setBrandsList(listingSearchRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(listingSearchRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.PLACES)){
			userFavouritesData.setPlacesList(listingSearchRequest.getKeyword());
		}
		else {
			userFavouritesData.setPlacesList(listingSearchRequest.getKeyword());
		}
		
		return userFavouritesData;
		
	}
	
	public void generateSuccessResponse(WSSearchListingResponse listingSearchResponse){
		
		listingSearchResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
