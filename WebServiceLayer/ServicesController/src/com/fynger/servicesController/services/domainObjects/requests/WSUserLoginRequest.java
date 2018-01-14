package com.fynger.servicesController.services.domainObjects.requests;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSUserDeviceAuth;

@XmlRootElement
public class WSUserLoginRequest extends WSBaseRequest{
	
	private String FBEmail;
	
	private String fyngerUser;
	
	private WSUserDeviceAuth deviceAuth;
	

	/**
	 * @return the fBEmail
	 */
	public String getFBEmail() {
		return FBEmail;
	}

	/**
	 * @param fBEmail the fBEmail to set
	 */
	public void setFBEmail(String fBEmail) {
		FBEmail = fBEmail;
	}

	/**
	 * @return the fyngerUser
	 */
	public String getFyngerUser() {
		return fyngerUser;
	}

	/**
	 * @param fyngerUser the fyngerUser to set
	 */
	public void setFyngerUser(String fyngerUser) {
		this.fyngerUser = fyngerUser;
	}

	/**
	 * @return the deviceAuth
	 */
	public WSUserDeviceAuth getDeviceAuth() {
		return deviceAuth;
	}

	/**
	 * @param deviceAuth the deviceAuth to set
	 */
	public void setDeviceAuth(WSUserDeviceAuth deviceAuth) {
		this.deviceAuth = deviceAuth;
	}
	
}
