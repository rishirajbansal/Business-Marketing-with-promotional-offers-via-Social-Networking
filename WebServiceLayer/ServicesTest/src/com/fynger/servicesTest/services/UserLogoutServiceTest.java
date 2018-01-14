/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSUserLogoutRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLogoutResponse;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class UserLogoutServiceTest extends BaseServicesTest {
	
	WSUserLogoutRequest request = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		request = new WSUserLogoutRequest();
	}
	
	@Test
	public final void testLogout() {
		
		System.out.println("\n========================== Logout : Success test case ===============================\n");
		
		request.setUsername("Raj");
		
		try{
			WSUserLogoutResponse response = service.path("logout").accept(MediaType.APPLICATION_XML).post(WSUserLogoutResponse.class, request);
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

}
