/**
 * 
 */
package com.fynger.servicesBusiness.business.threadManager;

import java.util.Calendar;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.notificationEngine.engine.NotificationEngine;
import com.fynger.notificationEngine.exception.NotificationEngineException;
import com.fynger.notificationEngine.requests.vo.NotificationRequestVO;
import com.fynger.notificationEngine.responses.vo.NotificationResponseVO;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FlashDealException;
import com.fynger.servicesBusiness.exception.FlashDealMobileNotificationException;
import com.fynger.servicesBusiness.exception.MonitorFlashDealResponseThreadException;
import com.fynger.servicesBusiness.exception.ThreadManagerException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFlashDealSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealDetailsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRespondedData;
import com.fynger.servicesBusiness.objects.MailContent;
import com.fynger.servicesBusiness.objects.NotificationMessageAssembler;
import com.fynger.servicesBusiness.utilities.MailDispatcher;

/**
 * @author Rishi
 *
 */
public class MonitorFlashDealResponse extends Thread {
	
	public static LoggerManager logger = GenericUtility.getLogger(MonitorFlashDealResponse.class.getName());
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private final static String THREAD_NAME = "FlashDeal-ResponseThread"; 
	
	private static MonitorFlashDealResponse monitorFlashDealResponse = null;
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String flashDealResponsePoolFrequency;
	private static long longFlashDealResponsePoolFrequency;
	
	private static String mailContentType;
	private static String flashDealResponseMailSubject;
	private static String flashDealResponseMailBody;
	
	private boolean runFlag = true;
	
	private static DAOFactory daoFactory;
	
	private static NotificationEngine notificationEngine;
	
	static{
		flashDealResponsePoolFrequency 		= propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FLASHDEAL_RESPONSE_POOL_FREQUENCY);
		longFlashDealResponsePoolFrequency	= Long.parseLong(flashDealResponsePoolFrequency) * 60 * 1000;
		
		mailContentType 					= propertyManager.getProperty(GenericConstants.EMAIL_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONFIG_CONTENT_TYPE);
		flashDealResponseMailSubject 		= propertyManager.getProperty(GenericConstants.EMAIL_CONTENT_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONTENT_FLASHDEAL_RESPONSE_SUBJECT);
		flashDealResponseMailBody 			= propertyManager.getProperty(GenericConstants.EMAIL_CONTENT_PROPERTIES_FILE_NAME, GenericConstants.MAIL_CONTENT_FLASHDEAL_RESPONSE_BODY);
		
		daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQL);
		
		notificationEngine = NotificationEngine.createInstance();
	}
	
	private MonitorFlashDealResponse() {
		super(THREAD_NAME);	
	}
	
	public static MonitorFlashDealResponse createInstance() {
		
		if (monitorFlashDealResponse == null) {
			synchronized (lockObject) {
				monitorFlashDealResponse = new MonitorFlashDealResponse();
			}
		}

		return monitorFlashDealResponse;
	}
	
	public void startMonitor(){
		
		try{
			monitorFlashDealResponse.start();
		}
		catch(Exception ex){
			logger.error("Exception occured while spawning the thread : " + ex.getMessage());
			new ThreadManagerException("Exception occured while spawning the thread - Monitor FlashDeal Response : " + ex.getMessage());
		}
		
	}
	
	public void run(){
		
		while(runFlag){
			
			logger.info(" ~^~^~^~^~^~ Time to execute FlashDeal Response  Monitor ~^~^~^~^~^~");
			logger.info(" Flash Deal Response Monitor Polling frequency :- " + longFlashDealResponsePoolFrequency);
			
			executeMonitor();
			
			try{
				Calendar now=Calendar.getInstance();
				int nowTimeHH	=	now.get(Calendar.HOUR_OF_DAY);
				int nowTimeMM	=	now.get(Calendar.MINUTE);
				int nowTimeSS	=	now.get(Calendar.SECOND);
				
				logger.info(" ~^~^~^~^~^~ Flash Deal Response Monitor refreshed with Current Time:- "+nowTimeHH+":"+nowTimeMM+":"+nowTimeSS+"  ~^~^~^~^~^~ ");
				
				//Make the thread sleep for the configurable time interval
				Thread.sleep(longFlashDealResponsePoolFrequency);
			}
			catch(InterruptedException iEx){
				logger.error("InterruptedException occurred while running the Flash Deal Response Thread : " + iEx.getMessage());
			}
			catch(Exception ex){
				logger.error("Exception occurred while running the Flash Deal Response Thread : " + ex.getMessage());
				runFlag = false;
				throw new MonitorFlashDealResponseThreadException("Exception occurred while running the Flash Deal Response Thread : " + ex.getMessage());
			}
			
		}
	}
	
	public void executeMonitor(){
		/* Send Email notification */
		emailNotification();
		
		/* Send Mobile notification */
		mobileNotification();
	}
	
	public void emailNotification(){
		
		try{
			IFlashDealSearchDAO flashDealSearchDAO = daoFactory.getFlashDealSearchDAO();
			
			List<FlashDealDetailsData> alFlashDealRespondedData = flashDealSearchDAO.fetchRespondedFlashDeals();
			
			if (null != alFlashDealRespondedData && alFlashDealRespondedData.size() > 0){
				
				logger.debug("Responded Flash deal data found. Eamils will be dispatched on the respective user email ids.");
				
				StringBuffer flashDealIds = new StringBuffer();
				
				for (FlashDealDetailsData daoData : alFlashDealRespondedData){
					
					String name = daoData.getName();					
					String address = daoData.getAddress();
					String phone = daoData.getPhone();
					String responseText = daoData.getFlashDealText();
					String email = daoData.getUserEmailId();
					String username = daoData.getUsername();
					
					/* Send the Email on User Email id */
					
					String body = flashDealResponseMailBody;
					body = body.replaceAll(GenericConstants.MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_NAME, name);
					body = body.replaceAll(GenericConstants.MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_ADDRESS, address);
					body = body.replaceAll(GenericConstants.MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_PHONE, phone);
					body = body.replaceAll(GenericConstants.MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_RESPONSETEXT, responseText);
					
					MailContent mailContent = new MailContent();
					
					mailContent.setToMailId(email);
					mailContent.setEvent(BusinessConstants.EMAIL_EVENT_FLASHDEAL_RESPONSE);
					mailContent.setUsername(username);
					mailContent.setSubject(flashDealResponseMailSubject);
					mailContent.setBody(body);
					mailContent.setContentType(mailContentType);
					
					logger.debug("Sending Mail on Flash Deal event...");
					
					MailDispatcher mailDispatcher = new MailDispatcher();
					boolean emailFlag = mailDispatcher.dispatchMail(mailContent);
					
					if (emailFlag){
						logger.debug("Email has been dispatched successfully.");
						
						flashDealIds.append(daoData.getFlashDealId()).append(",");
					}
					else{
						logger.debug("Fail to dispatch Email successfully. Check the email logs in the database.");
					}
				}
				
				/* Update the email status in database table */
				if (flashDealIds.toString().endsWith(",")) {
					flashDealIds = new StringBuffer((flashDealIds.substring(0, flashDealIds.lastIndexOf(","))));
				}
				
				boolean flag = flashDealSearchDAO.updateRespondedFlashDealsEmailStatus(flashDealIds.toString());
				
				if (flag){
					logger.debug("Flash Deal email status is updated succsssfully in database.");
				}
				else{
					logger.error("Flash Deal email status is Failed to update succsssfully in database.");
					throw new FlashDealException("Flash Deal email status is Failed to update succsssfully in database.");
				}
			}
			else{
				logger.debug("No responded Flash deal data found. No email will be dispatched.");
			}
		
		}
		catch(FlashDealException pEx){
			logger.debug("FlashDealException occurred : " + pEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealResponseThreadException("FlashDealException occurred  : " + pEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealResponseThreadException("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			runFlag = false;
			throw new MonitorFlashDealResponseThreadException("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	public void mobileNotification(){
		
		NotificationRequestVO notificationRequestVO = null;
		NotificationResponseVO notificationResponseVO = null;
		boolean notificationFlag = false;
		
		try{
			IFlashDealSearchDAO flashDealSearchDAO = daoFactory.getFlashDealSearchDAO();
			
			List<FlashDealRespondedData> alFlashDealRespondedData = flashDealSearchDAO.fetchMobileNotifiableRespondedFlashDeals();
			
			if (null != alFlashDealRespondedData && alFlashDealRespondedData.size() > 0){
				
				logger.debug("Responded Flash deal data found. Mobile notification will be send on the respective user mobiles.");
				
				StringBuffer flashDealRespondedIds = new StringBuffer();
				
				StringBuffer flashDealRespondedFailedIds = new StringBuffer();
				
				NotificationMessageAssembler notificationMessageAssembler = new NotificationMessageAssembler();
				
				for (FlashDealRespondedData daoData : alFlashDealRespondedData){
					
					String deviceRegistrationId = daoData.getDeviceRegId();
					
					if (!GenericUtility.safeTrim(deviceRegistrationId).equals(GenericConstants.EMPTY_STRING)){
						
						notificationRequestVO = new NotificationRequestVO();
						
						notificationRequestVO.setNameValuePairs(notificationMessageAssembler.getFlashDealMessageContent(daoData));
						
						notificationRequestVO.setRegistrationId(daoData.getDeviceRegId());
						
						notificationResponseVO = notificationEngine.sendNotification(notificationRequestVO);
						
						notificationFlag = notificationResponseVO.isStatus();
						
						if (notificationFlag){
							logger.debug("Notification has been dispatched successfully for user name : " + daoData.getUsername());
							
							flashDealRespondedIds.append(daoData.getFlashDealRespondedDataId()).append(",");
						}
						else{
							logger.debug("Fail to dispatch Notification successfully. Check the notification logs in the database.");
							
							flashDealRespondedFailedIds.append(daoData.getFlashDealRespondedDataId()).append(",");
						}
					}
					else{
						logger.debug("No device auth details are found for username : " + daoData.getUsername() + " . No mobile notification will be send.");
					}
					
				}
				
				/* Update the Notified status in database table */
				if (flashDealRespondedIds.toString().endsWith(",")) {
					flashDealRespondedIds = new StringBuffer((flashDealRespondedIds.substring(0, flashDealRespondedIds.lastIndexOf(","))));
					
					boolean flag = flashDealSearchDAO.updateRespondedFlashDealsNotifiedStatus(flashDealRespondedIds.toString());
					
					if (flag){
						logger.debug("Flash Deal Notification status is updated succsssfully in database.");
					}
					else{
						logger.error("Flash Deal Notification status is Failed to update succsssfully in database.");
						throw new FlashDealMobileNotificationException("Flash Deal Notification status is Failed to update succsssfully in database.");
					}
				}
				
				if (flashDealRespondedFailedIds.toString().endsWith(",")) {
					flashDealRespondedFailedIds = new StringBuffer((flashDealRespondedFailedIds.substring(0, flashDealRespondedFailedIds.lastIndexOf(","))));
					
					boolean flag = flashDealSearchDAO.updateRespondedFlashDealsNotifiedFailedStatus(flashDealRespondedFailedIds.toString());
					
					if (flag){
						logger.debug("Flash Deal Notification status is updated succsssfully in database.");
					}
					else{
						logger.error("Flash Deal Notification status is Failed to update succsssfully in database.");
						throw new FlashDealMobileNotificationException("Flash Deal Notification status is Failed to update succsssfully in database.");
					}
				}
			}
		}
		catch(NotificationEngineException neEx){
			logger.debug("NotificationEngineException occurred : " + neEx.getMessage());
			//runFlag = false;
			//TODO: Mechanism to handle this exception or to log
		}
		catch(FlashDealMobileNotificationException pEx){
			logger.debug("FlashDealMobileNotificationException occurred : " + pEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealResponseThreadException("FlashDealMobileNotificationException occurred  : " + pEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//runFlag = false;
			//throw new MonitorFlashDealResponseThreadException("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			//TODO: Mechanism to handle this exception or to log
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			runFlag = false;
			throw new MonitorFlashDealResponseThreadException("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	public void terminate() {
		this.runFlag = false;
	}

}
