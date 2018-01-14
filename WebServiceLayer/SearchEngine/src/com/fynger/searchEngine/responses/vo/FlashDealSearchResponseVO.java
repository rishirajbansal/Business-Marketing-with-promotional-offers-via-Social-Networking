package com.fynger.searchEngine.responses.vo;

public class FlashDealSearchResponseVO extends BaseResponseVO{
	
	private int merchantId;
	
	/**
	 * Case I: If search request VO contains location string then set this string in this field
	 * Case II: IF search request VO contains only coordinates then find out the location string from GMap API by reverse mapping and set it in this field 
	 */
	private String locationString;
	
	private int storeId;
	

	/**
	 * @return the merchantId
	 */
	public int getMerchantId() {
		return merchantId;
	}

	/**
	 * @param merchantId the merchantId to set
	 */
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

		/**
	 * @return the locationString
	 */
	public String getLocationString() {
		return locationString;
	}

	/**
	 * @param locationString the locationString to set
	 */
	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	/**
	 * @return the storeId
	 */
	public int getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	@Override
    public String toString(){
        String object = "";
        //Implement this method to print all the attributes on log for debugging purposes

        return object;
    }

}
