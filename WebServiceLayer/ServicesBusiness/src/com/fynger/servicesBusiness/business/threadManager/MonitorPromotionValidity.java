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
import com.fynger.servicesBusiness.exception.MonitorPromotionValidityThreadException;
import com.fynger.servicesBusiness.exception.PromotionException;
import com.fynger.servicesBusiness.exception.ThreadManagerException;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPromotionsSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.PromotionsSearchDAOImpl;

/**
 * @author Rishi
 *
 */
public class MonitorPromotionValidity extends Thread {
	
	public static LoggerManager logger = GenericUtility.getLogger(MonitorPromotionValidity.class.getName());
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private final static String THREAD_NAME = "PromotionThread"; 
	
	private static MonitorPromotionValidity monitorPromotionValidity = null;
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String promotionValidityPoolFrequency;
	private static long longPromotionValidityPoolFrequency;
	
	private static String promotionValidityDiff;
	
	private boolean runFlag = true;
	
	
	static{
		promotionValidityPoolFrequency = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.PROMOTION_VALIDITY_POOL_FREQUENCY);
		longPromotionValidityPoolFrequency = Long.parseLong(promotionValidityPoolFrequency) * 60 * 1000;
		
		promotionValidityDiff = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.PROMOTION_VALIDITY_DIFF);
	}
	
	private MonitorPromotionValidity() {
		super(THREAD_NAME);		
	}
	
	public static MonitorPromotionValidity createInstance() {
		
		if (monitorPromotionValidity == null) {
			synchronized (lockObject) {
				monitorPromotionValidity = new MonitorPromotionValidity();
			}
		}

		return monitorPromotionValidity;
	}
	
	public void startMonitor(){
		
		try{
			monitorPromotionValidity.start();
		}
		catch(Exception ex){
			logger.error("Exception occured while spawning the thread : " + ex.getMessage());
			new ThreadManagerException("Exception occured while spawning the thread - Monitor Promotion Validity : " + ex.getMessage());
		}
		
	}
	
	public void run(){
		
		while(runFlag){
			
			logger.info(" ~^~^~^~^~^~ Time to execute Promotion Validity  Monitor ~^~^~^~^~^~");
			logger.info(" Promotion Validity Monitor Polling frequency :- " + longPromotionValidityPoolFrequency);
			
			executeMonitor();
			
			try{
				Calendar now=Calendar.getInstance();
				int nowTimeHH	=	now.get(Calendar.HOUR_OF_DAY);
				int nowTimeMM	=	now.get(Calendar.MINUTE);
				int nowTimeSS	=	now.get(Calendar.SECOND);
				
				logger.info(" ~^~^~^~^~^~ Promotion Validity Monitor refreshed with Current Time:- "+nowTimeHH+":"+nowTimeMM+":"+nowTimeSS+"  ~^~^~^~^~^~ ");
				
				//Make the thread sleep for the configurable time interval
				Thread.sleep(longPromotionValidityPoolFrequency);
			}
			catch(InterruptedException iEx){
				logger.error("InterruptedException occurred while running the Promotion Validity Thread : " + iEx.getMessage());
				//runFlag = false;
				//throw new MonitorPromotionValidityThreadException("InterruptedException occurred while running the Promotion Validity Thread : " + iEx.getMessage());
				
			}
			catch(Exception ex){
				logger.error("Exception occurred while running the Promotion Validity Thread : " + ex.getMessage());
				runFlag = false;
				throw new MonitorPromotionValidityThreadException("Exception occurred while running the Promotion Validity Thread : " + ex.getMessage());
			}
			
		}
	}
	
	public void executeMonitor(){
		
		try{
			IPromotionsSearchDAO promotionSearchDAO = new PromotionsSearchDAOImpl();
			
			boolean flag = promotionSearchDAO.validateAndUpdatePromotionStatus(Integer.parseInt(promotionValidityDiff));
			
			if (flag){
				logger.debug("Promotion status is validated succsssfully in database.");
			}
			else{
				logger.debug("Promotion status is Failed to update succsssfully in database.");
				throw new PromotionException("Promotion status is Failed to update succsssfully in database.");
			}
		}
		catch(PromotionException pEx){
			logger.debug("PromotionException occurred : " + pEx.getMessage());
			//runFlag = false;
			//throw new MonitorPromotionValidityThreadException("PromotionException occurred  : " + pEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//runFlag = false;
			//throw new MonitorPromotionValidityThreadException("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			runFlag = false;
			throw new MonitorPromotionValidityThreadException("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	public void terminate(){
		this.runFlag = false;
	}

}
