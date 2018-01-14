/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class EmailLogData {
	
	private String username;
	
	private String event;
	
	private String dispatchStatus;
	
	private String comments;
	
	private Timestamp sentTime;

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
