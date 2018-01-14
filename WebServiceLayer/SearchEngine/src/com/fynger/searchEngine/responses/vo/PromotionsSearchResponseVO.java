package com.fynger.searchEngine.responses.vo;

public class PromotionsSearchResponseVO extends BaseResponseVO{

	private String name;

    private String address;
    
    private String promotionText;
    
    private String validityPeriod;
    
    private int entityId;
    
    private String city;
    
    private String state;
    
    private String pinCode;
    
    private double latitude;

    private double longitude;
    
    private String promotionType;
    
        
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
	 * @return the entityId
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
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
	 * @param the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the pinCode
	 */
	public String getPinCode() {
		return pinCode;
	}
	
	/**
	 * @param the state to set
	 */
	
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @param promotype to set
	 */

	public void setPromotionType(String promoType) {
		this.promotionType = promoType;
	}

	/**
	 * @return the promotion type
	 */
	public String getPromotionType() {
		return promotionType;
	}


	@Override
    public String toString(){
        String object = "";
        //Implement this method to print all the attributes on log for debugging purposes

        return object;
    }

}
