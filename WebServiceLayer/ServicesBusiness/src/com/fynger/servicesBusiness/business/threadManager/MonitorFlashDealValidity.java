/**
 * 
 */
package com.fynger.servicesBusiness.business.threadManager;

import java.util.Calendar;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FlashDealException;
import com.fynger.servicesBusiness.exception.MonitorFlashDealValidityThreadException;
import com.fynger.servicesBusiness.exception.ThreadManagerException;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FlashDealSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFlashDealSearchDAO;

/**
 * @author Rishi
 *
 */
public class MonitorFlashDealValidity extends Thread {
	
	public static LoggerManager logger = GenericUtility.getLogger(MonitorFlashDealValidity.class.getName());
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private final static String THREAD_NAME = "FlashDeal-ValidityThread"; 
	
	private static MonitorFlashDealValidity monitorFlashDealValidity = null;
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String flashDealValidityPoolFrequency;
	private static long longFlashDealValidityPoolFrequency;
	
	private boolean runFlag = true;
	
	
	static{
		flashDealValidityPoolFrequency = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FLASHDEAL_VALIDITY_POOL_FREQUENCY);
		longFlashDealValidityPoolFrequency = Long.parseLong(flashDealValidityPoolFrequency) * 60 * 1000;
	}
	
	private MonitorFlashDealValidity() {
		super(THREAD_NAME);		
	}
	
	public static MonitorFlashDealValidity createInstance() {
		
		if (monitorFlashDealValidity == null) {
			synchronized (lockObject) {
				monitorFlashDealValidity = new MonitorFlashDealValidity();
			}
		}

		return monitorFlashDealValidity;
	}
	
	public void startMonitor(){
		
		try{
			monitorFlashDealValidity.start();
		}
		catch(Exception ex){
			logger.error("Exception occured while spawning the thread : " + ex.getMessage());
			new ThreadManagerException("Exception occured while spawning the thread - Monitor FlashDeal Validity : " + ex.getMessage());
		}
		
	}
	
	public void run(){
		
		while(runFlag){
			
			logger.info(" ~^~^~^~^~^~ Time to execute FlashDeal Validity  Monitor ~^~^~^~^~^~");
			logger.info(" Flash Deal Validity Monitor Polling frequency :- " + longFlashDealValidityPoolFrequency);
			
			executeMonitor();
			
			try{
				Calendar now=Calendar.getInstance();
				int nowTimeHH	=	now.get(Calendar.HOUR_OF_DAY);
				int nowTimeMM	=	now.get(Calendar.MINUTE);
				int nowTimeSS	=	now.get(Calendar.SECOND);
				
				logger.info(" ~^~^~^~^~^~ Flash Deal Validity Monitor refreshed with Current Time:- "+nowTimeHH+":"+nowTimeMM+":"+nowTimeSS+"  ~^~^~^~^~^~ ");
				
				//Make the thread sleep for the configurable time interval
				Thread.sleep(longFlashDealValidityPoolFrequency);
			}
			catch(InterruptedException iEx){
				logger.error("InterruptedException occurred while running the Flash Deal Validity Thread : " + iEx.getMessage());
				//runFlag = false;
				//throw new MonitorFlashDealValidityThreadException("InterruptedException occurred while running the Flash Deal Validity Thread : " + iEx.getMessage());
				
			}
			catch(Exception ex){
				logger.error("Exception occurred while running the Flash Deal Validity Thread : " + ex.getMessage());
				runFlag = false;
				throw new MonitorFlashDealValidityThreadException("Exception occurred while running the Flash Deal Validity Thread : " + ex.getMessage());
			}
			
		}
	}
	
	public void executeMonitor(){
		
		try{
			IFlashDealSearchDAO flashDealSearchDAO = new FlashDealSearchDAOImpl();
			
			boolean flag = flashDealSearchDAO.validateAndUpdateFlashDealStatus();
			
			if (flag){
				logger.debug("Flash Deal status is validated succsssfully in database.");
			}
			else{
				logger.debug("Flash Deal status is Failed to update succsssfully in database.");
				throw new FlashDealException("Flash Deal status is Failed to update succsssfully in database.");
			}
		
		}
		catch(FlashDealException pEx){
			logger.debug("FlashDealException occurred : " + pEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealValidityThreadException("FlashDealException occurred  : " + pEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealValidityThreadException("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			runFlag = false;
			throw new MonitorFlashDealValidityThreadException("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	public void terminate(){
		this.runFlag = false;
		
	}

}
