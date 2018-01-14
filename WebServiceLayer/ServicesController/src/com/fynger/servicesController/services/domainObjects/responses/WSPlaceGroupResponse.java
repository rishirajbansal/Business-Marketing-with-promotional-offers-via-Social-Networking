/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPost;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSPlaceGroupResponse extends WSBaseResponse {
	
	private String placeGroupId;
	
	private String newPlace;
	
	private String message;
	
	private List<WSPlaceGroupPost> postDetails;
	
	private List<WSPlaceGroupPoll> pollDetails;
	
	private WSPlaceGroupPost placeGroupPost;
	
	private WSPlaceGroupPoll placeGroupPoll;

	/**
	 * @return the placeGroupId
	 */
	public String getPlaceGroupId() {
		return placeGroupId;
	}

	/**
	 * @param placeGroupId the placeGroupId to set
	 */
	public void setPlaceGroupId(String placeGroupId) {
		this.placeGroupId = placeGroupId;
	}

	/**
	 * @return the newPlace
	 */
	public String getNewPlace() {
		return newPlace;
	}

	/**
	 * @param newPlace the newPlace to set
	 */
	public void setNewPlace(String newPlace) {
		this.newPlace = newPlace;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the postDetails
	 */
	public List<WSPlaceGroupPost> getPostDetails() {
		return postDetails;
	}

	/**
	 * @param postDetails the postDetails to set
	 */
	public void setPostDetails(List<WSPlaceGroupPost> postDetails) {
		this.postDetails = postDetails;
	}

	/**
	 * @return the pollDetails
	 */
	public List<WSPlaceGroupPoll> getPollDetails() {
		return pollDetails;
	}

	/**
	 * @param pollDetails the pollDetails to set
	 */
	public void setPollDetails(List<WSPlaceGroupPoll> pollDetails) {
		this.pollDetails = pollDetails;
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
