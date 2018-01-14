/**
 * 
 */
package com.fynger.servicesController.controller;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.exception.PropertyManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.notificationEngine.engine.NotificationEngine;
import com.fynger.servicesBusiness.business.base.ActionFactory;
import com.fynger.servicesBusiness.business.initialize.MailConfiguration;
import com.fynger.servicesBusiness.business.initialize.ShoutGroupLoader;
import com.fynger.servicesBusiness.business.threadManager.ThreadManager;
import com.fynger.servicesBusiness.exception.ActionException;
import com.fynger.servicesBusiness.exception.MailConfigurationException;
import com.fynger.servicesBusiness.exception.OpenfireInitializationException;
import com.fynger.servicesBusiness.exception.ShoutGroupLoaderException;
import com.fynger.servicesBusiness.exception.ThreadManagerException;
import com.fynger.servicesBusiness.integration.openfire.OpenfireConnectionManager;
import com.fynger.servicesController.exception.InitException;

/**
 * @author Rishi
 *
 */
public class InitConfigurator {
	
	public static LoggerManager logger = GenericUtility.getLogger(InitConfigurator.class.getName());
	
	public String propertiesFilePath = null;
	public String configurationFilePath = null;
	
	PropertyManager propertyManager = null;
	
	public InitConfigurator(String propertiesFilePath, String configurationFilePath){
		this.propertiesFilePath = propertiesFilePath;
		this.configurationFilePath = configurationFilePath;
	}
	
	public boolean initialize() throws InitException{
		
		boolean flag = true;
		
		try{
			if (!GenericUtility.safeTrim(propertiesFilePath).equals(GenericConstants.EMPTY_STRING)){
				
				if (propertiesFilePath.lastIndexOf(GenericConstants.FILE_SEPARATOR) != propertiesFilePath.length() - 1){
					propertiesFilePath = propertiesFilePath + GenericConstants.FILE_SEPARATOR;
				}
				
				/*Load common properties file in memory */
				propertyManager = PropertyManager.createInstance(propertiesFilePath);
				if (propertyManager == null) {
					logger.error("InitConfigurator Failed to load the common properties file.");
					throw new InitException("InitConfigurator Failed to load the common properties file.");
				}
				else {
					logger.info("InitConfigurator loaded the common properties file successfully in memory.");
				}
				
				/* Initialize Database's DataSource & Pool Manager */
				DatabaseConnectionManager.instantiate();
				boolean status = DatabaseConnectionManager.testDBConnection();
				if (status){
					logger.info("Database connections & pooling are configured successfully. Test Passed.");
				}
				else{
					logger.info("Database connections & pooling are FAILED to be configured successfully. Test FAILED.");
					throw new InitException("Database connections & pooling are FAILED to be configured successfully. Test FAILED.");
				}
				
				/* Load Action factory */
				ActionFactory.instantiate();
				logger.info("Action Factory loaded the Action Directory successfully.");
				
				/* Load Email Configuration details */
				MailConfiguration mailConfiguration = MailConfiguration.createInstance();
				mailConfiguration.configure();
				logger.info("Email Configuration details are loaded successfully.");
				
				/* Load Notification Engine Configuration details */
				NotificationEngine notificationEngine = NotificationEngine.createInstance();
				notificationEngine.configure();
				logger.info("Notification Configuration details are loaded successfully.");
				
				/* Load Shout Group geographic details */
				ShoutGroupLoader shoutGroupLoader = ShoutGroupLoader.createInstance();
				shoutGroupLoader.loadShoutGroupGeoDetails();
				logger.info("Shout Group Geographice details are loaded successfully.");
				
				/* Start, Initialize the openfire server */
				OpenfireConnectionManager openfireConnectionManager = OpenfireConnectionManager.createInstance(configurationFilePath);
				openfireConnectionManager.initialize();
				logger.info("Openfire sever started successfully.");
				
				/* Call Thread Manager to spawn business threads */
				try{
					logger.info("Calling Thread Manager to spawn business threads...");
					ThreadManager threadManager = ThreadManager.createInstance();
					threadManager.igniteThreads();
				}
				catch(ThreadManagerException tmEx){
					logger.error("ThreadManagerException occurred while spawning Business Threads : "  + tmEx.getMessage());
					throw new InitException("ThreadManagerException occurred while spawning Business Threads : "  + tmEx.getMessage());
				}
				logger.info("Thread Manager spawned the business threads successfully.");
				
			}
			else{
				throw new InitException("Properties file path is empty. Init Configurator failed.");
			}
			
		}
		catch(ShoutGroupLoaderException sglEx){
			logger.debug("ShoutGroupLoaderException occurred during loading Shout Group Geo details. Exception is : " + sglEx.getMessage());
			flag = false;
		}
		catch(MailConfigurationException mcEx){
			logger.debug("MailConfigurationException occurred during initializing Mail configuration details. Exception is : " + mcEx.getMessage());
			flag = false;
		}
		catch(PropertyManagerException pmEx){
			logger.error("PropertyManagerException occurred in initializing the application : " + pmEx.getMessage());
			flag = false;
		}
		catch(DatabaseConnectionManagerException dcmEx){
			logger.error("DatabaseConnectionManagerException occurred in initializing the application : " + dcmEx.getMessage());
			flag = false;
		}
		catch(OpenfireInitializationException openfireEx){
			logger.error("OpenfireConnectionManager occurred in initializing the application : " + openfireEx.getMessage());
			flag = false;
		}
		catch(ActionException aEx){
			logger.error("ActionException occurred in initializing the application : " + aEx.getMessage());
			flag = false;
		}
		
		return flag;
		
	}
	
	public void terminate(){
		try{
			logger.info("Terminating Business Threads...");
			ThreadManager threadManager = ThreadManager.createInstance();
			threadManager.terminateThreadManager();
			logger.info("Business Threads terminated.");
			
			logger.info("Terminating openfire server...");
			OpenfireConnectionManager openfireConnectionManager = OpenfireConnectionManager.getInstance();
			openfireConnectionManager.terminate();
			logger.info("openfire server terminated.");
		}
		catch(Exception ex){
			logger.error("Exception occurred while terminating Business threads : " + ex.getMessage());
			throw new ThreadManagerException("Exception occurred while terminating Business threads : " + ex.getMessage());
		}
	}

}
