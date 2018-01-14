/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
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
import com.fynger.servicesBusiness.exception.DeleteFavouritesException;
import com.fynger.servicesBusiness.exception.FavouriteAlreadyExistsException;
import com.fynger.servicesBusiness.exception.FavouritesException;
import com.fynger.servicesBusiness.exception.NoFavouritesFoundException;
import com.fynger.servicesBusiness.exception.NoFavouritesResultsFoundException;
import com.fynger.servicesBusiness.exception.PromotionIdNotFoundException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FavouritesSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFavouritesSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PromotionData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.FavouritesSearchValidator;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchFavouritesRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchFavouritesResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;

/**
 * @author Rishi
 *
 */
public class FavouritesSearchAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(FavouritesSearchAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public FavouritesSearchAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}

	
	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSSearchFavouritesRequest wsSearchFavouritesRequest = (WSSearchFavouritesRequest)request;
		
		WSSearchFavouritesResponse wsSearchFavouritesResponse = new WSSearchFavouritesResponse();
		
		try{
			switch(requestType){
				case SEARCH_FAVOURITES_ADD: 
					addFavourite(wsSearchFavouritesRequest, wsSearchFavouritesResponse, requestType);
					break;
					
				case SEARCH_FAVOURITES_LIST: 
					listFavourites(wsSearchFavouritesRequest, wsSearchFavouritesResponse, requestType);
					break;
					
				case SEARCH_FAVOURITES_DELETE: 
					deleteFavourite(wsSearchFavouritesRequest, wsSearchFavouritesResponse, requestType);
					break;
					
				case SEARCH_FAVOURITES_RESULTS_LIST: 
					searchResultsList(wsSearchFavouritesRequest, wsSearchFavouritesResponse, requestType);
					break;
					
				case SEARCH_FAVOURITES_DETAIL: 
					favouriteDetail(wsSearchFavouritesRequest, wsSearchFavouritesResponse, requestType);
					break;
					
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Favourites Search service : " + bvEx.getMessage());
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
		catch(AddFavouritesException afEx){
			logger.debug("AddFavouritesException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_ADD_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_ADD_FAVOURITES_EXCEPTION, afEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoFavouritesFoundException nffEx){
			logger.debug("NoFavouritesFoundException occurred : " + nffEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_FAVOURITES_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_NO_FAVOURITES_FOUND_EXCEPTION, nffEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(DeleteFavouritesException dfEx){
			logger.debug("DeleteFavouritesException occurred : " + dfEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_DELETE_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_DELETE_FAVOURITES_EXCEPTION, dfEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch (NoFavouritesResultsFoundException nfrfEx){
			logger.debug("NoFavouritesResultsFoundException occurred : " + nfrfEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_FAVOURITES_RESULTS_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_NO_FAVOURITES_RESULTS_FOUND_EXCEPTION, nfrfEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FavouriteAlreadyExistsException afEx){
			logger.debug("FavouriteAlreadyExistsException occurred : " + afEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.USERMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, ExceptionConstants.ERRORMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch (FavouritesException fEx){
			logger.debug("FavouritesException occurred : " + fEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_FAVOURITES_EXCEPTION, fEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PromotionIdNotFoundException pinfEx){
			logger.debug("PromotionIdNotFoundException occurred in DAO layer as Promotion id not found in the database.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PROMOTION_ID_NOT_FOUND, ExceptionConstants.USERMESSAGE_PROMOTION_ID_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_PROMOTION_ID_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
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
			
		return wsSearchFavouritesResponse;

	}
	
	public void addFavourite(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new FavouritesSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFavouritesRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			UserFavouritesData userFavouritesData = populateUserFavouriteDAOData(wsSearchFavouritesRequest);
			
			boolean flag = favouritesSearchDAO.addFavourite(userFavouritesData);
			
			if (flag){
				logger.debug("Add Favourite request is updated successfully in the database.");
				
				/* Notify the user favourite promotion thread to refresh the results for new favourite */
				logger.debug("Refresh user favourite search results list by invoking the user's thread...");
				ThreadManager threadManager = ThreadManager.createInstance();
				RefreshUserFavouritesSearchResults thread = threadManager.getRefreshUserFavPromotionsThread(wsSearchFavouritesRequest.getUsername());
				
				if (null != thread){
					try{
						synchronized (thread) {
							thread.notify();
						}
					}
					catch(Exception ex){
						logger.error("An exception occured while notifying user favourite search results thread for user name : " + wsSearchFavouritesRequest.getUsername());
						//TODO: This error should be reported
					}
				}
				else{
					logger.error("NO thread exists for refresh user favourite promotions");
					//TODO: This error should be reported
				}
				
				generateSuccessResponse(wsSearchFavouritesResponse);
			}
			else{
				logger.debug("Add Favourite request is FAILED to update successfully in the database.");
				throw new AddFavouritesException("Add Favourite request is FAILED to update successfully in the database.");
			}
		}
		else{
			logger.debug("Favourites Search - Add Favourite validations failed.");
			throw new AddFavouritesException("Favourites Search - Add Favourite validations failed.");
		}
		
	}
	
	public void listFavourites(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new FavouritesSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFavouritesRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			UserFavouritesData userFavouritesData = favouritesSearchDAO.listFavourites(wsSearchFavouritesRequest.getUsername());
			
			if (null != userFavouritesData){
				populateListFavouritesData(wsSearchFavouritesResponse, userFavouritesData);
			}
			else{
				logger.debug("No favourites found for the username : " + wsSearchFavouritesRequest.getUsername());
				throw new NoFavouritesFoundException("No favourites found for the username : " + wsSearchFavouritesRequest.getUsername());
			}
		}
		else{
			logger.debug("Favourites Search - List Favourite validations failed.");
			throw new NoFavouritesFoundException("Favourites Search - List Favourite validations failed.");
		}
		
	}
	
	public void deleteFavourite(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new FavouritesSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFavouritesRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			UserFavouritesData userFavouritesData = populateUserFavouriteDAOData(wsSearchFavouritesRequest);
			
			boolean flag = favouritesSearchDAO.deleteFavourite(userFavouritesData);
			
			if (flag){
				logger.debug("Delete Favourite request is executed successfully in the database.");
				
				/* Notify the user favourite promotion thread to refresh the results for removed favourite */
				logger.debug("Refresh user favourite search results list by invoking the user's thread...");
				ThreadManager threadManager = ThreadManager.createInstance();
				RefreshUserFavouritesSearchResults thread = threadManager.getRefreshUserFavPromotionsThread(wsSearchFavouritesRequest.getUsername());
				
				if (null != thread){
					try{
						synchronized (thread) {
							thread.notify();
						}
					}
					catch(Exception ex){
						logger.error("An exception occured while notifying user favourite search results thread for user name : " + wsSearchFavouritesRequest.getUsername() + " : " + ex.getMessage());
						//TODO: This error should be reported
					}
				}
				else{
					logger.error("NO thread exists for refresh user favourite promotions");
					//TODO: This error should be reported
				}
				
				generateSuccessResponse(wsSearchFavouritesResponse);
			}
			else{
				logger.debug("Delete Favourite request is FAILED to update successfully in the database.");
				throw new DeleteFavouritesException("Delete Favourite request is FAILED to update successfully in the database.");
			}
		}
		else{
			logger.debug("Favourites Search - Delete Favourite validations failed.");
			throw new DeleteFavouritesException("Favourites Search - Delete Favourite validations failed.");
		}
		
	}
	
	public void searchResultsList(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new FavouritesSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFavouritesRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			List<PromotionData> alPromotionsDataList = favouritesSearchDAO.loadFavouritesResultsList(wsSearchFavouritesRequest.getUsername());
			
			if (null != alPromotionsDataList && alPromotionsDataList.size() > 0){
				/* Fetch user coordinates from the database to calculate distance */
				
				IUserDAO userDao = daoFactory.getUserDAO();
				
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchFavouritesRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					wsSearchFavouritesRequest.setLatitude(coordArr[0]);
					wsSearchFavouritesRequest.setLongitude(coordArr[1]);
				}
				populateFavouritesResultsResponse(wsSearchFavouritesRequest, wsSearchFavouritesResponse, alPromotionsDataList);
			}
			else{
				logger.debug("No Favourties results found for username : " + wsSearchFavouritesRequest.getUsername());
				throw new NoFavouritesResultsFoundException("No Favourties results found for username : " + wsSearchFavouritesRequest.getUsername());
			}
		}
		else{
			logger.debug("Favourites Search - Search Results List validations failed.");
			throw new NoFavouritesResultsFoundException("Favourites Search - Search Results List validations failed.");
		}
		
	}
	
	public void favouriteDetail(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new FavouritesSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFavouritesRequest, requestType);
		
		if (isValid){
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			PromotionData promotionData = favouritesSearchDAO.loadFavouritePromotionDetail(wsSearchFavouritesRequest.getPromotionId());
			
			if (null != promotionData){
				
				/* Fetch user coordinates from the database to calculate distance */
				IUserDAO userDao = daoFactory.getUserDAO();

				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchFavouritesRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					wsSearchFavouritesRequest.setLatitude(coordArr[0]);
					wsSearchFavouritesRequest.setLongitude(coordArr[1]);
				}
				
				populateFavouritePromotionDetailResponse(wsSearchFavouritesRequest, wsSearchFavouritesResponse, promotionData);
				
			}
			else{
				logger.debug("Failed to load Promotion details from the database.");
				throw new FavouritesException("Failed to load Promotion details from the database.");
			}
		}
		else{
			logger.debug("Favourites Search - Favourite Promotion Detail validations failed.");
			throw new FavouritesException("Favourites Search - Favourite Promotion Detail validations failed.");
		}
		
	}
	
	private UserFavouritesData populateUserFavouriteDAOData(WSSearchFavouritesRequest wsSearchFavouritesRequest){
		
		UserFavouritesData userFavouritesData = new UserFavouritesData();
		
		userFavouritesData.setUsername(wsSearchFavouritesRequest.getUsername());
		
		if (EFavouriteGroupTypes.valueOf(wsSearchFavouritesRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.CATEGORIES)){
			userFavouritesData.setCategoriesList(wsSearchFavouritesRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(wsSearchFavouritesRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.BRANDS)){
			userFavouritesData.setBrandsList(wsSearchFavouritesRequest.getKeyword());
		}
		else if (EFavouriteGroupTypes.valueOf(wsSearchFavouritesRequest.getGroupType().toUpperCase()).equals(EFavouriteGroupTypes.PLACES)){
			userFavouritesData.setPlacesList(wsSearchFavouritesRequest.getKeyword());
		}
		else {
			logger.debug("Invalid Favourite Group type : " + wsSearchFavouritesRequest.getGroupType() + " Valid group types are : Categories, Places, Brands");
			throw new FavouritesException("Invalid Favourite Group type : " + wsSearchFavouritesRequest.getGroupType() + " Valid group types are : Categories, Places, Brands");
		}
		
		return userFavouritesData;
		
	}
	
	private void populateListFavouritesData(WSSearchFavouritesResponse wsSearchFavouritesResponse, UserFavouritesData userFavouritesData){
		
		String categories = userFavouritesData.getCategoriesList();
		String brands = userFavouritesData.getBrandsList();
		String places = userFavouritesData.getPlacesList();
		
		if (!GenericUtility.safeTrim(categories).equals(GenericConstants.EMPTY_STRING)){
			String[] str = categories.split("\\|");
			List<String> alCategories = Arrays.asList(str);
			wsSearchFavouritesResponse.setCategories(alCategories);
		}
		if (!GenericUtility.safeTrim(brands).equals(GenericConstants.EMPTY_STRING)){
			String[] str = brands.split("\\|");
			List<String> alBrands = Arrays.asList(str);
			wsSearchFavouritesResponse.setBrands(alBrands);
		}
		
		if (!GenericUtility.safeTrim(places).equals(GenericConstants.EMPTY_STRING)){
			String[] str = places.split("\\|");
			List<String> alPlaces = Arrays.asList(str);
			wsSearchFavouritesResponse.setPlaces(alPlaces);
		}
				
	}
	
	private void populateFavouritesResultsResponse(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, List<PromotionData> alPromotionsDataList){
		
		List<WSPromotion> alPromotionsData = new ArrayList<WSPromotion>();
		
		for (PromotionData daoData : alPromotionsDataList){
			
			WSPromotion promotion = new WSPromotion();
			
			promotion.setName(daoData.getName());
			promotion.setAddress(daoData.getAddress());
			promotion.setPromotionText(daoData.getPromotionText());
			promotion.setValidityPeriod(daoData.getValidityPeriod());
			promotion.setFavouritePromotionId(Integer.toString(daoData.getPromotionId()));
			promotion.setLatitude(Double.toString(daoData.getLatitude()));
			promotion.setLongitude(Double.toString(daoData.getLongitude()));
			
			/* Calculate distance between the source & destination coordinates */
			if ((Double.parseDouble(wsSearchFavouritesRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchFavouritesRequest.getLongitude()) != 0)){
				double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchFavouritesRequest.getLatitude()), Double.parseDouble(wsSearchFavouritesRequest.getLongitude()), daoData.getLatitude(), daoData.getLongitude());
				promotion.setDistance(Double.toString(distance));
			}
			
			alPromotionsData.add(promotion);			
		}
		
		wsSearchFavouritesResponse.setPromotionsData(alPromotionsData);
		
	}
	
	private void populateFavouritePromotionDetailResponse(WSSearchFavouritesRequest wsSearchFavouritesRequest, WSSearchFavouritesResponse wsSearchFavouritesResponse, PromotionData promotionData){
		
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
		
		/* Calculate distance between the source & destination coordinates */
		if ((Double.parseDouble(wsSearchFavouritesRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchFavouritesRequest.getLongitude()) != 0)){
			double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchFavouritesRequest.getLatitude()), Double.parseDouble(wsSearchFavouritesRequest.getLongitude()), promotionData.getLatitude(), promotionData.getLongitude());
			promotion.setDistance(Double.toString(distance));
		}
				
		wsSearchFavouritesResponse.setPromotionDetail(promotion);
	}
	
	public void generateSuccessResponse(WSSearchFavouritesResponse wsSearchFavouritesResponse){
		
		wsSearchFavouritesResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
