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
public class WSPromotion {
	
	private String name;

    private String address;
    
    private String promotionText;
    
    private String validityPeriod;
    
    private String city;

    private String state;
    
    private String phone;
    
    private String mobile;
    
    private String categories;
    
    private String latitude;

    private String longitude;
    
    private String distance;
    
    private String entityId;
    
    private String entityType;
    
    private String rating;
    
    private String reviewsCount;
    
    private String favouritesCount;
    
    private String storeId;
    
    private String multimediaPath;
    
    private String multimediaType;
    
    private String favouritePromotionId;	

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the promotionText
	 */
	public String getPromotionText() {
		return promotionText;
	}

	/**
	 * @param promotionText the promotionText to set
	 */
	public void setPromotionText(String promotionText) {
		this.promotionText = promotionText;
	}

	/**
	 * @return the validityPeriod
	 */
	public String getValidityPeriod() {
		return validityPeriod;
	}

	/**
	 * @param validityPeriod the validityPeriod to set
	 */
	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
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
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the categories
	 */
	public String getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(String categories) {
		this.categories = categories;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the rating
	 */
	public String getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(String rating) {
		this.rating = rating;
	}

	/**
	 * @return the reviewsCount
	 */
	public String getReviewsCount() {
		return reviewsCount;
	}

	/**
	 * @param reviewsCount the reviewsCount to set
	 */
	public void setReviewsCount(String reviewsCount) {
		this.reviewsCount = reviewsCount;
	}

	/**
	 * @return the favouritesCount
	 */
	public String getFavouritesCount() {
		return favouritesCount;
	}

	/**
	 * @param favouritesCount the favouritesCount to set
	 */
	public void setFavouritesCount(String favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the multimediaPath
	 */
	public String getMultimediaPath() {
		return multimediaPath;
	}

	/**
	 * @param multimediaPath the multimediaPath to set
	 */
	public void setMultimediaPath(String multimediaPath) {
		this.multimediaPath = multimediaPath;
	}

	/**
	 * @return the favouritePromotionId
	 */
	public String getFavouritePromotionId() {
		return favouritePromotionId;
	}

	/**
	 * @param favouritePromotionId the favouritePromotionId to set
	 */
	public void setFavouritePromotionId(String favouritePromotionId) {
		this.favouritePromotionId = favouritePromotionId;
	}
	
	/**
	 * @return the multimediaType
	 */
	public String getMultimediaType() {
		return multimediaType;
	}

	/**
	 * @param multimediaType the multimediaType to set
	 */
	public void setMultimediaType(String multimediaType) {
		this.multimediaType = multimediaType;
	}


	@Override
    public String toString(){
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("[");
        sBuffer.append("Entity Id : " + entityId).append(" || ");
        sBuffer.append("Entity Type : " + entityType).append(" || ");
        sBuffer.append("Favourite Promotion Id : " + favouritePromotionId).append(" || ");
        sBuffer.append("Promotion Text : " + promotionText).append(" || ");
        sBuffer.append("Validity Period : " + validityPeriod).append(" || ");
        sBuffer.append("Name : " + name).append(" || ");
        sBuffer.append("Address : " + address).append(" || ");
        sBuffer.append("City : " + city).append(" || ");
        sBuffer.append("State : " + state).append(" || ");
        sBuffer.append("Phone : " + phone).append(" || ");
        sBuffer.append("Categories : " + categories).append(" || ");
        sBuffer.append("Latitude : " + latitude).append(" || ");
        sBuffer.append("Longitude : " + longitude).append(" || ");
        sBuffer.append("Distance : " + distance).append(" || ");
        sBuffer.append("Rating : " + rating).append(" || ");
        sBuffer.append("Reviews Count : " + reviewsCount).append(" || ");
        sBuffer.append("Store Id : " + storeId).append(" || ");
        sBuffer.append("multimediaPath : " + multimediaPath).append(" || ");
        sBuffer.append("multimediaType : " + multimediaType).append(" || ");
        sBuffer.append("Favourites Count : " + favouritesCount);
        sBuffer.append("]");

        return sBuffer.toString();
    }

	

}
