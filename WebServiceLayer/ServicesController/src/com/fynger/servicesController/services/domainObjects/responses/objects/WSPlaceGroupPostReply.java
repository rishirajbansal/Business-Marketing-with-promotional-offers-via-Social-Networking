/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses.objects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSPlaceGroupPostReply {
	
	private String repliedText;
	
	private String repliedUsername;
	
	private String userPicturePath;
	
	private String createdTimestamp;

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
        sBuffer.append("Replied Text : " + repliedText).append(" || ");
        sBuffer.append("Replied Username : " + repliedUsername).append(" || ");
        sBuffer.append("User Picture Path : " + userPicturePath).append(" || ");
        sBuffer.append("Created Timestamp : " + createdTimestamp);
        sBuffer.append("]");

        return sBuffer.toString();
    }

}
