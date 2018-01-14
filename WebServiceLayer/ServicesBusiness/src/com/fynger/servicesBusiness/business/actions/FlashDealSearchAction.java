/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.LocationNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.handlers.SearchQueryHandler;
import com.fynger.searchEngine.requests.vo.FlashDealSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FlashDealSearchResponseVO;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FlashDealException;
import com.fynger.servicesBusiness.exception.NoFlashDealRespondedResultsFoundException;
import com.fynger.servicesBusiness.exception.NoSearchResultsFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFlashDealSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealDetailsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRequestData;
import com.fynger.servicesBusiness.integration.searchEngine.facade.SearchFacade;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.FlashDealSearchValidator;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSSearchFlashDealRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchFlashDealResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSFlashDeal;

/**
 * @author Rishi
 *
 */
public class FlashDealSearchAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(FlashDealSearchAction.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String flashDealValidityDiff;
	private static String maxPageResults;
	
	DAOFactory daoFactory = null;
	
	public FlashDealSearchAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	
	static{
		flashDealValidityDiff = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FLASHDEAL_VALIDITY_DIFF);
		maxPageResults = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.MAX_PAGE_RESULTS);
	}
	

	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSSearchFlashDealRequest wsSearchFlashDealRequest = (WSSearchFlashDealRequest)request;
		
		WSSearchFlashDealResponse wsSearchFlashDealResponse = new WSSearchFlashDealResponse();
		
		try{
			switch(requestType){
			
				case SEARCH_FLASHDEAL_REQUEST: 
					flashDealRequest(wsSearchFlashDealRequest, wsSearchFlashDealResponse, requestType);
					break;
					
				case SEARCH_FLASHDEAL_RESULTS_LIST: 
					searchResultsList(wsSearchFlashDealRequest, wsSearchFlashDealResponse, requestType);
					break;
					
				case SEARCH_FLASHDEAL_DETAIL: 
					flashDealDetail(wsSearchFlashDealRequest, wsSearchFlashDealResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Flash Deal Search service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(GeoCoordinatesNotFoundException gcnfEx){
			logger.debug("GeoCoordinatesNotFoundException recevied from Search Engine : " + gcnfEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_GEO_COORDINATES_NOT_FOUND, ExceptionConstants.USERMESSAGE_GEO_COORDINATES_NOT_FOUND, gcnfEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(LocationNotFoundException lnfEx){
			logger.debug("LocationNotFoundException recevied from Search Engine : " + lnfEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_LOCATION_NOT_FOUND, ExceptionConstants.USERMESSAGE_LOCATION_NOT_FOUND, lnfEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(SearchEngineException searchEx){
			logger.debug("SearchEngineException recevied from Search Engine : " + searchEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SEARCH_ENGINE_EXCEPTION, ExceptionConstants.USERMESSAGE_SEARCH_ENGINE_EXCEPTION, searchEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FlashDealException fdEx){
			logger.debug("FlashDealException occurred : " + fdEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FLASHDEAL_EXCEPTION, ExceptionConstants.USERMESSAGE_FLASHDEAL_EXCEPTION, fdEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoFlashDealRespondedResultsFoundException noResponseEx){
			logger.debug("FlashDealException occurred as no responded results found for flash deal : " + noResponseEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND, ExceptionConstants.USERMESSAGE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND, ExceptionConstants.ERRORMESSAGE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoSearchResultsFoundException nsrfEx){
			logger.debug("NoSearchResultsFoundException occurred as Search engine returned no results.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.USERMESSAGE_NO_SEARCH_RESULTS_FOUND, ExceptionConstants.ERRORMESSAGE_NO_SEARCH_RESULTS_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PageCountExceededException pceEx){
			logger.debug("PageCountExceededException occurred as page no. count is exceeded.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PAGE_COUNT_EXCEEDED_EXCEPTION, ExceptionConstants.USERMESSAGE_PAGE_COUNT_EXCEEDED_EXCEPTION, pceEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
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
			
		return wsSearchFlashDealResponse;
		
	}
	
	public void flashDealRequest(WSSearchFlashDealRequest wsSearchFlashDealRequest, WSSearchFlashDealResponse wsSearchFlashDealResponse, ERequestType requestType){
		
		IBusinessValidator validator = new FlashDealSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFlashDealRequest, requestType);
		
		if (isValid){
			
			IUserDAO userDao = daoFactory.getUserDAO();

			double latitude = GenericUtility.safeTrim(wsSearchFlashDealRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchFlashDealRequest.getLatitude());
			double longitude = GenericUtility.safeTrim(wsSearchFlashDealRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchFlashDealRequest.getLongitude());
			
			/* Verify if the request is containing coordinates */
			if (latitude != 0 && longitude != 0){
				wsSearchFlashDealRequest.setLocationCoordinates(wsSearchFlashDealRequest.getLatitude() + "|" + wsSearchFlashDealRequest.getLongitude());
				
				/* Update the coordinates in User login table */
				
				boolean flag = userDao.updateUserLocationCoordinates(wsSearchFlashDealRequest.getLocationCoordinates(), wsSearchFlashDealRequest.getUsername());
				
				if (flag){
					logger.debug("User coordinates updated successfully in the database for username : " + wsSearchFlashDealRequest.getUsername());
				}
				else{
					logger.error("User coordinates failed to update successfully in the database for username : " + wsSearchFlashDealRequest.getUsername());
				}
			}
			else{
				/* Fetch user coordinates from the database */
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchFlashDealRequest.getUsername());
				
				String[] coordArr = locationCoordinates.split("\\|");
				wsSearchFlashDealRequest.setLatitude(coordArr[0]);
				wsSearchFlashDealRequest.setLongitude(coordArr[1]);
			}
			
			
			/* Fetch the search results from Search Engine */

			SearchQueryHandler queryHandler = SearchFacade.getFlashDealSearchHandler();
			
			FlashDealSearchRequestVO requestVO = populateSearchQueryHandlerRequest(wsSearchFlashDealRequest);
			
			queryHandler.setQueryType(SearchQueryTypes.FlashDeal_resultset);
			
			queryHandler.setQueryRequest(requestVO);
			
			queryHandler.handleQuery();
			
			List<BaseResponseVO> alFlashDealResultSet = queryHandler.getQueryResponseResultSet();
			
			if (null != alFlashDealResultSet && alFlashDealResultSet.size() > 0){
				List<FlashDealRequestData> alFlashDealData = populateFlashDealRequestDAOData(alFlashDealResultSet, wsSearchFlashDealRequest);
				
				IFlashDealSearchDAO flashDealSearchDAO = daoFactory.getFlashDealSearchDAO();
				
				boolean flag = flashDealSearchDAO.storeFlashDealRequests(alFlashDealData);
				
				if (flag){
					logger.debug("Flash Deal Requests are stored successfully in the database.");
					
					generateSuccessResponse(wsSearchFlashDealResponse);
				}
				else{
					logger.debug("Flash Deal Requests are failed to store successfully in the database.");
					throw new BusinessException("Flash Deal Requests are failed to store successfully in the database.");
				}
			}
			else{
				logger.debug("Search Engine returned blank response for Flash Deal Search.");
				throw new NoSearchResultsFoundException("Search Engine returned blank response for Flash Deal Search.");
			}
			
		}
		else{
			logger.debug("Flash Deal Search - Request validations failed.");
			throw new BusinessException("Flash Deal Search - Request validations failed.");
		}
	}
	
	public void searchResultsList(WSSearchFlashDealRequest wsSearchFlashDealRequest, WSSearchFlashDealResponse wsSearchFlashDealResponse, ERequestType requestType){
		
		IBusinessValidator validator = new FlashDealSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFlashDealRequest, requestType);
		
		if (isValid){
			IFlashDealSearchDAO flashDealSearchDAO = daoFactory.getFlashDealSearchDAO();
			
			List<FlashDealDetailsData> alFlashDealDetailsData = flashDealSearchDAO.loadFlashDealResultsList(wsSearchFlashDealRequest.getUsername(), Integer.parseInt(flashDealValidityDiff), wsSearchFlashDealRequest.getPageCount(), Integer.parseInt(maxPageResults));
			
			if (null != alFlashDealDetailsData && alFlashDealDetailsData.size() > 0){
				
				/* Fetch user coordinates from the database to calculate distance */
				IUserDAO userDao = daoFactory.getUserDAO();
				/* Fetch user coordinates from the database */
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchFlashDealRequest.getUsername());
				
				if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
					String[] coordArr = locationCoordinates.split("\\|");
					wsSearchFlashDealRequest.setLatitude(coordArr[0]);
					wsSearchFlashDealRequest.setLongitude(coordArr[1]);
				}
				
				populateFlashDealRespondedResults(wsSearchFlashDealRequest, wsSearchFlashDealResponse, alFlashDealDetailsData);
				
			}
			else{
				logger.debug("No flash deal responded results found in the database");
				throw new NoFlashDealRespondedResultsFoundException("No flash deal responded results found in the database");
			}
		}
		else{
			logger.debug("Flash Deal Search - Search Results List validations failed.");
			throw new BusinessException("Flash Deal Search - Search Results List validations failed.");
		}
		
	}
	
	public void flashDealDetail(WSSearchFlashDealRequest wsSearchFlashDealRequest, WSSearchFlashDealResponse wsSearchFlashDealResponse, ERequestType requestType){
		
		IBusinessValidator validator = new FlashDealSearchValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsSearchFlashDealRequest, requestType);
		
		if (isValid){
			IFlashDealSearchDAO flashDealSearchDAO = daoFactory.getFlashDealSearchDAO();
			
			FlashDealDetailsData daoData = flashDealSearchDAO.loadFlashDealDetail(wsSearchFlashDealRequest.getFlashDealId());
			
			if (null != daoData){
				
				/* Fetch user coordinates from the database to calculate distance */
				IUserDAO userDao = daoFactory.getUserDAO();
				String locationCoordinates = userDao.fetchUserLocationCoordinates(wsSearchFlashDealRequest.getUsername());
				String[] coordArr = locationCoordinates.split("\\|");
				wsSearchFlashDealRequest.setLatitude(coordArr[0]);
				wsSearchFlashDealRequest.setLongitude(coordArr[1]);
				
				populateFlashDealDetails(wsSearchFlashDealRequest, wsSearchFlashDealResponse, daoData);
			}
			else{
				logger.debug("Failed to load Flash Deal data details from the database.");
				throw new FlashDealException("Failed to load Flash Deal data details from the database.");
			}
			
		}
		else{
			logger.debug("Flash Deal Search - Detail validations failed.");
			throw new BusinessException("Flash Deal Search - Detail validations failed.");
		}
		
	}
	
	private FlashDealSearchRequestVO populateSearchQueryHandlerRequest(WSSearchFlashDealRequest wsSearchFlashDealRequest){
		
		FlashDealSearchRequestVO requestVO = new FlashDealSearchRequestVO();
		
		requestVO.setKeyword(wsSearchFlashDealRequest.getSearchText());
		requestVO.setLatitude(GenericUtility.safeTrim(wsSearchFlashDealRequest.getLatitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchFlashDealRequest.getLatitude()));
		requestVO.setLongitude(GenericUtility.safeTrim(wsSearchFlashDealRequest.getLongitude()).equals(GenericConstants.EMPTY_STRING) ? 0.0 : Double.parseDouble(wsSearchFlashDealRequest.getLongitude()));
		requestVO.setLocation(wsSearchFlashDealRequest.getLocationString());
		requestVO.setResultSetCount(wsSearchFlashDealRequest.getPageCount() <= 0 ? 1: wsSearchFlashDealRequest.getPageCount());
		
		return requestVO;
	}
	
	private List<FlashDealRequestData> populateFlashDealRequestDAOData(List<BaseResponseVO> alFlashDealResultSet, WSSearchFlashDealRequest wsSearchFlashDealRequest){
		
		FlashDealSearchResponseVO flashDealSearchResponseVO = null;
		
		List<FlashDealRequestData> alFlashDealRequestData = new ArrayList<FlashDealRequestData>();
		
		for (BaseResponseVO responseVO : alFlashDealResultSet){
			flashDealSearchResponseVO = (FlashDealSearchResponseVO)responseVO;
			
			FlashDealRequestData flashDealRequestData = new FlashDealRequestData();
			
			flashDealRequestData.setMerchantId(flashDealSearchResponseVO.getMerchantId());
			flashDealRequestData.setRequestLocation(flashDealSearchResponseVO.getLocationString());
			flashDealRequestData.setUsername(wsSearchFlashDealRequest.getUsername());
			flashDealRequestData.setRequestCategory(wsSearchFlashDealRequest.getSearchText());
			flashDealRequestData.setHasResponded(DAOConstants.FLASHDEAL_REQUEST_RESPONDED_NO);
			flashDealRequestData.setStatus(DAOConstants.FLASHDEAL_REQUEST_STATUS_ACTIVE);
			flashDealRequestData.setRequestExpiryPeriod(Integer.parseInt(wsSearchFlashDealRequest.getExpiryPeriod()));
			flashDealRequestData.setStoreId(flashDealSearchResponseVO.getStoreId());
			
			alFlashDealRequestData.add(flashDealRequestData);
		}
		
		return alFlashDealRequestData;
	}
	
	public void populateFlashDealRespondedResults(WSSearchFlashDealRequest wsSearchFlashDealRequest, WSSearchFlashDealResponse wsSearchFlashDealResponse, List<FlashDealDetailsData> alFlashDealDetailsData){
		
		List<WSFlashDeal> flashDealsData = new ArrayList<WSFlashDeal>();
		
		int totalResults = 0;
		
		for (FlashDealDetailsData daoData : alFlashDealDetailsData){
			
			WSFlashDeal wsFlashDeal = new WSFlashDeal();
			
			wsFlashDeal.setFlashDealId(Integer.toString(daoData.getFlashDealId()));
			wsFlashDeal.setName(daoData.getName());
			wsFlashDeal.setAddress(daoData.getAddress());
			wsFlashDeal.setFlashDealText(daoData.getFlashDealText());
			wsFlashDeal.setLatitude(Double.toString(daoData.getLatitude()));
			wsFlashDeal.setLongitude(Double.toString(daoData.getLongitude()));
			
			/* Calculate distance between the source & destination coordinates */
			if ((Double.parseDouble(wsSearchFlashDealRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchFlashDealRequest.getLongitude()) != 0)){
				double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchFlashDealRequest.getLatitude()), Double.parseDouble(wsSearchFlashDealRequest.getLongitude()), daoData.getLatitude(), daoData.getLongitude());
				wsFlashDeal.setDistance(Double.toString(distance));
			}
			
			totalResults = daoData.getTotalResults();
			
			flashDealsData.add(wsFlashDeal);
	
		}
		
		wsSearchFlashDealResponse.setFlashDealsData(flashDealsData);
		wsSearchFlashDealResponse.setTotalResults(Integer.toString(totalResults));
		wsSearchFlashDealResponse.setTotalPages(Integer.toString(getTotalPages(totalResults, Integer.parseInt(maxPageResults))));
		
	}
	
	public void populateFlashDealDetails(WSSearchFlashDealRequest wsSearchFlashDealRequest, WSSearchFlashDealResponse wsSearchFlashDealResponse, FlashDealDetailsData daoData){
		
		WSFlashDeal wsFlashDeal = new WSFlashDeal();
		
		wsFlashDeal.setName(daoData.getName());
		wsFlashDeal.setAddress(daoData.getAddress());
		wsFlashDeal.setLatitude(Double.toString(daoData.getLatitude()));
		wsFlashDeal.setLongitude(Double.toString(daoData.getLongitude()));
		wsFlashDeal.setFlashDealText(daoData.getFlashDealText());
		wsFlashDeal.setCity(daoData.getCity());
		wsFlashDeal.setState(daoData.getState());
		wsFlashDeal.setPhone(daoData.getPhone());
		wsFlashDeal.setCategories(daoData.getCategories());
		
		/* Calculate distance between the source & destination coordinates */
		if ((Double.parseDouble(wsSearchFlashDealRequest.getLatitude()) != 0) && (Double.parseDouble(wsSearchFlashDealRequest.getLongitude()) != 0)){
			double distance = Utility.calculateLocationCoordinatesDistance(Double.parseDouble(wsSearchFlashDealRequest.getLatitude()), Double.parseDouble(wsSearchFlashDealRequest.getLongitude()), daoData.getLatitude(), daoData.getLongitude());
			wsFlashDeal.setDistance(Double.toString(distance));
		}
		
		wsSearchFlashDealResponse.setFlashDealDetail(wsFlashDeal);
		
	}
	
	 public int getTotalPages(int totalResults, int maxPageResults) {

        int totalPages = totalResults / maxPageResults;

        if (!(totalResults % maxPageResults == 0)) {
            totalPages++;
        }

        return totalPages;
    }
	
	public void generateSuccessResponse(WSSearchFlashDealResponse wsSearchFlashDealResponse){
		
		wsSearchFlashDealResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
