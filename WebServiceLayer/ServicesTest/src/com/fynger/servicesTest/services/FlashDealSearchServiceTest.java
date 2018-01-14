/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSSearchFlashDealRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchFlashDealResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSFlashDeal;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class FlashDealSearchServiceTest extends BaseServicesTest {
	
	WSSearchFlashDealRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSSearchFlashDealRequest();
	}
	
	@Test
	public final void testFlashDealSearch_Request_WithCoodinates() {
		
		System.out.println("\n========================== Flash Deal Search - Request : With coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Real Estate");
		request.setLocationString("andheri, mumbai");
		request.setLatitude("28.9999999");
		request.setLongitude("77.9999999");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSSearchFlashDealResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSSearchFlashDealResponse.class, request);
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
	public final void testFlashDealSearch_Request_WithoutCoodinates() {
		
		System.out.println("\n========================== Flash Deal Search - Request : Without coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Lilliput");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSSearchFlashDealResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSSearchFlashDealResponse.class, request);
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
	public final void testFlashDealSearch_Request_InvalidCoodinates() {
		
		System.out.println("\n========================== Flash Deal Search - Request : Invalid coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Lilliput");
		request.setLocationString("SDFDSF&DS)(F*&DSFJDOSKJFDSFY");
		request.setLatitude("3");
		request.setLongitude("6");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSErrorResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_Request_WithoutLocationString() {
		
		System.out.println("\n========================== Flash Deal Search - Request : Without Location String test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Little Italy");
		request.setLatitude("28.635307");
		request.setLongitude("77.22496");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSSearchFlashDealResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSSearchFlashDealResponse.class, request);
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
	public final void testFlashDealSearch_Request_LocationNotFoundException() {
		
		System.out.println("\n========================== Flash Deal Search - Request : Location Not Found Exception test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Little Italy");
		request.setLatitude("2");
		request.setLongitude("8");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSErrorResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_Request_NoResultsFound() {
		
		System.out.println("\n========================== Flash Deal Search - Request : No Results Found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Invalid location");
		request.setLocationString("SDFDSF&DS)(F*&DSFJDOSKJFDSFY");
		request.setLatitude("3");
		request.setLongitude("6");
		request.setPageCount(1);
		request.setExpiryPeriod("30");
		
		try{
			WSErrorResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_Request_MandatoryFieldValidationFailed() {
		
		System.out.println("\n========================== Flash Deal Search - Request : Mandatory Field Validation test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setLatitude("0");
		request.setLongitude("0");
		request.setLocationString("");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("flashDeal").path("request").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_ResultsList() {
		
		System.out.println("\n========================== Flash Deal Search - Result List : Success test case ===============================\n");
		
		request.setUsername("shubham51");
		request.setPageCount(1);
				
		try{
			WSSearchFlashDealResponse response = service.path("flashDeal").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSSearchFlashDealResponse.class, request);
			assertNotNull(response);
			
			assertNotNull(response.getFlashDealsData());
			
			List<WSFlashDeal> flashDealsData = response.getFlashDealsData();
			
			for (WSFlashDeal flashDeal : flashDealsData){
				System.out.println("Flash deal Data : " + flashDeal);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testFlashDealSearch_ResultsList_PageCountExceededException() {
		
		System.out.println("\n========================== Flash Deal Search - Result List : Page Count Exceeded Exception test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPageCount(5);
				
		try{
			WSErrorResponse response = service.path("flashDeal").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_ResultsList_NoRespondedResults() {
		
		System.out.println("\n========================== Flash Deal Search - Request : NO responded results found test case ===============================\n");
		
		request.setUsername("NoResutls");
				
		try{
			WSErrorResponse response = service.path("flashDeal").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFlashDealSearch_Detail() {
		
		System.out.println("\n========================== Flash Deal Search - Result List : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setFlashDealId("12");
				
		try{
			WSSearchFlashDealResponse response = service.path("flashDeal").path("detail").accept(MediaType.APPLICATION_XML).post(WSSearchFlashDealResponse.class, request);
			assertNotNull(response);
			
			assertNotNull(response.getFlashDealDetail());
			
			System.out.println("Flash deal Data : " + response.getFlashDealDetail());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testFlashDealSearch_Detail_InvalidFlashDealId() {
		
		System.out.println("\n========================== Flash Deal Search - Result List : Invalid Flash Deal Id test case ===============================\n");
		
		request.setUsername("Raj");
		request.setFlashDealId("99999");
				
		try{
			WSErrorResponse response = service.path("flashDeal").path("detail").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
