package com.fynger.servicesBusiness.business.base;

import java.util.HashMap;
import java.util.Map;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.ActionException;

public class ActionFactory {
	
	public static LoggerManager logger = GenericUtility.getLogger(ActionFactory.class.getName());
	
	private static ActionFactory actionFactory = null;
	
	private static Map<String, IBusinessAction> actionObjects = new HashMap<String, IBusinessAction>();
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private ActionFactory(){
		loadActionDirectory();
	}
	
	public static void instantiate() throws ActionException{
		
		try{
			if (null == actionFactory){
				synchronized(lockObject){
					actionFactory = new ActionFactory();
				}
			}
		}
		catch(Exception ex){
			logger.error(" Exception occured in instantiating ActionFactory : " + ex.getMessage());
			throw new ActionException("Exception occured in in instantiating ActionFactory ", ex);
		}
		catch(Throwable th){
			logger.error(" Throwable occured in instantiating ActionDirectory : " + th.getMessage());
			throw new ActionException("Throwable occured in instantiating ActionFactory ", th);
		}
	}
	
	private void loadActionDirectory(){
		
		actionObjects.put(ActionDirectory.ACTION_LOGIN, new com.fynger.servicesBusiness.business.actions.LoginAction());
		actionObjects.put(ActionDirectory.ACTION_USERPROFILE, new com.fynger.servicesBusiness.business.actions.ProfileAction());
		actionObjects.put(ActionDirectory.ACTION_LISTINGSEARCH, new com.fynger.servicesBusiness.business.actions.ListingSearchAction());
		actionObjects.put(ActionDirectory.ACTION_PROMOTIONSSEARCH, new com.fynger.servicesBusiness.business.actions.PromotionsSearchAction());
		actionObjects.put(ActionDirectory.ACTION_FLASHDEALSEARCH, new com.fynger.servicesBusiness.business.actions.FlashDealSearchAction());
		actionObjects.put(ActionDirectory.ACTION_FAVOURITESSEARCH, new com.fynger.servicesBusiness.business.actions.FavouritesSearchAction());
		actionObjects.put(ActionDirectory.ACTION_SHOUTGROUP, new com.fynger.servicesBusiness.business.actions.ShoutGroupAction());
		actionObjects.put(ActionDirectory.ACTION_PLACEGROUP, new com.fynger.servicesBusiness.business.actions.PlaceGroupAction());
		actionObjects.put(ActionDirectory.ACTION_REFRESHLOCATION, new com.fynger.servicesBusiness.business.actions.RefreshLocationAction());
		actionObjects.put(ActionDirectory.ACTION_LOGOUT, new com.fynger.servicesBusiness.business.actions.LogoutAction());
		
	}
	
	public static synchronized IBusinessAction getActionInstance(String action){
		
		return actionObjects.get(action);
	}


}
