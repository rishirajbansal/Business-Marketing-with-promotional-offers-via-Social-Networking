/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class ListingRatingLikesData {
	
	private int listingId;
	
	private int rating;
	
	private int likesCount;
	
	private boolean merchantAssociation;
	

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
