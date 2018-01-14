/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSPromotion;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;

/**
 * @author Rishi
 *
 */

@XmlRootElement
public class WSSearchPromotionResponse extends WSBaseResponse {
	
	private List<WSPromotion> promotionsData;
	
	private WSPromotion promotionDetail;
	
	private List<WSReview> reviewData;
	

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

	/**
	 * @return the reviewData
	 */
	public List<WSReview> getReviewData() {
		return reviewData;
	}

	/**
	 * @param reviewData the reviewData to set
	 */
	public void setReviewData(List<WSReview> reviewData) {
		this.reviewData = reviewData;
	}

}
