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

import com.fynger.servicesController.services.domainObjects.requests.WSShoutGroupRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSShoutGroupResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPoll;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPost;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSShoutGroupPostReply;
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
public class ShoutGroupServiceTest extends BaseServicesTest {
	
	WSShoutGroupRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSShoutGroupRequest();
	}
	
	@Test
	public final void testShoutGroup_LoadDetails_NoPostAndNoPollDataExists() {
		
		System.out.println("\n========================== Shout Group - Load Details : No Post And No Poll Data exits test case ===============================\n");
		
		request.setUsername("Raj");
		request.setLatitude("18.0");
		request.setLongitude("73.0");
		
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("loadDetails").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Shout Group Id : " + response.getShoutGroupId());
			System.out.println("New Shout Group : " + response.getNewShoutGroup());
			System.out.println("Message : " + response.getMessage());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_LoadDetails_Success() {
		
		System.out.println("\n========================== Shout Group - Load Details : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setLatitude("28.62");
		request.setLongitude("77.22");
		request.setPageCount(1);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("loadDetails").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Shout Group Id : " + response.getShoutGroupId());
			System.out.println("New Shout Group : " + response.getNewShoutGroup());
			
			List<WSShoutGroupPost> shoutGroupPostDetails = response.getPostDetails();
			List<WSShoutGroupPoll> shoutGroupPollDetails = response.getPollDetails();
			
			for (WSShoutGroupPost postData : shoutGroupPostDetails){
				System.out.println("Post Data : " + postData);
			}
			
			for (WSShoutGroupPoll pollData : shoutGroupPollDetails){
				System.out.println("Poll Data : " + pollData);
			}

			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_ViewPostResponses_NoShoutGroupPostReplyDataFound() {
		
		System.out.println("\n========================== Shout Group - View Post Responses : No Shout Group Post Reply Data Found test case ===============================\n");
		
		WSShoutGroupPost postData = new WSShoutGroupPost();
		postData.setPostId("25");
		
		request.setUsername("Raj");
		request.setShoutGroupPost(postData);
		request.setPageCount(1);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getShoutGroupPost();

			System.out.println("PostData : " + postData);
			System.out.println("Message : " + response.getMessage());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_ViewPostResponses_Success() {
		
		System.out.println("\n========================== Shout Group - View Post Responses : Success test case ===============================\n");
		
		WSShoutGroupPost postData = new WSShoutGroupPost();
		postData.setPostId("2");
		
		request.setUsername("Raj");
		request.setShoutGroupPost(postData);
		request.setPageCount(1);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getShoutGroupPost();

			System.out.println("PostData : " + postData);
			
			List<WSShoutGroupPostReply> postReplyData = postData.getPostReplies();
			
			for (WSShoutGroupPostReply data : postReplyData){
				System.out.println("Post Reply Data : " + data);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_ViewPostResponses_PageCountExceeded() {
		
		System.out.println("\n========================== Shout Group - View Post Responses : Page Count Exceeded test case ===============================\n");
		
		WSShoutGroupPost postData = new WSShoutGroupPost();
		postData.setPostId("2");
		
		request.setUsername("Raj");
		request.setShoutGroupPost(postData);
		request.setPageCount(2);
		
		try{
			WSErrorResponse response = service.path("shout").path("viewPostResponses").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testShoutGroup_CreatePost_Success() {
		
		System.out.println("\n========================== Shout Group - Create Post : Success test case ===============================\n");
		
		WSShoutGroupPost postData = new WSShoutGroupPost();
		
		postData.setShoutGroupId("64");
		postData.setPostText("Shout Post - Social");
		
		request.setUsername("Raj");
		request.setLatitude("28.612919");
		request.setLongitude("77.219555");
		request.setShoutGroupPost(postData);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("createPost").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			postData = response.getShoutGroupPost();

			System.out.println("Post Id : " + postData.getPostId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_CreatePoll_Success() {
		
		System.out.println("\n========================== Shout Group - Create Poll : Success test case ===============================\n");
		
		WSShoutGroupPoll pollData = new WSShoutGroupPoll();
		
		pollData.setShoutGroupId("64");
		pollData.setPollQuestion("Best Place to watch movie in Delhi ?");
		pollData.setPollOption1("3Cs");
		pollData.setPollOption2("Plaza");
		pollData.setPollOption3("PVR");
		pollData.setPollOption4("Satyam");
		
		request.setUsername("Raj");
		request.setLatitude("28.612919");
		request.setLongitude("77.219555");
		request.setShoutGroupPoll(pollData);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("createPoll").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			pollData = response.getShoutGroupPoll();

			System.out.println("Poll Id : " + pollData.getPollId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_ReplyPost_Success() {
		
		System.out.println("\n========================== Shout Group - Reply Post : Success test case ===============================\n");
		
		WSShoutGroupPost postData = new WSShoutGroupPost();
		
		postData.setPostId("2");
		postData.setPostReply("Test Post Reply Text");
		
		request.setUsername("Bansal");
		request.setShoutGroupPost(postData);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("replyPost").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Response : " + response.getResponse());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_ReplyPoll_Success() {
		
		System.out.println("\n========================== Shout Group - Reply Poll : Success test case ===============================\n");
		
		WSShoutGroupPoll pollData = new WSShoutGroupPoll();
		
		pollData.setPollId("1");
		pollData.setSelectedPollOption("1");
		
		request.setUsername("Raj");
		request.setShoutGroupPoll(pollData);
		
		try{
			WSShoutGroupResponse response = service.path("shout").path("replyPoll").accept(MediaType.APPLICATION_XML).post(WSShoutGroupResponse.class, request);
			assertNotNull(response);

			System.out.println("Response : " + response.getResponse());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}
	
	@Test
	public final void testShoutGroup_UploadPost_Success() {
		
		System.out.println("\n========================== Shout Group - Upload Post : Success test case ===============================\n");
		
		
		try{
			ClientConfig cc = new DefaultClientConfig();
			cc.getClasses().add(MultiPartWriter.class);
			Client client = Client.create(cc);
			String url = "http://localhost:8088/FYNGERWEB/services/shout/uploadPost";
			File f = new File("C:\\Users\\Rishi\\Pictures\\Nature\\MapleLeaves.jpg");
			
			FormDataMultiPart form = new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("file", f));
			form.field("username", "Raj");
			form.field("shoutGroupId", "64");
			form.field("postText", "Test Upload Post");
			form.field("latitude", "28.612919");
			form.field("longitude", "77.219555");
		
			WSShoutGroupResponse response = client.resource(url).type(MediaType.MULTIPART_FORM_DATA).post(WSShoutGroupResponse.class, form);
			
			assertNotNull(response);
			
			WSShoutGroupPost postData = new WSShoutGroupPost();

			postData = response.getShoutGroupPost();

			System.out.println("Post Id : " + postData.getPostId());			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
		
	}


}
