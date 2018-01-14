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
public class WSReview {
	
	private String userName;
	
	private String reviewText;
	
	private String userPicturePath;
	

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
	 * @return the reviewText
	 */
	public String getReviewText() {
		return reviewText;
	}

	/**
	 * @param reviewText the reviewText to set
	 */
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
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
	
	@Override
    public String toString(){
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("[");
        sBuffer.append("User Name : " + userName).append(" || ");
        sBuffer.append("Review Text : " + reviewText).append(" || ");
        sBuffer.append("User Picture Path : " + userPicturePath);
        sBuffer.append("]");

        return sBuffer.toString();
    }

}
