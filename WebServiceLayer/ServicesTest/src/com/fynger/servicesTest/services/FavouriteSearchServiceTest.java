/**
 * 
 */
package com.fynger.servicesTest.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fynger.servicesController.services.domainObjects.requests.WSSearchFavouritesRequest;
import com.fynger.servicesController.services.domainObjects.responses.WSErrorResponse;
import com.fynger.servicesController.services.domainObjects.responses.WSSearchFavouritesResponse;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;
import com.fynger.servicesTest.base.BaseServicesTest;

/**
 * @author Rishi
 *
 */
public class FavouriteSearchServiceTest extends BaseServicesTest {
	
	WSSearchFavouritesRequest request = null;
	
	public void setUp() throws Exception {
		
		super.setUp();
		
		request = new WSSearchFavouritesRequest();
	}
	
	@Test
	public final void testFavouriteSearch_AddFavourite_New_Success() {
		
		System.out.println("\n========================== Favourite Search - New Add Favourite : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Brands");
		request.setKeyword("Levis");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("add").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
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
	public final void testFavouriteSearch_AddFavourite_Update_Success() {
		
		System.out.println("\n========================== Favourite Search - Update Add Favourite : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Categories");
		request.setKeyword("Pasta");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("add").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
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
	public final void testFavouriteSearch_AddFavourite_Update_AlreadyExists() {
		
		System.out.println("\n========================== Favourite Search - Update Add Favourite : Already Exists test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Categories");
		request.setKeyword("Pasta");
		
		try{
			WSErrorResponse response = service.path("favourite").path("add").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFavouriteSearch_ListFavourite_Success() {
		
		System.out.println("\n========================== Favourite Search - List Favourite : Success test case ===============================\n");
		
		request.setUsername("Raj");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("list").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());
			System.out.println("Categories : " + response.getCategories());
			System.out.println("Brands : " + response.getBrands());
			System.out.println("Places : " + response.getPlaces());
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testFavouriteSearch_ListFavourite_NoFavouritesFoundException() {
		
		System.out.println("\n========================== Favourite Search - List Favourite : No favourites Found Exception test case ===============================\n");
		
		request.setUsername("NoUser");
		
		try{
			WSErrorResponse response = service.path("favourite").path("list").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFavouriteSearch_DeleteFavourite_Success() {
		
		System.out.println("\n========================== Favourite Search - Delete Favourite : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setGroupType("Categories");
		request.setKeyword("Pizza");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("delete").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
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
	public final void testFavouriteSearch_DeleteFavourite_NoFavouritesFoundException() {
		
		System.out.println("\n========================== Favourite Search - Delete Favourite : No favourites Found Exception test case ===============================\n");
		
		request.setUsername("NoUser");
		request.setGroupType("Categories");
		request.setKeyword("Pasta");
		
		try{
			WSErrorResponse response = service.path("favourite").path("delete").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
	public final void testFavouriteSearch_SearchResultsList_Success() {
		
		System.out.println("\n========================== Favourite Search - Search Results List : Success test case ===============================\n");
		
		request.setUsername("Raj");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("results").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
			assertNotNull(response);
			
			System.out.println("response : " + response.getResponse());
			System.out.println("status : " + response.getStatus());	
			
			List<WSPromotion> promotionData = response.getPromotionsData();
			
			for (WSPromotion promotion : promotionData){
				System.out.println("Promption Data : " + promotion);
			}	
			
		}
		catch(Exception ex){
			System.out.println("Exception occurred : " + ex.getMessage());
			fail("Exception occurred : " + ex.getMessage());
		}
	}
	
	@Test
	public final void testFavouriteSearch_FavouritePromotionDetail_Success() {
		
		System.out.println("\n========================== Favourite Search - Favourite Promotion Detail : Success test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPromotionId("88");
		
		try{
			WSSearchFavouritesResponse response = service.path("favourite").path("detail").accept(MediaType.APPLICATION_XML).post(WSSearchFavouritesResponse.class, request);
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
	public final void testFavouriteSearch_FavouritePromotionDetail_PromotionIdNotFoundException() {
		
		System.out.println("\n========================== Favourite Search - Favourite Promotion Detail : Promotion Id Not found exception test case ===============================\n");
		
		request.setUsername("Raj");
		request.setPromotionId("999");
		
		try{
			WSErrorResponse response = service.path("favourite").path("search").path("detail").accept(MediaType.APPLICATION_XML).post(WSErrorResponse.class, request);
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
