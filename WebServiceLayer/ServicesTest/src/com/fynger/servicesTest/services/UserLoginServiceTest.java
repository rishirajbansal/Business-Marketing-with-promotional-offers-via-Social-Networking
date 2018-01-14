/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSUserLoginRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLoginResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSUserDeviceAuth;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class UserLoginServiceTest extends BaseServicesTest {
	
	WSUserLoginRequest request = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		request = new WSUserLoginRequest();
	}

	@Test
	public final void testLoginSuccess() {
		
		System.out.println("\n========================== Login : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setLocationCoordinates("19.226182327270508|72.84025573730469");
		//request.setLocationCoordinates("0|0");
		
		try{
			WSUserLoginResponse response = service.path("login").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
			
			/*String XMLResponse = service.path("login").accept(MediaType.APPLICATION_XML).post(String.class, request);
			assertNotNull(XMLResponse);
			
			System.out.println("\nResponse in XML format : \n" + XMLResponse);*/
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLoginSuccess_DeviceAuthDetails() {
		
		System.out.println("\n========================== Login - Device Auth details : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setLocationCoordinates("19.226182327270508|72.84025573730469");
		
		WSUserDeviceAuth wsUserDeviceAuth = new WSUserDeviceAuth();
		wsUserDeviceAuth.setDeviceRegistrationId("APA91bHX8d2GJGJVSfrrbiuHX_SPGufLI8Txs1w0sHAp2t7ld62iZGKs9qi3KRqJdpoP6S0M9CXYXHJO0qHBL7jGIifOzhhhByjsHb4U08Nl23ryHgV9NoV-XOylD3Et-ABTyVip9Kmmz5DSn6W_1Fx8PXuQ3FeBhA");
		wsUserDeviceAuth.setDeviceType("1");
		
		request.setDeviceAuth(wsUserDeviceAuth);
		
		try{
			WSUserLoginResponse response = service.path("login").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
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
	public final void testLoginInvalidUserName() {
		
		System.out.println("\n========================== Login : Invalid User name test case ===============================\n");
		
		populateRequest();
		request.setUsername("invalid");
		
		try{
			WSErrorResponse response = service.path("login").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testLoginIncorrectPassword() {
		
		System.out.println("\n========================== Login : Incorrect Password test case ===============================\n");
		
		populateRequest();
		request.setPassword("invalid");
		
		try{
			WSErrorResponse response = service.path("login").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testLogin_FBLogin_Success() {
		
		System.out.println("\n========================== Login - FB Login : Success test case ===============================\n");
		
		request.setFBEmail("FBUser@facebook.com");
		request.setLocationCoordinates("19.226182327270508|72.84025573730469");
		//request.setLocationCoordinates("0|0");

		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("login").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			System.out.println("username : " + response.getUsername());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLogin_FBLogin_DeviceAuthDetails() {
		
		System.out.println("\n========================== Login - FB Login - Device Auth : Success test case ===============================\n");
		
		request.setFBEmail("FBUser@facebook.com");
		request.setLocationCoordinates("19.226182327270508|72.84025573730469");
		
		WSUserDeviceAuth wsUserDeviceAuth = new WSUserDeviceAuth();
		wsUserDeviceAuth.setDeviceRegistrationId("ABC1234");
		wsUserDeviceAuth.setDeviceType("1");
		
		request.setDeviceAuth(wsUserDeviceAuth);
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("login").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			System.out.println("username : " + response.getUsername());
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLogin_FBLogin_UserNotExist() {
		
		System.out.println("\n========================== Login - FB login : User does not exist test case ===============================\n");
		
		request.setFBEmail("FBUser@facebook.com");
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("login").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
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
	public final void testLogin_CreateFBLogin_FyngerUser() {
		
		System.out.println("\n========================== Login - Create FB login : Fynger user test case ===============================\n");
		
		request.setFBEmail("FBUser@facebook.com");
		request.setUsername("Bansal");
		request.setPassword("agent009");
		request.setFyngerUser("true");
		request.setLocationCoordinates("28.8888|77.8888");
		//request.setLocationCoordinates("0|0");
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("register").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			System.out.println("username : " + response.getUsername());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLogin_CreateFBLogin_FyngerUser_DeviceAuthDetails() {
		
		System.out.println("\n========================== Login - Create FB login - Device Auth Details : Fynger user test case ===============================\n");
		
		request.setFBEmail("FBUser@facebook.com");
		request.setUsername("Bansal");
		request.setPassword("agent009");
		request.setFyngerUser("true");
		request.setLocationCoordinates("28.9999999|77.9999999");
		
		WSUserDeviceAuth wsUserDeviceAuth = new WSUserDeviceAuth();
		wsUserDeviceAuth.setDeviceRegistrationId("ABC1234");
		wsUserDeviceAuth.setDeviceType("1");
		
		request.setDeviceAuth(wsUserDeviceAuth);
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("register").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			System.out.println("username : " + response.getUsername());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLogin_CreateFBLogin_NonFyngerUser() {
		
		System.out.println("\n========================== Login - Create FB login : Non Fynger user test case ===============================\n");
		
		request.setFBEmail("FBUser10@facebook.com");
		request.setFyngerUser("false");
		request.setLocationCoordinates("28.7777|77.777");
		//request.setLocationCoordinates("0|0");
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("register").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			System.out.println("username : " + response.getUsername());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	@Test
	public final void testLogin_CreateFBLogin_NonFyngerUser_DeviceAuthDetails() {
		
		System.out.println("\n========================== Login - Create FB login - Device Auth Details : Non Fynger user test case ===============================\n");
		
		request.setFBEmail("FBUser6@facebook.com");
		request.setFyngerUser("false");
		request.setLocationCoordinates("28.9999999|77.9999999");
		
		WSUserDeviceAuth wsUserDeviceAuth = new WSUserDeviceAuth();
		wsUserDeviceAuth.setDeviceRegistrationId("ABC1234");
		wsUserDeviceAuth.setDeviceType("1");
		
		request.setDeviceAuth(wsUserDeviceAuth);
		
		try{
			WSUserLoginResponse response = service.path("login").path("fb").path("register").accept(MediaType.APPLICATION_XML).post(WSUserLoginResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			assertEquals("true", response.getResponse());
			
			assertNotNull(response.getUsername());
			System.out.println("username : " + response.getUsername());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
			    
	}
	
	private void populateRequest(){
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setLocationCoordinates("28.9999999|77.9999999");
	}

}
