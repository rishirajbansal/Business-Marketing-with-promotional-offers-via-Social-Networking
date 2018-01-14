/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class ListingReviewsData {
	
	private int id;
	
	private int listingId;
	
	private String username;
	
	private String userPicturePath;
	
	private String reviewText;
	
	private boolean merchantAssociation;
	

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
	 * @return the listingId
	 */
	public int getListingId() {
		return listingId;
	}

	/**
	 * @param listingId the listingId to set
	 */
	public void setListingId(int listingId) {
		this.listingId = listingId;
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
	 * @return the merchantAssociation
	 */
	public boolean isMerchantAssociation() {
		return merchantAssociation;
	}

	/**
	 * @param merchantAssociation the merchantAssociation to set
	 */
	public void setMerchantAssociation(boolean merchantAssociation) {
		this.merchantAssociation = merchantAssociation;
	}
	


}
