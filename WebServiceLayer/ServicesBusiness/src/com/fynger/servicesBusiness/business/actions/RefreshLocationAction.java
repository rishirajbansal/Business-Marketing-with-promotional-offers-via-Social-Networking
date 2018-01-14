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
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.RefreshLocationException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.RefreshLocationValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSUserLocationRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLocationResponse;

/**
 * @author Rishi
 *
 */
public class RefreshLocationAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(RefreshLocationAction.class.getName());
	
	DAOFactory daoFactory = null;
	
	public RefreshLocationAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
	}

	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSUserLocationRequest wsUserLocationRequest = (WSUserLocationRequest)request;
		
		WSUserLocationResponse wsUserLocationResponse = new WSUserLocationResponse();
		
		try{
			switch(requestType){
			
				case USER_REFRESH_LOCATION: 
					refreshLocation(wsUserLocationRequest, wsUserLocationResponse, requestType);
					break;
			
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Refresh Location service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(RefreshLocationException rlEx){
			logger.debug("RefreshLocationException occurred : " + rlEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_REFRESH_LOCATION_EXCEPTION, ExceptionConstants.USERMESSAGE_REFRESH_LOCATION_EXCEPTION, rlEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
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
			
		return wsUserLocationResponse;

	}
	
	public void refreshLocation(WSUserLocationRequest wsUserLocationRequest, WSUserLocationResponse wsUserLocationResponse, ERequestType requestType){
		
		IBusinessValidator validator = new RefreshLocationValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsUserLocationRequest, requestType);
		
		if (isValid){
			/* Update the coordinates in User login table */
			
			IUserDAO userDao = daoFactory.getUserDAO();
			
			wsUserLocationRequest.setLocationCoordinates(wsUserLocationRequest.getLatitude() + "|" + wsUserLocationRequest.getLongitude());
			
			boolean flag = userDao.updateUserLocationCoordinates(wsUserLocationRequest.getLocationCoordinates(), wsUserLocationRequest.getUsername());
			
			if (flag){
				logger.debug("User coordinates updated successfully in the database for username : " + wsUserLocationRequest.getUsername());
				
				generateSuccessResponse(wsUserLocationResponse);
			}
			else{
				logger.error("User coordinates failed to update successfully in the database for username : " + wsUserLocationRequest.getUsername());
				throw new RefreshLocationException("User coordinates failed to update successfully in the database for username : " + wsUserLocationRequest.getUsername());
			}
		}
		else{
			logger.debug("Refresh Location validations failed.");
			throw new BusinessException("Refresh Location validations failed.");
		}
		
	}
		
	public void generateSuccessResponse(WSUserLocationResponse wsUserLocationResponse){
		
		wsUserLocationResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
