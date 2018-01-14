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
public class WSFlashDeal {
	
	private String name;

    private String address;
    
    private String flashDealText;
    
    private String city;

    private String state;
    
    private String phone;
    
    private String mobile;
    
    private String categories;
    
    private String latitude;

    private String longitude;
    
    private String distance;
    
    private String flashDealId;
    

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
	 * @return the flashDealText
	 */
	public String getFlashDealText() {
		return flashDealText;
	}

	/**
	 * @param flashDealText the flashDealText to set
	 */
	public void setFlashDealText(String flashDealText) {
		this.flashDealText = flashDealText;
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
	 * @return the flashDealId
	 */
	public String getFlashDealId() {
		return flashDealId;
	}

	/**
	 * @param flashDealId the flashDealId to set
	 */
	public void setFlashDealId(String flashDealId) {
		this.flashDealId = flashDealId;
	}
	
	@Override
    public String toString(){
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("[");
        sBuffer.append("Flash Deal Id : " + flashDealId).append(" || ");
        sBuffer.append("Flash Deal Text : " + flashDealText).append(" || ");
        sBuffer.append("Name : " + name).append(" || ");
        sBuffer.append("Address : " + address).append(" || ");
        sBuffer.append("City : " + city).append(" || ");
        sBuffer.append("State : " + state).append(" || ");
        sBuffer.append("Phone : " + phone).append(" || ");
        sBuffer.append("Categories : " + categories).append(" || ");
        sBuffer.append("Latitude : " + latitude).append(" || ");
        sBuffer.append("Longitude : " + longitude).append(" || ");
        sBuffer.append("Distance : " + distance);
        sBuffer.append("]");

        return sBuffer.toString();
    }
}
