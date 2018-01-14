package com.fynger.servicesController.services.domainObjects.responses;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WSUserLoginResponse extends WSBaseResponse{
	
	private String username;
	
	private String fbPassword;

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
	 * @return the fbPassword
	 */
	public String getFbPassword() {
		return fbPassword;
	}

	/**
	 * @param fbPassword the fbPassword to set
	 */
	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}
	
	
}
