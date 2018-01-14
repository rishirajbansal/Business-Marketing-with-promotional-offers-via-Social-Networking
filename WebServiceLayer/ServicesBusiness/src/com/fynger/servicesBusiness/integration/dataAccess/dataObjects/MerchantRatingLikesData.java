/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class MerchantRatingLikesData {
	
	private int merchantStoreId;
	
	private int rating;
	
	private int likesCount;
	
	private boolean merchantAssociation;
	

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
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the likesCount
	 */
	public int getLikesCount() {
		return likesCount;
	}

	/**
	 * @param likesCount the likesCount to set
	 */
	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
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
