/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class UserFavouritesData {
	
	private String username;
	
	private String categoriesList;
	
	private String brandsList;
	
	private String placesList;

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
	 * @return the categoriesList
	 */
	public String getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @param categoriesList the categoriesList to set
	 */
	public void setCategoriesList(String categoriesList) {
		this.categoriesList = categoriesList;
	}

	/**
	 * @return the brandsList
	 */
	public String getBrandsList() {
		return brandsList;
	}

	/**
	 * @param brandsList the brandsList to set
	 */
	public void setBrandsList(String brandsList) {
		this.brandsList = brandsList;
	}

	/**
	 * @return the placesList
	 */
	public String getPlacesList() {
		return placesList;
	}

	/**
	 * @param placesList the placesList to set
	 */
	public void setPlacesList(String placesList) {
		this.placesList = placesList;
	}

}
