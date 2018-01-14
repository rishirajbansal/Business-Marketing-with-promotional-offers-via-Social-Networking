/**
 * 
 */
package com.fynger.servicesBusiness.business.initialize;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.ShoutGroupException;
import com.fynger.servicesBusiness.exception.ShoutGroupLoaderException;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IShoutGroupDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dao.ShoutGroupDAOImpl;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.CityGeoData;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.ShoutGroupData;

/**
 * @author Rishi
 *
 */
public class ShoutGroupLoader {
	
	public static LoggerManager logger = GenericUtility.getLogger(ShoutGroupLoader.class.getName());
	
	private static ShoutGroupLoader shoutGroupLoader = null;
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	private List<CityGeoData> alCityGeoDetails = new ArrayList<CityGeoData>(25);
	
	private SortedMap<Integer, List<ShoutGroupData>> tmShoutGroupGeoDetails = new TreeMap<Integer, List<ShoutGroupData>>();
	
	
	private ShoutGroupLoader() {
		
	}
	
	public static ShoutGroupLoader createInstance() {
		
		if (shoutGroupLoader == null) {
			synchronized (lockObject) {
				shoutGroupLoader = new ShoutGroupLoader();
			}
		}

		return shoutGroupLoader;
	}
	
	public void loadShoutGroupGeoDetails(){
		
		try{
			IShoutGroupDAO shoutGroupDAO = new ShoutGroupDAOImpl();
			
			/* Load City geographic details */
			alCityGeoDetails = shoutGroupDAO.loadCityGeoDetails();
			
			if (null != alCityGeoDetails && alCityGeoDetails.size() > 0){
				//Do Nothing
			}
			else{
				logger.error("Empty list is returned by the loadCityGeoDetails DAO. Unable to load the city geo details.");
				throw new ShoutGroupLoaderException("Empty list is returned by the loadCityGeoDetails DAO. Unable to load the city geo details.");
			}
			
			/* Load Shout Group geographic details */
			List<ShoutGroupData> alShoutGroupDetails = shoutGroupDAO.loadShoutGroupDetails();
			
			if (null != alShoutGroupDetails && alShoutGroupDetails.size() > 0){
				
				List<ShoutGroupData> innerList = null;
				
				for (ShoutGroupData daoData : alShoutGroupDetails){
					if (tmShoutGroupGeoDetails.containsKey(daoData.getCityId())){
						innerList = tmShoutGroupGeoDetails.get(daoData.getCityId());
						innerList.add(daoData);
					}
					else{
						innerList = new ArrayList<ShoutGroupData>(25);
						innerList.add(daoData);
						tmShoutGroupGeoDetails.put(daoData.getCityId(), innerList);
					}
				}
				
			}
			else{
				logger.error("Empty list is returned by the loadShoutGroupDetails DAO. Unable to load the shout group geo details.");
				throw new ShoutGroupLoaderException("Empty list is returned by the loadShoutGroupDetails DAO. Unable to load the shout group geo details.");
			}
		}
		catch(ShoutGroupException sgEx){
			throw new ShoutGroupLoaderException(sgEx.getMessage());
		}
		catch(Exception ex){
			throw new ShoutGroupLoaderException(ex.getMessage());		
		}
	}
	
	public List<CityGeoData> getListOfCityGeoDetails(){
		return alCityGeoDetails;
	}
	
	public SortedMap<Integer, List<ShoutGroupData>> getMapOfShoutGroupGeoDetails(){
		return tmShoutGroupGeoDetails;
	}
	
	public List<ShoutGroupData> getCityBasedListofShoutGroupGeoDetails(Integer cityId){
		if (tmShoutGroupGeoDetails.containsKey(cityId)){
			return tmShoutGroupGeoDetails.get(cityId);
		}
		else {
			return null;
		}
		
	}

}
