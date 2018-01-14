/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class PlaceGroupPostData {
	
	private int postId;
	
	private int placeGroupId;
	
	/* Contains user full name */
	private String createdUsername;
	
	private String postText;
	
	private String postImagePath;
	
	private String userPicturePath;
	
	private Timestamp createdTimestamp;
	
	private String username;

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
	 * @return the placeGroupId
	 */
	public int getPlaceGroupId() {
		return placeGroupId;
	}

	/**
	 * @param placeGroupId the placeGroupId to set
	 */
	public void setPlaceGroupId(int placeGroupId) {
		this.placeGroupId = placeGroupId;
	}

	/**
	 * @return the createdUsername
	 */
	public String getCreatedUsername() {
		return createdUsername;
	}

	/**
	 * @param createdUsername the createdUsername to set
	 */
	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	/**
	 * @return the postText
	 */
	public String getPostText() {
		return postText;
	}

	/**
	 * @param postText the postText to set
	 */
	public void setPostText(String postText) {
		this.postText = postText;
	}

	/**
	 * @return the postImagePath
	 */
	public String getPostImagePath() {
		return postImagePath;
	}

	/**
	 * @param postImagePath the postImagePath to set
	 */
	public void setPostImagePath(String postImagePath) {
		this.postImagePath = postImagePath;
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
