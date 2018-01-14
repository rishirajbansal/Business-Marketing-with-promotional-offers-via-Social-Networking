/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */

@XmlRootElement
public class WSErrorResponse extends WSBaseResponse {
	
	private String code;
	
	private String userMessage;
	
	private String errorMessage;
	

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the userMessage
	 */
	public String getUserMessage() {
		return userMessage;
	}

	/**
	 * @param userMessage the userMessage to set
	 */
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
