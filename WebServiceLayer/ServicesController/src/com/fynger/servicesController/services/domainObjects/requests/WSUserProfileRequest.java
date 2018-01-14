/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.requests;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rishi
 *
 */

@XmlRootElement
public class WSUserProfileRequest extends WSBaseRequest {
	
	private String fullName;
	
	private String email;
	
	private String zipcode;
	
	private String gender;
	
	private String dateOfBirth;
	
	private String phone;
	
	private String userPicturePath;
	
	private InputStream pictureStream;
	
	private String pictureExt;

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
			

}
