/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class MerchantReviewsData {
	
	private int id;
	
	private int merchantStoreId;
	
	private String username;
	
	private String userPicturePath;
	
	private String reviewText;
	
	private boolean listingAssociation;
	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the merchantStoreId
	 */
	public int getMerchantStoreId() {
		return merchantStoreId;
	}

	/**
	 * @param merchantStoreId the merchantStoreId to set
	 */
	public void setMerchantStoreId(int merchantStoreId) {
		this.merchantStoreId = merchantStoreId;
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
	 * @return the listingAssociation
	 */
	public boolean isListingAssociation() {
		return listingAssociation;
	}

	/**
	 * @param listingAssociation the listingAssociation to set
	 */
	public void setListingAssociation(boolean listingAssociation) {
		this.listingAssociation = listingAssociation;
	}

}
