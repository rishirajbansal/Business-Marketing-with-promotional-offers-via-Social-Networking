package com.fynger.searchEngine.query.handlers;

import java.util.List;

import org.apache.log4j.Logger;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.business.PromotionsSearchManager;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.utilities.LocationToCoordinatesConverter;
import com.fynger.searchEngine.requests.vo.BaseRequestVO;
import com.fynger.searchEngine.requests.vo.PromotionsSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.PromotionsSearchResponseVO;

public class PromotionsSearchQueryHandler extends AbstractSearchQueryHandler {

    public static Logger logger = GenericUtility.getLogger(PromotionsSearchQueryHandler.class.getName());
    private PromotionsSearchRequestVO promotionsSearchRequestVO;
    private PromotionsSearchResponseVO promotionsSearchResponseVO;
    private List<BaseResponseVO> alPromotionsResultSet;

    public void setQueryRequest(BaseRequestVO baseRequestVO) {

        boolean coordinateStatus;

        double latitude;
        double longitude;

        promotionsSearchRequestVO = (PromotionsSearchRequestVO) baseRequestVO;

        if (getQueryType().equals(SearchQueryTypes.Promotion_resultset.toString())) {

            try {
                if (!GenericUtility.safeTrim(promotionsSearchRequestVO.getLocation()).equals(GenericConstants.EMPTY_STRING)) {

                    /** Retrieving Geo coordinates of the specified location */
                    LocationToCoordinatesConverter locationToCoordinates = new LocationToCoordinatesConverter();
                    String location = promotionsSearchRequestVO.getLocation();

                    coordinateStatus = locationToCoordinates.setLocation(location);

                    if (coordinateStatus == false) {

                        logger.debug("Unable to locate Geo Coordinates for the given location string in Promotion Search : " + location);

                        /** Retrieving Coordinates from Request Object because coordinates could not be populated using the location **/
                        latitude = promotionsSearchRequestVO.getLatitude();
                        longitude = promotionsSearchRequestVO.getLongitude();

                        if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                            logger.debug("Geo Coordinates not present in the request object for Promotion Search");
                            throw new GeoCoordinatesNotFoundException("Geo Coordinates not present in the request object for Promotion Search and Unable to locate Geo Coordinates in the request object for the given location string : " + location);
                        } 
                        else {
                            logger.debug("Geo Coordinates of the request Object Used for Promotion Search");
                        }
                    } 
                    else {

                        logger.debug("Geo Coordinates found using Coordinate Retrieval Service for Promotion Search");

                        latitude = locationToCoordinates.getLatitude();
                        longitude = locationToCoordinates.getLongitude();

                        promotionsSearchRequestVO.setLatitude(latitude);
                        promotionsSearchRequestVO.setLongitude(longitude);
                    }
                } 
                else {
                    /** setting the coordinates from request object in case the location is not mentioned **/
                    latitude = promotionsSearchRequestVO.getLatitude();
                    longitude = promotionsSearchRequestVO.getLongitude();
                    
                    if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                        logger.debug("Location string is empty and also Geo Coordinates not present in the request object for Promotion Search");                            
                        throw new GeoCoordinatesNotFoundException("Location string is empty and also Geo Coordinates not present in the request object for Promotion Search");
                    } 
                    else {
                        logger.debug("Geo Coordinates of the request Object Used for Promotion Search");
                    }    
                }
                
            } 
            catch (GeoCoordinatesNotFoundException gcnfEx) {
                throw gcnfEx;
            } 
            catch (Exception ex) {
                logger.error(" Exception occured in setQueryRequest of Promotion Search : " + ex.getMessage());
                throw new SearchEngineException("Exception occured in instantiating Coordinates of Promotion Search ", ex);
            } 
            catch (Throwable th) {
                logger.error(" Throwable occured in setQueryRequest of Promotion Search : " + th.getMessage());
                throw new SearchEngineException("Throwable occured in setQueryRequest of Promotion Search ", th);
            }

        }


    }

    public void handleQuery() throws SearchEngineException {
    	
    	int totalPages;
    	int totalResults;
    	
        super.handleQuery();
        logger.debug("Querying The Promotions Index");

        /** Querying The Server */
        PromotionsSearchManager promotionsSearchManager = new PromotionsSearchManager();
        alPromotionsResultSet = promotionsSearchManager.execute(promotionsSearchRequestVO);

        totalResults = promotionsSearchManager.getTotalResults();
        totalPages = promotionsSearchManager.getTotalPages();
        
        setTotalResults(totalResults);
        setTotalPages(totalPages);


        //Search type based implementation
    }

    public BaseResponseVO getQueryResponse() {

        promotionsSearchResponseVO = new PromotionsSearchResponseVO();

        //Populate the response VO here

        return promotionsSearchResponseVO;

    }

    public List<BaseResponseVO> getQueryResponseResultSet() {

        return alPromotionsResultSet;


    }
}
