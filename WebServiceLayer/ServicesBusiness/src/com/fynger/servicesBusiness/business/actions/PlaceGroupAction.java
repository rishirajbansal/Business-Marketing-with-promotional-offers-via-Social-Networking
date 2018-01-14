/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.List;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.exception.base.EExceptionTypes;
import com.fynger.generic.exception.base.ExceptionUtility;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.notificationEngine.engine.NotificationEngine;
import com.fynger.notificationEngine.exception.NotificationEngineException;
import com.fynger.notificationEngine.requests.vo.NotificationRequestVO;
import com.fynger.notificationEngine.responses.vo.NotificationResponseVO;
import com.fynger.servicesBusiness.business.base.AbstractBusinessAction;
import com.fynger.servicesBusiness.business.base.ERequestType;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FileUploadFailedException;
import com.fynger.servicesBusiness.exception.NoPlaceGroupPostReplyDataFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.PlaceGroupCreatePollException;
import com.fynger.servicesBusiness.exception.PlaceGroupCreatePostException;
import com.fynger.servicesBusiness.exception.PlaceGroupPollReplyException;
import com.fynger.servicesBusiness.exception.PlaceGroupPostReplyException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPlaceGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.PlaceGroupPostReplyData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserDeviceAuthData;
import com.fynger.servicesBusiness.objects.NotificationMessageAssembler;
import com.fynger.servicesBusiness.utilities.FileUploader;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.PlaceGroupValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSPlaceGroupRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSPlaceGroupResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPost;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPostReply;

/**
 * @author Rishi
 *
 */
