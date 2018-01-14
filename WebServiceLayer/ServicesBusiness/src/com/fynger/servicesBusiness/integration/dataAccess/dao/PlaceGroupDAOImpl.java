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

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.NoPlaceGroupPostReplyDataFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.PlaceGroupCreatePollException;
import com.fynger.servicesBusiness.exception.PlaceGroupCreatePostException;
import com.fynger.servicesBusiness.exception.PlaceGroupPollReplyException;
import com.fynger.servicesBusiness.exception.PlaceGroupPostReplyException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePollException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePostException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOConstants;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostReplyData;

/**
 * @author Rishi
 *
 */
public class PlaceGroupDAOImpl implements IPlaceGroupDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(PlaceGroupDAOImpl.class.getName());
	
	private static final String SQL_SELECT_PLACEGROUP_FIND_BY_MAP_ID			=	"SELECT * FROM placegroup WHERE placegroup_mapid = ?";
	
	private static final String SQL_INSERT_PLACEGROUP							= 	"INSERT INTO placegroup (name, latitude, longitude, placegroup_mapid, created_on, last_updated) " +
																					"VALUES(?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_COUNT_PLACE_GROUP_MAPPING					= 	"SELECT count(*) AS count FROM placegroup_mapping WHERE place_group_id  = ? ";
	
	private static final String SQL_SELECT_PLACE_GROUP_MAPPING					= 	"SELECT * FROM placegroup_mapping WHERE place_group_id  = ? ORDER BY created_on desc LIMIT ?,?";
	
	private static final String SQL_INSERT_PLACE_GROUP_MAPPING					=	"INSERT INTO placegroup_mapping (place_group_id, entity_id, entity_type, created_on) VALUES (?, ?, ?, NOW())";	
	
	private static final String SQL_SELECT_PLACE_GROUP_POST_BY_PLACEGROUP_ID_1 	= 	"SELECT * FROM placegroup_post, placegroup_mapping, user_profile, user_login " +
																					"WHERE user_profile.login_id = user_login.login_id " +
																					"AND placegroup_post.post_id = placegroup_mapping.entity_id " +
																					"AND user_login.username = placegroup_post.created_username " +
																					"AND placegroup_mapping.entity_type = 1 " +
																					"AND placegroup_post.post_id IN ( " ;
																				
	private static final String SQL_SELECT_PLACE_GROUP_POST_BY_PLACEGROUP_ID_2	=	") ORDER BY placegroup_mapping.created_on DESC";
	
	private static final String SQL_SELECT_PLACE_GROUP_POLL_BY_PLACEGROUP_ID_1 	= 	"SELECT * FROM fyngerdb.placegroup_poll, fyngerdb.placegroup_mapping, fyngerdb.user_profile, fyngerdb.user_login " +
																					"WHERE user_profile.login_id = user_login.login_id " +
																					"AND placegroup_poll.poll_id = placegroup_mapping.entity_id " +
																					"AND user_login.username = placegroup_poll.created_username " +
																					"AND placegroup_mapping.entity_type = 2 " +
																					"AND placegroup_poll.poll_id IN ( ";
	
	private static final String SQL_SELECT_PLACE_GROUP_POLL_BY_PLACEGROUP_ID_2 	= 	") ORDER BY placegroup_mapping.created_on DESC";
	
	private static final String SQL_COUNT_PLACE_GROUP_POST_REPLY_DETAILS 		= 	"SELECT count(*) AS count FROM placegroup_post_reply WHERE post_id = ?";
	
	private static final String SQL_SELECT_PLACE_GROUP_POST_REPLY_DETAILS 		= 	"SELECT * FROM placegroup_post_reply, user_profile, user_login WHERE user_profile.login_id = user_login.login_id " +
																						"AND user_login.username = placegroup_post_reply.replied_username AND post_id = ? ORDER BY placegroup_post_reply.created_on DESC LIMIT ?, ?";
	
	private static final String SQL_SELECT_PLACE_GROUP_POST_BY_POST_ID 			= 	"SELECT * FROM placegroup_post, user_profile, user_login WHERE user_profile.login_id = user_login.login_id " +
																						"AND user_login.username = placegroup_post.created_username AND post_id = ?";
	
	private static final String SQL_INSERT_PLACE_GROUP_POST 					= 	"INSERT INTO placegroup_post (place_group_id, created_username, post_text, post_image_path, created_on, last_updated) " +
																						"VALUES (?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_PLACE_GROUP_POLL						=	"INSERT INTO placegroup_poll (place_group_id, created_username, poll_question, poll_option_1, poll_option_2, poll_option_3, poll_option_4, created_on, last_updated) " +
																						"VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_INSERT_PLACE_GROUP_POST_REPLY				= 	"INSERT INTO placegroup_post_reply (post_id, replied_username, replied_text, created_on, last_updated) " +
																						"VALUES (?, ?, ?, NOW(), NOW())";
	
	private static final String SQL_SELECT_PLACE_GROUP_POLL_BY_POLL_ID 			= 	"SELECT * FROM placegroup_poll WHERE poll_id = ?";
	
	private static final String SQL_UPDATE_PLACE_GROUP_POLL						=	"UPDATE placegroup_poll SET poll_option_count_1 = ?, poll_option_count_2 = ?, poll_option_count_3 = ?, poll_option_count_4 = ?, poll_total_count = ?, last_updated = NOW() WHERE poll_id = ?";



	@Override
	public int findPlaceGroupByMapId(String placeMapId) throws DataAccessException {
		
		int placeGroupId = -1;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_PLACEGROUP_FIND_BY_MAP_ID);
			ps.setString(1, placeMapId);
			
			logger.debug("[findPlaceGroupByMapId()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				logger.debug("Place group record exists for Place Map Id : " + placeMapId);
				placeGroupId = rs.getInt(DAOConstants.TABLE_PLACEGROUP_COLUMN_PLACE_GROUP_ID);
			}
			else{
				logger.debug("No Place group exists for Place Map Id : " + placeMapId);
			}
		}
		catch(SQLException sqlEx){
			logger.error("findPlaceGroupByMapId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("findPlaceGroupByMapId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("findPlaceGroupByMapId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("findPlaceGroupByMapId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("findPlaceGroupByMapId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return placeGroupId;

	}


	@Override
	public int createPlaceGroup(PlaceGroupData placeGroupData) throws DataAccessException {
		
		int placeGroupId = -1;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_PLACEGROUP, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, placeGroupData.getPlaceGroupName());
			ps.setString(2, placeGroupData.getPlaceGroupLatitude());
			ps.setString(3, placeGroupData.getPlaceGroupLongitude());
			ps.setString(4, placeGroupData.getPlaceGroupMapId());
			
			logger.debug("[createPlaceGroup()-QUERY] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("createPlaceGroup() -> Failed to insert record for new Place Group in database.");
				throw new DataAccessException("createPlaceGroup() -> Failed to insert record for new Place Group in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				placeGroupId = generatedKeys.getInt(1);
				con.commit();
			}
			else{
				logger.error("createPlaceGroup() -> Failed to insert record for new Place Group in database., no generated key obtained.");
				throw new DataAccessException("createPlaceGroup() -> Failed to insert record for new Place Group in database., no generated key obtained.");
			}

		}
		catch(SQLException sqlEx){
			logger.error("createPlaceGroup", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("createPlaceGroup() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("createPlaceGroup", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("createPlaceGroup() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("createPlaceGroup() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return placeGroupId;

	}


	@Override
	public List<PlaceGroupPostData> fetchPostDataByPlaceGroupId(int placeGroupId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		List<PlaceGroupPostData> alPlaceGroupPostData = null;
		StringBuffer consPostIds = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_PLACE_GROUP_MAPPING);
			ps.setInt(1, placeGroupId);
			
			logger.debug("[fetchPostDataByPlaceGroupId()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total Post counts for Place Group Id : " + placeGroupId + " are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					consPostIds = new StringBuffer();
					
					ps = con.prepareStatement(SQL_SELECT_PLACE_GROUP_MAPPING);
					ps.setInt(1, placeGroupId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPostDataByPlaceGroupId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					while (rs.next()){
						if (DAOConstants.PLACEGROUP_MAPPING_ENTITY_TYPE_POST == rs.getInt(DAOConstants.TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_TYPE)){
							consPostIds.append(rs.getInt(DAOConstants.TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_ID)).append(",");
						}
					}
					
					if (consPostIds.toString().endsWith(",")) {
						consPostIds = new StringBuffer((consPostIds.substring(0, consPostIds.lastIndexOf(","))));
					}
					
					if (!GenericUtility.safeTrim(consPostIds.toString()).equals(GenericConstants.EMPTY_STRING)){
						stmt = con.createStatement();
						
						sql = SQL_SELECT_PLACE_GROUP_POST_BY_PLACEGROUP_ID_1 + consPostIds + SQL_SELECT_PLACE_GROUP_POST_BY_PLACEGROUP_ID_2;
						
						logger.debug("[fetchPostDataByPlaceGroupId()-QUERY 3] : " + sql);
						
						rs = stmt.executeQuery(sql);			
						
						if (rs.next()){
							rs.previous();
							
							alPlaceGroupPostData = new ArrayList<PlaceGroupPostData>();
							
							while(rs.next()){
								PlaceGroupPostData placeGroupPostData = new PlaceGroupPostData();
								
								placeGroupPostData.setPostId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POSTID));
								placeGroupPostData.setPlaceGroupId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_PLACEGROUP_ID));
								placeGroupPostData.setPostText(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POST_TEXT));
								placeGroupPostData.setPostImagePath(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POST_IMAGE_PATH));
								placeGroupPostData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
								placeGroupPostData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
								placeGroupPostData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_CREATED_TIMESTAMP));
								
								alPlaceGroupPostData.add(placeGroupPostData);
							}
						}
					}
				
				}
			}
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchPostDataByPlaceGroupId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPostDataByPlaceGroupId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPostDataByPlaceGroupId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPostDataByPlaceGroupId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPostDataByPlaceGroupId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alPlaceGroupPostData;

	}


	@Override
	public List<PlaceGroupPollData> fetchPollDataByPlaceGroupId(int placeGroupId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		
		List<PlaceGroupPollData> alPlaceGroupPollData = null;
		StringBuffer consPollIds = null;		
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_PLACE_GROUP_MAPPING);
			ps.setInt(1, placeGroupId);
			
			logger.debug("[fetchPollDataByPlaceGroupId()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			int count = 0;
			
			if (rs.next()){
				count = rs.getInt("count");
				
				logger.debug("Total Poll counts for Place Group Id : " + placeGroupId + " are : " + count);
			}
			
			if (count > 0){
				int offset = (pageCount-1) * maxPageResults;
				
				if (offset >= count){
					logger.debug("Page count has exceeded the no. of available pages. No results are avialable on this page.");
					throw new PageCountExceededException("Page count has exceeded the no. of available pages. No results are avialable on this page.");
				}
				else{
					consPollIds = new StringBuffer();
					
					ps = con.prepareStatement(SQL_SELECT_PLACE_GROUP_MAPPING);
					ps.setInt(1, placeGroupId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPollDataByPlaceGroupId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					while (rs.next()){
						if (DAOConstants.PLACEGROUP_MAPPING_ENTITY_TYPE_POLL == rs.getInt(DAOConstants.TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_TYPE)){
							consPollIds.append(rs.getInt(DAOConstants.TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_ID)).append(",");
						}
					}
					
					if (consPollIds.toString().endsWith(",")) {
						consPollIds = new StringBuffer((consPollIds.substring(0, consPollIds.lastIndexOf(","))));
					}
					
					if (!GenericUtility.safeTrim(consPollIds.toString()).equals(GenericConstants.EMPTY_STRING)){
						stmt = con.createStatement();
						
						sql = SQL_SELECT_PLACE_GROUP_POLL_BY_PLACEGROUP_ID_1 + consPollIds + SQL_SELECT_PLACE_GROUP_POLL_BY_PLACEGROUP_ID_2;
						
						logger.debug("[fetchPollDataByPlaceGroupId()-QUERY 3] : " + sql);
						
						rs = stmt.executeQuery(sql);
						
						if (rs.next()){
							rs.previous();
							
							alPlaceGroupPollData = new ArrayList<PlaceGroupPollData>();
							
							while(rs.next()){
								PlaceGroupPollData placeGroupPollData = new PlaceGroupPollData();
								
								placeGroupPollData.setPollId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLLID));
								placeGroupPollData.setPlaceGroupId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_PLACEGROUP_ID));
								placeGroupPollData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
								placeGroupPollData.setPollQuestion(rs.getString(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_QUESTION));
								placeGroupPollData.setPollOption1(rs.getString(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_1));
								placeGroupPollData.setPollOption2(rs.getString(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_2));
								placeGroupPollData.setPollOption3(rs.getString(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_3));
								placeGroupPollData.setPollOption4(rs.getString(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_4));
								placeGroupPollData.setPollOptionCount1(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1));
								placeGroupPollData.setPollOptionCount2(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2));
								placeGroupPollData.setPollOptionCount3(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3));
								placeGroupPollData.setPollOptionCount4(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4));
								placeGroupPollData.setPollTotalCount(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_TOTAL_COUNT));
								placeGroupPollData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
								placeGroupPollData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_CREATED_TIMESTAMP));
								
								alPlaceGroupPollData.add(placeGroupPollData);
							}
						}
					}
					
				}
			}
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(SQLException sqlEx){
			logger.error("fetchPollDataByPlaceGroupId", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("fetchPollDataByPlaceGroupId() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("fetchPollDataByPlaceGroupId", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("fetchPollDataByPlaceGroupId() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("fetchPollDataByPlaceGroupId() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return alPlaceGroupPollData;

	}


	@Override
	public List<PlaceGroupPostReplyData> fetchPostReplyDataByPostId(int postId, int pageCount, int maxPageResults) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<PlaceGroupPostReplyData> alPlaceGroupPostReplyData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_COUNT_PLACE_GROUP_POST_REPLY_DETAILS);
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
					ps = con.prepareStatement(SQL_SELECT_PLACE_GROUP_POST_REPLY_DETAILS);
					ps.setInt(1, postId);
					ps.setInt(2, offset);
					ps.setInt(3, maxPageResults);
					
					logger.debug("[fetchPostReplyDataByPostId()-QUERY 2] : " + ps.toString());
					
					rs = ps.executeQuery();
					
					if (rs.next()){
						rs.previous();
						
						alPlaceGroupPostReplyData = new ArrayList<PlaceGroupPostReplyData>();
						
						while(rs.next()){
							PlaceGroupPostReplyData placeGroupPostReplyData = new PlaceGroupPostReplyData();
							
							placeGroupPostReplyData.setPostId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POST_REPLY_COLUMN_POSTID));
							placeGroupPostReplyData.setRepliedText(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_REPLY_COLUMN_REPLIED_TEXT));
							placeGroupPostReplyData.setRepliedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
							placeGroupPostReplyData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
							placeGroupPostReplyData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_PLACEGROUP_POST_REPLY_COLUMN_CREATED_TIMESTAMP));
							
							placeGroupPostReplyData.setTotalResults(count);
							
							alPlaceGroupPostReplyData.add(placeGroupPostReplyData);
						}
					}
					else{
						logger.debug("No post responses found for the post id : " + postId);
						//throw new NoPlaceGroupPostReplyDataFoundException("No post responses found for the post id : " + postId);
					}
				}
			}
			else{
				logger.debug("No post responses found for the post id : " + postId);
				//throw new NoPlaceGroupPostReplyDataFoundException("No post responses found for the post id : " + postId);
			}
		}
		catch (PageCountExceededException pageCountEx){
			throw pageCountEx;
		}
		catch(NoPlaceGroupPostReplyDataFoundException noDataEx){
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
		
		return alPlaceGroupPostReplyData;

	}


	@Override
	public PlaceGroupPostData fetchPostDataByPostId(int postId) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		PlaceGroupPostData placeGroupPostData = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_PLACE_GROUP_POST_BY_POST_ID);
			ps.setInt(1, postId);
			
			logger.debug("[fetchPostDataByPostId()-QUERY] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				placeGroupPostData = new PlaceGroupPostData();
				
				placeGroupPostData.setPostId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POSTID));
				placeGroupPostData.setPlaceGroupId(rs.getInt(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_PLACEGROUP_ID));
				placeGroupPostData.setPostText(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POST_TEXT));
				placeGroupPostData.setPostImagePath(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_POST_IMAGE_PATH));
				placeGroupPostData.setCreatedUsername(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_FULLNAME));
				placeGroupPostData.setUserPicturePath(rs.getString(DAOConstants.TABLE_USER_PROFILE_COLUMN_PICTUREPATH));
				placeGroupPostData.setCreatedTimestamp(rs.getTimestamp(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_CREATED_TIMESTAMP));
				placeGroupPostData.setUsername(rs.getString(DAOConstants.TABLE_PLACEGROUP_POST_COLUMN_CREATED_USERNAME));
				
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
		
		return placeGroupPostData;

	}


	@Override
	public int createPost(PlaceGroupPostData placeGroupPostData) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		int postId = -1;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_PLACE_GROUP_POST, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, placeGroupPostData.getPlaceGroupId());
			ps.setString(2, placeGroupPostData.getCreatedUsername());
			ps.setString(3, placeGroupPostData.getPostText());
			ps.setString(4, placeGroupPostData.getPostImagePath());
			
			logger.debug("[createPost()-QUERY 1] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("createPost() -> Failed to insert record for Place Group new Post request in database.");
				throw new PlaceGroupCreatePostException("createPost() -> Failed to insert record for Place Group new Post request in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				postId = generatedKeys.getInt(1);
				
				/* Insert entry into mapping table */
				ps = con.prepareStatement(SQL_INSERT_PLACE_GROUP_MAPPING);
				ps.setInt(1, placeGroupPostData.getPlaceGroupId());
				ps.setInt(2, postId);
				ps.setInt(3, DAOConstants.PLACEGROUP_MAPPING_ENTITY_TYPE_POST);
				
				logger.debug("[createPost()-QUERY 2] : " + ps.toString());
				
				rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					logger.error("createPost() -> Failed to insert record for Place Group mapping for new Post request in database.");
					throw new ShoutGroupCreatePostException("createPost() -> Failed to insert record for Place Group mapping for new Post request in database.");
				}
				else{
					con.commit();
				}
			}
			else{
				logger.error("createPost() -> Failed to insert record for Place Group new Post request in database, no generated key obtained.");
				throw new PlaceGroupCreatePostException("createPost() -> Failed to insert record for Place Group new Post request in database, no generated key obtained.");
			}
		}
		catch(PlaceGroupCreatePostException createPostEx){
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
	public int createPoll(PlaceGroupPollData placeGroupPollData) throws DataAccessException {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet generatedKeys = null;
		
		int pollId = -1;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_PLACE_GROUP_POLL, Statement.RETURN_GENERATED_KEYS);
			
			ps.setInt(1, placeGroupPollData.getPlaceGroupId());
			ps.setString(2, placeGroupPollData.getCreatedUsername());
			ps.setString(3, placeGroupPollData.getPollQuestion());
			ps.setString(4, placeGroupPollData.getPollOption1());
			ps.setString(5, placeGroupPollData.getPollOption2());
			ps.setString(6, placeGroupPollData.getPollOption3());
			ps.setString(7, placeGroupPollData.getPollOption4());
			
			logger.debug("[createPoll()-QUERY 1] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("createPoll() -> Failed to insert record for Place Group new Poll request in database.");
				throw new PlaceGroupCreatePollException("createPoll() -> Failed to insert record for Place Group new Poll request in database.");
			}
			generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()){
				pollId = generatedKeys.getInt(1);

				/* Insert entry into mapping table */
				ps = con.prepareStatement(SQL_INSERT_PLACE_GROUP_MAPPING);
				ps.setInt(1, placeGroupPollData.getPlaceGroupId());
				ps.setInt(2, pollId);
				ps.setInt(3, DAOConstants.PLACEGROUP_MAPPING_ENTITY_TYPE_POLL);
				
				logger.debug("[createPoll()-QUERY 2] : " + ps.toString());
				
				rowsInserted = ps.executeUpdate();
				
				if (rowsInserted <= 0){
					logger.error("createPoll() -> Failed to insert record for Place Group mapping for new Poll request in database.");
					throw new ShoutGroupCreatePollException("createPoll() -> Failed to insert record for Place Group mapping for new Poll request in database.");
				}
				else{
					con.commit();
				}
			}
			else{
				logger.error("createPoll() -> Failed to insert record for Place Group new Poll request in database, no generated key obtained.");
				throw new PlaceGroupCreatePollException("createPoll() -> Failed to insert record for Place Group new Poll request in database, no generated key obtained.");
			}
		}
		catch(PlaceGroupCreatePollException createPollEx){
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
	public boolean replyPost(PlaceGroupPostReplyData placeGroupPostReplyData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_PLACE_GROUP_POST_REPLY);
			
			ps.setInt(1, placeGroupPostReplyData.getPostId());
			ps.setString(2, placeGroupPostReplyData.getRepliedUsername());
			ps.setString(3, placeGroupPostReplyData.getRepliedText());
			
			logger.debug("[replyPost()-QUERY] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				logger.error("replyPost() -> Failed to insert record for Place Group Post Reply request in database.");
				throw new PlaceGroupPostReplyException("replyPost() -> Failed to insert record for Place Group Post Reply request in database.");
			}
			else{
				flag = true;
				con.commit();
			}
		}
		catch(PlaceGroupPostReplyException postReplyEx){
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
	public boolean replyPoll(PlaceGroupPollData placeGroupPollData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_SELECT_PLACE_GROUP_POLL_BY_POLL_ID);
			
			ps.setInt(1, placeGroupPollData.getPollId());
			
			logger.debug("[replyPoll()-QUERY 1] : " + ps.toString());
			
			rs = ps.executeQuery();
			
			if (rs.next()){
				int pollOption1 = rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1);
				int pollOption2 = rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2);
				int pollOption3 = rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3);
				int pollOption4 = rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4);
				int totalPollsCount = rs.getInt(DAOConstants.TABLE_PLACEGROUP_POLL_COLUMN_POLL_TOTAL_COUNT);
				
				switch(placeGroupPollData.getSelectedPollCount()){
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
						logger.error("Invalid option selected for Poll Id : " + placeGroupPollData.getPollId());
						throw new PlaceGroupPollReplyException("Invalid option selected for Poll Id : " + placeGroupPollData.getPollId());
				}
				
				/* Update Poll reply */
				ps = con.prepareStatement(SQL_UPDATE_PLACE_GROUP_POLL);
				
				ps.setInt(1, pollOption1);
				ps.setInt(2, pollOption2);
				ps.setInt(3, pollOption3);
				ps.setInt(4, pollOption4);
				ps.setInt(5, totalPollsCount);
				ps.setInt(6, placeGroupPollData.getPollId());
				
				logger.debug("[replyPoll()-QUERY 2] : " + ps.toString());
				
				int rowsUpdated = ps.executeUpdate();
				
				if (rowsUpdated <= 0){
					logger.error("replyPoll() -> Failed to update record for Place Group Poll Reply request in database.");
					throw new PlaceGroupPollReplyException("replyPoll() -> Failed to insert record for Place Group Poll Reply request in database.");
				}
				else{
					flag = true;
					con.commit();
				}
			}
			else{
				logger.error("No record found for Poll Id : " + placeGroupPollData.getPollId());
				throw new PlaceGroupPollReplyException("No record found for Poll Id : " + placeGroupPollData.getPollId());
			}
		}
		catch(PlaceGroupPollReplyException pollReplyEx){
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
