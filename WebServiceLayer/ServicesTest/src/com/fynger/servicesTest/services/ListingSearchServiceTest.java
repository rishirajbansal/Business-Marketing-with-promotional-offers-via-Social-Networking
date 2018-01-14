/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSSearchListingRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchListingResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSListing;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class ListingSearchServiceTest extends BaseServicesTest {
	
	WSSearchListingRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSSearchListingRequest();
	}
	
	@Test
	public final void testListingSearchResultsList_WithCoodinates() {
		
		System.out.println("\n========================== Listing Search - Results List : With coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Restaurant");
		request.setLocationString("Andheri East, Mumbai, Maharashtra, India");
		/*request.setLatitude("28.9999999");
		request.setLongitude("77.9999999");*/
		request.setPageCount(1);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
			assertNotNull(response);
			
			System.out.println("status : " + response.getStatus());
			
			List<WSListing> listingData = response.getListingsData();
			
			for (WSListing listing : listingData){
				System.out.println("Listing Data : " + listing);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testListingSearchResultsList_WithoutCoodinates() {
		
		System.out.println("\n========================== Listing Search - Results List : Without coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Hotels");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
			assertNotNull(response);
			
			System.out.println("status : " + response.getStatus());
			
			List<WSListing> listingData = response.getListingsData();
			
			for (WSListing listing : listingData){
				System.out.println("Listing Data : " + listing);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testListingSearchResultsList_InvalidCoodinates() {
		
		System.out.println("\n========================== Listing Search - Results List : Invalid coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Hotels");
		request.setLocationString("");
		request.setLatitude("0.0");
		request.setLongitude("0.0");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testListingSearchResultsList_GeoCoordniatesNotFound() {
		
		System.out.println("\n========================== Listing Search - Results List : Geo Coordinates not found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Hotels");
		request.setLocationString("SDFDSF&DS)(F*&DSFJDOSKJFDSFY");
		request.setLatitude("0.0");
		request.setLongitude("0.0");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testListingSearchResultsListDataNotFound() {
		
		System.out.println("\n========================== Listing Search - Results List : Matched Data not found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Invalid location");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testListingSearchResultsListMandatoryFieldValidation() {
		
		System.out.println("\n========================== Listing Search - Results List : Mandatory Field Validation test case ===============================\n");
		
		populateRequest();
		request.setLatitude("0");
		request.setLongitude("0");
		request.setLocationString("");
		
		try{
			WSErrorResponse response = service.path("listing").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testListingRatingNewNoMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Rating : First Rating with no merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("2");
		request.setRating(2);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingRatingUpdateNoMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Rating : Update Rating with no merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("2");
		request.setRating(4);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingRatingNewWithMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Rating : First Rating with merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("3");
		request.setRating(2);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingRatingUpdateWithMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Rating : Update Rating with merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("3");
		request.setRating(4);
		
		try{
			WSSearchListingResponse response = service.path("listing").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingReviewNewNoMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Review : With no merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("2");
		request.setReviewText("Review Text");
		
		try{
			WSSearchListingResponse response = service.path("listing").path("review").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingReviewNewWithMerchantAssoc() {
		
		System.out.println("\n========================== Listing Search - Review : With merchant association test case ===============================\n");
		
		populateRequest();
		request.setListingId("3");
		request.setReviewText("Review Text");
		
		try{
			WSSearchListingResponse response = service.path("listing").path("review").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListingLoadReviews() {
		
		System.out.println("\n========================== Listing Search - Load Reviews : Success test case ===============================\n");
		
		populateRequest();
		request.setListingId("3");
		
		try{
			WSSearchListingResponse response = service.path("listing").path("loadReviews").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			List<WSReview> alReview = response.getReviewData();
			
			for (WSReview review : alReview){
				System.out.println("Review Data : " + review);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testListingLoadReviewsNoReviewsFound() {
		
		System.out.println("\n========================== Listing Search - Load Reviews : No Reviews found test case ===============================\n");
		
		populateRequest();
		request.setListingId("5");
		
		try{
			WSErrorResponse response = service.path("listing").path("loadReviews").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testListing_AddtoFavourite_Success() {
		
		System.out.println("\n========================== Listing Search - Add Favouritess : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Places");
		request.setKeyword("Flames Restaurant & Bar - Hilton");
		request.setListingId("7");
		
		try{
			WSSearchListingResponse response = service.path("listing").path("like").accept(MediaType.APPLICATION_XML).post(WSSearchListingResponse.class, request);
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
	public final void testListing_AddtoFavourite_AlreadyExists() {
		
		System.out.println("\n========================== Listing Search - Add Favouritess : Already Exists test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Places");
		request.setKeyword("Red Fort");
		request.setListingId("7");
		
		try{
			WSErrorResponse response = service.path("listing").path("like").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	
	private void populateRequest(){
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Hotels");
		request.setLatitude("28.9999999");
		request.setLatitude("28.9999999");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		
	}

}
