/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.requests;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPost;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSPlaceGroupRequest extends WSBaseRequest {
	
	private String placeName;
	
	private String placeMapId;
	
	private String latitude;
	
	private String longitude;
	
	private int pageCount;
	
	private WSPlaceGroupPost placeGroupPost;
	
	private WSPlaceGroupPoll placeGroupPoll;

	/**
	 * @return the placeName
	 */
	public String getPlaceName() {
		return placeName;
	}

	/**
	 * @param placeName the placeName to set
	 */
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	/**
	 * @return the placeMapId
	 */
	public String getPlaceMapId() {
		return placeMapId;
	}

	/**
	 * @param placeMapId the placeMapId to set
	 */
	public void setPlaceMapId(String placeMapId) {
		this.placeMapId = placeMapId;
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
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount the pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/**
	 * @return the placeGroupPost
	 */
	public WSPlaceGroupPost getPlaceGroupPost() {
		return placeGroupPost;
	}

	/**
	 * @param placeGroupPost the placeGroupPost to set
	 */
	public void setPlaceGroupPost(WSPlaceGroupPost placeGroupPost) {
		this.placeGroupPost = placeGroupPost;
	}

	/**
	 * @return the placeGroupPoll
	 */
	public WSPlaceGroupPoll getPlaceGroupPoll() {
		return placeGroupPoll;
	}

	/**
	 * @param placeGroupPoll the placeGroupPoll to set
	 */
	public void setPlaceGroupPoll(WSPlaceGroupPoll placeGroupPoll) {
		this.placeGroupPoll = placeGroupPoll;
	}

}
