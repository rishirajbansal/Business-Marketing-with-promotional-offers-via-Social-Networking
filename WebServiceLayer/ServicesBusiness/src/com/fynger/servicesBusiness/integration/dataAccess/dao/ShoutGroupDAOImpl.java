/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.pkcs.SafeBag;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.NoShoutGroupPostReplyDataFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePollException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePostException;
import com.fynger.servicesBusiness.exception.ShoutGroupException;
import com.fynger.servicesBusiness.exception.ShoutGroupPollReplyException;
import com.fynger.servicesBusiness.exception.ShoutGroupPostReplyException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.CityGeoData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostReplyData;

/**
 * @author Rishi
 *
 */
public class ShoutGroupDAOImpl implements IShoutGroupDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(ShoutGroupDAOImpl.class.getName());
	
	private static final String SQL_SELECT_LOAD_CITY_GEO_DETAILS 				= 	"SELECT * FROM city_geo";
	
	private static final String SQL_SELECT_LOAD_SHOUT_GROUP_DETAILS 			= 	"SELECT * FROM shoutgroup";
	
	private static final String SQL_COUNT_SHOUT_GROUP_MAPPING					= 	"SELECT count(*) AS count FROM shoutgroup_mapping WHERE shout_group_id  = ? ";
	
	private static final String SQL_SELECT_SHOUT_GROUP_MAPPING					= 	"SELECT * FROM shoutgroup_mapping WHERE shout_group_id  = ? ORDER BY created_on desc LIMIT ?,?";
	
	private static final String SQL_INSERT_SHOUT_GROUP_MAPPING					=	"INSERT INTO shoutgroup_mapping (shout_group_id, entity_id, entity_type, created_on) VALUES (?, ?, ?, NOW())";	
	
	private static final String SQL_SELECT_SHOUT_GROUP_POST_BY_SHOUTGROUP_ID_1 	= 	"SELECT * FROM shoutgroup_post, shoutgroup_mapping, user_profile, user_login " +
																					"WHERE user_profile.login_id = user_login.login_id " +
																					"AND shoutgroup_post.post_id = shoutgroup_mapping.entity_id " +
																					"AND user_login.username = shoutgroup_post.created_username " +
																					"AND shoutgroup_mapping.entity_type = 1 " +
																					"AND shoutgroup_post.post_id IN ( " ;
																				
	private static final String SQL_SELECT_SHOUT_GROUP_POST_BY_SHOUTGROUP_ID_2	=	") ORDER BY shoutgroup_mapping.created_on DESC";
	
	private static final String SQL_SELECT_SHOUT_GROUP_POLL_BY_SHOUTGROUP_ID_1 	= 	"SELECT * FROM fyngerdb.shoutgroup_poll, fyngerdb.shoutgroup_mapping, fyngerdb.user_profile, fyngerdb.user_login " +
																					"WHERE user_profile.login_id = user_login.login_id " +
																					"AND shoutgroup_poll.poll_id = shoutgroup_mapping.entity_id " +
																					"AND user_login.username = shoutgroup_poll.created_username " +
																					"AND shoutgroup_mapping.entity_type = 2 " +
																					"AND shoutgroup_poll.poll_id IN ( ";
	
	private static final String SQL_SELECT_SHOUT_GROUP_POLL_BY_SHOUTGROUP_ID_2 	= 	") ORDER BY shoutgroup_mapping.created_on DESC";
	
	private static final String SQL_COUNT_SHOUT_GROUP_POST_REPLY_DETAILS 		= 	"SELECT count(*) AS count FROM shoutgroup_post_reply WHERE post_id = ?";
	
	private static final String SQL_SELECT_SHOUT_GROUP_POST_REPLY_DETAILS 		= 	"SELECT * FROM shoutgroup_post_reply, user_profile, user_login WHERE user_profile.login_id = user_login.login_id " +
																						"AND user_login.username = shoutgroup_post_reply.replied_username AND post_id = ? ORDER BY shoutgroup_post_reply.created_on DESC LIMIT ?, ?";
	
	private static final String SQL_SELECT_SHOUT_GROUP_POST_BY_POST_ID 			= 	"SELECT * FROM shoutgroup_post, user_profile, user_login WHERE user_profile.login_id = user_login.login_id " +
																						"AND user_login.username = shoutgroup_post.created_username AND post_id = ?";
	
	private static final String SQL_INSERT_SHOUT_GROUP_POST 					= 	"INSERT INTO shoutgroup_post (shout_group_id, created_username, post_text, post_image_path, post_latitude, post_longitude, created_on, last_updated) " +
																						"VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_SHOUT_GROUP_POLL						=	"INSERT INTO shoutgroup_poll (shout_group_id, created_username, poll_question, poll_option_1, poll_option_2, poll_option_3, poll_option_4, poll_latitude, poll_longitude, created_on, last_updated) " +
																						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_SHOUT_GROUP_POST_REPLY				= 	"INSERT INTO shoutgroup_post_reply (post_id, replied_username, replied_text, created_on, last_updated) " +
																						"VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_SHOUT_GROUP_POLL_BY_POLL_ID 			= 	"SELECT * FROM shoutgroup_poll WHERE poll_id = ?";
	
	private static final String SQL_UPDATE_SHOUT_GROUP_POLL						=	"UPDATE shoutgroup_poll SET poll_option_count_1 = ?, poll_option_count_2 = ?, poll_option_count_3 = ?, poll_option_count_4 = ?, poll_total_count = ?, last_updated = NOW() WHERE poll_id = ?";

	
	@Override
	public List<CityGeoData> loadCityGeoDetails() throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<CityGeoData> alCityGeoDetails = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_LOAD_CITY_GEO_DETAILS);
			
			logger.debug("[loadCityGeoDetails()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				rs.previous();
				
				alCityGeoDetails = new ArrayList<CityGeoData>();
				
				while(rs.next()){
					CityGeoData daoData = new CityGeoData();
					
					daoData.setCityId(rs.getInt(DAOConstants.TABLE_CITY_GEO_COLUMN_CITYID));
					daoData.setCityName(rs.getString(DAOConstants.TABLE_CITY_GEO_COLUMN_CITYNAME));
					daoData.setCityCenterAddress(rs.getString(DAOConstants.TABLE_CITY_GEO_COLUMN_CITYADDRESS));
					daoData.setCityCenterLatitude(rs.getString(DAOConstants.TABLE_CITY_GEO_COLUMN_CITYCENTER_LATITUDE));
					daoData.setCityCenterLongitude(rs.getString(DAOConstants.TABLE_CITY_GEO_COLUMN_CITYCENTER_LONGITUDE));
					
					alCityGeoDetails.add(daoData);
				}
			}
			else{
				logger.error("No city found in the database to load the geocoordinates for Shout Groups.");
				throw new ShoutGroupException("No city found in the database to load the geocoordinates for Shout Groups.");
			}
		}
		catch(ShoutGroupException sgEx){
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_CITY_GEO_DETAILS, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_GENERAL_MESSAGE, sgEx.getMessage(), EExceptionTypes.SHOUT_GROUP_EXCEPTION);
		}
		catch(SQLException sqlEx){
			logger.error("loadCityGeoDetails", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadCityGeoDetails() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadCityGeoDetails", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadCityGeoDetails() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadCityGeoDetails() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alCityGeoDetails;
	}

	@Override
	public List<ShoutGroupData> loadShoutGroupDetails() throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<ShoutGroupData> alShoutGroupDetails = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_LOAD_SHOUT_GROUP_DETAILS);
			
			logger.debug("[loadShoutGroupDetails()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				rs.previous();
				
				alShoutGroupDetails = new ArrayList<ShoutGroupData>();
				
				while(rs.next()){
					ShoutGroupData daoData = new ShoutGroupData();
					
					daoData.setShoutGroupId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_COLUMN_SHOUT_GROUP_ID));
					daoData.setShoutGroupName(rs.getString(DAOConstants.TABLE_SHOUTGROUP_COLUMN_NAME));
					daoData.setShoutGroupDescription(rs.getString(DAOConstants.TABLE_SHOUTGROUP_COLUMN_DESCRIPTION));
					daoData.setAddress(rs.getString(DAOConstants.TABLE_SHOUTGROUP_COLUMN_ADDRESS));
					daoData.setCityId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_COLUMN_CITYID));
					daoData.setShoutGroupLatitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_COLUMN_LATITUDE));
					daoData.setShoutGroupLongitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_COLUMN_LONGITUDE));
					
					alShoutGroupDetails.add(daoData);
				}
			}
			else{
				logger.error("No shout group found in the database to load the geocoordinates.");
				throw new ShoutGroupException("No shout group found in the database to load the geocoordinates.");
			}
		}
		catch(ShoutGroupException sgEx){
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_DETAILS, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_GENERAL_MESSAGE, sgEx.getMessage(), EExceptionTypes.SHOUT_GROUP_EXCEPTION);
		}
		catch(SQLException sqlEx){
			logger.error("loadShoutGroupDetails", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("loadShoutGroupDetails() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("loadShoutGroupDetails", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("loadShoutGroupDetails() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("loadShoutGroupDetails() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alShoutGroupDetails;
		
	}

	@Override
	public List<ShoutGroupPostData> fetchPostDataByShoutGroupId(int shoutGroupId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		List<ShoutGroupPostData> alShoutGroupPostData = null; 
		StringBuffer consPostIds = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_SHOUT_GROUP_MAPPING);
			ps.setInt(1, shoutGroupId);
			
			logger.debug("[fetchPostDataByShoutGroupId()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total Post/Poll counts for Shout Group Id : " + shoutGroupId + " are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					consPostIds = new StringBuffer();
					
					ps = con.prepareStatement(SQL_SELECT_SHOUT_GROUP_MAPPING);
					ps.setInt(1, shoutGroupId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPostDataByShoutGroupId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					while (rs.next()){
						if (DAOConstants.SHOUTGROUP_MAPPING_ENTITY_TYPE_POST == rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_TYPE)){
							consPostIds.append(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_ID)).append(",");
						}
					}
					
					if (consPostIds.toString().endsWith(",")) {
						consPostIds = new StringBuffer((consPostIds.substring(0, consPostIds.lastIndexOf(","))));
					}
					
					if (!GenericUtility.safeTrim(consPostIds.toString()).equals(GenericConstants.EMPTY_STRING)){
						stmt = con.createStatement();
						
						sql = SQL_SELECT_SHOUT_GROUP_POST_BY_SHOUTGROUP_ID_1 + consPostIds + SQL_SELECT_SHOUT_GROUP_POST_BY_SHOUTGROUP_ID_2;
						
						logger.debug("[fetchPostDataByShoutGroupId()-QUERY 3] : " + sql);
						
						rs = stmt.executeQuery(sql);			
						
						if (rs.next()){
							rs.previous();
							
							alShoutGroupPostData = new ArrayList<ShoutGroupPostData>();
							
							while(rs.next()){
								ShoutGroupPostData shoutGroupPostData = new ShoutGroupPostData();
								
								shoutGroupPostData.setPostId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POSTID));
								shoutGroupPostData.setShoutGroupId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_SHOUTGROUP_ID));
								shoutGroupPostData.setPostLatitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_LATITUDE));
								shoutGroupPostData.setPostLongitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_LONGITUDE));
								shoutGroupPostData.setPostText(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_TEXT));
								shoutGroupPostData.setPostImagePath(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_IMAGE_PATH));
								shoutGroupPostData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
								shoutGroupPostData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
								shoutGroupPostData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_CREATED_TIMESTAMP));
								shoutGroupPostData.setEntityType(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_TYPE));
								
								alShoutGroupPostData.add(shoutGroupPostData);
							}
						}
					}
					else{
						logger.debug("No Posts exists for this shout group : " + shoutGroupId);
					}
				}
			}
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchPostDataByShoutGroupId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPostDataByShoutGroupId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPostDataByShoutGroupId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPostDataByShoutGroupId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPostDataByShoutGroupId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alShoutGroupPostData;
	}

	@Override
	public List<ShoutGroupPollData> fetchPollDataByShoutGroupId(int shoutGroupId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		List<ShoutGroupPollData> alShoutGroupPollData = null; 
		StringBuffer consPollIds = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_SHOUT_GROUP_MAPPING);
			ps.setInt(1, shoutGroupId);
			
			logger.debug("[fetchPollDataByShoutGroupId()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total Post/Poll counts for Shout Group Id : " + shoutGroupId + " are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					consPollIds = new StringBuffer();
					
					ps = con.prepareStatement(SQL_SELECT_SHOUT_GROUP_MAPPING);
					ps.setInt(1, shoutGroupId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPollDataByShoutGroupId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					while (rs.next()){
						if (DAOConstants.SHOUTGROUP_MAPPING_ENTITY_TYPE_POLL == rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_TYPE)){
							consPollIds.append(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_ID)).append(",");
						}
					}
					
					if (consPollIds.toString().endsWith(",")) {
						consPollIds = new StringBuffer((consPollIds.substring(0, consPollIds.lastIndexOf(","))));
					}
					
					if (!GenericUtility.safeTrim(consPollIds.toString()).equals(GenericConstants.EMPTY_STRING)){
						stmt = con.createStatement();
						
						sql = SQL_SELECT_SHOUT_GROUP_POLL_BY_SHOUTGROUP_ID_1 + consPollIds + SQL_SELECT_SHOUT_GROUP_POLL_BY_SHOUTGROUP_ID_2;
						
						logger.debug("[fetchPollDataByShoutGroupId()-QUERY 3] : " + sql);
						
						rs = stmt.executeQuery(sql);
						
						if (rs.next()){
							rs.previous();
							
							alShoutGroupPollData = new ArrayList<ShoutGroupPollData>();
							
							while(rs.next()){
								ShoutGroupPollData shoutGroupPollData = new ShoutGroupPollData();
								
								shoutGroupPollData.setPollId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLLID));
								shoutGroupPollData.setShoutGroupId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_SHOUTGROUP_ID));
								shoutGroupPollData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
								shoutGroupPollData.setPollLatitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_LATITUDE));
								shoutGroupPollData.setPollLongitude(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_LONGITUDE));
								shoutGroupPollData.setPollQuestion(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_QUESTION));
								shoutGroupPollData.setPollOption1(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_1));
								shoutGroupPollData.setPollOption2(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_2));
								shoutGroupPollData.setPollOption3(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_3));
								shoutGroupPollData.setPollOption4(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_4));
								shoutGroupPollData.setPollOptionCount1(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1));
								shoutGroupPollData.setPollOptionCount2(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2));
								shoutGroupPollData.setPollOptionCount3(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3));
								shoutGroupPollData.setPollOptionCount4(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4));
								shoutGroupPollData.setPollTotalCount(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_TOTAL_COUNT));
								shoutGroupPollData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
								shoutGroupPollData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_CREATED_TIMESTAMP));
								shoutGroupPollData.setEntityType(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_TYPE));
								
								alShoutGroupPollData.add(shoutGroupPollData);
							}
						}
					}
					else{
						logger.debug("No Polls exists for this shout group : " + shoutGroupId);
					}
				}
			}
			
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchPollDataByShoutGroupId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPollDataByShoutGroupId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPollDataByShoutGroupId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPollDataByShoutGroupId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
				DatabaseConnectionManager.clearResources(stmt);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPollDataByShoutGroupId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alShoutGroupPollData;

	}

	@Override
	public List<ShoutGroupPostReplyData> fetchPostReplyDataByPostId(int postId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<ShoutGroupPostReplyData> alShoutGroupPostReplyData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_SHOUT_GROUP_POST_REPLY_DETAILS);
			ps.setInt(1, postId);
			
			logger.debug("[fetchPostReplyDataByPostId()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total Post responses for Post Id : " + postId + "are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					ps = con.prepareStatement(SQL_SELECT_SHOUT_GROUP_POST_REPLY_DETAILS);
					ps.setInt(1, postId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPostReplyDataByPostId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					if (rs.next()){
						rs.previous();
						
						alShoutGroupPostReplyData = new ArrayList<ShoutGroupPostReplyData>();
						
						while(rs.next()){
							ShoutGroupPostReplyData shoutGroupPostReplyData = new ShoutGroupPostReplyData();
							
							shoutGroupPostReplyData.setPostId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POST_REPLY_COLUMN_POSTID));
							shoutGroupPostReplyData.setRepliedText(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_REPLY_COLUMN_REPLIED_TEXT));
							shoutGroupPostReplyData.setRepliedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
							shoutGroupPostReplyData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
							shoutGroupPostReplyData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_SHOUTGROUP_POST_REPLY_COLUMN_CREATED_TIMESTAMP));
							
							shoutGroupPostReplyData.setTotalResults(count);
							
							alShoutGroupPostReplyData.add(shoutGroupPostReplyData);
						}
					}
					else{
						logger.debug("No post responses found for the post id : " + postId);
						//throw new NoShoutGroupPostReplyDataFoundException("No post responses found for the post id : " + postId);
					}
				}
			}
			else{
				logger.debug("No post responses found for the post id : " + postId);
				//throw new NoShoutGroupPostReplyDataFoundException("No post responses found for the post id : " + postId);
			}
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(NoShoutGroupPostReplyDataFoundException noDataEx){
			throw noDataEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchPostReplyDataByPostId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPostReplyDataByPostId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPostReplyDataByPostId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPostReplyDataByPostId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPostReplyDataByPostId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alShoutGroupPostReplyData;

	}

	@Override
	public ShoutGroupPostData fetchPostDataByPostId(int postId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ShoutGroupPostData shoutGroupPostData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_SHOUT_GROUP_POST_BY_POST_ID);
			ps.setInt(1, postId);
			
			logger.debug("[fetchPostDataByPostId()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				shoutGroupPostData = new ShoutGroupPostData();
				
				shoutGroupPostData.setPostId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POSTID));
				shoutGroupPostData.setShoutGroupId(rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_SHOUTGROUP_ID));
				shoutGroupPostData.setPostText(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_TEXT));
				shoutGroupPostData.setPostImagePath(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_POST_IMAGE_PATH));
				shoutGroupPostData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
				shoutGroupPostData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
				shoutGroupPostData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_CREATED_TIMESTAMP));
				shoutGroupPostData.setUsername(rs.getString(DAOConstants.TABLE_SHOUTGROUP_POST_COLUMN_CREATED_USERNAME));
			}
		}
		catch(SQLException sqlEx){
			logger.error("fetchPostDataByPostId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPostDataByPostId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPostDataByPostId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPostDataByPostId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPostDataByPostId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return shoutGroupPostData;
		
	}

	@Override
	public int createPost(ShoutGroupPostData shoutGroupPostData) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		int postId = -1;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_SHOUT_GROUP_POST, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, shoutGroupPostData.getShoutGroupId());
			ps.setString(2, shoutGroupPostData.getCreatedUsername());
			ps.setString(3, shoutGroupPostData.getPostText());
			ps.setString(4, shoutGroupPostData.getPostImagePath());
			ps.setString(5, shoutGroupPostData.getPostLatitude());
			ps.setString(6, shoutGroupPostData.getPostLongitude());
			
			logger.debug("[createPost()-QUERY 1] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("createPost() -> Failed to insert record for Shout Group new Post request in database.");
				throw new ShoutGroupCreatePostException("createPost() -> Failed to insert record for Shout Group new Post request in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				postId = generatedKeys.getInt(1);
				
				/* Insert entry into mapping table */
				ps = con.prepareStatement(SQL_INSERT_SHOUT_GROUP_MAPPING);
				ps.setInt(1, shoutGroupPostData.getShoutGroupId());
				ps.setInt(2, postId);
				ps.setInt(3, DAOConstants.SHOUTGROUP_MAPPING_ENTITY_TYPE_POST);
				
				logger.debug("[createPost()-QUERY 2] : " + ps.toString());
				
				rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					logger.error("createPost() -> Failed to insert record for Shout Group mapping for new Post request in database.");
					throw new ShoutGroupCreatePostException("createPost() -> Failed to insert record for Shout Group mapping for new Post request in database.");
				}
				else{
					con.commit();
				}
			}
			else{
				logger.error("createPost() -> Failed to insert record for Shout Group new Post request in database, no generated key obtained.");
				throw new ShoutGroupCreatePostException("createPost() -> Failed to insert record for Shout Group new Post request in database, no generated key obtained.");
			}
		}
		catch(ShoutGroupCreatePostException createPostEx){
			try {
				logger.debug("createPost() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPost() -> SQLException occurred during rollback.");
			}
			throw createPostEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("createPost() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPost() -> SQLException occurred during rollback.");
			}
			logger.error("createPost", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("createPost() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("createPost", "Exception occurred in DAO layer : " + ex.getMessage());
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPost() -> SQLException occurred during rollback.");
			}
			logger.error("createPost", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("createPost() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, generatedKeys);
				DatabaseConnectionManager.clearResources(rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("createPost() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return postId;

	}

	@Override
	public int createPoll(ShoutGroupPollData shoutGroupPollData) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		int pollId = -1;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_SHOUT_GROUP_POLL, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, shoutGroupPollData.getShoutGroupId());
			ps.setString(2, shoutGroupPollData.getCreatedUsername());
			ps.setString(3, shoutGroupPollData.getPollQuestion());
			ps.setString(4, shoutGroupPollData.getPollOption1());
			ps.setString(5, shoutGroupPollData.getPollOption2());
			ps.setString(6, shoutGroupPollData.getPollOption3());
			ps.setString(7, shoutGroupPollData.getPollOption4());
			ps.setString(8, shoutGroupPollData.getPollLatitude());
			ps.setString(9, shoutGroupPollData.getPollLongitude());
			
			logger.debug("[createPoll()-QUERY 1] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("createPoll() -> Failed to insert record for Shout Group new Poll request in database.");
				throw new ShoutGroupCreatePollException("createPoll() -> Failed to insert record for Shout Group new Poll request in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				pollId = generatedKeys.getInt(1);
				
				/* Insert entry into mapping table */
				ps = con.prepareStatement(SQL_INSERT_SHOUT_GROUP_MAPPING);
				ps.setInt(1, shoutGroupPollData.getShoutGroupId());
				ps.setInt(2, pollId);
				ps.setInt(3, DAOConstants.SHOUTGROUP_MAPPING_ENTITY_TYPE_POLL);
				
				logger.debug("[createPoll()-QUERY 2] : " + ps.toString());
				
				rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					logger.error("createPoll() -> Failed to insert record for Shout Group mapping for new Poll request in database.");
					throw new ShoutGroupCreatePollException("createPoll() -> Failed to insert record for Shout Group mapping for new Poll request in database.");
				}
				else{
					con.commit();
				}
			}
			else{
				logger.error("createPoll() -> Failed to insert record for Shout Group new Poll request in database, no generated key obtained.");
				throw new ShoutGroupCreatePollException("createPoll() -> Failed to insert record for Shout Group new Poll request in database, no generated key obtained.");
			}
		}
		catch(ShoutGroupCreatePollException createPollEx){
			try {
				logger.debug("createPoll() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPoll() -> SQLException occurred during rollback.");
			}
			throw createPollEx;
		}
		catch(SQLException sqlEx){
			try {
				logger.debug("createPoll() -> DataAccessException occurred. Rollback will be executed.");
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPoll() -> SQLException occurred during rollback.");
			}
			logger.error("createPoll", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("createPoll() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			try {
				logger.debug("createPoll", "Exception occurred in DAO layer : " + ex.getMessage());
				con.rollback();
			} 
			catch (SQLException e) {
				throw new DataAccessException("createPoll() -> SQLException occurred during rollback.");
			}
			logger.error("createPoll", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("createPoll() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, generatedKeys);
				DatabaseConnectionManager.clearResources(rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("createPoll() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return pollId;

	}

	@Override
	public boolean replyPost(ShoutGroupPostReplyData shoutGroupPostReplyData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_SHOUT_GROUP_POST_REPLY);
			
			ps.setInt(1, shoutGroupPostReplyData.getPostId());
			ps.setString(2, shoutGroupPostReplyData.getRepliedUsername());
			ps.setString(3, shoutGroupPostReplyData.getRepliedText());
			
			logger.debug("[replyPost()-QUERY] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("replyPost() -> Failed to insert record for Shout Group Post Reply request in database.");
				throw new ShoutGroupPostReplyException("replyPost() -> Failed to insert record for Shout Group Post Reply request in database.");
			}
			else{
				flag = true;
				con.commit();
			}
		}
		catch(ShoutGroupPostReplyException postReplyEx){
			throw postReplyEx;
		}
		catch(SQLException sqlEx){
			logger.error("createPoll", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("createPoll() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("createPoll", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("createPoll() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("createPoll() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

	@Override
	public boolean replyPoll(ShoutGroupPollData shoutGroupPollData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_SHOUT_GROUP_POLL_BY_POLL_ID);
			
			ps.setInt(1, shoutGroupPollData.getPollId());
			
			logger.debug("[replyPoll()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				int pollOption1 = rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1);
				int pollOption2 = rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2);
				int pollOption3 = rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3);
				int pollOption4 = rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4);
				int totalPollsCount = rs.getInt(DAOConstants.TABLE_SHOUTGROUP_POLL_COLUMN_POLL_TOTAL_COUNT);
				
				switch(shoutGroupPollData.getSelectedPollCount()){
					case 1:
						++pollOption1;
						++totalPollsCount;
						break;
							
					case 2:
						++pollOption2;
						++totalPollsCount;
						break;
						
					case 3:
						++pollOption3;
						++totalPollsCount;
						break;
						
					case 4:
						++pollOption4;
						++totalPollsCount;
						break;
						
					default:
						logger.error("Invalid option selected for Poll Id : " + shoutGroupPollData.getPollId());
						throw new ShoutGroupPollReplyException("Invalid option selected for Poll Id : " + shoutGroupPollData.getPollId());
				}
				
				/* Update Poll reply */
				ps = con.prepareStatement(SQL_UPDATE_SHOUT_GROUP_POLL);
				
				ps.setInt(1, pollOption1);
				ps.setInt(2, pollOption2);
				ps.setInt(3, pollOption3);
				ps.setInt(4, pollOption4);
				ps.setInt(5, totalPollsCount);
				ps.setInt(6, shoutGroupPollData.getPollId());
				
				logger.debug("[replyPoll()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("replyPoll() -> Failed to update record for Shout Group Poll Reply request in database.");
					throw new ShoutGroupPollReplyException("replyPoll() -> Failed to insert record for Shout Group Poll Reply request in database.");
				}
				else{
					flag = true;
					con.commit();
				}
			}
			else{
				logger.error("No record found for Poll Id : " + shoutGroupPollData.getPollId());
				throw new ShoutGroupPollReplyException("No record found for Poll Id : " + shoutGroupPollData.getPollId());
			}
		}
		catch(ShoutGroupPollReplyException pollReplyEx){
			throw pollReplyEx;
		}
		catch(SQLException sqlEx){
			logger.error("replyPoll", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("replyPoll() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("replyPoll", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("replyPoll() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("replyPoll() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}
	
	

}
