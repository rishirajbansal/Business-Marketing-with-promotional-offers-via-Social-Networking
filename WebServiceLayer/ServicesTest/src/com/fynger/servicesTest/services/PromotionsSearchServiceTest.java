/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSSearchPromotionRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchPromotionResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class PromotionsSearchServiceTest extends BaseServicesTest {
	
	WSSearchPromotionRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSSearchPromotionRequest();
	}
	
	@Test
	public final void testPromotionSearch_SearchResults_WithCoodinates() {
		
		System.out.println("\n========================== Promotions Search - Results List : With coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Restaurant");
		request.setLocationString("Andheri East, Mumbai, Maharashtra, India");
		/*request.setLatitude("28.9999999");
		request.setLongitude("77.9999999");*/
		request.setPageCount(1);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			List<WSPromotion> listingData = response.getPromotionsData();
			
			for (WSPromotion promotion : listingData){
				System.out.println("Promption Data : " + promotion);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testPromotionSearch_SearchResults_WithoutCoodinates() {
		
		System.out.println("\n========================== Promotions Search - Results List : Without coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Lilliput");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			List<WSPromotion> listingData = response.getPromotionsData();
			
			for (WSPromotion promotion : listingData){
				System.out.println("Promption Data : " + promotion);
			}
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testPromotionSearch_SearchResults_InvalidCoodinates() {
		
		System.out.println("\n========================== Promotions Search - Results List : Invalid coordinates test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Lilliput");
		request.setLocationString("SDFDSF&DS)(F*&DSFJDOSKJFDSFY");
		request.setLatitude("3");
		request.setLongitude("6");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("promotions").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testPromotionSearch_SearchResults_NoResultsFound() {
		
		System.out.println("\n========================== Promotions Search - Results List : No results found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setSearchText("Invalid location");
		request.setLocationString("New Delhi, India");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("promotions").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testPromotionSearch_SearchResults_MandatoryFieldValidationFailed() {
		
		System.out.println("\n========================== Promotions Search - Results List : Mandatory Field Validation test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setLatitude("0");
		request.setLongitude("0");
		request.setLocationString("");
		request.setPageCount(1);
		
		try{
			WSErrorResponse response = service.path("promotions").path("resultsList").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testPromotionSearch_Detail_Store_Success() {
		
		System.out.println("\n========================== Promotions Search - detail : Store details retrieval Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setEntityType("Promotion");
		request.setEntityId("88");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("detail").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			WSPromotion promotionData = response.getPromotionDetail();
			
			assertNotNull(promotionData);
			
			System.out.println("Promotion Data : " + promotionData);
					
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testPromotionSearch_Detail_Event_Success() {
		
		System.out.println("\n========================== Promotions Search - detail : Event details retrieval Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setEntityType("Event");
		request.setEntityId("13");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("detail").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			
			WSPromotion promotionData = response.getPromotionDetail();
			
			assertNotNull(promotionData);
			
			System.out.println("Promotion Data : " + promotionData);
					
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testPromotionSearch_Detail_PromotionIdNotFound() {
		
		System.out.println("\n========================== Promotions Search - detail : Promotion Id not found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setEntityType("Promotion");
		request.setEntityId("99999");
		
		try{
			WSErrorResponse response = service.path("promotions").path("detail").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testPromotionSearch_Rating_NewNoMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Rating : First Rating with no merchant association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("8");
		request.setRating(2);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_Rating_UpdateNoMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Rating : Update Rating with no merchant association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("8");
		request.setRating(4);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_Rating_NewWithMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Rating : First Rating with listing association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("9");
		request.setRating(4);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_Rating_UpdateWithMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Rating : Update Rating with merchant association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("9");
		request.setRating(2);
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("rating").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_Review_NoMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Review : With no merchant association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("58");
		request.setReviewText("this is subsequent review");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("review").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_Review_WithMerchantAssoc() {
		
		System.out.println("\n========================== Promotion Search - Review : With merchant association test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("9");
		request.setReviewText("Review Text");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("review").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_LoadReviews() {
		
		System.out.println("\n========================== Promotion Search - Load Reviews : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("58");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("loadReviews").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionSearch_LoadReviews_NoReviewsFound() {
		
		System.out.println("\n========================== Promotion Search - Load Reviews : No Reviews found test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPassword("agent009");
		request.setStoreId("898988");
		
		try{
			WSErrorResponse response = service.path("promotions").path("loadReviews").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testPromotionsSearch_AddtoFavourite_Success() {
		
		System.out.println("\n========================== Promotions Search - Add Favourites Reviews : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Brands");
		request.setKeyword("Levis");
		request.setStoreId("7");
		
		try{
			WSSearchPromotionResponse response = service.path("promotions").path("like").accept(MediaType.APPLICATION_XML).post(WSSearchPromotionResponse.class, request);
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
	public final void testPromotionsSearch_AddtoFavourite_AlreadyExists() {
		
		System.out.println("\n========================== Promotions Search - Add Favourites Reviews : Already Exists test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Brands");
		request.setKeyword("Levis");
		request.setStoreId("7");
		
		try{
			WSErrorResponse response = service.path("promotions").path("like").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
