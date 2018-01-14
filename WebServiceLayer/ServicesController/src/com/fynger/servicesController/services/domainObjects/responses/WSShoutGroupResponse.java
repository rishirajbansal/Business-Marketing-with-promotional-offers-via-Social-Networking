/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPost;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSShoutGroupResponse extends WSBaseResponse {
	
	private String shoutGroupId;
	
	private String newShoutGroup;
	
	private String message;
	
	private String shoutGroupName;
	
	private String shoutGroupDescription;
	
	private String shoutGroupAddress;
	
	private List<WSShoutGroupPost> postDetails;
	
	private List<WSShoutGroupPoll> pollDetails;
	
	private WSShoutGroupPost shoutGroupPost;
	
	private WSShoutGroupPoll shoutGroupPoll;

	/**
	 * @return the shoutGroupId
	 */
	public String getShoutGroupId() {
		return shoutGroupId;
	}

	/**
	 * @param shoutGroupId the shoutGroupId to set
	 */
	public void setShoutGroupId(String shoutGroupId) {
		this.shoutGroupId = shoutGroupId;
	}

	/**
	 * @return the newShoutGroup
	 */
	public String getNewShoutGroup() {
		return newShoutGroup;
	}

	/**
	 * @param newShoutGroup the newShoutGroup to set
	 */
	public void setNewShoutGroup(String newShoutGroup) {
		this.newShoutGroup = newShoutGroup;
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
	 * @return the shoutGroupName
	 */
	public String getShoutGroupName() {
		return shoutGroupName;
	}

	/**
	 * @param shoutGroupName the shoutGroupName to set
	 */
	public void setShoutGroupName(String shoutGroupName) {
		this.shoutGroupName = shoutGroupName;
	}

	/**
	 * @return the shoutGroupDescription
	 */
	public String getShoutGroupDescription() {
		return shoutGroupDescription;
	}

	/**
	 * @param shoutGroupDescription the shoutGroupDescription to set
	 */
	public void setShoutGroupDescription(String shoutGroupDescription) {
		this.shoutGroupDescription = shoutGroupDescription;
	}

	/**
	 * @return the postDetails
	 */
	public List<WSShoutGroupPost> getPostDetails() {
		return postDetails;
	}

	/**
	 * @param postDetails the postDetails to set
	 */
	public void setPostDetails(List<WSShoutGroupPost> postDetails) {
		this.postDetails = postDetails;
	}

	/**
	 * @return the pollDetails
	 */
	public List<WSShoutGroupPoll> getPollDetails() {
		return pollDetails;
	}

	/**
	 * @param pollDetails the pollDetails to set
	 */
	public void setPollDetails(List<WSShoutGroupPoll> pollDetails) {
		this.pollDetails = pollDetails;
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

	/**
	 * @return the shoutGroupAddress
	 */
	public String getShoutGroupAddress() {
		return shoutGroupAddress;
	}

	/**
	 * @param shoutGroupAddress the shoutGroupAddress to set
	 */
	public void setShoutGroupAddress(String shoutGroupAddress) {
		this.shoutGroupAddress = shoutGroupAddress;
	}

}
