package com.fynger.searchEngine.query.handlers;

import java.util.List;

import org.apache.log4j.Logger;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.business.ListingSearchManager;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.utilities.LocationToCoordinatesConverter;
import com.fynger.searchEngine.requests.vo.BaseRequestVO;
import com.fynger.searchEngine.requests.vo.ListingSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.ListingSearchResponseVO;

public class ListingSearchQueryHandler extends AbstractSearchQueryHandler {

    public static Logger logger = GenericUtility.getLogger(ListingSearchQueryHandler.class.getName());
    
    private ListingSearchRequestVO listingSearchRequestVO;
    private ListingSearchResponseVO listingSearchResponseVO;
    private List<BaseResponseVO> alListingResultSet;

    public void setQueryRequest(BaseRequestVO baseRequestVO) {

        boolean coordinateStatus;
        double latitude;
        double longitude;

        listingSearchRequestVO = (ListingSearchRequestVO) baseRequestVO;

        if (getQueryType().equals(SearchQueryTypes.Listing_resultset.toString())) {

            /** Retrieving Geo coordinates of the specified location */
            try {
            	
            	if (!GenericUtility.safeTrim(listingSearchRequestVO.getLocation()).equals(GenericConstants.EMPTY_STRING)){

                    /** Retrieving Geo coordinates of the specified location */
                    LocationToCoordinatesConverter locationToCoordinates = new LocationToCoordinatesConverter();
                    String location = listingSearchRequestVO.getLocation();

                    coordinateStatus = locationToCoordinates.setLocation(location);

                    if (coordinateStatus == false) {

                        logger.debug("Unable to locate Geo Coordinates for the given location string in Listing Search : " + location);

                        /** Retrieving Coordinates from Request Object because coordinates could not be populated using the location **/
                        latitude = listingSearchRequestVO.getLatitude();
                        longitude = listingSearchRequestVO.getLongitude();

                        if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                            logger.debug("Geo Coordinates not present in the request object for Listing Search");                            
                            throw new GeoCoordinatesNotFoundException("Geo Coordinates not present in the request object for Listing Search and Unable to locate Geo Coordinates in the request object for the given location string : " + location);
                        }
                        else {
                            logger.debug("Geo Coordinates of the request Object Used for listing Search");
                        }
                    } 
                    else {

                        logger.debug("Geo Coordinates found using Coordinate Retrieval Service for lisitng Search");

                        latitude = locationToCoordinates.getLatitude();
                        longitude = locationToCoordinates.getLongitude();

                        listingSearchRequestVO.setLatitude(latitude);
                        listingSearchRequestVO.setLongitude(longitude);
                    }
                } 
            	else {
                    /** setting the coordinates from request object in case the location is not mentioned **/
                    latitude = listingSearchRequestVO.getLatitude();
                    longitude = listingSearchRequestVO.getLongitude();
                    
                    if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                        logger.debug("Location string is empty and also Geo Coordinates not present in the request object for Listing Search");                            
                        throw new GeoCoordinatesNotFoundException("Location string is empty and also Geo Coordinates not present in the request object for Listing Search");
                    } 
                    else {
                        logger.debug("Geo Coordinates of the request Object Used for listing Search");
                    }                    
                    
                }

            } 
            catch (GeoCoordinatesNotFoundException gcnfEx) {
                throw gcnfEx;
            } 
            catch (Exception ex) {
                logger.error(" Exception occured in setQueryRequest of listing Search : " + ex.getMessage());
                throw new SearchEngineException("Exception occured in instantiating Coordinates of lisitng Search ", ex);
            } 
            catch (Throwable th) {
                logger.error(" Throwable occured in setQueryRequest of listing Search : " + th.getMessage());
                throw new SearchEngineException("Throwable occured in setQueryRequest of listing Search ", th);
            }


        }

    }

    public void handleQuery() throws SearchEngineException {
    	
    	int totalPages;
    	int totalResults;
		
	    super.handleQuery();
	    logger.debug("Querying The Listing Index");
	
	    /** Querying The Server */
	    ListingSearchManager listingSearchManager = new ListingSearchManager();
	
	    alListingResultSet = listingSearchManager.execute(listingSearchRequestVO);
	    
	    totalResults = listingSearchManager.getTotalResults();
	    totalPages = listingSearchManager.getTotalPages();
	    
	    setTotalResults(totalResults);
	    setTotalPages(totalPages);
    }

    public BaseResponseVO getQueryResponse() {

        listingSearchResponseVO = new ListingSearchResponseVO();

        //Populate the response VO here

        return listingSearchResponseVO;

    }

    public List<BaseResponseVO> getQueryResponseResultSet() {

        return alListingResultSet;
    }
}
