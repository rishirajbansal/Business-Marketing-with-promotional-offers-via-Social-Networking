/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSPlaceGroupRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSPlaceGroupResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPost;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPlaceGroupPostReply;
import com.fynger.servicesTest.base.BaseServicesTest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

/**
 * @author Rishi
 *
 */
public class PlaceGroupServiceTest extends BaseServicesTest {
	
	WSPlaceGroupRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSPlaceGroupRequest();
	}
	
	@Test
	public final void testPlaceGroup_LoadDetails_NoPostAndNoPollDataExists() {
		
		System.out.println("\n========================== Place Group - Load Details : No Post And No Poll Data exits test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPlaceMapId("P6765ABC");
		request.setLatitude("28.612919");
		request.setLongitude("77.219555");
		request.setPlaceName("Connaught Circus");
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("loadDetails").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Place Group Id : " + response.getPlaceGroupId());
			System.out.println("New Place : " + response.getNewPlace());
			System.out.println("Message : " + response.getMessage());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_LoadDetails_MandatoryValidation() {
		
		System.out.println("\n========================== Place Group - Load Details : Mandatory Validation Failure test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPlaceMapId("P6765ABC");
		request.setLatitude("28.612919");
		request.setLongitude("0.0");
		request.setPlaceName("Connaught Circus");
		
		try{
			WSErrorResponse response = service.path("place").path("loadDetails").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);

			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());	
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_LoadDetails_Success() {
		
		System.out.println("\n========================== Shout Group - Load Details : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPlaceMapId("P1234ABC");
		request.setLatitude("28.612919");
		request.setLongitude("77.219555");
		request.setPlaceName("Connaught Circus");
		request.setPageCount(2);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("loadDetails").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Place Group Id : " + response.getPlaceGroupId());
			System.out.println("New Place : " + response.getNewPlace());
			
			List<WSPlaceGroupPost> placeGroupPostDetails = response.getPostDetails();
			List<WSPlaceGroupPoll> placeGroupPollDetails = response.getPollDetails();
			
			for (WSPlaceGroupPost postData : placeGroupPostDetails){
				System.out.println("Post Data : " + postData);
			}
			
			for (WSPlaceGroupPoll pollData : placeGroupPollDetails){
				System.out.println("Poll Data : " + pollData);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_ViewPostResponses_NoPlaceGroupPostReplyDataFound() {
		
		System.out.println("\n========================== Place Group - View Post Responses : No Place Group Post Reply Data Found test case ===============================\n");
		
		WSPlaceGroupPost postData = new WSPlaceGroupPost();
		postData.setPostId("4");
		
		request.setUsername("Raj");
		request.setPlaceGroupPost(postData);
		request.setPageCount(1);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getPlaceGroupPost();

			System.out.println("PostData : " + postData);
			System.out.println("Message : " + response.getMessage());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_ViewPostResponses_Success() {
		
		System.out.println("\n========================== Place Group - View Post Responses : Success test case ===============================\n");
		
		WSPlaceGroupPost postData = new WSPlaceGroupPost();
		postData.setPostId("1");
		
		request.setUsername("Raj");
		request.setPlaceGroupPost(postData);
		request.setPageCount(1);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getPlaceGroupPost();

			System.out.println("PostData : " + postData);
			
			List<WSPlaceGroupPostReply> postReplyData = postData.getPostReplies();
			
			for (WSPlaceGroupPostReply data : postReplyData){
				System.out.println("Post Reply Data : " + data);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_ViewPostResponses_PageCountExceeded() {
		
		System.out.println("\n========================== Shout Group - View Post Responses : Page Count Exceeded test case ===============================\n");
		
		WSPlaceGroupPost postData = new WSPlaceGroupPost();
		postData.setPostId("1");
		
		request.setUsername("Raj");
		request.setPlaceGroupPost(postData);
		request.setPageCount(2);
		
		try{
			WSErrorResponse response = service.path("place").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);

			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_CreatePost_Success() {
		
		System.out.println("\n========================== Place Group - Create Post : Success test case ===============================\n");
		
		WSPlaceGroupPost postData = new WSPlaceGroupPost();
		
		postData.setPlaceGroupId("1");
		postData.setPostText("Place Group - Movie Post");
		
		request.setUsername("Raj");
		request.setPlaceGroupPost(postData);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("createPost").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getPlaceGroupPost();

			System.out.println("Post Id : " + postData.getPostId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_CreatePoll_Success() {
		
		System.out.println("\n========================== Place Group - Create Poll : Success test case ===============================\n");
		
		WSPlaceGroupPoll pollData = new WSPlaceGroupPoll();
		
		pollData.setPlaceGroupId("1");
		pollData.setPollQuestion("Best Place to watch movie in Delhi ?");
		pollData.setPollOption1("3Cs");
		pollData.setPollOption2("Plaza");
		pollData.setPollOption3("PVR");
		pollData.setPollOption4("Satyam");
		
		request.setUsername("Raj");
		request.setPlaceGroupPoll(pollData);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("createPoll").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			pollData = response.getPlaceGroupPoll();

			System.out.println("Poll Id : " + pollData.getPollId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_ReplyPost_Success() {
		
		System.out.println("\n========================== Place Group - Reply Post : Success test case ===============================\n");
		
		WSPlaceGroupPost postData = new WSPlaceGroupPost();
	
		postData.setPostId("1");
		postData.setPostReply("Test Post Reply Text");
		
		request.setUsername("Bansal");
		request.setPlaceGroupPost(postData);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("replyPost").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Response : " + response.getResponse());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_ReplyPoll_Success() {
		
		System.out.println("\n========================== Place Group - Reply Poll : Success test case ===============================\n");
		
		WSPlaceGroupPoll pollData = new WSPlaceGroupPoll();
		
		pollData.setPollId("1");
		pollData.setSelectedPollOption("1");
		
		request.setUsername("Raj");
		request.setPlaceGroupPoll(pollData);
		
		try{
			WSPlaceGroupResponse response = service.path("place").path("replyPoll").accept(MediaType.APPLICATION_XML).post(WSPlaceGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Response : " + response.getResponse());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testPlaceGroup_UploadPost_Success() {
		
		System.out.println("\n========================== Place Group - Upload Post : Success test case ===============================\n");
		
		
		try{
			ClientConfig cc = new DefaultClientConfig();
			cc.getClasses().add(MultiPartWriter.class);
			Client client = Client.create(cc);
			String url = "http://localhost:8088/FYNGERWEB/services/place/uploadPost";
			File f = new File("C:\\Users\\Rishi\\Pictures\\Nature\\nature1.jpg");
			
			FormDataMultiPart form = new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("file", f));
			form.field("username", "Raj");
			form.field("placeGroupId", "1");
			form.field("postText", "Test Upload Post");
		
			WSPlaceGroupResponse response = client.resource(url).type(MediaType.MULTIPART_FORM_DATA).post(WSPlaceGroupResponse.class, form);
			
			assertNotNull(response);
			
			WSPlaceGroupPost postData = new WSPlaceGroupPost();

			postData = response.getPlaceGroupPost();

			System.out.println("Post Id : " + postData.getPostId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	

}
