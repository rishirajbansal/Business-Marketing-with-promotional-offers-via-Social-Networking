package com.fynger.servicesController.services.domainObjects.responses;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class WSBaseResponse {
	
	private String status;
	
	private String response;
	
	private String totalPages;
	
	private String totalResults;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return the totalPages
	 */
	public String getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the totalResults
	 */
	public String getTotalResults() {
		return totalResults;
	}

	/**
	 * @param totalResults the totalResults to set
	 */
	public void setTotalResults(String totalResults) {
		this.totalResults = totalResults;
	}
	
	

}
