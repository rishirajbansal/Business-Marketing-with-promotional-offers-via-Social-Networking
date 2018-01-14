/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.requests;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPost;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSShoutGroupRequest extends WSBaseRequest {
	
	private String latitude;
	
	private String longitude;
	
	private int pageCount;
	
	private WSShoutGroupPost shoutGroupPost;
	
	private WSShoutGroupPoll shoutGroupPoll;

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
	 * @return the shoutGroupPost
	 */
	public WSShoutGroupPost getShoutGroupPost() {
		return shoutGroupPost;
	}

	/**
	 * @param shoutGroupPost the shoutGroupPost to set
	 */
	public void setShoutGroupPost(WSShoutGroupPost shoutGroupPost) {
		this.shoutGroupPost = shoutGroupPost;
	}

	/**
	 * @return the shoutGroupPoll
	 */
	public WSShoutGroupPoll getShoutGroupPoll() {
		return shoutGroupPoll;
	}

	/**
	 * @param shoutGroupPoll the shoutGroupPoll to set
	 */
	public void setShoutGroupPoll(WSShoutGroupPoll shoutGroupPoll) {
		this.shoutGroupPoll = shoutGroupPoll;
	}

	
}
