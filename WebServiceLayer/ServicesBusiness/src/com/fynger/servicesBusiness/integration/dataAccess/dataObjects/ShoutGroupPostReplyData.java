/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class ShoutGroupPostReplyData {
	
	private int postId;
	
	private String repliedUsername;
	
	private String repliedText;
	
	private String userPicturePath;
	
	private int totalResults;
	
	private Timestamp createdTimestamp;

	/**
	 * @return the postId
	 */
	public int getPostId() {
		return postId;
	}

	/**
	 * @param postId the postId to set
	 */
	public void setPostId(int postId) {
		this.postId = postId;
	}

	/**
	 * @return the repliedUsername
	 */
	public String getRepliedUsername() {
		return repliedUsername;
	}

	/**
	 * @param repliedUsername the repliedUsername to set
	 */
	public void setRepliedUsername(String repliedUsername) {
		this.repliedUsername = repliedUsername;
	}

	/**
	 * @return the repliedText
	 */
	public String getRepliedText() {
		return repliedText;
	}

	/**
	 * @param repliedText the repliedText to set
	 */
	public void setRepliedText(String repliedText) {
		this.repliedText = repliedText;
	}

	/**
	 * @return the userPicturePath
	 */
	public String getUserPicturePath() {
		return userPicturePath;
	}

	/**
	 * @param userPicturePath the userPicturePath to set
	 */
	public void setUserPicturePath(String userPicturePath) {
		this.userPicturePath = userPicturePath;
	}

	/**
	 * @return the totalResults
	 */
	public int getTotalResults() {
		return totalResults;
	}

	/**
	 * @param totalResults the totalResults to set
	 */
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * @return the createdTimestamp
	 */
	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	
}
