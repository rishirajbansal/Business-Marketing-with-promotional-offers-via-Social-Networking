package com.fynger.notificationEngine.constants;



public class NotificationEngineConstants {
	
	public static final String MESSAGE											= "Message";
	public static final String DEVICE_TYPE										= "DeviceType";
	public static final String MESSAGE_TYPE										= "MessageType";

	public static final String KEY_STORE	    			 	             	= "iOSKeyStore";
	public static final String KEY_STORE_PASSWORD  			 	             	= "iOSKeyStorePassword";
	public static final String FLASHDEAL_RESPONSE_MESSAGE	 	             	= "Flash Deal Response from ";
	public static final String SHOUT_RESPONSE_MESSAGE	 	             		= " has replied to your post. ";
	public static final String PLACES_RESPONSE_MESSAGE	 	             		= " has replied to your post. This is a places response ";
	
	public static final String MESSAGE_RESPONSE_CANNONICAL_1	 	            = "Push Notification Sent Successfully. Same device has more than one registration ID, Update old RegistrationId =";
	public static final String MESSAGE_RESPONSE_CANNONICAL_2	 	            = " with new RegistrationId =";
	public static final String MESSAGE_RESPONSE_SUCCESS			 	            = "Push Notification Sent Successsfully";
	public static final String MESSAGE_RESPONSE_UNSUCCESSFULL_APP_REMOVED       = "Push Notification Sending Unsuccessful. Application has been removed from device, unregister user from database";
	public static final String MESSAGE_RESPONSE_FAILURE			 	            = "Push Notification Sending Unsuccessful";
	public static final String MESSAGE_RESPONSE_GCM_AUTHENTICATION_ERROR        = "Flash Deal Notification could not be pushed to android device because of an authentication error :";
	public static final String MESSAGE_RESPONSE_GCM_JSON_ERROR			        = "Flash Deal Notification could not be pushed to android device because of JSON error :";
	public static final String MESSAGE_RESPONSE_GCM_SERVER_ERROR				= "Flash Deal Notification could not be pushed to android device because of server error :";
	public static final String MESSAGE_RESPONSE_APNS_INVALID_PAYLOAD_SIZE_ERROR	= "Flash Deal Notification could not be pushed to iOS device because of an INVALID PAYLOAD SIZE error :";
	public static final String MESSAGE_RESPONSE_APNS_INVALID_TOKEN_ERROR		= "Flash Deal Notification could not be pushed to iOS device because of an INVALID TOKEN error :";
	public static final String MESSAGE_RESPONSE_APNS_INVALID_TOKEN_SIZE_ERROR	= "Flash Deal Notification could not be pushed to iOS device because of an INVALID TOKEN SIZE error :";
	public static final String MESSAGE_RESPONSE_APNS_INVALID_TOPIC_SIZE_ERROR	= "Flash Deal Notification could not be pushed to iOS device because of an INVALID TOPIC SIZE error :";
	public static final String MESSAGE_RESPONSE_APNS_MISSING_DEVICE_TOKEN_ERROR	= "Flash Deal Notification could not be pushed to iOS device because of MISSING DEVICE TOKEN error :";
	public static final String MESSAGE_RESPONSE_APNS_MISSING_PAYLOAD_ERROR		= "Flash Deal Notification could not be pushed to iOS device because of MISSING PAYLOAD ERROR error :";
	public static final String MESSAGE_RESPONSE_APNS_MISSING_TOPIC_ERROR		= "Flash Deal Notification could not be pushed to iOS device because of MISSING TOPIC ERROR error :";
	public static final String MESSAGE_RESPONSE_APNS_PROCESSING_ERROR			= "Flash Deal Notification could not be pushed to iOS device because of PROCESSING ERROR error :";
	public static final String MESSAGE_RESPONSE_APNS_UNKNOWN_ERROR				= "Flash Deal Notification could not be pushed to iOS device because of an UNKNOWN error :";	
	public static final String MESSAGE_RESPONSE_APNS_UNKNOWN_RESPONSE_CODE		= "Flash Deal Notification Failed to be pushed with unknown response code :";

	
	public static final String FULL_STOP					 	             	= ".";
	public static final String SINGLE_SPACE					 	             	= " ";
	
