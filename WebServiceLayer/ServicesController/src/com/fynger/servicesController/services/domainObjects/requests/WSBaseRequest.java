/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.requests;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */

@XmlRootElement
public class WSBaseRequest {
	
	private String username;
	
	private String password;
	
	private String locationString;
	
	private String locationCoordinates;

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the locationString
	 */
	public String getLocationString() {
		return locationString;
	}

	/**
	 * @param locationString the locationString to set
	 */
	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	/**
	 * @return the locationCoordinates
	 */
	public String getLocationCoordinates() {
		return locationCoordinates;
	}

	/**
	 * @param locationCoordinates the locationCoordinates to set
	 */
	public void setLocationCoordinates(String locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}

}
