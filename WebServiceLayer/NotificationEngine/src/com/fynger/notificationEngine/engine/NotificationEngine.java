package com.fynger.notificationEngine.engine;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.json.JSONException;

import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.notificationEngine.constants.NotificationEngineConstants;
import com.fynger.notificationEngine.exception.NotificationEngineException;
import com.fynger.notificationEngine.requests.vo.NotificationRequestVO;
import com.fynger.notificationEngine.responses.vo.NotificationResponseVO;
import com.fynger.notificationEngine.utilities.NotificationStatusString;
import com.fynger.servicesBusiness.integration.dataAccess.dao.INotificationLogDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.NotificationLogDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.NotificationLogData;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;



public class NotificationEngine {
	
	public static LoggerManager logger = GenericUtility.getLogger(NotificationEngine.class.getName());
	
    private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
    
    private static Properties prop = null;       
    
    private Map<String, String> nameValuePairs;
    
    private static NotificationEngine notificationEngine = null;
    
    private static String googleAPIKey = null;
    private static String retries = null;
    private static String timeToLive = null;
    private static String collapseKey = null;
    private static String iOSKeyStore = null;
    private static String iOSKeyStorePassword = null;
    
    
    private NotificationEngine() {
        prop = propertyManager.getProperties(NotificationEngineConstants.NOTIFICATIONENGINE_PROPERTIES_FILE_NAME);
    }

    public static NotificationEngine createInstance() {

        if (notificationEngine == null) {
        	notificationEngine = new NotificationEngine();
        }

        return notificationEngine;
    }

    public void configure() {
        setConfigParmsFromPropFile();

    }
    
    public void setConfigParmsFromPropFile() {

        try {
            /** Data retrieval from the notification properties file **/
            googleAPIKey = prop.getProperty(NotificationEngineConstants.GOOGLE_API_KEY);
            if (googleAPIKey == null) {
                logger.error("GCM API key is not found in notification property file.");
                throw new NotificationEngineException("GCM API key is not found in notofication property file.");
            }

            retries = prop.getProperty(NotificationEngineConstants.RETRIES);
            if (retries == null) {
                logger.error("Number of retries not found in notification property file.");
                throw new NotificationEngineException("Number of retries not found in notification property file.");
            }

            timeToLive = prop.getProperty(NotificationEngineConstants.TIME_TO_LIVE);
            if (timeToLive == null) {
                logger.error("Time to live not found in notification property file.");
                throw new NotificationEngineException("Time to live not found in notification property file.");
            }

            collapseKey = prop.getProperty(NotificationEngineConstants.COLLAPSE_KEY);
            if (collapseKey == null) {
                logger.error("Collapse Key not found in notification property file.");
                throw new NotificationEngineException("Collapse Key not found in notificaation property file.");
            }

            iOSKeyStore = prop.getProperty(NotificationEngineConstants.KEY_STORE);
            if (iOSKeyStore == null) {
                logger.error("iOSKey Store not found in notification property file.");
                throw new NotificationEngineException("iOS Key Store not found in notification property file.");
            }

            iOSKeyStorePassword = prop.getProperty(NotificationEngineConstants.KEY_STORE_PASSWORD);
            if (iOSKeyStorePassword == null) {
                logger.error("iOS key store is not found in notification property file.");
                throw new NotificationEngineException("iOS key store is not found in notification property file.");
            }

        } 
        catch (Exception ex) {
            logger.error("Error while setting up the local variables from notification properties file: " + ex.getMessage());
            throw new NotificationEngineException("Error while setting up the local variables from notification properties file: " + ex.getMessage());
        }

    }
    
