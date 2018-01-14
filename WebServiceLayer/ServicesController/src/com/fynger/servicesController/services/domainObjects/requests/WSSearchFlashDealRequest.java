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
public class WSSearchFlashDealRequest extends WSBaseRequest {
	
	private String searchText;
	
	private int pageCount;
	
	private String latitude;
	
	private String longitude;
	
	private String flashDealId;
	
	private String expiryPeriod;
	

	/**
	 * @return the searchText
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @param searchText the searchText to set
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the flashDealId
	 */
	public String getFlashDealId() {
		return flashDealId;
	}

	/**
	 * @param flashDealId the flashDealId to set
	 */
	public void setFlashDealId(String flashDealId) {
		this.flashDealId = flashDealId;
	}

	/**
	 * @return the expiryPeriod
	 */
	public String getExpiryPeriod() {
		return expiryPeriod;
	}

	/**
	 * @param expiryPeriod the expiryPeriod to set
	 */
	public void setExpiryPeriod(String expiryPeriod) {
		this.expiryPeriod = expiryPeriod;
	}

}
