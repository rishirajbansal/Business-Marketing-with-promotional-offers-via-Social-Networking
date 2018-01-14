/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class FlashDealRespondedData {
	
	private int flashDealRespondedDataId;
	
	private int flashDealId;
	
	private int merchantId;
	
	private String username;
	
	private int basicStoreId;
	
	private String responseText;
	
	private Timestamp respondedOn;
	
	private String merchantName;
	
	private int notified;
	
	private String deviceRegId;
	
	private String deviceType;

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
	 * @return the basicStoreId
	 */
	public int getBasicStoreId() {
		return basicStoreId;
	}

	/**
	 * @param basicStoreId the basicStoreId to set
	 */
	public void setBasicStoreId(int basicStoreId) {
		this.basicStoreId = basicStoreId;
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
	 * @return the respondedOn
	 */
	public Timestamp getRespondedOn() {
		return respondedOn;
	}

	/**
	 * @param respondedOn the respondedOn to set
	 */
	public void setRespondedOn(Timestamp respondedOn) {
		this.respondedOn = respondedOn;
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

	/**
	 * @return the flashDealRespondedDataId
	 */
	public int getFlashDealRespondedDataId() {
		return flashDealRespondedDataId;
	}

	/**
	 * @param flashDealRespondedDataId the flashDealRespondedDataId to set
	 */
	public void setFlashDealRespondedDataId(int flashDealRespondedDataId) {
		this.flashDealRespondedDataId = flashDealRespondedDataId;
	}

	/**
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}

	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	

}
