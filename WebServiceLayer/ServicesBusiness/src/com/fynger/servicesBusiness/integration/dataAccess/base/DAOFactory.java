/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.base;

import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IEmailLogDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFlashDealSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IListingSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPlaceGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPromotionsSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IShoutGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;

/**
 * @author Rishi
 *
 */
public abstract class DAOFactory {
	
	public static final int SQL = 1;
	
	public static DAOFactory getDAOFactory(int factory) {
		
		switch(factory) {
			case SQL:
				return new SQLDAOFactory();
			
			default:
				throw new DataAccessException("Only SQL Factories are supported"); 
		}
		
	}
	
	public abstract IUserDAO getUserDAO();
	
	public abstract IListingSearchDAO getListingSearchDAO();
	
	public abstract IPromotionsSearchDAO getPromotionsSearchDAO();
	
	public abstract IFlashDealSearchDAO getFlashDealSearchDAO();
	
	public abstract IShoutGroupDAO getShoutGroupDAO();
	
	public abstract IPlaceGroupDAO getPlaceGroupDAO();
	
	public abstract IEmailLogDAO getEmailLogDAO();

}
