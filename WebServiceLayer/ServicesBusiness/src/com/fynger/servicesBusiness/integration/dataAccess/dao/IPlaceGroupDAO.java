/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostReplyData;

/**
 * @author Rishi
 *
 */
public interface IPlaceGroupDAO {
	
	public int findPlaceGroupByMapId(String placeMapId) throws DataAccessException;
	
	public int createPlaceGroup(PlaceGroupData placeGroupData) throws DataAccessException;
	
	public List<PlaceGroupPostData> fetchPostDataByPlaceGroupId(int placeGroupId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public List<PlaceGroupPollData> fetchPollDataByPlaceGroupId(int placeGroupId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public List<PlaceGroupPostReplyData> fetchPostReplyDataByPostId(int postId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public PlaceGroupPostData fetchPostDataByPostId(int postId) throws DataAccessException;
	
	public int createPost(PlaceGroupPostData placeGroupPostData) throws DataAccessException;
	
	public int createPoll(PlaceGroupPollData placeGroupPollData) throws DataAccessException;
	
	public boolean replyPost(PlaceGroupPostReplyData placeGroupPostReplyData) throws DataAccessException;
	
	public boolean replyPoll(PlaceGroupPollData placeGroupPollData) throws DataAccessException;

}
