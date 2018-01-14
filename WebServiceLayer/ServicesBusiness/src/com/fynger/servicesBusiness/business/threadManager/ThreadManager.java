/**
 * 
 */
package com.fynger.servicesBusiness.business.threadManager;

import java.util.HashMap;
import java.util.Map;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.ThreadManagerException;

/**
 * @author Rishi
 *
 */
public class ThreadManager {
	
	public static LoggerManager logger = GenericUtility.getLogger(ThreadManager.class.getName());
	
	public static final String THREAD_STATUS_RUNNING		= "RUNNING";
	
	private static ThreadManager threadManager = null;
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private static MonitorPromotionValidity monitorPromotionValidity;
	private static MonitorFlashDealValidity monitorFlashDealValidity;
	private static MonitorFlashDealResponse monitorFlashDealResponse;
	
	private Map<String, RefreshUserFavouritesSearchResults> hmUserFavouriteThreads = new HashMap<String, RefreshUserFavouritesSearchResults>();
	
	private Object userFavLockObject = new Object();
	
	
	private ThreadManager() {
		
	}
	
	public static ThreadManager createInstance() {
		
		if (threadManager == null) {
			synchronized (lockObject) {
				threadManager = new ThreadManager();
			}
		}

		return threadManager;
	}
	
	public void igniteThreads(){
		
		try{
			/* Start Promotion Validity Monitor */
			monitorPromotionValidity = MonitorPromotionValidity.createInstance();
			if (null != monitorPromotionValidity){
				monitorPromotionValidity.startMonitor();
			}
			else{
				logger.error("Failed to instantiate Thread for Promotion Validity Monitor");
				throw new ThreadManagerException("Failed to instantiate Thread for Promotion Validity Monitor");
			}
			
			/* Start Flash Deal Validity Monitor */
			monitorFlashDealValidity = MonitorFlashDealValidity.createInstance();
			if (null != monitorFlashDealValidity){
				monitorFlashDealValidity.startMonitor();
			}
			else{
				logger.error("Failed to instantiate Thread for Flash Deal Validity Monitor");
				throw new ThreadManagerException("Failed to instantiate Thread for Flash Deal Validity Monitor");
			}
			
			/* Start Flash Deal Response Monitor */
			monitorFlashDealResponse = MonitorFlashDealResponse.createInstance();
			if (null != monitorFlashDealResponse){
				monitorFlashDealResponse.startMonitor();
			}
			else{
				logger.error("Failed to instantiate Thread for Flash Deal Response Monitor");
				throw new ThreadManagerException("Failed to instantiate Thread for Flash Deal Response Monitor");
			}
		}
		catch(ThreadManagerException tmEx){
			throw tmEx;
		}
		catch (Exception ex){
			logger.error("Exception occurred in Thread manager : " + ex.getMessage());
			throw new ThreadManagerException("Exception occurred in Thread manager : " + ex.getMessage());
		}
		
	}
	
	public boolean spawnRefreshUserFavouriteSearchThread(String username){
		
		synchronized (userFavLockObject) {
			boolean flag = false;
			
			if (!hmUserFavouriteThreads.containsKey(username)){
				RefreshUserFavouritesSearchResults userThread = new RefreshUserFavouritesSearchResults(username);
				hmUserFavouriteThreads.put(username, userThread);
				logger.debug("RefreshUserFavouritesSearchResults thread is created successfully for username : " + username);
				flag = true;
			}
			
			return flag;
		}
	}
	
	public void startRefreshUserFavouriteSearchThread(String username){
		
		synchronized (userFavLockObject) {
			
			if (hmUserFavouriteThreads.containsKey(username)){
				RefreshUserFavouritesSearchResults userThread = hmUserFavouriteThreads.get(username);
				userThread.start();
				userThread.setStatus(THREAD_STATUS_RUNNING);
				logger.debug("RefreshUserFavouritesSearchResults thread is started successfully for username : " + username);
			}
			else{
				logger.error("No thread found for user name : " + username);
			}
			
		}
	}
	
	public RefreshUserFavouritesSearchResults getRefreshUserFavPromotionsThread(String username){
		synchronized (userFavLockObject) {
			if (hmUserFavouriteThreads.containsKey(username)){
				return hmUserFavouriteThreads.get(username);
			}
			else{
				return null;
			}
		}
	}
	
	public boolean terminateRefreshUserFavouriteSearchThread(String username){
		
		synchronized (userFavLockObject) {
			boolean flag = false;
			
			if (hmUserFavouriteThreads.containsKey(username)){
				RefreshUserFavouritesSearchResults userThread = hmUserFavouriteThreads.get(username);
				userThread.terminate();
				logger.debug("RefreshUserFavouritesSearchResults thread is terminated successfully for username : " + username);
				flag = true;
			}
			else{
				logger.error("No thread found for user name : " + username);
			}
			
			return flag;
		}
	}
	
	public void terminateThreadManager(){
		
		try{
			monitorPromotionValidity.terminate();
			monitorPromotionValidity.join();
			
			monitorFlashDealValidity.terminate();
			monitorFlashDealValidity.join();
			
			monitorFlashDealResponse.terminate();
			monitorFlashDealResponse.join();
			
			for (String username : hmUserFavouriteThreads.keySet()){
				RefreshUserFavouritesSearchResults userThread = hmUserFavouriteThreads.get(username);
				userThread.terminate();
				if (userThread.getState().equals(Thread.State.TIMED_WAITING) || userThread.getState().equals(Thread.State.WAITING)){
					synchronized (userThread) {
						userThread.notify();
					}
				}
				userThread.join();
				logger.debug("RefreshUserFavouritesSearchResults thread is terminated successfully for username : " + username);
			}
		}
		catch(Exception ex){
			logger.error("Exception occurred while terminating Thread manager : " + ex.getMessage());
			throw new ThreadManagerException("Exception occurred while terminating Thread manager : " + ex.getMessage());
		}
		
	}

}
