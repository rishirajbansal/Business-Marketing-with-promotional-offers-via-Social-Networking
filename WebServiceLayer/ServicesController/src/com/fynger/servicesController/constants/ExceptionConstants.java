/**
 * 
 */
package com.fynger.servicesController.constants;

import javax.ws.rs.core.Response;

/**
 * @author Rishi
 *
 */
public class ExceptionConstants {
	
	public static final String SERVICE_STATUS_SUCCESS							= Integer.toString(Response.Status.OK.getStatusCode());;
	
	public static final String SERVICE_STATUS_APPLICATION_EXCEPTION				= Integer.toString(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	public static final String SERVICE_STATUS_BUSINESS_EXCEPTION				= Integer.toString(Response.Status.BAD_REQUEST.getStatusCode());
	public static final String SERVICE_STATUS_EXCEPTION							= Integer.toString(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	public static final String SERVICE_STATUS_THROWABLE							= Integer.toString(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
	public static final String SERVICE_STATUS_BAD_REQUEST_EXCEPTION				= Integer.toString(Response.Status.BAD_REQUEST.getStatusCode());
	public static final String SERVICE_STATUS_SEARCH_ENGINE_EXCEPTION			= Integer.toString(Response.Status.BAD_REQUEST.getStatusCode());
	
	
	/* Generic Exception/Throwable details */
	public static final String CODE_EXCEPTION = "WS301";
	public static final String USERMESSAGE_EXCEPTION = "There was some problem in processing your request. Please try after some time.";
	
	/* Bad Request Exception details */
	public static final String CODE_BAD_REQUEST_EXCEPTION = "WS302";
	public static final String USERMESSAGE_BAD_REQUEST_1 = "Request is empty.";
	public static final String ERRORMESSAGE_BAD_REQUEST_1 = "Requsest object found null.";
	
	
}
