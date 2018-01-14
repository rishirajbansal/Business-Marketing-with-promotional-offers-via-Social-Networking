/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses.objects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSUserDeviceAuth {
	
	private String deviceRegistrationId;
	
	private String deviceType;

	/**
	 * @return the deviceRegistrationId
	 */
	public String getDeviceRegistrationId() {
		return deviceRegistrationId;
	}

	/**
	 * @param deviceRegistrationId the deviceRegistrationId to set
	 */
	public void setDeviceRegistrationId(String deviceRegistrationId) {
		this.deviceRegistrationId = deviceRegistrationId;
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
