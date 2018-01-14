/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSUserLocationRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSUserLocationResponse;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class UserLocationServiceTest extends BaseServicesTest {
	
	WSUserLocationRequest request = null;
	
	public void setUp() throws Exception {
		super.setUp();
		
		request = new WSUserLocationRequest();
	}
	
	@Test
	public final void testRefreshLocation() {
		
		System.out.println("\n========================== Refresh Location : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setLatitude("28.612919");
		request.setLongitude("77.219555");
		
		try{
			WSUserLocationResponse response = service.path("location").path("refresh").accept(MediaType.APPLICATION_XML).post(WSUserLocationResponse.class, request);
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
	public final void testRefreshLocation_MandatoryValidation() {
		
		System.out.println("\n========================== Refresh Location : Mandatory Validation failure test case ===============================\n");
		
		request.setUsername("Raj");
		request.setLatitude("28.612919");
		request.setLongitude("0.0");
		
		try{
			WSErrorResponse response = service.path("location").path("refresh").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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


}
