/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

/**
 * @author Rishi
 *
 */
public class CityGeoData {
	
	private int cityId;
	
	private String cityName;
	
	private String cityCenterAddress;
	
	private String cityCenterLatitude;
	
	private String cityCenterLongitude;

	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the cityCenterAddress
	 */
	public String getCityCenterAddress() {
		return cityCenterAddress;
	}

	/**
	 * @param cityCenterAddress the cityCenterAddress to set
	 */
	public void setCityCenterAddress(String cityCenterAddress) {
		this.cityCenterAddress = cityCenterAddress;
	}

	/**
	 * @return the cityCenterLatitude
	 */
	public String getCityCenterLatitude() {
		return cityCenterLatitude;
	}

	/**
	 * @param cityCenterLatitude the cityCenterLatitude to set
	 */
	public void setCityCenterLatitude(String cityCenterLatitude) {
		this.cityCenterLatitude = cityCenterLatitude;
	}

	/**
	 * @return the cityCenterLongitude
	 */
	public String getCityCenterLongitude() {
		return cityCenterLongitude;
	}

	/**
	 * @param cityCenterLongitude the cityCenterLongitude to set
	 */
	public void setCityCenterLongitude(String cityCenterLongitude) {
		this.cityCenterLongitude = cityCenterLongitude;
	}

	
}
