/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class FlashDealRequestData {
	
	private int flashDealId;
	
	private int merchantId;
	
	private String username;
	
	private String requestCategory;
	
	private String requestLocation;
	
	private int hasResponded;
	
	private int status;
	
	private int notified;
	
	private Timestamp requestedTimestamp;
	
	private Timestamp responsedTimestamp;
	
	private int requestExpiryPeriod;
	
	private int storeId;
	
	private String responseText;
	
	private String userEmailId;
	
	
	/**
	 * @return the flashDealId
	 */
	public int getFlashDealId() {
		return flashDealId;
	}

	/**
	 * @param flashDealId the flashDealId to set
	 */
	public void setFlashDealId(int flashDealId) {
		this.flashDealId = flashDealId;
	}

	/**
	 * @return the merchantId
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

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
	 * @return the requestCategory
	 */
	public String getRequestCategory() {
		return requestCategory;
	}

	/**
	 * @param requestCategory the requestCategory to set
	 */
	public void setRequestCategory(String requestCategory) {
		this.requestCategory = requestCategory;
	}

	/**
	 * @return the requestLocation
	 */
	public String getRequestLocation() {
		return requestLocation;
	}

	/**
	 * @param requestLocation the requestLocation to set
	 */
	public void setRequestLocation(String requestLocation) {
		this.requestLocation = requestLocation;
	}
	
	/**
	 * @return the hasResponded
	 */
	public int getHasResponded() {
		return hasResponded;
	}

	/**
	 * @param hasResponded the hasResponded to set
	 */
	public void setHasResponded(int hasResponded) {
		this.hasResponded = hasResponded;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the notified
	 */
	public int getNotified() {
		return notified;
	}

	/**
	 * @param notified the notified to set
	 */
	public void setNotified(int notified) {
		this.notified = notified;
	}

	/**
	 * @return the requestedTimestamp
	 */
	public Timestamp getRequestedTimestamp() {
		return requestedTimestamp;
	}

	/**
	 * @param requestedTimestamp the requestedTimestamp to set
	 */
	public void setRequestedTimestamp(Timestamp requestedTimestamp) {
		this.requestedTimestamp = requestedTimestamp;
	}

	/**
	 * @return the responsedTimestamp
	 */
	public Timestamp getResponsedTimestamp() {
		return responsedTimestamp;
	}

	/**
	 * @param responsedTimestamp the responsedTimestamp to set
	 */
	public void setResponsedTimestamp(Timestamp responsedTimestamp) {
		this.responsedTimestamp = responsedTimestamp;
	}

	/**
	 * @return the requestExpiryPeriod
	 */
	public int getRequestExpiryPeriod() {
		return requestExpiryPeriod;
	}

	/**
	 * @param requestExpiryPeriod the requestExpiryPeriod to set
	 */
	public void setRequestExpiryPeriod(int requestExpiryPeriod) {
		this.requestExpiryPeriod = requestExpiryPeriod;
	}

	/**
	 * @return the storeId
	 */
	public int getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the responseText
	 */
	public String getResponseText() {
		return responseText;
	}

	/**
	 * @param responseText the responseText to set
	 */
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

	/**
	 * @return the userEmailId
	 */
	public String getUserEmailId() {
		return userEmailId;
	}

	/**
	 * @param userEmailId the userEmailId to set
	 */
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	
}
