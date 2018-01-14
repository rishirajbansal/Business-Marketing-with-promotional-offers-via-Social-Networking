/**
 * 
 */
package com.fynger.servicesBusiness.integration.openfire;

import java.util.Collection;

import org.jivesoftware.openfire.ConnectionManager;
import org.jivesoftware.openfire.ServerPort;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.XMPPServerInfo;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.util.Version;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.OpenfireInitializationException;


/**
 * @author Rishi
 *
 */
public class OpenfireConnectionManager {
	
	public static LoggerManager logger = GenericUtility.getLogger(OpenfireConnectionManager.class.getName());
		
	private static OpenfireConnectionManager openfireConnectionManager = null;
	
	private static final String OPENFIRE_HOME_DIRECTORY_SYSTEM_PROPERTY	= "openfireHome";
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	private XMPPServer server;
	
	private OpenfireConnectionManager(String configurationFilePath){
		System.setProperty(OPENFIRE_HOME_DIRECTORY_SYSTEM_PROPERTY, configurationFilePath + System.getProperty("file.separator") + "openfire");
	}

	
	public static OpenfireConnectionManager createInstance(String configurationFilePath) {
		
		if (openfireConnectionManager == null) {
			synchronized (lockObject) {
				openfireConnectionManager = new OpenfireConnectionManager(configurationFilePath);
			}
		}

		return openfireConnectionManager;
	}
	
	public static OpenfireConnectionManager getInstance(){
		return openfireConnectionManager;
	}
	
	public void initialize(){
		
		try{
			logger.info("Initializing, Loading & starting the Openfire server...");
			
			server = new XMPPServer();
			server.finishSetup();
			
			/* Server Info */
			XMPPServerInfo serverInfo = server.getServerInfo();			
			String hostname = serverInfo.getHostname();
			logger.info("Host Name : " + hostname);			
			Version version = serverInfo.getVersion();
			logger.info("Version : " + version.getMajor() + "." + version.getMinor() + "." + version.getMicro());			
			String XMPPDomain = serverInfo.getXMPPDomain();
			logger.info("XMPP Domain : " + XMPPDomain);
			
			/* Get the connection manager */			
			connectionManager = server.getConnectionManager();			
			Collection<ServerPort> colServerPorts = connectionManager.getPorts();
			logger.info("Client to server ports : ");
			
			for (ServerPort port : colServerPorts){
				logger.info("port : " + port.getPort());
			}
			
			/* Get the User manager */
			userManager = server.getUserManager();
			
			logger.info("Openfire server is started successfully.");
			logger.info("Login to Admin console to see the details of server settings.");
			
		}
		catch(Exception ex){
			logger.error("An exception occurred in initializing the openfire server : " + ex.getMessage());
			throw new OpenfireInitializationException("An exception occurred in initializing the openfire server : " + ex.getMessage());
		}
		catch(Throwable th){
			logger.error("An error occurred in initializing the openfire server : " + th.getMessage());
			throw new OpenfireInitializationException("An error occurred in initializing the openfire server : " + th.getMessage());
		}
		
	}
	
	public void terminate(){
		server.stop();
	}

	/**
	 * @return the connectionManager
	 */
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	/**
	 * @param connectionManager the connectionManager to set
	 */
	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * @param userManager the userManager to set
	 */
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
