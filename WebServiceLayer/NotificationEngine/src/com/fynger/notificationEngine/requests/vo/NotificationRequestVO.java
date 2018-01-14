package com.fynger.notificationEngine.requests.vo;

import java.util.Map;

public class NotificationRequestVO {
	
	private Map<String, String> nameValuePairs;
    
    private String registrationId;

	/**
	 * @return the nameValuePairs
	 */
	public Map<String, String> getNameValuePairs() {
		return nameValuePairs;
	}

	/**
	 * @param nameValuePairs the nameValuePairs to set
	 */
	public void setNameValuePairs(Map<String, String> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}

	/**
	 * @return the registrationId
	 */
	public String getRegistrationId() {
		return registrationId;
	}

	/**
	 * @param registrationId the registrationId to set
	 */
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

   
}
