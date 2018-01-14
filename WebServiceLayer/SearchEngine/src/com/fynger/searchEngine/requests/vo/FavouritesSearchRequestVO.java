
package com.fynger.searchEngine.requests.vo;

import java.util.List;


public class FavouritesSearchRequestVO extends BaseRequestVO{

	private double latitude;

    private double longitude;
    
    private List<String> categories;
    
    private List<String> brands;
    
    private List<String> places;
    

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

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the brands
	 */
	public List<String> getBrands() {
		return brands;
	}

	/**
	 * @param brands the brands to set
	 */
	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	/**
	 * @return the places
	 */
	public List<String> getPlaces() {
		return places;
	}

	/**
	 * @param places the places to set
	 */
	public void setPlaces(List<String> places) {
		this.places = places;
	}

	@Override
    public String toString(){
        String object = "";
        //Implement this method to print all the attributes on log for debugging purposes

        return object;
    }

}
