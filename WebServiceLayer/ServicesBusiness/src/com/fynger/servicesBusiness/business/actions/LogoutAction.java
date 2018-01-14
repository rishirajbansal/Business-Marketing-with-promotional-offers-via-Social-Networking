/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.business.threadManager.RefreshUserFavouritesSearchResults;
import com.fynger.servicesBusiness.business.threadManager.ThreadManager;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FavouritesSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFavouritesSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.LogoutValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLogoutRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLogoutResponse;

/**
 * @author Rishi
 *
 */
public class LogoutAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(LogoutAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public LogoutAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}
	

	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSUserLogoutRequest wsUserLogoutRequest = (WSUserLogoutRequest)request;
		
		WSUserLogoutResponse wsUserLogoutResponse = new WSUserLogoutResponse();
		
		try{
			switch(requestType){
			
				case USER_LOGOUT: 
					logout(wsUserLogoutRequest, wsUserLogoutResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Logout service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
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
			
		return wsUserLogoutResponse;

	}
	
	public void logout(WSUserLogoutRequest wsUserLogoutRequest, WSUserLogoutResponse wsUserLogoutResponse, ERequestType requestType){
		
		IBusinessValidator validator = new LogoutValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsUserLogoutRequest, requestType);
		
		if (isValid){
			/*
			 * Task 1 : Stop the Refresh User's Favourite Search Results thread
			 */
			logger.info("Calling Thread Manager to terminate Refresh User Favourite search results thread ...");
			ThreadManager threadManager = ThreadManager.createInstance();
			RefreshUserFavouritesSearchResults userThread = threadManager.getRefreshUserFavPromotionsThread(wsUserLogoutRequest.getUsername());
			
			if (null != userThread){
				try{
					userThread.terminate();
					if (userThread.getState().equals(Thread.State.TIMED_WAITING) || userThread.getState().equals(Thread.State.WAITING)){
						synchronized (userThread) {
							userThread.notify();
						}
					}
					userThread.join();
				}
				catch(Exception ex){
					logger.error("An exception occured while terminating user favourite search results thread for user name : " + wsUserLogoutRequest.getUsername());
				}
			}
			else{
				logger.info("NO thread exists for refresh user favourite promotions for username : " + wsUserLogoutRequest.getUsername());
			}
			
			/*
			 * Task 2 : Flush out the User's Favourite Search Results as they will be outdated
			 */
			IFavouritesSearchDAO favouritesSearchDAO = new FavouritesSearchDAOImpl();
			
			boolean flag = favouritesSearchDAO.deleteUserFavouritePromotions(wsUserLogoutRequest.getUsername());
			
			if (flag){
				logger.debug("Search Results are flushed out from USER FAVOURITE PROMOTIONS database table successfully.");
			}
			else{
				logger.debug("No record exists in USER FAVOURITE PROMOTIONS that need to be deleted.");
			}
			
			/*
			 * Task 3 : Clear the User Device Auth details
			 */
			IUserDAO userDao = daoFactory.getUserDAO();
			
			flag = userDao.clearUserDeviceAuthDetails(wsUserLogoutRequest.getUsername());
			
			if (flag){
				logger.debug("User device auth details are flushed out from USER MOBILE AUTH database table successfully.");
			}
			else{
				logger.debug("No record exists in USER MOBILE AUTH that need to be deleted.");
			}
			
			generateSuccessResponse(wsUserLogoutResponse);
		}
		else{
			logger.debug("User Logout validations failed.");
			throw new BusinessException("User Logout validations failed.");
		}
		
	}
	
	public void generateSuccessResponse(WSUserLogoutResponse wsUserLogoutResponse){
		
		wsUserLogoutResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
