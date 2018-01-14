/**
 * 
 */
package com.fynger.servicesBusiness.integration.openfire;

import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.OpenfireUserRegistrationException;

/**
 * @author Rishi
 *
 */
public class OpenfireManager {
	
	public static LoggerManager logger = GenericUtility.getLogger(OpenfireManager.class.getName());
	
	private OpenfireConnectionManager openfireConnectionManager;
	
	public OpenfireManager(){
		openfireConnectionManager = OpenfireConnectionManager.getInstance();
	}
	
	
	public boolean registerUser(String username, String password, String email, String name){
		
		boolean flag = false;
		
		try{
			User user = openfireConnectionManager.getUserManager().createUser(username, password, name, email);
			
			if (null != user){
				logger.debug("User is registered successfully with openfire server");
				flag = true;
			}
			else{
				logger.debug("User is failed to registered successfully with openfire server");
			}
		}
		catch(UserAlreadyExistsException userExistsEx){
			logger.error("User already exists with the user name : " + username);
			throw new OpenfireUserRegistrationException("User already exists with the user name : " + username);
		}
		catch(Exception ex){
			logger.error("An excpetion occurred during registering the user with openfire");
			throw new OpenfireUserRegistrationException("An excpetion occurred during registering the user with openfire");
		}
		catch(Throwable th){
			logger.error("An error occurred during registering the user with openfire");
			throw new OpenfireUserRegistrationException("An error occurred during registering the user with openfire");
		}
		
		return flag;
	}

}
