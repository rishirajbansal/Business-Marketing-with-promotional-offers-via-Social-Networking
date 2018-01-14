/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.base;

import com.fynger.servicesBusiness.integration.dataAccess.dao.EmailLogDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.FlashDealSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IEmailLogDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IFlashDealSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IListingSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPlaceGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IPromotionsSearchDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IShoutGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IUserDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.ListingSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.PlaceGroupDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.PromotionsSearchDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.ShoutGroupDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dao.UserDAOImpl;

/**
 * @author Rishi
 *
 */
public class SQLDAOFactory extends DAOFactory {
	
	/**
     * Returns the User DAO associated with the current DAOFactory.
     * @return the User DAO associated with the current DAOFactory.
     */
	@Override
	public IUserDAO getUserDAO(){
		return new UserDAOImpl();
	}

	/**
     * Returns the Listing Search DAO associated with the current DAOFactory.
     * @return the Listing Search DAO associated with the current DAOFactory.
     */
	@Override
	public IListingSearchDAO getListingSearchDAO() {
		return new ListingSearchDAOImpl();
	}

	/**
     * Returns the Promotions Search DAO associated with the current DAOFactory.
     * @return the Promotions Search DAO associated with the current DAOFactory.
     */
	@Override
	public IPromotionsSearchDAO getPromotionsSearchDAO() {
		return new PromotionsSearchDAOImpl();
	}
	
	/**
     * Returns the Flash Deal Search DAO associated with the current DAOFactory.
     * @return the Flash Deal Search DAO associated with the current DAOFactory.
     */
	@Override
	public IFlashDealSearchDAO getFlashDealSearchDAO() {
		return new FlashDealSearchDAOImpl();
	}
	
	/**
     * Returns the Shout Group DAO associated with the current DAOFactory.
     * @return the Shout Group DAO associated with the current DAOFactory.
     */
	@Override
	public IShoutGroupDAO getShoutGroupDAO() {
		return new ShoutGroupDAOImpl();
	}
	
	/**
     * Returns the Place Group DAO associated with the current DAOFactory.
     * @return the Place Group DAO associated with the current DAOFactory.
     */
	@Override
	public IPlaceGroupDAO getPlaceGroupDAO() {
		return new PlaceGroupDAOImpl();
	}
	
	/**
     * Returns the Email Log DAO associated with the current DAOFactory.
     * @return the Email Log DAO associated with the current DAOFactory.
     */
	@Override
	public IEmailLogDAO getEmailLogDAO() {
		return new EmailLogDAOImpl();
	}
	

}
