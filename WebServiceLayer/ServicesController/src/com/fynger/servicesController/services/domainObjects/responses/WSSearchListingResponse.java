/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSReview;
import com.fynger.servicesController.services.domainObjects.responses.objects.WSListing;

/**
 * @author Rishi
 *
 */

@XmlRootElement
public class WSSearchListingResponse extends WSBaseResponse {
	
	private List<WSListing> listingsData;
	
	private List<WSReview> reviewData;

	/**
	 * @return the listingsData
	 */
	public List<WSListing> getListingsData() {
		return listingsData;
	}

	/**
	 * @param listingsData the listingsData to set
	 */
	public void setListingsData(List<WSListing> listingsData) {
		this.listingsData = listingsData;
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
