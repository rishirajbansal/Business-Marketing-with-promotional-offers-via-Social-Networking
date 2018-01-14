/**
 * 
 */
package com.fynger.servicesBusiness.business.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
import com.fynger.servicesBusiness.business.initialize.ShoutGroupLoader;
import com.fynger.servicesBusiness.constants.BusinessConstants;
import com.fynger.servicesBusiness.constants.ExceptionConstants;
import com.fynger.servicesBusiness.exception.BusinessException;
import com.fynger.servicesBusiness.exception.BusinessValidationException;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.exception.FileUploadFailedException;
import com.fynger.servicesBusiness.exception.NoShoutGroupPostAndPollDataFoundException;
import com.fynger.servicesBusiness.exception.NoShoutGroupPostReplyDataFoundException;
import com.fynger.servicesBusiness.exception.PageCountExceededException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePollException;
import com.fynger.servicesBusiness.exception.ShoutGroupCreatePostException;
import com.fynger.servicesBusiness.exception.ShoutGroupException;
import com.fynger.servicesBusiness.exception.ShoutGroupPollReplyException;
import com.fynger.servicesBusiness.exception.ShoutGroupPostReplyException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IShoutGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.CityGeoData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPollData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupPostReplyData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.UserDeviceAuthData;
import com.fynger.servicesBusiness.objects.NotificationMessageAssembler;
import com.fynger.servicesBusiness.utilities.FileUploader;
import com.fynger.servicesBusiness.utilities.Utility;
import com.fynger.servicesBusiness.validations.IBusinessValidator;
import com.fynger.servicesBusiness.validations.ShoutGroupValidator;
import com.fynger.servicesController.services.domainObjects.requests.WSBaseRequest;
import com.fynger.servicesController.services.domainObjects.requests.WSShoutGroupRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSBaseResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSShoutGroupResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPost;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPostReply;

/**
 * @author Rishi
 *
 */
public class ShoutGroupAction extends AbstractBusinessAction {
	
	public static LoggerManager logger = GenericUtility.getLogger(ShoutGroupAction.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static String maxPageResults;
	
	DAOFactory daoFactory = null;
	
	private static NotificationEngine notificationEngine;
	
	public ShoutGroupAction(){
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
		
		notificationEngine = NotificationEngine.createInstance();
	}
	
	static{
		maxPageResults = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.SHOUT_GROUP_MAX_PAGE_RESULTS);
	}