    public NotificationResponseVO sendNotification(NotificationRequestVO request) {

    	NotificationResponseVO notificationResponse;
    	
    	NotificationLogData notificationLogData = new NotificationLogData();
    	
    	INotificationLogDAO notificationLogDAO = new NotificationLogDAOImpl();  
    	
        String msg, msgTypeKey, name, msgTypeString, username;
        String regId = "";
        int deviceType, msgType, msgTypeValue;        
        
        try{
	        /** Extracting and Parsing values from the request object **/
	        nameValuePairs = request.getNameValuePairs();
	        regId = request.getRegistrationId();
	
	        msg = nameValuePairs.get(NotificationEngineConstants.MESSAGE);
	        deviceType = Integer.parseInt(nameValuePairs.get(NotificationEngineConstants.DEVICE_TYPE));
	        msgType = Integer.parseInt(nameValuePairs.get(NotificationEngineConstants.MESSAGE_TYPE));
	        name = nameValuePairs.get(NotificationEngineConstants.NAME);
	        username = nameValuePairs.get(NotificationEngineConstants.USER_NAME);
	
	        /** Building message text based upon type of message **/
	        switch (msgType) {
	            case NotificationEngineConstants.MESSAGE_TYPE_FLASHDEAL:
	
	                msgTypeKey = NotificationEngineConstants.KEY_FLASHDEAL_ID;
	                msgTypeString = NotificationEngineConstants.MESSAGE_TYPE_FLASHDEAL_STRING;
	                msgTypeValue = Integer.parseInt(nameValuePairs.get(NotificationEngineConstants.KEY_FLASHDEAL_ID).toString());
	                break;
	
	            case NotificationEngineConstants.MESSAGE_TYPE_SHOUT:
	
	                msgTypeKey = NotificationEngineConstants.KEY_POST_ID;
	                msgTypeString = NotificationEngineConstants.MESSAGE_TYPE_SHOUT_STRING;
	                msgTypeValue = Integer.parseInt(nameValuePairs.get(NotificationEngineConstants.KEY_POST_ID).toString());
	                break;
	
	            case NotificationEngineConstants.MESSAGE_TYPE_PLACES:
	
	                msgTypeKey = NotificationEngineConstants.KEY_POST_ID;
	                msgTypeString = NotificationEngineConstants.MESSAGE_TYPE_PLACES_STRING;
	                msgTypeValue = Integer.parseInt(nameValuePairs.get(NotificationEngineConstants.KEY_POST_ID).toString());
	                break;
	
	            default:
	                logger.error("Invalid Message Type :" + msgType);
	                throw new NotificationEngineException("Invalid Message Type");
	        }
	        
	        /** Pushing the message to the respective device **/
	        
	        switch (deviceType) {
	            case NotificationEngineConstants.TYPE_ANDROID:
	
	                msg = androidMessageBuilder(msg, msgType, name);               
	                notificationResponse = androidPushNotification(msg, msgTypeKey, msgTypeValue, regId);
	                break;
	
	            case NotificationEngineConstants.TYPE_IOS:
	
	                msg = iOSMessageBuilder(msg, msgType, name);                
	                notificationResponse = iOSPushNotification(msg, msgTypeKey, msgTypeValue, regId);
	                break;
	
	            default:
	                logger.error("Invalid Device Type :" + deviceType);
	                throw new NotificationEngineException("Invalid Device Type");
	
	        }
	        
	        notificationResponse = setDetailedResponse(notificationResponse);
	        
	        notificationLogData.setUserName(username);
	        notificationLogData.setEvent(msgTypeString);
	        notificationLogData.setDeviceRegId(regId);
	        notificationLogData.setDeviceType(deviceType);
	        notificationLogData.setRetriedCount(Integer.parseInt(retries));
	        notificationLogData.setSentTime(new java.sql.Timestamp(System.currentTimeMillis()));
	        notificationLogData.setDispatchStatus(NotificationStatusString.statusString(notificationResponse.isStatus()));
	        notificationLogData.setText(msg);
	        notificationLogData.setComments(notificationResponse.getResponseMsg());
	        
	        notificationLogDAO.storeNotificationLog(notificationLogData);
        }
        catch (Exception e) {
            logger.error("Exception in sendingnotification with RegistrationId : " + regId + " Error Message :" + e.getMessage());
            throw new NotificationEngineException("Exception in sendingnotification with RegistrationId : " + regId + " Error Message :" + e.getMessage());
        }

        return notificationResponse;
    }
    
