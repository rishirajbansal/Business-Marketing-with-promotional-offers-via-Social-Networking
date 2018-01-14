/**
 * 
 */
package com.fynger.servicesController.services.domainObjects.responses;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fynger.servicesController.services.domainObjects.responses.objects.WSFlashDeal;

/**
 * @author Rishi
 *
 */
@XmlRootElement
public class WSSearchFlashDealResponse extends WSBaseResponse {
	
	private List<WSFlashDeal> flashDealsData;
	
	private WSFlashDeal flashDealDetail;

	/**
	 * @return the flashDealsData
	 */
	public List<WSFlashDeal> getFlashDealsData() {
		return flashDealsData;
	}

	/**
	 * @param flashDealsData the flashDealsData to set
	 */
	public void setFlashDealsData(List<WSFlashDeal> flashDealsData) {
		this.flashDealsData = flashDealsData;
	}

	/**
	 * @return the flashDealDetail
	 */
	public WSFlashDeal getFlashDealDetail() {
		return flashDealDetail;
	}

	/**
	 * @param flashDealDetail the flashDealDetail to set
	 */
	public void setFlashDealDetail(WSFlashDeal flashDealDetail) {
		this.flashDealDetail = flashDealDetail;
	}
	

	

}
