/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class UserData {
	
	private String loginId;
	
	private String username;
	
	private String password;
	
	private String locationCoordinates;
	
	private String previousLocationCoordinates;
	
	private String fullName;
	
	private String email;
	
	private String zipcode;
	
	private String gender;
	
	private String dateOfBirth;
	
	private String phone;
	
	private String userPicturePath;
	
	private Timestamp createdOn;
	
	private Timestamp lasUpdated;
	

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the locationCoordinates
	 */
	public String getLocationCoordinates() {
		return locationCoordinates;
	}

	/**
	 * @param locationCoordinates the locationCoordinates to set
	 */
	public void setLocationCoordinates(String locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}

	/**
	 * @return the previousLocationCoordinates
	 */
	public String getPreviousLocationCoordinates() {
		return previousLocationCoordinates;
	}

	/**
	 * @param previousLocationCoordinates the previousLocationCoordinates to set
	 */
	public void setPreviousLocationCoordinates(String previousLocationCoordinates) {
		this.previousLocationCoordinates = previousLocationCoordinates;
	}

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
	 * @return the createdOn
	 */
	public Timestamp getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the lasUpdated
	 */
	public Timestamp getLasUpdated() {
		return lasUpdated;
	}

	/**
	 * @param lasUpdated the lasUpdated to set
	 */
	public void setLasUpdated(Timestamp lasUpdated) {
		this.lasUpdated = lasUpdated;
	}
	
	
    
}