    public NotificationResponseVO androidPushNotification(String msg, String msgTypeKey, int msgTypeValue, String regId) {
    	
    	NotificationResponseVO notificationResponse = new NotificationResponseVO();
    	boolean status = false;
    	
        try {

            /** Sending Push Notification to the android user**/
        	
            String responseCode, canonicalRegId, messageResponse;            
            
            String msgTypeValueString = Integer.toString(msgTypeValue);
            int timeToLiveInt = Integer.parseInt(timeToLive);
            int retriesInt = Integer.parseInt(retries);

            Sender sender = new Sender(googleAPIKey);

            /** Using the message builder class to assign values to message parameters such as time to live, collapse key, additional data **/
            Message message = new Message.Builder().timeToLive(timeToLiveInt).collapseKey(collapseKey).addData(NotificationEngineConstants.MESSAGE, msg).addData(msgTypeKey, msgTypeValueString).build();

            Result result = sender.send(message, regId, retriesInt);

            responseCode = result.getMessageId();

            /** Setting the response object based on the push result **/
            if (responseCode != null) {
            	status = true;
                canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                	
                    /** same device has more than on registration ID: update database **/
                    logger.info("Push Notification Successfully to " + regId + " Canonical registrationId found.");
                    messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_CANNONICAL_1+ regId +NotificationEngineConstants.MESSAGE_RESPONSE_CANNONICAL_2  + canonicalRegId;
                } else {
                	
                    /** Message Pushed successfully**/
                    logger.info("Push Notification Sent Successfully to " + regId);
                    messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_SUCCESS;
                }

            } else {
            	responseCode = NotificationEngineConstants.NOTIFICATION_GCM_PUSH_FAILURE;
                messageResponse = result.getErrorCodeName();

                /** Message Push Unsuccessful **/
                status = false;
                if (messageResponse.equals(NotificationEngineConstants.NOT_REGISTERED)) {
                    /** application has been removed from device - unregister database **/
                    logger.info("Push Notification Sending Unsuccessful to " + regId + "Application has been removed.");
                    //messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_UNSUCCESSFULL_APP_REMOVED;
                } else {
                    logger.info("Push Notification Sending Unsuccessful to " + regId);
                    //messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_FAILURE;
                }

            }

            notificationResponse.setResponseCode(responseCode);
            notificationResponse.setResponseMsg(messageResponse);
            notificationResponse.setStatus(status);
            
        } catch (IOException ex) {
            logger.error("Input Output Exception in sending message to Android User with RegistrationId : " + regId + " Error Message :" + ex.getMessage());
            throw new NotificationEngineException("Input Output Exception in sending message to Android User", ex);
        } catch (Exception e) {
            logger.error("Exception in sending message to Android User with RegistrationId : " + regId + " Error Message :" + e.getMessage());
            throw new NotificationEngineException("Exception in sending message to Android User", e);
        }

