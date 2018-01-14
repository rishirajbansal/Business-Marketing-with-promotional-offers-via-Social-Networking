package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

public class NotificationLogData {
	
	private String event;
	
	private String userName;
	
	private String dispatchStatus;
	
	private int deviceType;
	
	private int retriedCount;
	
	private Timestamp sentTime;
	
	private String comments;
	
	private String deviceRegId;
	
	private String text;

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the dispatchStatus
	 */
	public String getDispatchStatus() {
		return dispatchStatus;
	}

	/**
	 * @param dispatchStatus the dispatchStatus to set
	 */
	public void setDispatchStatus(String dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}

	/**
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}

	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * @return the retriedCount
	 */
	public int getRetriedCount() {
		return retriedCount;
	}

	/**
	 * @param retriedCount the retriedCount to set
	 */
	public void setRetriedCount(int retriedCount) {
		this.retriedCount = retriedCount;
	}


	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the sentTime
	 */
	public Timestamp getSentTime() {
		return sentTime;
	}

	/**
	 * @param sentTime the sentTime to set
	 */
	public void setSentTime(Timestamp sentTime) {
		this.sentTime = sentTime;
	}

}
