/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.CityGeoData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostReplyData;

/**
 * @author Rishi
 *
 */
public interface IShoutGroupDAO {
	
	public List<CityGeoData> loadCityGeoDetails() throws DataAccessException;
	
	public List<ShoutGroupData> loadShoutGroupDetails() throws DataAccessException;
	
	public List<ShoutGroupPostData> fetchPostDataByShoutGroupId(int shoutGroupId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public List<ShoutGroupPollData> fetchPollDataByShoutGroupId(int shoutGroupId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public List<ShoutGroupPostReplyData> fetchPostReplyDataByPostId(int postId, int pageCount, int maxPageResults) throws DataAccessException;
	
	public ShoutGroupPostData fetchPostDataByPostId(int postId) throws DataAccessException;
	
	public int createPost(ShoutGroupPostData shoutGroupPostData) throws DataAccessException;
	
	public int createPoll(ShoutGroupPollData shoutGroupPollData) throws DataAccessException;
	
	public boolean replyPost(ShoutGroupPostReplyData shoutGroupPostReplyData) throws DataAccessException;
	
	public boolean replyPoll(ShoutGroupPollData shoutGroupPollData) throws DataAccessException;

}
