package com.fynger.searchEngine.requests.vo;


public class FlashDealSearchRequestVO extends BaseRequestVO {

	private String keyword;

    private String location;

    private double latitude;

    private double longitude;
    

    /**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = "\""+keyword+"\"";
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the latitude
	 */
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

	@Override
    public String toString(){
        String object = "";
        //Implement this method to print all the attributes on log for debugging purposes

        return object;
    }

}