        return notificationResponse;
    }
    
    public NotificationResponseVO iOSPushNotification(String msg, String msgTypeKey, int msgTypeValue, String regId) {

    	NotificationResponseVO notificationResponse = new NotificationResponseVO();
    	boolean status;
    	
        try {

            PushNotificationPayload payload = PushNotificationPayload.complex();

            int timeToLiveInt = Integer.parseInt(timeToLive);
            String messageResponse, responseCode;

            /** Customizing the payload **/
            payload.addAlert(msg);
            payload.addCustomDictionary(msgTypeKey, msgTypeValue);
            payload.setExpiry(timeToLiveInt);
            PushedNotification notification = Push.payload(payload, iOSKeyStore, iOSKeyStorePassword, NotificationEngineConstants.PRODUCTION_SERVICE, regId).firstElement();

            if (notification.isSuccessful()) {
                /** Notification Successfully Pushed **/
            	
            	status = true;
                logger.info("Push Notification Sent Successfully to " + regId);
                responseCode = Integer.toString(notification.getResponse().getCommand());
                messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_SUCCESS;

            } else {
                /** Notification Push Unsuccessful **/
            	
            	status= false;
                /** extracting details about the exception **/            	
                Exception exception = notification.getException();
                messageResponse = exception.getMessage();

                ResponsePacket error = notification.getResponse();

                /** If the problem was an error-response packet returned by Apple Server **/
                if (error != null) {
                    messageResponse = error.getMessage();

                }
                logger.info("Push Notification Sending Unsuccessful to " + regId);
                responseCode = Integer.toString(notification.getResponse().getCommand());
                messageResponse = NotificationEngineConstants.MESSAGE_RESPONSE_FAILURE + messageResponse;

            }

            notificationResponse.setResponseCode(responseCode);
            notificationResponse.setResponseMsg(messageResponse);
            notificationResponse.setStatus(status);

        } catch (KeystoreException ek) {
            logger.error("Keystore Exception in sending message to iOS User with RegistrationId : " + regId + " Error Message :" + ek.getMessage());
            throw new NotificationEngineException("Keystore Exception in sending message to iOS User", ek);

        } catch (CommunicationException ec) {
            logger.error("Communication Exception in sending message to iOS User with RegistrationId : " + regId + " Error Message :" + ec.getMessage());
            throw new NotificationEngineException("Communication Exception in sending message to iOS User", ec);
        } catch (JSONException ej) {
            logger.error("JSON Exception in sending message to iOS User with RegistrationId : " + regId + " Error Message :" + ej.getMessage());
            throw new NotificationEngineException("Communication Exception in sending message to iOS User", ej);
        }
        
        return notificationResponse;
    }

    
    public String androidMessageBuilder(String msg, int msgType, String name) {
        switch (msgType) {
            case NotificationEngineConstants.MESSAGE_TYPE_FLASHDEAL:

                msg = NotificationEngineConstants.FLASHDEAL_RESPONSE_MESSAGE + name + NotificationEngineConstants.FULL_STOP + NotificationEngineConstants.SINGLE_SPACE + msg;
                break;

            case NotificationEngineConstants.MESSAGE_TYPE_SHOUT:

                msg = name + NotificationEngineConstants.SHOUT_RESPONSE_MESSAGE + msg;
                break;
                
            case NotificationEngineConstants.MESSAGE_TYPE_PLACES:

                msg = name + NotificationEngineConstants.PLACES_RESPONSE_MESSAGE + msg;
                break;    

            default:
                logger.error("Invalid Message Type :" + msgType);
                throw new NotificationEngineException("Invalid Message Type");
        }
        return msg;
    }
    
    public String iOSMessageBuilder(String msg, int msgType, String name) {
        switch (msgType) {
            case NotificationEngineConstants.MESSAGE_TYPE_FLASHDEAL:

                msg = NotificationEngineConstants.FLASHDEAL_RESPONSE_MESSAGE + name + NotificationEngineConstants.FULL_STOP + NotificationEngineConstants.SINGLE_SPACE + msg;
                break;

            case NotificationEngineConstants.MESSAGE_TYPE_SHOUT:

                msg = name + NotificationEngineConstants.SHOUT_RESPONSE_MESSAGE + msg;
                break;
                
            case NotificationEngineConstants.MESSAGE_TYPE_PLACES:

                msg = name + NotificationEngineConstants.PLACES_RESPONSE_MESSAGE + msg;
                break;    


            default:
                logger.error("Invalid Message Type :" + msgType);
                throw new NotificationEngineException("Invalid Message Type");
        }
        return msg;
    }
    
    public NotificationResponseVO setDetailedResponse(NotificationResponseVO notificationResponseVO){
		
		String responseMsg = notificationResponseVO.getResponseMsg();
    	
    	
    	/*if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_GCM_PUSH_SUCCESS) || notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_SUCCESS)){
			logger.debug("Flash Deal Notification pushed succsssfully");					
		} */  	
    	
		if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_GCM_PUSH_AUTHENTICATION_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to android device because of an authentication error :"+notificationResponseVO.getResponseMsg());
			notificationResponseVO.setResponseMsg(NotificationEngineConstants.MESSAGE_RESPONSE_GCM_AUTHENTICATION_ERROR+responseMsg);
			
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_GCM_PUSH_JSON_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to android device because of JSON error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_GCM_JSON_ERROR+responseMsg);
		}
		//Throwing exception while converting from string to int
		/*else if(Integer.parseInt(notificationResponseVO.getResponseCode()) > Integer.parseInt(NotificationEngineConstants.NOTIFICATION_GCM_PUSH_SERVER_ERROR_LOWERLIMIT) && Integer.parseInt(notificationResponseVO.getResponseCode()) < Integer.parseInt(NotificationEngineConstants.NOTIFICATION_GCM_PUSH_SERVER_ERROR_UPPERLIMIT)){
			logger.debug("Flash Deal Notification could not be pushed to android device because of server error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_GCM_SERVER_ERROR+responseMsg);
		}*/
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_INVALID_PAYLOAD_SIZE_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of an INVALID PAYLOAD SIZE error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_INVALID_PAYLOAD_SIZE_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_INVALID_TOKEN_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of an INVALID TOKEN error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_INVALID_TOKEN_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_INVALID_TOKEN_SIZE_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of an INVALID TOKEN SIZE error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_INVALID_TOKEN_SIZE_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_INVALID_TOPIC_SIZE_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of an INVALID TOPIC SIZE error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_INVALID_TOPIC_SIZE_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_MISSING_DEVICE_TOKEN_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of MISSING DEVICE TOKEN error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_MISSING_DEVICE_TOKEN_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_MISSING_PAYLOAD_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of MISSING PAYLOAD ERROR error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_MISSING_PAYLOAD_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_MISSING_TOPIC_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of MISSING TOPIC ERROR error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_MISSING_TOPIC_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_PROCESSING_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of PROCESSING ERROR error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_PROCESSING_ERROR+responseMsg);
		}
		else if(notificationResponseVO.getResponseCode().equals(NotificationEngineConstants.NOTIFICATION_APNS_PUSH_UNKNOWN_ERROR)){
			logger.debug("Flash Deal Notification could not be pushed to iOS device because of an UNKNOWN error :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_UNKNOWN_ERROR+responseMsg);
		}
	   else{
			logger.debug("Flash Deal Notification Failed to be pushed with unknown response code :"+responseMsg);
			notificationResponseVO.setResponseCode(NotificationEngineConstants.MESSAGE_RESPONSE_APNS_UNKNOWN_RESPONSE_CODE+responseMsg);
		}	

		return notificationResponseVO;	
    }
       

}
