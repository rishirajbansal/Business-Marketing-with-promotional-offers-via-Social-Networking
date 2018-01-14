/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dataObjects;

import java.sql.Timestamp;

/**
 * @author Rishi
 *
 */
public class ShoutGroupPollData extends ShoutGroupData{
	
	private int pollId;
	
	private int shoutGroupId;
	
	private String createdUsername;
	
	private String pollLatitude;
	
	private String pollLongitude;
	
	private String userPicturePath;
	
	private String pollQuestion;
	
	private String pollOption1;
	
	private String pollOption2;
	
	private String pollOption3;
	
	private String pollOption4;
	
	private int pollOptionCount1;
	
	private int pollOptionCount2;
	
	private int pollOptionCount3;
	
	private int pollOptionCount4;
	
	private int pollTotalCount;
	
	private int selectedPollCount;
	
	private Timestamp createdTimestamp;

	/**
	 * @return the pollId
	 */
	public int getPollId() {
		return pollId;
	}

	/**
	 * @param pollId the pollId to set
	 */
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}

	/**
	 * @return the shoutGroupId
	 */
	public int getShoutGroupId() {
		return shoutGroupId;
	}

	/**
	 * @param shoutGroupId the shoutGroupId to set
	 */
	public void setShoutGroupId(int shoutGroupId) {
		this.shoutGroupId = shoutGroupId;
	}

	/**
	 * @return the createdUsername
	 */
	public String getCreatedUsername() {
		return createdUsername;
	}

	/**
	 * @param createdUsername the createdUsername to set
	 */
	public void setCreatedUsername(String createdUsername) {
		this.createdUsername = createdUsername;
	}

	/**
	 * @return the pollLatitude
	 */
	public String getPollLatitude() {
		return pollLatitude;
	}

	/**
	 * @param pollLatitude the pollLatitude to set
	 */
	public void setPollLatitude(String pollLatitude) {
		this.pollLatitude = pollLatitude;
	}

	/**
	 * @return the pollLongitude
	 */
	public String getPollLongitude() {
		return pollLongitude;
	}

	/**
	 * @param pollLongitude the pollLongitude to set
	 */
	public void setPollLongitude(String pollLongitude) {
		this.pollLongitude = pollLongitude;
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
	public int getPollOptionCount1() {
		return pollOptionCount1;
	}

	/**
	 * @param pollOptionCount1 the pollOptionCount1 to set
	 */
	public void setPollOptionCount1(int pollOptionCount1) {
		this.pollOptionCount1 = pollOptionCount1;
	}

	/**
	 * @return the pollOptionCount2
	 */
	public int getPollOptionCount2() {
		return pollOptionCount2;
	}

	/**
	 * @param pollOptionCount2 the pollOptionCount2 to set
	 */
	public void setPollOptionCount2(int pollOptionCount2) {
		this.pollOptionCount2 = pollOptionCount2;
	}

	/**
	 * @return the pollOptionCount3
	 */
	public int getPollOptionCount3() {
		return pollOptionCount3;
	}

	/**
	 * @param pollOptionCount3 the pollOptionCount3 to set
	 */
	public void setPollOptionCount3(int pollOptionCount3) {
		this.pollOptionCount3 = pollOptionCount3;
	}

	/**
	 * @return the pollOptionCount4
	 */
	public int getPollOptionCount4() {
		return pollOptionCount4;
	}

	/**
	 * @param pollOptionCount4 the pollOptionCount4 to set
	 */
	public void setPollOptionCount4(int pollOptionCount4) {
		this.pollOptionCount4 = pollOptionCount4;
	}

	/**
	 * @return the pollTotalCount
	 */
	public int getPollTotalCount() {
		return pollTotalCount;
	}

	/**
	 * @param pollTotalCount the pollTotalCount to set
	 */
	public void setPollTotalCount(int pollTotalCount) {
		this.pollTotalCount = pollTotalCount;
	}

	/**
	 * @return the selectedPollCount
	 */
	public int getSelectedPollCount() {
		return selectedPollCount;
	}

	/**
	 * @param selectedPollCount the selectedPollCount to set
	 */
	public void setSelectedPollCount(int selectedPollCount) {
		this.selectedPollCount = selectedPollCount;
	}

	/**
	 * @return the createdTimestamp
	 */
	public Timestamp getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	public void setCreatedTimestamp(Timestamp createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}	

}