	public static final String GOOGLE_API_KEY						 		    = "googleAPIKey";
	public static final String RETRIES						 	             	= "retries";
	public static final String TIME_TO_LIVE  				 	             	= "timeToLive";
	public static final String COLLAPSE_KEY  				 	             	= "collapseKey";
	public static final String NOT_REGISTERED    			 	             	= "NotRegistered";
	
	public static final String NOTIFICATIONENGINE_PROPERTIES_FILE_NAME 		    = "notificationEngine.properties";	
	
	public static final String KEY_FLASHDEAL_ID    			 	             	= "idFlashDeal";
	public static final String KEY_POST_ID	    			 	             	= "postId";
	public static final String KEY_PLACES_ID	    			 	            = "placesId";
	public static final String NAME	    			 	            			= "name";
	public static final String USER_NAME	    								= "userName";
	
	public static final boolean SANDBOX_SERVICE    			 	             	= false;
	public static final boolean PRODUCTION_SERVICE    			 	            = true;

		
	public static final int TYPE_ANDROID										= 1;
	public static final int TYPE_IOS	   										= 2;
	
	public static final int MESSAGE_TYPE_FLASHDEAL   							= 1;
	public static final int MESSAGE_TYPE_SHOUT 									= 2;
	public static final int MESSAGE_TYPE_PLACES		   							= 3;
	
	public static final String MESSAGE_TYPE_FLASHDEAL_STRING					= "Flash Deal";
	public static final String MESSAGE_TYPE_SHOUT_STRING						= "Shout";
	public static final String MESSAGE_TYPE_PLACES_STRING						= "Places";
	public static final String DEVICE_TYPE_ANDROID_STRING						= "Android";
	public static final String DEVICE_TYPE_iOS_STRING							= "iOS";
	
	public static final String NOTIFICATION_GCM_PUSH_SUCCESS  					= "200";
	public static final String NOTIFICATION_GCM_PUSH_JSON_ERROR					= "400";
	public static final String NOTIFICATION_GCM_PUSH_AUTHENTICATION_ERROR		= "401";
	public static final String NOTIFICATION_GCM_PUSH_SERVER_ERROR_UPPERLIMIT	= "599";
	public static final String NOTIFICATION_GCM_PUSH_SERVER_ERROR_LOWERLIMIT	= "500";
	public static final String NOTIFICATION_GCM_PUSH_FAILURE					= "999";
	
	
	public static final String NOTIFICATION_APNS_PUSH_SUCCESS  					= "0";
	public static final String NOTIFICATION_APNS_PUSH_PROCESSING_ERROR			= "1";
	public static final String NOTIFICATION_APNS_PUSH_MISSING_DEVICE_TOKEN_ERROR= "2";
	public static final String NOTIFICATION_APNS_PUSH_MISSING_TOPIC_ERROR		= "3";
	public static final String NOTIFICATION_APNS_PUSH_MISSING_PAYLOAD_ERROR		= "4";
	public static final String NOTIFICATION_APNS_PUSH_INVALID_TOKEN_SIZE_ERROR	= "5";
	public static final String NOTIFICATION_APNS_PUSH_INVALID_TOPIC_SIZE_ERROR	= "6";
	public static final String NOTIFICATION_APNS_PUSH_INVALID_PAYLOAD_SIZE_ERROR= "7";
	public static final String NOTIFICATION_APNS_PUSH_INVALID_TOKEN_ERROR		= "8";
	public static final String NOTIFICATION_APNS_PUSH_UNKNOWN_ERROR				= "255";

}