	@Override
	public WSBaseResponse execute(WSBaseRequest request, ERequestType requestType) throws Exception {
		
		WSShoutGroupRequest wsShoutGroupRequest = (WSShoutGroupRequest)request;
		
		WSShoutGroupResponse wsShoutGroupResponse = new WSShoutGroupResponse();
		
		try{
			switch(requestType){
			
				case SHOUT_LOAD_DETAILS: 
					loadDetails(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_VIEW_POST_RESPONSES: 
					viewPostResponses(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_CREATE_POST: 
					createPost(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_CREATE_POST_UPLOAD: 
					uploadPost(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_CREATE_POLL: 
					createPoll(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_REPLY_POST: 
					replyPost(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				case SHOUT_REPLY_POLL: 
					replyPoll(wsShoutGroupRequest, wsShoutGroupResponse, requestType);
					break;
					
				default: 
					throw new BusinessException("Unsupported request type.");
			
			}
		}
		catch(BusinessValidationException bvEx){
			logger.debug("BusinessValidationException occurred due to validation failure of Shout Group service : " + bvEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(bvEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoShoutGroupPostReplyDataFoundException noPostReplyDataEx){
			logger.debug("NoShoutGroupPostReplyDataFoundException occurred : " + noPostReplyDataEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION, noPostReplyDataEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(NoShoutGroupPostAndPollDataFoundException noDataEx){
			logger.debug("NoShoutGroupPostAndPollDataFoundException occurred : " + noDataEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_NO_POST_POLL_DATA_FOUND_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_NO_POST_POLL_DATA_FOUND_EXCEPTION, noDataEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ShoutGroupCreatePostException createPostEx){
			logger.debug("ShoutGroupCreatePostException occurred : " + createPostEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_CREATE_POST_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_CREATE_POST_EXCEPTION, createPostEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ShoutGroupCreatePollException createPollEx){
			logger.debug("ShoutGroupCreatePollException occurred : " + createPollEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_CREATE_POLL_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_CREATE_POLL_EXCEPTION, createPollEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ShoutGroupPostReplyException postReplyEx){
			logger.debug("ShoutGroupPostReplyException occurred : " + postReplyEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_POST_REPLY_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_POST_REPLY_EXCEPTION, postReplyEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ShoutGroupPollReplyException pollReplyEx){
			logger.debug("ShoutGroupPollReplyException occurred : " + pollReplyEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_POLL_REPLY_EXCEPTION, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_POLL_REPLY_EXCEPTION, pollReplyEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(FileUploadFailedException fileEx){
			logger.debug("FileUploadFailedException occurred during uploading the picture.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_FILE_UPLOAD_FAILED, ExceptionConstants.USERMESSAGE_FILE_UPLOAD_FAILED, ExceptionConstants.ERRORMESSAGE_FILE_UPLOAD_FAILED, EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(PageCountExceededException pceEx){
			logger.debug("PageCountExceededException occurred as page no. count is exceeded.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_PAGE_COUNT_EXCEEDED_EXCEPTION, ExceptionConstants.USERMESSAGE_PAGE_COUNT_EXCEEDED_EXCEPTION, pceEx.getMessage(), EExceptionTypes.BUSINESS_EXCEPTION);
		}
		catch(ShoutGroupException sgEx){
			logger.debug("ShoutGroupException occurred : " + sgEx.getMessage());
			throw ExceptionUtility.createExceptionDetail(sgEx.getExceptionDetail(), EExceptionTypes.BUSINESS_EXCEPTION);
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
			logger.error("Exception occurred : " + ex.toString());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, ex.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_BUSINESS_EXCEPTION, ExceptionConstants.USERMESSAGE_BUSINESS_EXCEPTION, th.getMessage(), EExceptionTypes.APPLICATION_EXCEPTION);
		}
			
		return wsShoutGroupResponse;
				
	}
	
	public void loadDetails(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		boolean dataExists = false;
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			double userLatitude = Double.parseDouble(wsShoutGroupRequest.getLatitude());
			double userLongitude = Double.parseDouble(wsShoutGroupRequest.getLongitude());
			
			ShoutGroupLoader shoutGroupLoader = ShoutGroupLoader.createInstance();

			List<CityGeoData> alCityGeoDetails = shoutGroupLoader.getListOfCityGeoDetails();
			
			/* Get the closest city to the user's coordinates */
			int closestCityId = findClosestCityCoordinates(alCityGeoDetails, userLatitude, userLongitude);
			
			if (closestCityId > 0){
				/* Get the closest Shout Group to the user's coordinates */
				List<ShoutGroupData> alCityBasedShoutGroupDetails = shoutGroupLoader.getCityBasedListofShoutGroupGeoDetails(closestCityId);
				
				ShoutGroupData shoutGroupData = findClosestShoutGroup(alCityBasedShoutGroupDetails, userLatitude, userLongitude);
				
				if (null != shoutGroupData){
					wsShoutGroupResponse.setShoutGroupName(shoutGroupData.getShoutGroupName());
					wsShoutGroupResponse.setShoutGroupDescription(shoutGroupData.getShoutGroupDescription());
					wsShoutGroupResponse.setShoutGroupAddress(shoutGroupData.getAddress());
					
					/* Get the post & poll details of the selected shout group */
					
					IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
					
					if (wsShoutGroupRequest.getPageCount() == 0){
						wsShoutGroupRequest.setPageCount(1);
					}
					
					/* Load the Post data */
					List<ShoutGroupPostData> alShoutGroupPostData = shoutGroupDAO.fetchPostDataByShoutGroupId(shoutGroupData.getShoutGroupId(), wsShoutGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
					
					/* Load the Poll data */
					List<ShoutGroupPollData> alShoutGroupPollData = shoutGroupDAO.fetchPollDataByShoutGroupId(shoutGroupData.getShoutGroupId(), wsShoutGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
					
					if (null != alShoutGroupPostData && alShoutGroupPostData.size() > 0){
						dataExists = true;
						populateShoutGroupPostData(wsShoutGroupResponse, alShoutGroupPostData);
					}
					
					if (null != alShoutGroupPollData && alShoutGroupPollData.size() > 0){
						dataExists = true;
						populateShoutGroupPollData(wsShoutGroupResponse, alShoutGroupPollData);
					}
					
					if (!dataExists){
						wsShoutGroupResponse.setShoutGroupId(Integer.toString(shoutGroupData.getShoutGroupId()));
						wsShoutGroupResponse.setNewShoutGroup(GenericConstants.STRING_TRUE);
						wsShoutGroupResponse.setMessage(ExceptionConstants.USERMESSAGE_SHOUT_GROUP_NO_RECORD_FOUND);
					}
					else{
						wsShoutGroupResponse.setShoutGroupId(Integer.toString(shoutGroupData.getShoutGroupId()));
						wsShoutGroupResponse.setNewShoutGroup(GenericConstants.STRING_FALSE);
					}
				}
				else{
					logger.error("No shout group found for the given coordinates");
					throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_LOAD_DETAILS, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_LOAD_DETAILS, 
									"No shout group found for the given coordinates", EExceptionTypes.SHOUT_GROUP_EXCEPTION);
				}
			}
			else{
				logger.error("No city found for the given coordinates");
				throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_LOAD_DETAILS, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_LOAD_DETAILS, 
										"No city found for the given coordinates", EExceptionTypes.SHOUT_GROUP_EXCEPTION);
			}
		}
		else{
			logger.debug("Shout Group - Load Details validations failed.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_LOAD_DETAILS, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_LOAD_DETAILS, 
															"Shout Group - Load Details validations failed.", EExceptionTypes.SHOUT_GROUP_EXCEPTION);
		}
		
	}
	
	public void viewPostResponses(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			WSShoutGroupPost wsShoutGroupPost = wsShoutGroupRequest.getShoutGroupPost();
			
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			if (wsShoutGroupRequest.getPageCount() == 0){
				wsShoutGroupRequest.setPageCount(1);
			}
			
			List<ShoutGroupPostReplyData> alShoutGroupPostReplyData = shoutGroupDAO.fetchPostReplyDataByPostId(Integer.parseInt(wsShoutGroupPost.getPostId()), wsShoutGroupRequest.getPageCount(), Integer.parseInt(maxPageResults));
			
			ShoutGroupPostData shoutGroupPostData = shoutGroupDAO.fetchPostDataByPostId(Integer.parseInt(wsShoutGroupPost.getPostId()));	
			
			if (null != shoutGroupPostData){
				populatePostResponsesData(wsShoutGroupResponse, alShoutGroupPostReplyData, shoutGroupPostData);
			}
			
			if (null != alShoutGroupPostReplyData && alShoutGroupPostReplyData.size() > 0){
				//Do Nothing
			}
			else{
				logger.debug("No post Reply data found for the post id : " + wsShoutGroupPost.getPostId());
				wsShoutGroupResponse.setMessage(ExceptionConstants.USERMESSAGE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND);
				//throw new NoShoutGroupPostReplyDataFoundException("No post Reply data found for the post id : " + wsShoutGroupPost.getPostId());
			}
		}
		else{
			logger.debug("Shout Group - View Post Reponses validations failed.");
			throw ExceptionUtility.createExceptionDetail(ExceptionConstants.CODE_SHOUT_GROUP_VIEW_POST_RESPONSES, ExceptionConstants.USERMESSAGE_SHOUT_GROUP_VIEW_POST_RESPONSES, 
															"Shout Group - View Post Reponses validations failed.", EExceptionTypes.SHOUT_GROUP_EXCEPTION);
		}
		
	}
	
	public void createPost(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			WSShoutGroupPost wsShoutGroupPost = wsShoutGroupRequest.getShoutGroupPost();
			
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			ShoutGroupPostData shoutGroupPostData = new ShoutGroupPostData();
			
			shoutGroupPostData.setShoutGroupId(Integer.parseInt(wsShoutGroupPost.getShoutGroupId()));
			shoutGroupPostData.setPostLatitude(wsShoutGroupRequest.getLatitude());
			shoutGroupPostData.setPostLongitude(wsShoutGroupRequest.getLongitude());
			shoutGroupPostData.setCreatedUsername(wsShoutGroupRequest.getUsername());
			shoutGroupPostData.setPostText(wsShoutGroupPost.getPostText());
			shoutGroupPostData.setPostImagePath(null);
			
			int postId = shoutGroupDAO.createPost(shoutGroupPostData);
			
			if (postId > 0){
				logger.debug("New post is created successfully with Post Id : " + postId);
				
				wsShoutGroupPost = new WSShoutGroupPost();
				wsShoutGroupPost.setPostId(Integer.toString(postId));
				
				generateSuccessResponse(wsShoutGroupResponse);
				wsShoutGroupResponse.setShoutGroupPost(wsShoutGroupPost);
			}
			else{
				logger.error("Failed to insert record for new Post request.");
				throw new ShoutGroupCreatePostException("Failed to insert record for new Post request.");
			}
		}
		else{
			logger.debug("Shout Group - Create Post validations failed.");
			throw new ShoutGroupCreatePostException("Shout Group - Create Post validations failed.");
		}
		
	}
	
	public void uploadPost(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			/* Upload the picture */
			
			FileUploader fileUploader = new FileUploader();
			String uploadFilePath = fileUploader.generateUploadFilePath(BusinessConstants.SHOUT_GROUP_POST_PICTURE_UPLOAD_PREFIX, wsShoutGroupRequest.getShoutGroupPost().getPictureFileName());
			String downloadFilePath = fileUploader.generateDownloadFilePath(BusinessConstants.SHOUT_GROUP_POST_PICTURE_UPLOAD_PREFIX, wsShoutGroupRequest.getShoutGroupPost().getPictureFileName());
			fileUploader.uploadFile(wsShoutGroupRequest.getShoutGroupPost().getPictureStream(), uploadFilePath);
			
			logger.debug("User Picture is uploaded successfully.");
			
			/* Update the record in database */
			
			WSShoutGroupPost wsShoutGroupPost = wsShoutGroupRequest.getShoutGroupPost();
			
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			ShoutGroupPostData shoutGroupPostData = new ShoutGroupPostData();
			
			shoutGroupPostData.setShoutGroupId(Integer.parseInt(wsShoutGroupPost.getShoutGroupId()));
			shoutGroupPostData.setPostLatitude(wsShoutGroupRequest.getLatitude());
			shoutGroupPostData.setPostLongitude(wsShoutGroupRequest.getLongitude());
			shoutGroupPostData.setCreatedUsername(wsShoutGroupRequest.getUsername());
			shoutGroupPostData.setPostText(wsShoutGroupPost.getPostText());
			shoutGroupPostData.setPostImagePath(downloadFilePath);
			
			int postId = shoutGroupDAO.createPost(shoutGroupPostData);
			
			if (postId > 0){
				logger.debug("New post is created successfully with Post Id : " + postId);
				
				wsShoutGroupPost = new WSShoutGroupPost();
				wsShoutGroupPost.setPostId(Integer.toString(postId));
				
				generateSuccessResponse(wsShoutGroupResponse);
				wsShoutGroupResponse.setShoutGroupPost(wsShoutGroupPost);
			}
			else{
				logger.error("Failed to insert record for new Post With Upload request.");
				throw new ShoutGroupCreatePostException("Failed to insert record for new Post With Upload request.");
			}
		}
		else{
			logger.debug("Shout Group - Create Post With Upload validations failed.");
			throw new ShoutGroupCreatePostException("Shout Group - Create Post With Upload validations failed.");
		}
	}
	
	public void createPoll(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			WSShoutGroupPoll wsShoutGroupPoll = wsShoutGroupRequest.getShoutGroupPoll();
			
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			ShoutGroupPollData shoutGroupPollData = new ShoutGroupPollData();
			
			shoutGroupPollData.setShoutGroupId(Integer.parseInt(wsShoutGroupPoll.getShoutGroupId()));
			shoutGroupPollData.setCreatedUsername(wsShoutGroupRequest.getUsername());
			shoutGroupPollData.setPollLatitude(wsShoutGroupRequest.getLatitude());
			shoutGroupPollData.setPollLongitude(wsShoutGroupRequest.getLongitude());
			shoutGroupPollData.setPollQuestion(wsShoutGroupPoll.getPollQuestion());
			shoutGroupPollData.setPollOption1(wsShoutGroupPoll.getPollOption1());
			shoutGroupPollData.setPollOption2(wsShoutGroupPoll.getPollOption2());
			shoutGroupPollData.setPollOption3(wsShoutGroupPoll.getPollOption3());
			shoutGroupPollData.setPollOption4(wsShoutGroupPoll.getPollOption4());
			
			int pollId = shoutGroupDAO.createPoll(shoutGroupPollData);
			
			if (pollId > 0){
				logger.debug("New poll is created successfully with Poll Id : " + pollId);
				
				wsShoutGroupPoll = new WSShoutGroupPoll();
				wsShoutGroupPoll.setPollId(Integer.toString(pollId));
				
				generateSuccessResponse(wsShoutGroupResponse);
				wsShoutGroupResponse.setShoutGroupPoll(wsShoutGroupPoll);
			}
			else{
				logger.error("Failed to insert record for new Poll request.");
				throw new ShoutGroupCreatePollException("Failed to insert record for new Poll request.");
			}
		}
		else{
			logger.debug("Shout Group - Create Poll validations failed.");
			throw new ShoutGroupCreatePollException("Shout Group - Create Poll validations failed.");
		}
	}
	
	public void replyPost(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			ShoutGroupPostReplyData shoutGroupPostReplyData = new ShoutGroupPostReplyData();
			shoutGroupPostReplyData.setPostId(Integer.parseInt(wsShoutGroupRequest.getShoutGroupPost().getPostId()));
			shoutGroupPostReplyData.setRepliedText(wsShoutGroupRequest.getShoutGroupPost().getPostReply());
			shoutGroupPostReplyData.setRepliedUsername(wsShoutGroupRequest.getUsername());
			
			boolean flag = shoutGroupDAO.replyPost(shoutGroupPostReplyData);
			
			if (flag){
				logger.debug("Post reply record is saved successfully in the database.");
				
				/* Send Mobile Notification */
				
				ShoutGroupPostData shoutGroupPostData = shoutGroupDAO.fetchPostDataByPostId(Integer.parseInt(wsShoutGroupRequest.getShoutGroupPost().getPostId()));
				
				IUserDAO userDao = daoFactory.getUserDAO();
				
				UserDeviceAuthData userDeviceAuthData = userDao.fetchUserDeviceAuthDetails(shoutGroupPostData.getUsername());
				
				if (null != userDeviceAuthData){
					
					String deviceAuthId = userDeviceAuthData.getDeviceRegId();
					
					if (!GenericUtility.safeTrim(deviceAuthId).equals(GenericConstants.EMPTY_STRING)){
						NotificationRequestVO notificationRequestVO = new NotificationRequestVO();
						NotificationMessageAssembler notificationMessageAssembler = new NotificationMessageAssembler();
						
						notificationRequestVO.setNameValuePairs(notificationMessageAssembler.getShoutGroupPostReplyMessageContent(shoutGroupPostData.getUsername(), wsShoutGroupRequest.getUsername(), wsShoutGroupRequest.getShoutGroupPost().getPostReply(), userDeviceAuthData.getDeviceType(), wsShoutGroupRequest.getShoutGroupPost().getPostId()));
						
						notificationRequestVO.setRegistrationId(userDeviceAuthData.getDeviceRegId());
						
						NotificationResponseVO notificationResponseVO = notificationEngine.sendNotification(notificationRequestVO);
						
						boolean notificationFlag = notificationResponseVO.isStatus();
						
						if (notificationFlag){
							logger.debug("Notification has been dispatched successfully for user name : " + wsShoutGroupRequest.getUsername());
						}
						else{
							logger.debug("Fail to dispatch Notification successfully for user name : " + wsShoutGroupRequest.getUsername() + ". Check the notification logs in the database.");
						}
					}
					else{
						logger.debug("No device auth id found for username : " + wsShoutGroupRequest.getUsername() + " . No mobile notification will be send.");
					}
				}
				else{
					logger.debug("No device auth details are found for username : " + wsShoutGroupRequest.getUsername() + " . No mobile notification will be send.");
				}
				
				generateSuccessResponse(wsShoutGroupResponse);
			}
			else{
				logger.debug("Post reply record is failed to save successfully in the database.");
				throw new ShoutGroupPostReplyException("Post reply record is failed to save successfully in the database.");
			}
		}
		else{
			logger.debug("Shout Group - Post Reply validations failed.");
			throw new ShoutGroupPostReplyException("Shout Group - Post Reply validations failed.");
		}
		
	}
	
	public void replyPoll(WSShoutGroupRequest wsShoutGroupRequest, WSShoutGroupResponse wsShoutGroupResponse, ERequestType requestType) {
		
		IBusinessValidator validator = new ShoutGroupValidator();
		
		boolean isValid = validator.validate((WSBaseRequest)wsShoutGroupRequest, requestType);
		
		if (isValid){
			IShoutGroupDAO shoutGroupDAO = daoFactory.getShoutGroupDAO();
			
			ShoutGroupPollData shoutGroupPollData = new ShoutGroupPollData();
			shoutGroupPollData.setPollId(Integer.parseInt(wsShoutGroupRequest.getShoutGroupPoll().getPollId()));
			shoutGroupPollData.setSelectedPollCount(Integer.parseInt(wsShoutGroupRequest.getShoutGroupPoll().getSelectedPollOption()));
			
			boolean flag = shoutGroupDAO.replyPoll(shoutGroupPollData);
			
			if (flag){
				logger.debug("Poll reply record is saved successfully in the database.");
				
				generateSuccessResponse(wsShoutGroupResponse);
			}
			else{
				logger.debug("Poll reply record is failed to save successfully in the database.");
				throw new ShoutGroupPollReplyException("Poll reply record is failed to save successfully in the database.");
			}
		}
		else{
			logger.debug("Shout Group - Poll Reply validations failed.");
			throw new ShoutGroupPollReplyException("Shout Group - Poll Reply validations failed.");
		}
		
	}
		
	
	private Integer findClosestCityCoordinates(List<CityGeoData> alCityGeoDetails, double userLatitude, double userLongitude){
		
		SortedMap<Double, Integer> mapSortedCityDistances;
		int closestCityId = -1;
		
		if (null != alCityGeoDetails && alCityGeoDetails.size() > 0){
			
			mapSortedCityDistances = new TreeMap<Double, Integer>();
			
			for (CityGeoData cityData : alCityGeoDetails){
				double cityCentralLatitude = Double.parseDouble(cityData.getCityCenterLatitude());
				double cityCentralLongitude = Double.parseDouble(cityData.getCityCenterLongitude());
				
				Integer cityId = cityData.getCityId();
				
				double distance = Utility.calculateLocationCoordinatesDistance(userLatitude, userLongitude, cityCentralLatitude, cityCentralLongitude);
				
				mapSortedCityDistances.put(distance, cityId);
			}
			
			closestCityId = mapSortedCityDistances.get(mapSortedCityDistances.firstKey());
		}
		
		//Empty the collection so that it can be garbage collected
		mapSortedCityDistances = null;
		
		return closestCityId;
	}
	
	private ShoutGroupData findClosestShoutGroup(List<ShoutGroupData> alCityBasedShoutGroupDetails, double userLatitude, double userLongitude){
		
		SortedMap<Double, ShoutGroupData> mapSortedShoutGroupDistances;
		ShoutGroupData closestShoutGroup = null;
		
		if (null != alCityBasedShoutGroupDetails && alCityBasedShoutGroupDetails.size() > 0){
			
			mapSortedShoutGroupDistances = new TreeMap<Double, ShoutGroupData>();
			
			for (ShoutGroupData data : alCityBasedShoutGroupDetails){
				double shoutGroupLatitude = Double.parseDouble(data.getShoutGroupLatitude());
				double shoutGroupLongitude = Double.parseDouble(data.getShoutGroupLongitude());
					
				double distance = Utility.calculateLocationCoordinatesDistance(userLatitude, userLongitude, shoutGroupLatitude, shoutGroupLongitude);
				
				mapSortedShoutGroupDistances.put(distance, data);
			}
			
			closestShoutGroup = mapSortedShoutGroupDistances.get(mapSortedShoutGroupDistances.firstKey());
		}
		
		//Empty the collection so that it can be garbage collected
		mapSortedShoutGroupDistances = null;
		
		return closestShoutGroup;
	}
		
	private void populateShoutGroupPostData(WSShoutGroupResponse wsShoutGroupResponse, List<ShoutGroupPostData> alShoutGroupPostData){
		
		List<WSShoutGroupPost> shoutGroupPostDetails = new ArrayList<WSShoutGroupPost>();
		
		for (ShoutGroupPostData daoData : alShoutGroupPostData){
			
			WSShoutGroupPost wsShoutGroupPost = new WSShoutGroupPost();
			
			wsShoutGroupPost.setPostId(Integer.toString(daoData.getPostId()));
			wsShoutGroupPost.setCreatorUsername(daoData.getCreatedUsername());
			wsShoutGroupPost.setPostImagePath(daoData.getPostImagePath());
			wsShoutGroupPost.setPostText(daoData.getPostText());
			wsShoutGroupPost.setShoutGroupId(Integer.toString(daoData.getShoutGroupId()));
			wsShoutGroupPost.setUserPicturePath(daoData.getUserPicturePath());
			
			wsShoutGroupPost.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
			
			shoutGroupPostDetails.add(wsShoutGroupPost);
			
		}
		
		wsShoutGroupResponse.setPostDetails(shoutGroupPostDetails);
	}
	
	private void populateShoutGroupPollData(WSShoutGroupResponse wsShoutGroupResponse, List<ShoutGroupPollData> alShoutGroupPollData){
		
		List<WSShoutGroupPoll> shoutGroupPollDetails = new ArrayList<WSShoutGroupPoll>();
		
		for (ShoutGroupPollData daoData : alShoutGroupPollData){
			
			WSShoutGroupPoll wsShoutGroupPoll = new WSShoutGroupPoll();
			
			wsShoutGroupPoll.setPollId(Integer.toString(daoData.getPollId()));
			wsShoutGroupPoll.setCreatorUsername(daoData.getCreatedUsername());
			wsShoutGroupPoll.setShoutGroupId(Integer.toString(daoData.getShoutGroupId()));
			wsShoutGroupPoll.setPollQuestion(daoData.getPollQuestion());
			wsShoutGroupPoll.setPollOption1(daoData.getPollOption1());
			wsShoutGroupPoll.setPollOption2(daoData.getPollOption2());
			wsShoutGroupPoll.setPollOption3(daoData.getPollOption3());
			wsShoutGroupPoll.setPollOption4(daoData.getPollOption4());
			wsShoutGroupPoll.setPollOptionCount1(Integer.toString(daoData.getPollOptionCount1()));
			wsShoutGroupPoll.setPollOptionCount2(Integer.toString(daoData.getPollOptionCount2()));
			wsShoutGroupPoll.setPollOptionCount3(Integer.toString(daoData.getPollOptionCount3()));
			wsShoutGroupPoll.setPollOptionCount4(Integer.toString(daoData.getPollOptionCount4()));
			wsShoutGroupPoll.setPollTotalCount(Integer.toString(daoData.getPollTotalCount()));
			wsShoutGroupPoll.setUserPicturePath(daoData.getUserPicturePath());
			
			wsShoutGroupPoll.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
			
			shoutGroupPollDetails.add(wsShoutGroupPoll);
			
		}
		
		wsShoutGroupResponse.setPollDetails(shoutGroupPollDetails);
	}
	
	private void populatePostResponsesData(WSShoutGroupResponse wsShoutGroupResponse, List<ShoutGroupPostReplyData> alShoutGroupPostReplyData, ShoutGroupPostData shoutGroupPostData){
		
		List<WSShoutGroupPostReply> wsPostReplies = new ArrayList<WSShoutGroupPostReply>();
		
		int totalResults = 0;
		
		WSShoutGroupPost wsShoutGroupPost = new WSShoutGroupPost();
		
		if (null != alShoutGroupPostReplyData && alShoutGroupPostReplyData.size() > 0){
			for (ShoutGroupPostReplyData daoData : alShoutGroupPostReplyData){
				WSShoutGroupPostReply wsShoutGroupPostReply = new WSShoutGroupPostReply();
				
				wsShoutGroupPostReply.setRepliedText(daoData.getRepliedText());
				wsShoutGroupPostReply.setRepliedUsername(daoData.getRepliedUsername());
				wsShoutGroupPostReply.setUserPicturePath(daoData.getUserPicturePath());
				
				wsShoutGroupPostReply.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(daoData.getCreatedTimestamp()));
				
				totalResults = daoData.getTotalResults();
				
				wsPostReplies.add(wsShoutGroupPostReply);
			}
			
			wsShoutGroupPost.setPostReplies(wsPostReplies);
		}
		
		
		
		wsShoutGroupPost.setPostId(Integer.toString(shoutGroupPostData.getPostId()));
		wsShoutGroupPost.setCreatorUsername(shoutGroupPostData.getCreatedUsername());
		wsShoutGroupPost.setPostImagePath(shoutGroupPostData.getPostImagePath());
		wsShoutGroupPost.setPostText(shoutGroupPostData.getPostText());
		wsShoutGroupPost.setUserPicturePath(shoutGroupPostData.getUserPicturePath());
		
		wsShoutGroupPost.setCreatedTimestamp(Utility.formatShoutAndPlaceTimestamp(shoutGroupPostData.getCreatedTimestamp()));
		
		wsShoutGroupResponse.setShoutGroupPost(wsShoutGroupPost);
		wsShoutGroupResponse.setTotalResults(Integer.toString(totalResults));
		wsShoutGroupResponse.setTotalPages(Integer.toString(getTotalPages(totalResults, Integer.parseInt(maxPageResults))));
		
	}
	
	public int getTotalPages(int totalResults, int maxPageResults) {

        int totalPages = totalResults / maxPageResults;

        if (!(totalResults % maxPageResults == 0)) {
            totalPages++;
        }

        return totalPages;
    }
	
	public void generateSuccessResponse(WSShoutGroupResponse wsShoutGroupResponse){
		
		wsShoutGroupResponse.setResponse(BusinessConstants.RESPONSE_SUCCESS);
		
	}

}