public class PlaceGroupAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(PlaceGroupAction.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String maxPageResults;
	
	DAOFactory daoFactory = null;
	
	private static NotificationEngine notificationEngine;
	
	public PlaceGroupAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
		
		notificationEngine = NotificationEngine.createInstance();
	}
	
	static{
		maxPageResults = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.PLACE_GROUP_MAX_PAGE_RESULTS);
	}

	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSPlaceGroupRequest wsPlaceGroupRequest = (WSPlaceGroupRequest)request;
		
		WSPlaceGroupResponse wsPlaceGroupResponse = new WSPlaceGroupResponse();
		
		try{
			switch(requestType){
			
				case PLACE_LOAD_DETAILS: 
					loadDetails(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_VIEW_POST_RESPONSES: 
					viewPostResponses(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_CREATE_POST: 
					createPost(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_CREATE_POST_UPLOAD: 
					uploadPost(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_CREATE_POLL: 
					createPoll(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_REPLY_POST: 
					replyPost(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				case PLACE_REPLY_POLL: 
					replyPoll(wsPlaceGroupRequest, wsPlaceGroupResponse, requestType);
					break;
					
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Place Group service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoPlaceGroupPostReplyDataFoundException noPostReplyDataEx){
			logger.debug("NoPlaceGroupPostReplyDataFoundException occurred : " + noPostReplyDataEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PLACE_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_PLACET_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION, noPostReplyDataEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PlaceGroupCreatePostException createPostEx){
			logger.debug("PlaceGroupCreatePostException occurred : " + createPostEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PLACE_GROUP_CREATE_POST_EXCEPTION, ExceptionConstants.USERMESSAGE_PLACE_GROUP_CREATE_POST_EXCEPTION, createPostEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PlaceGroupCreatePollException createPollEx){
			logger.debug("PlaceGroupCreatePollException occurred : " + createPollEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PLACE_GROUP_CREATE_POLL_EXCEPTION, ExceptionConstants.USERMESSAGE_PLACE_GROUP_CREATE_POLL_EXCEPTION, createPollEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PlaceGroupPostReplyException postReplyEx){
			logger.debug("PlaceGroupPostReplyException occurred : " + postReplyEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PLACE_GROUP_POST_REPLY_EXCEPTION, ExceptionConstants.USERMESSAGE_PLACE_GROUP_POST_REPLY_EXCEPTION, postReplyEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PlaceGroupPollReplyException pollReplyEx){
			logger.debug("PlaceGroupPollReplyException occurred : " + pollReplyEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PLACE_GROUP_POLL_REPLY_EXCEPTION, ExceptionConstants.USERMESSAGE_PLACE_GROUP_POLL_REPLY_EXCEPTION, pollReplyEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FileUploadFailedException fileEx){
			logger.debug("FileUploadFailedException occurred during uploading the picture.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FILE_UPLOAD_FAILED, ExceptionConstants.USERMESSAGE_FILE_UPLOAD_FAILED, ExceptionConstants.ERRORMESSAGE_FILE_UPLOAD_FAILED, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PageCountExceededException pceEx){
			logger.debug("PageCountExceededException occurred as page no. count is exceeded.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PAGE_COUNT_EXCEEDED_EXCEPTION, ExceptionConstants.USERMESSAGE_PAGE_COUNT_EXCEEDED_EXCEPTION, pceEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NotificationEngineException notifyEx){
			logger.debug("NotificationEngineException recevied from Notification Engine : " + notifyEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_NOTIFICATION_ENGINE_EXCEPTION, ExceptionConstants.USERMESSAGE_NOTIFICATION_ENGINE_EXCEPTION, notifyEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_DATA_ACCESS_EXCEPTION, ExceptionConstants.USERMESSAGE_DATA_ACCESS_EXCEPTION, daEx.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
			
		return wsPlaceGroupResponse;

	}
	
	public void loadDetails(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		boolean dataExists = false;
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			if (wsPlaceGroupRequest.getPageCount() == 0){
				wsPlaceGroupRequest.setPageCount(1);
			}
			
			//Check if any record exists for given place map id
			int placeGroupId = placeGroupDAO.findPlaceGroupByMapId(wsPlaceGroupRequest.getPlaceMapId());
			
			if (placeGroupId > 0){
				logger.debug("Place group found in the database with the map id : " + wsPlaceGroupRequest.getPlaceMapId());
				
				/* Load the Post data */
				List<PlaceGroupPostData> alPlaceGroupPostData = placeGroupDAO.fetchPostDataByPlaceGroupId(placeGroupId, wsPlaceGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
				
				/* Load the Poll data */
				List<PlaceGroupPollData> alPlaceGroupPollData = placeGroupDAO.fetchPollDataByPlaceGroupId(placeGroupId, wsPlaceGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
				
				
				if (null != alPlaceGroupPostData && alPlaceGroupPostData.size() > 0){
					dataExists = true;
					populatePlaceGroupPostData(wsPlaceGroupResponse, alPlaceGroupPostData);
				}
				
				if (null != alPlaceGroupPollData && alPlaceGroupPollData.size() > 0){
					dataExists = true;
					populatePlaceGroupPollData(wsPlaceGroupResponse, alPlaceGroupPollData);
				}
			}
			else{
				//Insert new record for the new place in the database
				
				PlaceGroupData placeGroupData = new PlaceGroupData();
				placeGroupData.setPlaceGroupMapId(wsPlaceGroupRequest.getPlaceMapId());
				placeGroupData.setPlaceGroupLatitude(wsPlaceGroupRequest.getLatitude());
				placeGroupData.setPlaceGroupLongitude(wsPlaceGroupRequest.getLongitude());
				placeGroupData.setPlaceGroupName(wsPlaceGroupRequest.getPlaceName());
				
				placeGroupId = placeGroupDAO.createPlaceGroup(placeGroupData);
				
				logger.debug("New record is inserted in the database for the new Place group");
			}
			
			if (!dataExists){
				//Send the place group id and message
				wsPlaceGroupResponse.setPlaceGroupId(Integer.toString(placeGroupId));
				wsPlaceGroupResponse.setMessage(ExceptionConstants.USERMESSAGE_PLACE_GROUP_NO_PLACE_RECORD_FOUND);
				wsPlaceGroupResponse.setNewPlace(GenericConstants.STRING_TRUE);
			}
			else{
				wsPlaceGroupResponse.setPlaceGroupId(Integer.toString(placeGroupId));
				wsPlaceGroupResponse.setNewPlace(GenericConstants.STRING_FALSE);
			}
		}
		else{
			logger.debug("Place Group - Load Details validations failed.");
			throw new BusinessException("Place Group - Load Details validations failed.");
		}
	}
	
	public void viewPostResponses(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			WSPlaceGroupPost wsPlaceGroupPost = wsPlaceGroupRequest.getPlaceGroupPost();
			
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			if (wsPlaceGroupRequest.getPageCount() == 0){
				wsPlaceGroupRequest.setPageCount(1);
			}
			
			List<PlaceGroupPostReplyData> alPlaceGroupPostReplyData = placeGroupDAO.fetchPostReplyDataByPostId(Integer.parseInt(wsPlaceGroupPost.getPostId()), wsPlaceGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
			
			PlaceGroupPostData placeGroupPostData = placeGroupDAO.fetchPostDataByPostId(Integer.parseInt(wsPlaceGroupPost.getPostId()));
			
			populatePostResponsesData(wsPlaceGroupResponse, alPlaceGroupPostReplyData, placeGroupPostData);
			
			if (null != alPlaceGroupPostReplyData && alPlaceGroupPostReplyData.size() > 0){
				//Do Nothing
			}
			else{
				logger.debug("No post Reply data found for the post id : " + wsPlaceGroupPost.getPostId());
				wsPlaceGroupResponse.setMessage(ExceptionConstants.USERMESSAGE_PLACE_GROUP_NO_POST_REPLY_DATA_FOUND);
				//throw new NoPlaceGroupPostReplyDataFoundException("No post Reply data found for the post id : " + wsPlaceGroupPost.getPostId());
			}
		}
		else{
			logger.debug("Place Group - View Post Reponses validations failed.");
			throw new BusinessException("Place Group - View Post Reponses validations failed.");
		}
		
	}
	
	public void createPost(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			WSPlaceGroupPost wsPlaceGroupPost = wsPlaceGroupRequest.getPlaceGroupPost();
			
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			PlaceGroupPostData placeGroupPostData = new PlaceGroupPostData();
			
			placeGroupPostData.setPlaceGroupId(Integer.parseInt(wsPlaceGroupPost.getPlaceGroupId()));
			placeGroupPostData.setCreatedUsername(wsPlaceGroupRequest.getUsername());
			placeGroupPostData.setPostText(wsPlaceGroupPost.getPostText());
			placeGroupPostData.setPostImagePath(null);
			
			int postId = placeGroupDAO.createPost(placeGroupPostData);
			
			if (postId > 0){
				logger.debug("New post is created successfully with Post Id : " + postId);
				
				wsPlaceGroupPost = new WSPlaceGroupPost();
				wsPlaceGroupPost.setPostId(Integer.toString(postId));
				
				generateSuccessResponse(wsPlaceGroupResponse);
				wsPlaceGroupResponse.setPlaceGroupPost(wsPlaceGroupPost);
			}
			else{
				logger.error("Failed to insert record for new Post request.");
				throw new PlaceGroupCreatePostException("Failed to insert record for new Post request.");
			}
		}
		else{
			logger.debug("Place Group - Create Post validations failed.");
			throw new PlaceGroupCreatePostException("Place Group - Create Post validations failed.");
		}
	}
	
	public void uploadPost(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			/* Upload the picture */
			
			FileUploader fileUploader = new FileUploader();
			String uploadFilePath = fileUploader.generateUploadFilePath(BusinessConstants.PLACE_GROUP_POST_PICTURE_UPLOAD_PREFIX, wsPlaceGroupRequest.getPlaceGroupPost().getPictureFileName());
			String downloadFilePath = fileUploader.generateDownloadFilePath(BusinessConstants.PLACE_GROUP_POST_PICTURE_UPLOAD_PREFIX, wsPlaceGroupRequest.getPlaceGroupPost().getPictureFileName());
			fileUploader.uploadFile(wsPlaceGroupRequest.getPlaceGroupPost().getPictureStream(), uploadFilePath);
			
			logger.debug("User Picture is uploaded successfully.");
			
			/* Update the record in database */
			
			WSPlaceGroupPost wsPlaceGroupPost = wsPlaceGroupRequest.getPlaceGroupPost();
			
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			PlaceGroupPostData placeGroupPostData = new PlaceGroupPostData();
			
			placeGroupPostData.setPlaceGroupId(Integer.parseInt(wsPlaceGroupPost.getPlaceGroupId()));
			placeGroupPostData.setCreatedUsername(wsPlaceGroupRequest.getUsername());
			placeGroupPostData.setPostText(wsPlaceGroupPost.getPostText());
			placeGroupPostData.setPostImagePath(downloadFilePath);
			
			int postId = placeGroupDAO.createPost(placeGroupPostData);
			
			if (postId > 0){
				logger.debug("New post is created successfully with Post Id : " + postId);
				
				wsPlaceGroupPost = new WSPlaceGroupPost();
				wsPlaceGroupPost.setPostId(Integer.toString(postId));
				
				generateSuccessResponse(wsPlaceGroupResponse);
				wsPlaceGroupResponse.setPlaceGroupPost(wsPlaceGroupPost);
			}
			else{
				logger.error("Failed to insert record for new Post With Upload request.");
				throw new PlaceGroupCreatePostException("Failed to insert record for new Post With Upload request.");
			}
		}
		else{
			logger.debug("Place Group - Create Post With Upload validations failed.");
			throw new PlaceGroupCreatePostException("Place Group - Create Post With Upload validations failed.");
		}
	}
	
	public void createPoll(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			WSPlaceGroupPoll wsPlaceGroupPoll = wsPlaceGroupRequest.getPlaceGroupPoll();
			
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			PlaceGroupPollData placeGroupPollData = new PlaceGroupPollData();
			
			placeGroupPollData.setPlaceGroupId(Integer.parseInt(wsPlaceGroupPoll.getPlaceGroupId()));
			placeGroupPollData.setCreatedUsername(wsPlaceGroupRequest.getUsername());
			placeGroupPollData.setPollQuestion(wsPlaceGroupPoll.getPollQuestion());
			placeGroupPollData.setPollOption1(wsPlaceGroupPoll.getPollOption1());
			placeGroupPollData.setPollOption2(wsPlaceGroupPoll.getPollOption2());
			placeGroupPollData.setPollOption3(wsPlaceGroupPoll.getPollOption3());
			placeGroupPollData.setPollOption4(wsPlaceGroupPoll.getPollOption4());
			
			int pollId = placeGroupDAO.createPoll(placeGroupPollData);
			
			if (pollId > 0){
				logger.debug("New post is created successfully with Poll Id : " + pollId);
				
				wsPlaceGroupPoll = new WSPlaceGroupPoll();
				wsPlaceGroupPoll.setPollId(Integer.toString(pollId));
				
				generateSuccessResponse(wsPlaceGroupResponse);
				wsPlaceGroupResponse.setPlaceGroupPoll(wsPlaceGroupPoll);
			}
			else{
				logger.error("Failed to insert record for new Poll request.");
				throw new PlaceGroupCreatePollException("Failed to insert record for new Poll request.");
			}
		}
		else{
			logger.debug("Place Group - Create Poll validations failed.");
			throw new PlaceGroupCreatePollException("Place Group - Create Poll validations failed.");
		}
		
	}
	
	public void replyPost(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			PlaceGroupPostReplyData placeGroupPostReplyData = new PlaceGroupPostReplyData();
			placeGroupPostReplyData.setPostId(Integer.parseInt(wsPlaceGroupRequest.getPlaceGroupPost().getPostId()));
			placeGroupPostReplyData.setRepliedText(wsPlaceGroupRequest.getPlaceGroupPost().getPostReply());
			placeGroupPostReplyData.setRepliedUsername(wsPlaceGroupRequest.getUsername());
			
			boolean flag = placeGroupDAO.replyPost(placeGroupPostReplyData);
			
			if (flag){
				logger.debug("Post reply record is saved successfully in the database.");
				
				/* Send Mobile Notification */
				
				PlaceGroupPostData placeGroupPostData = placeGroupDAO.fetchPostDataByPostId(Integer.parseInt(wsPlaceGroupRequest.getPlaceGroupPost().getPostId()));
				
				IUserDAO userDao = daoFactory.getUserDAO();
				
				UserDeviceAuthData userDeviceAuthData = userDao.fetchUserDeviceAuthDetails(placeGroupPostData.getUsername());
				
				if (null != userDeviceAuthData){
					
					String deviceAuthId = userDeviceAuthData.getDeviceRegId();
					
					if (!GenericUtility.safeTrim(deviceAuthId).equals(GenericConstants.EMPTY_STRING)){
					
						NotificationRequestVO notificationRequestVO = new NotificationRequestVO();
						NotificationMessageAssembler notificationMessageAssembler = new NotificationMessageAssembler();
						
						notificationRequestVO.setNameValuePairs(notificationMessageAssembler.getPlaceGroupPostReplyMessageContent(placeGroupPostData.getUsername(), wsPlaceGroupRequest.getUsername(), wsPlaceGroupRequest.getPlaceGroupPost().getPostReply(), userDeviceAuthData.getDeviceType(), wsPlaceGroupRequest.getPlaceGroupPost().getPostId()));
						
						notificationRequestVO.setRegistrationId(userDeviceAuthData.getDeviceRegId());
						
						NotificationResponseVO notificationResponseVO = notificationEngine.sendNotification(notificationRequestVO);
						
						boolean notificationFlag = notificationResponseVO.isStatus();
						
						if (notificationFlag){
							logger.debug("Notification has been dispatched successfully for user name : " + wsPlaceGroupRequest.getUsername());
						}
						else{
							logger.debug("Fail to dispatch Notification successfully for user name : " + wsPlaceGroupRequest.getUsername() + ". Check the notification logs in the database.");
						}
					}
					else{
						logger.debug("No device auth id found for username : " + wsPlaceGroupRequest.getUsername() + " . No mobile notification will be send.");
					}
				}
				else{
					logger.debug("No device auth details are found for username : " + wsPlaceGroupRequest.getUsername() + " . No mobile notification will be send.");
				}
				
				generateSuccessResponse(wsPlaceGroupResponse);
			}
			else{
				logger.debug("Post reply record is failed to save successfully in the database.");
				throw new PlaceGroupPostReplyException("Post reply record is failed to save successfully in the database.");
			}
		}
		else{
			logger.debug("Place Group - Post Reply validations failed.");
			throw new PlaceGroupPostReplyException("Place Group - Post Reply validations failed.");
		}
		
	}
	
	public void replyPoll(WSPlaceGroupRequest wsPlaceGroupRequest, WSPlaceGroupResponse wsPlaceGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new PlaceGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsPlaceGroupRequest, requestType);
		
		if (isValid){
			IPlaceGroupDAO placeGroupDAO = daoFactory.getPlaceGroupDAO();
			
			PlaceGroupPollData placeGroupPollData = new PlaceGroupPollData();
			placeGroupPollData.setPollId(Integer.parseInt(wsPlaceGroupRequest.getPlaceGroupPoll().getPollId()));
			placeGroupPollData.setSelectedPollCount(Integer.parseInt(wsPlaceGroupRequest.getPlaceGroupPoll().getSelectedPollOption()));
			
			boolean flag = placeGroupDAO.replyPoll(placeGroupPollData);
			
			if (flag){
				logger.debug("Poll reply record is saved successfully in the database.");
				
				generateSuccessResponse(wsPlaceGroupResponse);
			}
			else{
				logger.debug("Poll reply record is failed to save successfully in the database.");
				throw new PlaceGroupPollReplyException("Poll reply record is failed to save successfully in the database.");
			}
		}
		else{
			logger.debug("Place Group - Poll Reply validations failed.");
			throw new PlaceGroupPollReplyException("Place Group - Poll Reply validations failed.");
		}
		
	}
	
	private void populatePlaceGroupPostData(WSPlaceGroupResponse wsPlaceGroupResponse, List<PlaceGroupPostData> alPlaceGroupPostData){
		
		List<WSPlaceGroupPost> placeGroupPostDetails = new ArrayList<WSPlaceGroupPost>();
		
		for (PlaceGroupPostData daoData : alPlaceGroupPostData){
			
			WSPlaceGroupPost wsPlaceGroupPost = new WSPlaceGroupPost();
			
			wsPlaceGroupPost.setPostId(Integer.toString(daoData.getPostId()));
			wsPlaceGroupPost.setCreatorUsername(daoData.getCreatedUsername());
			wsPlaceGroupPost.setPostImagePath(daoData.getPostImagePath());
			wsPlaceGroupPost.setPostText(daoData.getPostText());
			wsPlaceGroupPost.setPlaceGroupId(Integer.toString(daoData.getPlaceGroupId()));
			wsPlaceGroupPost.setUserPicturePath(daoData.getUserPicturePath());
			
			wsPlaceGroupPost.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
			
			placeGroupPostDetails.add(wsPlaceGroupPost);
			
		}
		
		wsPlaceGroupResponse.setPostDetails(placeGroupPostDetails);
	}
	
	private void populatePlaceGroupPollData(WSPlaceGroupResponse wsPlaceGroupResponse, List<PlaceGroupPollData> alPlaceGroupPollData){
		
		List<WSPlaceGroupPoll> placeGroupPollDetails = new ArrayList<WSPlaceGroupPoll>();
		
		for (PlaceGroupPollData daoData : alPlaceGroupPollData){
			
			WSPlaceGroupPoll wsPlaceGroupPoll = new WSPlaceGroupPoll();
			
			wsPlaceGroupPoll.setPollId(Integer.toString(daoData.getPollId()));
			wsPlaceGroupPoll.setCreatorUsername(daoData.getCreatedUsername());
			wsPlaceGroupPoll.setPlaceGroupId(Integer.toString(daoData.getPlaceGroupId()));
			wsPlaceGroupPoll.setPollQuestion(daoData.getPollQuestion());
			wsPlaceGroupPoll.setPollOption1(daoData.getPollOption1());
			wsPlaceGroupPoll.setPollOption2(daoData.getPollOption2());
			wsPlaceGroupPoll.setPollOption3(daoData.getPollOption3());
			wsPlaceGroupPoll.setPollOption4(daoData.getPollOption4());
			wsPlaceGroupPoll.setPollOptionCount1(Integer.toString(daoData.getPollOptionCount1()));
			wsPlaceGroupPoll.setPollOptionCount2(Integer.toString(daoData.getPollOptionCount2()));
			wsPlaceGroupPoll.setPollOptionCount3(Integer.toString(daoData.getPollOptionCount3()));
			wsPlaceGroupPoll.setPollOptionCount4(Integer.toString(daoData.getPollOptionCount4()));
			wsPlaceGroupPoll.setPollTotalCount(Integer.toString(daoData.getPollTotalCount()));
			wsPlaceGroupPoll.setUserPicturePath(daoData.getUserPicturePath());
			
			wsPlaceGroupPoll.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
			
			placeGroupPollDetails.add(wsPlaceGroupPoll);
			
		}
		
		wsPlaceGroupResponse.setPollDetails(placeGroupPollDetails);
	}
	
	private void populatePostResponsesData(WSPlaceGroupResponse wsPlaceGroupResponse, List<PlaceGroupPostReplyData> alPlaceGroupPostReplyData, PlaceGroupPostData placeGroupPostData){
		
		List<WSPlaceGroupPostReply> wsPostReplies = new ArrayList<WSPlaceGroupPostReply>();
		
		int totalResults = 0;
		
		WSPlaceGroupPost wsPlaceGroupPost = new WSPlaceGroupPost();
		
		if (null != alPlaceGroupPostReplyData && alPlaceGroupPostReplyData.size() > 0){
			for (PlaceGroupPostReplyData daoData : alPlaceGroupPostReplyData){
				WSPlaceGroupPostReply wsPlaceGroupPostReply = new WSPlaceGroupPostReply();
				
				wsPlaceGroupPostReply.setRepliedText(daoData.getRepliedText());
				wsPlaceGroupPostReply.setRepliedUsername(daoData.getRepliedUsername());
				wsPlaceGroupPostReply.setUserPicturePath(daoData.getUserPicturePath());
				
				wsPlaceGroupPostReply.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
				
				totalResults = daoData.getTotalResults();
				
				wsPostReplies.add(wsPlaceGroupPostReply);
			}
			wsPlaceGroupPost.setPostReplies(wsPostReplies);
		}
		
		wsPlaceGroupPost.setPostId(Integer.toString(placeGroupPostData.getPostId()));
		wsPlaceGroupPost.setCreatorUsername(placeGroupPostData.getCreatedUsername());
		wsPlaceGroupPost.setPostImagePath(placeGroupPostData.getPostImagePath());
		wsPlaceGroupPost.setPostText(placeGroupPostData.getPostText());
		wsPlaceGroupPost.setUserPicturePath(placeGroupPostData.getUserPicturePath());
		
		wsPlaceGroupPost.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(placeGroupPostData.getCreatedTimestamp()));
		
		
		wsPlaceGroupResponse.setPlaceGroupPost(wsPlaceGroupPost);
		wsPlaceGroupResponse.setTotalResults(Integer.toString(totalResults));
		wsPlaceGroupResponse.setTotalPages(Integer.toString(getTotalPages(totalResults, Integer.parseInt(maxPageResults))));
		
	}
	
	public int getTotalPages(int totalResults, int maxPageResults) {

        int totalPages = totalResults / maxPageResults;

        if (!(totalResults % maxPageResults == 0)) {
            totalPages++;
        }

        return totalPages;
    }
	
	public void generateSuccessResponse(WSPlaceGroupResponse wsPlaceGroupResponse){
		
		wsPlaceGroupResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
