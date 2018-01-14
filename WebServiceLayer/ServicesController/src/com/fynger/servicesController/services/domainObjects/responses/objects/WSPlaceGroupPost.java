/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses.objects;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSPlaceGroupPost {
	
	private String postId;
	
	private String placeGroupId;
	
	private String creatorUsername;
	
	private String postText;
	
	private String userPicturePath;
	
	private String postImagePath;
	
	private List<WSPlaceGroupPostReply> postReplies;
	
	private String postReply;
	
	private InputStream pictureStream;
	
	private String pictureExt;
	
	private String pictureFileName;
	
	private String createdTimestamp;

	/**
	 * @return the postId
	 */
	public String getPostId() {
		return postId;
	}

	/**
	 * @param postId the postId to set
	 */
	public void setPostId(String postId) {
		this.postId = postId;
	}

	/**
	 * @return the placeGroupId
	 */
	public String getPlaceGroupId() {
		return placeGroupId;
	}

	/**
	 * @param placeGroupId the placeGroupId to set
	 */
	public void setPlaceGroupId(String placeGroupId) {
		this.placeGroupId = placeGroupId;
	}

	/**
	 * @return the creatorUsername
	 */
	public String getCreatorUsername() {
		return creatorUsername;
	}

	/**
	 * @param creatorUsername the creatorUsername to set
	 */
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
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
	 * @return the postReplies
	 */
	public List<WSPlaceGroupPostReply> getPostReplies() {
		return postReplies;
	}

	/**
	 * @param postReplies the postReplies to set
	 */
	public void setPostReplies(List<WSPlaceGroupPostReply> postReplies) {
		this.postReplies = postReplies;
	}

	/**
	 * @return the postReply
	 */
	public String getPostReply() {
		return postReply;
	}

	/**
	 * @param postReply the postReply to set
	 */
	public void setPostReply(String postReply) {
		this.postReply = postReply;
	}

	/**
	 * @return the pictureStream
	 */
	public InputStream getPictureStream() {
		return pictureStream;
	}

	/**
	 * @param pictureStream the pictureStream to set
	 */
	public void setPictureStream(InputStream pictureStream) {
		this.pictureStream = pictureStream;
	}

	/**
	 * @return the pictureExt
	 */
	public String getPictureExt() {
		return pictureExt;
	}

	/**
	 * @param pictureExt the pictureExt to set
	 */
	public void setPictureExt(String pictureExt) {
		this.pictureExt = pictureExt;
	}

	/**
	 * @return the pictureFileName
	 */
	public String getPictureFileName() {
		return pictureFileName;
	}

	/**
	 * @param pictureFileName the pictureFileName to set
	 */
	public void setPictureFileName(String pictureFileName) {
		this.pictureFileName = pictureFileName;
	}
	
	/**
	 * @return the createdTimestamp
	 */
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	@Override
    public String toString(){
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("[");
        sBuffer.append("Post Id : " + postId).append(" || ");
        sBuffer.append("Place Group Id : " + placeGroupId).append(" || ");
        sBuffer.append("Created Username : " + creatorUsername).append(" || ");
        sBuffer.append("Post Text : " + postText).append(" || ");
        sBuffer.append("User Picture Path : " + userPicturePath).append(" || ");
        sBuffer.append("Post Image Path : " + postImagePath).append(" || ");
        sBuffer.append("Created Timestamp : " + createdTimestamp);
        sBuffer.append("]");

        return sBuffer.toString();
    }

}
