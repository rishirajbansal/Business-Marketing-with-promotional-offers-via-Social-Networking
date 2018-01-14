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
public class WSShoutGroupPoll {
	
	private String pollId;
	
	private String shoutGroupId;
	
	private String creatorUsername;
	
	private String userPicturePath;
	
	private String pollQuestion;
	
	private String pollOption1;
	
	private String pollOption2;
	
	private String pollOption3;
	
	private String pollOption4;
	
	private String pollOptionCount1;
	
	private String pollOptionCount2;
	
	private String pollOptionCount3;
	
	private String pollOptionCount4;
	
	private String pollTotalCount;
	
	private String selectedPollOption;
	
	private String createdTimestamp;

	/**
	 * @return the pollId
	 */
	public String getPollId() {
		return pollId;
	}

	/**
	 * @param pollId the pollId to set
	 */
	public void setPollId(String pollId) {
		this.pollId = pollId;
	}

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
	 * @return the creatorUsername
	 */
	public String getCreatorUsername() {
		return creatorUsername;
	}

	/**
	 * @param creatorUsername the creatorUsername to set
	 */
	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
	}

	/**
	 * @return the userPicturePath
	 */
	public String getUserPicturePath() {
		return userPicturePath;
	}

	/**
	 * @param userPicturePath the userPicturePath to set
	 */
	public void setUserPicturePath(String userPicturePath) {
		this.userPicturePath = userPicturePath;
	}

	/**
	 * @return the pollQuestion
	 */
	public String getPollQuestion() {
		return pollQuestion;
	}

	/**
	 * @param pollQuestion the pollQuestion to set
	 */
	public void setPollQuestion(String pollQuestion) {
		this.pollQuestion = pollQuestion;
	}

	/**
	 * @return the pollOption1
	 */
	public String getPollOption1() {
		return pollOption1;
	}

	/**
	 * @param pollOption1 the pollOption1 to set
	 */
	public void setPollOption1(String pollOption1) {
		this.pollOption1 = pollOption1;
	}

	/**
	 * @return the pollOption2
	 */
	public String getPollOption2() {
		return pollOption2;
	}

	/**
	 * @param pollOption2 the pollOption2 to set
	 */
	public void setPollOption2(String pollOption2) {
		this.pollOption2 = pollOption2;
	}

	/**
	 * @return the pollOption3
	 */
	public String getPollOption3() {
		return pollOption3;
	}

	/**
	 * @param pollOption3 the pollOption3 to set
	 */
	public void setPollOption3(String pollOption3) {
		this.pollOption3 = pollOption3;
	}

	/**
	 * @return the pollOption4
	 */
	public String getPollOption4() {
		return pollOption4;
	}

	/**
	 * @param pollOption4 the pollOption4 to set
	 */
	public void setPollOption4(String pollOption4) {
		this.pollOption4 = pollOption4;
	}

	/**
	 * @return the pollOptionCount1
	 */
	public String getPollOptionCount1() {
		return pollOptionCount1;
	}

	/**
	 * @param pollOptionCount1 the pollOptionCount1 to set
	 */
	public void setPollOptionCount1(String pollOptionCount1) {
		this.pollOptionCount1 = pollOptionCount1;
	}

	/**
	 * @return the pollOptionCount2
	 */
	public String getPollOptionCount2() {
		return pollOptionCount2;
	}

	/**
	 * @param pollOptionCount2 the pollOptionCount2 to set
	 */
	public void setPollOptionCount2(String pollOptionCount2) {
		this.pollOptionCount2 = pollOptionCount2;
	}

	/**
	 * @return the pollOptionCount3
	 */
	public String getPollOptionCount3() {
		return pollOptionCount3;
	}

	/**
	 * @param pollOptionCount3 the pollOptionCount3 to set
	 */
	public void setPollOptionCount3(String pollOptionCount3) {
		this.pollOptionCount3 = pollOptionCount3;
	}

	/**
	 * @return the pollOptionCount4
	 */
	public String getPollOptionCount4() {
		return pollOptionCount4;
	}

	/**
	 * @param pollOptionCount4 the pollOptionCount4 to set
	 */
	public void setPollOptionCount4(String pollOptionCount4) {
		this.pollOptionCount4 = pollOptionCount4;
	}

	/**
	 * @return the pollTotalCount
	 */
	public String getPollTotalCount() {
		return pollTotalCount;
	}

	/**
	 * @param pollTotalCount the pollTotalCount to set
	 */
	public void setPollTotalCount(String pollTotalCount) {
		this.pollTotalCount = pollTotalCount;
	}
	
	/**
	 * @return the selectedPollOption
	 */
	public String getSelectedPollOption() {
		return selectedPollOption;
	}

	/**
	 * @param selectedPollOption the selectedPollOption to set
	 */
	public void setSelectedPollOption(String selectedPollOption) {
		this.selectedPollOption = selectedPollOption;
	}
	
	/**
	 * @return the createdTimestamp
	 */
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Override
    public String toString(){
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("[");
        sBuffer.append("Poll Id : " + pollId).append(" || ");
        sBuffer.append("Shout Group Id : " + shoutGroupId).append(" || ");
        sBuffer.append("Created Username : " + creatorUsername).append(" || ");
        sBuffer.append("User Picture Path : " + userPicturePath).append(" || ");
        sBuffer.append("Poll Question : " + pollQuestion).append(" || ");
        sBuffer.append("Poll Option 1 : " + pollOption1).append(" || ");
        sBuffer.append("Poll Option 2 : " + pollOption2).append(" || ");
        sBuffer.append("Poll Option 3 : " + pollOption3).append(" || ");
        sBuffer.append("Poll Option 4 : " + pollOption4).append(" || ");
        sBuffer.append("Poll Option Count 1 : " + pollOptionCount1).append(" || ");
        sBuffer.append("Poll Option Count 2 : " + pollOptionCount2).append(" || ");
        sBuffer.append("Poll Option Count 3 : " + pollOptionCount3).append(" || ");
        sBuffer.append("Poll Option Count 4 : " + pollOptionCount4).append(" || ");
        sBuffer.append("Poll Total Count : " + pollTotalCount).append(" || ");
        sBuffer.append("Created Timestamp : " + createdTimestamp);
        sBuffer.append("]");

        return sBuffer.toString();
    }

}
