/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSUserProfileRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserProfileResponse;
import com.fynger.servicesTest.base.BaseServicesTest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

/**
 * @author Rishi
 *
 */
public class UserProfileServiceTest extends BaseServicesTest {


	WSUserProfileRequest request = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		request = new WSUserProfileRequest();
	}

	@Test
	public final void testUserRegistrationSuccess() {
		
		System.out.println("\n========================== Registration : Success test case ===============================\n");
		
		request.setUsername("Jones");
		request.setPassword("agent009");
		request.setFullName("Catherine jones");
		request.setDateOfBirth("01/01/2000");
		request.setEmail("rishirbansal@gmail.com");
		request.setPhone("1234567890");
		request.setZipcode("122008");
		request.setGender("Male");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationUserNameExist() {
		
		System.out.println("\n========================== Registration : User Name exist test case ===============================\n");
		
		populateRequest();
		request.setUsername("Raj");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationMandatoryFieldValidation() {
		
		System.out.println("\n========================== Registration : Mandatory Fields Validation test case ===============================\n");
		
		populateRequest();
		request.setUsername("");
		request.setPassword("");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationLengthFieldValidation() {
		
		System.out.println("\n========================== Registration : Length Fields Validation test case ===============================\n");
		
		populateRequest();
		request.setUsername("RajRajRAJRajRajRAJRajRajRAJ");
		request.setPhone("123456789012");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationSyntaxValidationUserName() {
		
		System.out.println("\n========================== Registration : User Name syntax Validation test case ===============================\n");
		
		populateRequest();
		request.setUsername("Raj ()");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationSyntaxValidationPhone() {
		
		System.out.println("\n========================== Registration : Phone syntax Validation test case ===============================\n");
		
		populateRequest();
		request.setPhone("0123456789");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationSyntaxValidationEmail() {
		
		System.out.println("\n========================== Registration : Email syntax Validation test case ===============================\n");
		
		populateRequest();
		request.setEmail("test@");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserRegistrationSyntaxValidationDOB() {
		
		System.out.println("\n========================== Registration : Date of Birth syntax Validation test case ===============================\n");
		
		populateRequest();
		request.setDateOfBirth("2000/01/01");
		
		try{
			WSErrorResponse response = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
			assertNotNull(response);
			
			System.out.println("Code : " + response.getCode());
			System.out.println("status : " + response.getStatus());
			System.out.println("Error Message : " + response.getErrorMessage());
			System.out.println("User Message : " + response.getUserMessage());
			
			String XMLResponse = service.path("profile").path("registration").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserProfileLoad() {
		
		System.out.println("\n========================== Load User Profile : Success test case ===============================\n");
		
		request.setUsername("Bansal");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("loadProfile").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			System.out.println("Full name : " + response.getFullName());
			System.out.println("Date of birth : " + response.getDateOfBirth());
			System.out.println("Email : " + response.getEmail());
			System.out.println("Phone : " + response.getPhone());
			System.out.println("Zip Code : " + response.getZipcode());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}

	}
	
	@Test
	public final void testUserUpdateProfileSuccess() {
		
		System.out.println("\n========================== Update Profile : Success test case ===============================\n");
		
		populateRequest();
		//request.setUserPicturePath("http://203.123.147.41/fynger/resources/users/pictures/Raj.JPEG");
		request.setPhone("9999999999");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("update").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserUpdateProfileMandatoryFieldValidation() {
		
		System.out.println("\n========================== Update Profile : Mandatory Fields Validation test case ===============================\n");
		
		populateRequest();
		request.setEmail("");
		
		try{
			WSErrorResponse response = service.path("profile").path("update").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testUserForgotPasswordVerificationSuccess() {
		
		System.out.println("\n========================== Forgot Password Verification : Success Test Case ===============================\n");
		
		populateRequest();
		
		try{
			WSUserProfileResponse response = service.path("profile").path("forgotPassword/verification").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserForgotPasswordVerificationInvalid() {
		
		System.out.println("\n========================== Forgot Password Verification : Invalid verification details Test Case ===============================\n");
		
		populateRequest();
		request.setDateOfBirth("01/10/2000");
		
		try{
			WSErrorResponse response = service.path("profile").path("forgotPassword/verification").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testUserForgotPasswordUpdateSuccess() {
		
		System.out.println("\n========================== Forgot Password Update : Success Test Case ===============================\n");
		
		populateRequest();
		request.setPassword("agent009");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("forgotPassword/update").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserForgotPasswordUpdateLengthFieldValidation() {
		
		System.out.println("\n========================== Forgot Password Update : Length Fields Validation Test Case ===============================\n");
		
		populateRequest();
		request.setPassword("agent100agent100agent100agent100agent100");
		
		try{
			WSErrorResponse response = service.path("profile").path("forgotPassword/update").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testUserNameAvailabilitySuccess() {
		
		System.out.println("\n========================== User Name Availability : Success Test Case ===============================\n");
		
		populateRequest();
		request.setUsername("NewUser");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("userNameAvailability").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserNameAvailabilityNotAvailable() {
		
		System.out.println("\n========================== User Name Availability : Not Available Test Case ===============================\n");
		
		populateRequest();
		request.setUsername("Raj");
		
		try{
			WSUserProfileResponse response = service.path("profile").path("userNameAvailability").accept(MediaType.APPLICATION_XML).post(WSUserProfileResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("false", response.getResponse());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testUserProfile_FileUpload_Success() {
		
		System.out.println("\n========================== User Profile : File Upload Success Test Case ===============================\n");
	
		try{
			/*String url = "http://localhost:8088/FYNGERWEB/services/profile/upload";
			File file = new File("C:\\Users\\Rishi\\Pictures\\Nature\\Jungle01.jpg");
			
			HttpClient httpclient = new DefaultHttpClient();
			
			HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Content-Type", MediaType.MULTIPART_FORM_DATA );
			
			FileBody fileContent= new FileBody(file);
			StringBody stringBody = new StringBody("Raj");
			
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("file", fileContent);
			reqEntity.addPart("username", stringBody);
			
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			
			HttpEntity resEntity = response.getEntity();
			
			System.out.println(resEntity.toString());*/
			
			ClientConfig cc = new DefaultClientConfig();
			cc.getClasses().add(MultiPartWriter.class);
			Client client = Client.create(cc);
			String url = "http://localhost:8088/FYNGERWEB/services/profile/upload";
			File f = new File("C:\\Users\\Rishi\\Pictures\\Nature\\Jungle01.jpg");
			
			FormDataMultiPart form = new FormDataMultiPart();
			form.bodyPart(new FileDataBodyPart("file", f));
			form.field("username", "Raj");
			
			ClientResponse response = client.resource(url).type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, form);
			
			assertNotNull(response);
			assertEquals(200, response.getStatus());
			
			System.out.println(response.getStatus());
			System.out.println(response.getEntity(String.class));
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	
	
	private void populateRequest(){
		request.setUsername("RAJ");
		request.setPassword("agent009");
		request.setFullName("Raj Bansal");
		request.setDateOfBirth("01/01/2000");
		request.setEmail("test@test.com");
		request.setPhone("1234567890");
		request.setZipcode("122008");
		request.setGender("Male");
		//request.setUserPicturePath("/fynger/resources/user/pictures");
	}

}
