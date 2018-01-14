/**
 * 
 */
package com.fynger.servicesBusiness.business.threadManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.handlers.SearchQueryHandler;
import com.fynger.searchEngine.requests.vo.FavouritesSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FavouritesSearchResponseVO;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FavouritesException;
import com.fynger.servicesBusiness.exception.NoFavouritesFoundException;
import com.fynger.servicesBusiness.exception.RefreshUserFavouritesSearchResultsThreadException;
import com.fynger.servicesBusiness.exception.UserNameNotFoundException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FavouritesSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFavouritesSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserFavouritesData;
import com.fynger.servicesBusiness.integration.searchEngine.facade.SearchFacade;

/**
 * @author Rishi
 *
 */
public class RefreshUserFavouritesSearchResults extends Thread {
	
	public static LoggerManager logger = GenericUtility.getLogger(RefreshUserFavouritesSearchResults.class.getName());
	
	private final static String THREAD_NAME = "RefreshFavSearchResults";
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String userFavouritesRefreshFrequency;
	private static long longUserFavouritesRefreshFrequency;
	
	private boolean runFlag = true;
	
	private String username;
	
	DAOFactory daoFactory = null;
	
	private String status = "";
	
	
	static{
		userFavouritesRefreshFrequency = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.USER_FAVOURITES_REFRESH_FREQUENCY);
		longUserFavouritesRefreshFrequency = Long.parseLong(userFavouritesRefreshFrequency) * 60 * 1000;
	}
	
	public RefreshUserFavouritesSearchResults(String username) {
		super(THREAD_NAME + ">>" + username);
		this.username = username;
		
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	
	public void run(){
		
		while(runFlag){
			
			synchronized (this) {
				logger.info(" ~^~^~^~^~^~ Time to Refresh User Favourite Search Results ~^~^~^~^~^~");
				logger.info(" User Favourite Search Results Refresh frequency :- " + longUserFavouritesRefreshFrequency);
				
				refreshResults();
				
				try{
					Calendar now=Calendar.getInstance();
					int nowTimeHH	=	now.get(Calendar.HOUR_OF_DAY);
					int nowTimeMM	=	now.get(Calendar.MINUTE);
					int nowTimeSS	=	now.get(Calendar.SECOND);
					
					logger.info(" ~^~^~^~^~^~ User Favourite Search Results refreshed with Current Time:- "+nowTimeHH+":"+nowTimeMM+":"+nowTimeSS+"  ~^~^~^~^~^~ ");
					
					//Make the thread sleep for the configurable time interval
					//Thread.sleep(longUserFavouritesRefreshFrequency);
					wait(longUserFavouritesRefreshFrequency);
				}
				catch(InterruptedException iEx){
					logger.error("InterruptedException occurred while running the User Favourite Search Results Refresh Thread : " + iEx.getMessage());
					//runFlag = false;
					//throw new RefreshUserFavouritesSearchResultsThreadException("InterruptedException occurred while running the User Favourite Search Results Refresh Thread : " + iEx.getMessage());
					
				}
				catch(Exception ex){
					logger.error("Exception occurred while running the User Favourite Search Results Refresh Thread : " + ex.getMessage());
					runFlag = false;
					throw new RefreshUserFavouritesSearchResultsThreadException("Exception occurred while running the User Favourite Search Results Refresh Thread : " + ex.getMessage());
				}
			}
		}
	}
	
	public void refreshResults(){
		
		try{
			SearchQueryHandler queryHandler = SearchFacade.getFavouritesSearchHandler();
			
			FavouritesSearchRequestVO requestVO = populateSearchQueryHandlerRequest();
			
			if (null != requestVO){
				/* Call search engine only if any favourite keywords exists for the username */
				queryHandler.setQueryType(SearchQueryTypes.Favourite_resultset);
				
				queryHandler.setQueryRequest(requestVO);
				
				queryHandler.handleQuery();
				
				List<BaseResponseVO> alFavouritesSearchResultSet = queryHandler.getQueryResponseResultSet();
				
				if (null != alFavouritesSearchResultSet && alFavouritesSearchResultSet.size() > 0){
					
					String promotionIds = populateFavouritesResultsDAOData(alFavouritesSearchResultSet);
					
					IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
					
					boolean flag = favouritesSearchDAO.updateUserFavouritePromotions(promotionIds, this.username);
					
					if (flag){
						logger.debug("USER FAVOURITES PROMOTIONS is updated successfully");
					}
					else{
						logger.error("USER FAVOURITES PROMOTIONS is FAILED to update successfully");
						throw new FavouritesException("USER FAVOURITES PROMOTIONS is FAILED to update successfully");
					}
					
				}
				else{
					logger.debug("Search Engine returned blank response for Favourites search.");
					//throw new NoSearchResultsFoundException("Search Engine returned blank response for Flash Deal Search.");
				}
			}
			
		}
		catch(SearchEngineException searchEx){
			logger.error("SearchEngineException recevied from Search Engine : " + searchEx.getMessage());
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SEARCH_ENGINE_EXCEPTION, ExceptionConstants.USERMESSAGE_SEARCH_ENGINE_EXCEPTION, searchEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoFavouritesFoundException nffEx){
			logger.debug("NoFavouritesFoundException occurred : " + nffEx.getMessage());
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NO_FAVOURITES_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_NO_FAVOURITES_FOUND_EXCEPTION, nffEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(UserNameNotFoundException unnfEx){
			logger.debug("UserNameNotFoundException occurred in DAO layer as user name not found in the database");
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_USERNAME_NOT_FOUND, ExceptionConstants.USERMESSAGE_USERNAME_NOT_FOUND, ExceptionConstants.ERRORMESSAGE_USERNAME_NOT_FOUND, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch (FavouritesException fEx){
			logger.debug("FavouritesException occurred : " + fEx.getMessage());
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FAVOURITES_EXCEPTION, ExceptionConstants.USERMESSAGE_FAVOURITES_EXCEPTION, fEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_DATA_ACCESS_EXCEPTION, ExceptionConstants.USERMESSAGE_DATA_ACCESS_EXCEPTION, daEx.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.toString());
			//throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			///throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
	}
	
	public FavouritesSearchRequestVO populateSearchQueryHandlerRequest(){
		
		double latitude;
		double longitude;
		
		FavouritesSearchRequestVO requestVO = new FavouritesSearchRequestVO();
		
		/* Fetch the user coordinates*/
		
		IUserDAO userDao = daoFactory.getUserDAO();
		
		String locationCoordinates = userDao.fetchUserLocationCoordinates(this.username);
		
		if (!GenericUtility.safeTrim(locationCoordinates).equals(GenericConstants.EMPTY_STRING)){
			String[] coordArr = locationCoordinates.split("\\|");
			latitude = Double.parseDouble(coordArr[0]);
			longitude = Double.parseDouble(coordArr[1]);
			
			requestVO.setLatitude(latitude);
			requestVO.setLongitude(longitude);
		}	
		
		/* Fetch User favourites data */
		
		IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
		
		UserFavouritesData userFavouritesData = favouritesSearchDAO.listFavourites(this.username);
		
		if (null != userFavouritesData){
			populateListFavouritesData(requestVO, userFavouritesData);
		}
		else{
			logger.debug("No favourites found for the username : " + this.username);
			//throw new NoFavouritesFoundException("No favourites found for the username : " + this.username);
		}
		
		return requestVO;
	}
	
	private void populateListFavouritesData(FavouritesSearchRequestVO requestVO, UserFavouritesData userFavouritesData){
		
		String categories = userFavouritesData.getCategoriesList();
		String brands = userFavouritesData.getBrandsList();
		String places = userFavouritesData.getPlacesList();
		
		logger.debug("Favourites list of user : " + this.username);
		logger.debug("Categories : " + categories);
		logger.debug("Brands : " + brands);
		logger.debug("Places : " + places);
		
		if (!GenericUtility.safeTrim(categories).equals(GenericConstants.EMPTY_STRING)){
			String[] str = categories.split("\\|");
			List<String> alCategories = Arrays.asList(str);
			requestVO.setCategories(alCategories);
		}
		if (!GenericUtility.safeTrim(brands).equals(GenericConstants.EMPTY_STRING)){
			String[] str = brands.split("\\|");
			List<String> alBrands = Arrays.asList(str);
			requestVO.setBrands(alBrands);
		}
		
		if (!GenericUtility.safeTrim(places).equals(GenericConstants.EMPTY_STRING)){
			String[] str = places.split("\\|");
			List<String> alPlaces = Arrays.asList(str);
			requestVO.setPlaces(alPlaces);
		}
				
	}
	
	private String populateFavouritesResultsDAOData(List<BaseResponseVO> alFavouritesSearchResultSet){
		
		FavouritesSearchResponseVO favouritesResponseVO = null;
		
		StringBuffer consolidatedPromotionIds = new StringBuffer();
		
		for (BaseResponseVO searchResponseVO : alFavouritesSearchResultSet){
			favouritesResponseVO = (FavouritesSearchResponseVO)searchResponseVO;
			
			consolidatedPromotionIds.append(favouritesResponseVO.getPromotionId()).append("|");
		}
		if (consolidatedPromotionIds.toString().endsWith("|")) {
			consolidatedPromotionIds = new StringBuffer((consolidatedPromotionIds.substring(0, consolidatedPromotionIds.lastIndexOf("|"))));
		}
		
		return consolidatedPromotionIds.toString();
	}
	
	public void terminate(){
		this.runFlag = false;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
