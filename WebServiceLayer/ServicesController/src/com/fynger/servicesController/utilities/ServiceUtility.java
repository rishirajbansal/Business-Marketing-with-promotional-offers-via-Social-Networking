package com.fynger.servicesController.utilities;

import com.fynger.generic.exception.base.ExceptionDetail;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;


public class ServiceUtility {
	
	public static LoggerManager logger = GenericUtility.getLogger(ServiceUtility.class.getName());
	
	
	public static WSErrorResponse generateErrorResponse(ExceptionDetail exDetail){
		
		WSErrorResponse errorResponse = new WSErrorResponse();
		
		errorResponse.setStatus(exDetail.getStatus());
		errorResponse.setCode(exDetail.getCode());
		errorResponse.setUserMessage(exDetail.getUserMessage());
		errorResponse.setErrorMessage(exDetail.getErrorMessage());
		
		return errorResponse;
		//return new JAXBElement<WSErrorResponse>(null, WSErrorResponse.class, errorResponse);
	}
	
	

}
