/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSSearchFavouritesResponse extends WSBaseResponse {
	
	private List<String> categories;
	
	private List<String> brands;
	
	private List<String> places;
	
	private List<WSPromotion> promotionsData;
	
	private WSPromotion promotionDetail;
	

	/**
	 * @return the categories
	 */
	public List<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the brands
	 */
	public List<String> getBrands() {
		return brands;
	}

	/**
	 * @param brands the brands to set
	 */
	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	/**
	 * @return the places
	 */
	public List<String> getPlaces() {
		return places;
	}

	/**
	 * @param places the places to set
	 */
	public void setPlaces(List<String> places) {
		this.places = places;
	}

	/**
	 * @return the promotionsData
	 */
	public List<WSPromotion> getPromotionsData() {
		return promotionsData;
	}

	/**
	 * @param promotionsData the promotionsData to set
	 */
	public void setPromotionsData(List<WSPromotion> promotionsData) {
		this.promotionsData = promotionsData;
	}

	/**
	 * @return the promotionDetail
	 */
	public WSPromotion getPromotionDetail() {
		return promotionDetail;
	}

	/**
	 * @param promotionDetail the promotionDetail to set
	 */
	public void setPromotionDetail(WSPromotion promotionDetail) {
		this.promotionDetail = promotionDetail;
	}

}
 