/**
 * 
 */
package com.fynger.servicesBusiness.objects;

import java.util.HashMap;
import java.util.Map;

import com.fynger.notificationEngine.constants.NotificationEngineConstants;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRespondedData;

/**
 * @author Rishi
 *
 */
public class NotificationMessageAssembler {
	
	
	public Map<String, String> getFlashDealMessageContent(FlashDealRespondedData data) {
		
		Map<String, String> nameValuePairs = new HashMap<String, String>();
		
		String flashDealId = Integer.toString(data.getFlashDealId());
		
		nameValuePairs.put(NotificationEngineConstants.DEVICE_TYPE, data.getDeviceType());
		nameValuePairs.put(NotificationEngineConstants.KEY_FLASHDEAL_ID, flashDealId);
		nameValuePairs.put(NotificationEngineConstants.MESSAGE_TYPE, Integer.toString(NotificationEngineConstants.MESSAGE_TYPE_FLASHDEAL));
		nameValuePairs.put(NotificationEngineConstants.MESSAGE, data.getResponseText());
		nameValuePairs.put(NotificationEngineConstants.USER_NAME, data.getUsername());
		nameValuePairs.put(NotificationEngineConstants.NAME, data.getMerchantName());

		return nameValuePairs;
	}
	
	public Map<String, String> getShoutGroupPostReplyMessageContent(String username, String name, String postReply, String deviceType, String postId) {
		
		Map<String, String> nameValuePairs = new HashMap<String, String>();
		
		nameValuePairs.put(NotificationEngineConstants.DEVICE_TYPE, deviceType);
		nameValuePairs.put(NotificationEngineConstants.MESSAGE, postReply);
		nameValuePairs.put(NotificationEngineConstants.USER_NAME, username);
		nameValuePairs.put(NotificationEngineConstants.NAME, name);
		nameValuePairs.put(NotificationEngineConstants.MESSAGE_TYPE, Integer.toString(NotificationEngineConstants.MESSAGE_TYPE_SHOUT));
		nameValuePairs.put(NotificationEngineConstants.KEY_POST_ID, postId);

		return nameValuePairs;
	}
	
	public Map<String, String> getPlaceGroupPostReplyMessageContent(String username, String name, String postReply, String deviceType, String postId) {
		
		Map<String, String> nameValuePairs = new HashMap<String, String>();
		
		nameValuePairs.put(NotificationEngineConstants.DEVICE_TYPE, deviceType);
		nameValuePairs.put(NotificationEngineConstants.MESSAGE, postReply);
		nameValuePairs.put(NotificationEngineConstants.USER_NAME, username);
		nameValuePairs.put(NotificationEngineConstants.NAME, name);
		nameValuePairs.put(NotificationEngineConstants.MESSAGE_TYPE, Integer.toString(NotificationEngineConstants.MESSAGE_TYPE_PLACES));
		nameValuePairs.put(NotificationEngineConstants.KEY_POST_ID, postId);

		return nameValuePairs;
	}

}
