/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.dao;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.EmailLogData;

/**
 * @author Rishi
 *
 */
public interface IEmailLogDAO {
	
	public boolean storeEmailLog(EmailLogData emailLogData) throws DataAccessException;

}
