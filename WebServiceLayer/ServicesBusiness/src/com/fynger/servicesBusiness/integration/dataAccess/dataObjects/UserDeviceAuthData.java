/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class UserDeviceAuthData {
	
	private String username;
	
	private String deviceRegId;
	
	private String deviceType;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the deviceRegId
	 */
	public String getDeviceRegId() {
		return deviceRegId;
	}

	/**
	 * @param deviceRegId the deviceRegId to set
	 */
	public void setDeviceRegId(String deviceRegId) {
		this.deviceRegId = deviceRegId;
	}

	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
