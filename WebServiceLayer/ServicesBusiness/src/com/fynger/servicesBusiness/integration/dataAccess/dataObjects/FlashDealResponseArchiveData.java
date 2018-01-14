/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class FlashDealResponseArchiveData {
	
	private int flashDealId;
	
	private int merchantId;
	
	private Timestamp responsedTimestamp;
	
	private String username;

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

}
