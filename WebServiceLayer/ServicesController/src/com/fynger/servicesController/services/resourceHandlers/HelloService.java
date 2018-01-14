/**
 * 
 */
package com.fynger.servicesController.services.resourceHandlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;

/**
 * @author Rishi
 *
 */

@Path("/hello")
public class HelloService {
	
	public static LoggerManager logger = GenericUtility.getLogger(HelloService.class.getName());
	
	@GET
	@Produces( {MediaType.APPLICATION_XML, MediaType.TEXT_XML})
	public String sayXMLHello() {
		logger.debug("Hello Web Service is Called.");
		return "<?xml version=\"1.0\"?>" + "<hello> Hello. Welcome to Fynger Web Services." + "</hello>";
	}

}
