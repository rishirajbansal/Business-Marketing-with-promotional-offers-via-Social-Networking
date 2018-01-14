/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.util.List;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealDetailsData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRequestData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.FlashDealRespondedData;

/**
 * @author Rishi
 *
 */
public interface IFlashDealSearchDAO {
	
	public boolean storeFlashDealRequests(List<FlashDealRequestData> alFlashDealRequestData) throws DataAccessException;
	
	public List<FlashDealDetailsData> loadFlashDealResultsList(String username, int flashDealValidityDiff, int pageCount, int maxPageResults) throws DataAccessException;
	
	public FlashDealDetailsData loadFlashDealDetail(String flashDealId) throws DataAccessException;
	
	public boolean validateAndUpdateFlashDealStatus() throws DataAccessException;
	
	public List<FlashDealDetailsData> fetchRespondedFlashDeals() throws DataAccessException;
	
	public boolean updateRespondedFlashDealsEmailStatus(String flashDealIds) throws DataAccessException;
	
	public List<FlashDealRespondedData> fetchMobileNotifiableRespondedFlashDeals() throws DataAccessException;
	
	public boolean updateRespondedFlashDealsNotifiedStatus(String flashDealRespondedIds) throws DataAccessException;
	
	public boolean updateRespondedFlashDealsNotifiedFailedStatus(String flashDealRespondedIds) throws DataAccessException;

}
