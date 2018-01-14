/**
 * 
 */
package com.fynger.servicesController.controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesController.exception.InitException;
import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * @author Rishi
 *
 */
public class ApplicationController extends ServletContainer{


	private static final long serialVersionUID = 1L;

	public static LoggerManager logger = GenericUtility.getLogger(ApplicationController.class.getName());
	
	public String propertiesFilePath = null;
	public String configurationFilePath = null;
	
	InitConfigurator initConfigurator = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		logger.info("Loading the Application...");
		
		super.init(config);
		
		try{
			propertiesFilePath = config.getInitParameter(GenericConstants.PROPERTIES_FILE_PATH);
			configurationFilePath = config.getInitParameter(GenericConstants.CONFIGURATION_FILE_PATH);
			
			/*Call InitConfigurator to initialize prerequisites properties*/
			initConfigurator = new InitConfigurator(propertiesFilePath, configurationFilePath);
			
			boolean flag = initConfigurator.initialize();
			if (!flag){
				logger.error("Application Controller Failed to initialize prerequisites properties from InitConfigurator. System will be terminated.");
				throw new ApplicationException("Application Controller Failed to initialize prerequisites properties from InitConfigurator. System will be terminated.");
			}
			else{
				logger.info("Application Controller initialized prerequisites properties successfully from InitConfigurator.");
			}
		}
		catch(InitException iEx){
			logger.fatal("Problem occurred during initializing the application. Problem occurred in InitConfigurator." + iEx.getMessage());
			logger.fatal("System is terminated.");
			System.exit(1);
		}
		catch (ApplicationException aEx){
			logger.fatal("Problem occurred during initializing the application." + aEx.getMessage());
			logger.fatal("System is terminated.");
			System.exit(1);
		}
		catch(ContainerException cEx){
			logger.fatal("Problem occurred during initializing the application." + cEx.getMessage());
			logger.fatal("System is terminated.");
			System.exit(1);
		}
		catch(Exception ex){
			logger.fatal("Problem occurred during initializing the application." + ex.getMessage());
			logger.fatal("System is terminated.");
			System.exit(1);
		}
		catch(Throwable th){
			logger.fatal("Problem occurred during initializing the application." + th.getMessage());
			logger.fatal("System is terminated.");
			System.exit(1);
		}
		
		logger.info("Loading of Application is done.");
	}
	
	@Override
	public void destroy(){
		logger.info("In Destroy: Clearing the resources and terminating Business threads & openfire server...");
		initConfigurator.terminate();
		logger.info("In Destroy: Clearing the resources and terminating Business threads & openfire server is done.");
	}

}
