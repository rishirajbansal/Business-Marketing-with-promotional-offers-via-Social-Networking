package com.fynger.searchEngine.query.handlers;

import java.util.List;

import org.apache.log4j.Logger;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.business.FlashDealSearchManager;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.exception.LocationNotFoundException;
import com.fynger.searchEngine.query.utilities.CoordinatesToLocationConverter;
import com.fynger.searchEngine.query.utilities.LocationToCoordinatesConverter;
import com.fynger.searchEngine.requests.vo.BaseRequestVO;
import com.fynger.searchEngine.requests.vo.FlashDealSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FlashDealSearchResponseVO;

public class FlashDealSearchQueryHandler extends AbstractSearchQueryHandler {

    public static Logger logger = GenericUtility.getLogger(FlashDealSearchQueryHandler.class.getName());
    
    private FlashDealSearchRequestVO flashDealSearchRequestVO;
    private FlashDealSearchResponseVO flashDealSearchResponseVO;
    private List<BaseResponseVO> alFlashDealSearchResultSet;

    public void setQueryRequest(BaseRequestVO baseRequestVO) {

        boolean locationStatus;
        boolean coordinateStatus;
        double latitude, longitude;

        String location;

        flashDealSearchRequestVO = (FlashDealSearchRequestVO) baseRequestVO;

        if (getQueryType().equals(SearchQueryTypes.FlashDeal_resultset.toString())) {
            
            try {

                if (!GenericUtility.safeTrim(flashDealSearchRequestVO.getLocation()).equals(GenericConstants.EMPTY_STRING)) {

                    /** Retrieving Geo coordinates of the specified location */
                    LocationToCoordinatesConverter locationToCoordinates = new LocationToCoordinatesConverter();
                    location = flashDealSearchRequestVO.getLocation();

                    coordinateStatus = locationToCoordinates.setLocation(location);

                    if (coordinateStatus == false) {

                        logger.debug("Unable to locate Geo Coordinates for the given location string in Flash Deal Search : " + location);

                        /** Retrieving Coordinates from Request Object because coordinates could not be populated using the location **/
                        latitude = flashDealSearchRequestVO.getLatitude();
                        longitude = flashDealSearchRequestVO.getLongitude();

                        if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                            logger.debug("Geo Coordinates not present or Invalid Geo Coordinates present in the request object for Flash Deal Search");
                            throw new GeoCoordinatesNotFoundException("Unable to locate or Invalid Geo Coordinates in the request object for the given location string : " + location);
                        } else {
                            logger.debug("Geo Coordinates of the request Object Used for Flash Deal Search");
                        }
                    } else {

                        logger.debug("Geo Coordinates found using Coordinate Retrieval Service for Flash Deal Search");

                        latitude = locationToCoordinates.getLatitude();
                        longitude = locationToCoordinates.getLongitude();

                        flashDealSearchRequestVO.setLatitude(latitude);
                        flashDealSearchRequestVO.setLongitude(longitude);
                    }
                } else {
                    /** setting the coordinates from request object in case the location is not mentioned **/
                    latitude = flashDealSearchRequestVO.getLatitude();
                    longitude = flashDealSearchRequestVO.getLongitude();

                    if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {                                                       
                        logger.debug("Geo Coordinates not present or Invalid Geo Coordinates in the request object for Flash Deal Search");
                        throw new GeoCoordinatesNotFoundException("Unable to locate or Invalid Geo Coordinates in the request object");
                    } else {
                        logger.debug("Geo Coordinates of the request Object Used for Flash Deal Search");

                        /** retrieving the location based on the coordinates **/
                        CoordinatesToLocationConverter coordinatesToLocation = new CoordinatesToLocationConverter();
                        coordinatesToLocation.setLatitude(latitude);
                        coordinatesToLocation.setLongitude(longitude);
                        locationStatus = coordinatesToLocation.getLocationStatus();

                        if (locationStatus == false) {
                            logger.debug("Location could not be determined of the coordinates : " + latitude + SearchEngineConstants.COMMA + longitude);
                            throw new LocationNotFoundException("Unable to locate the location for the given coordinates in Flash Deal Search");
                        } else {
                            location = coordinatesToLocation.getLocation();
                            flashDealSearchRequestVO.setLocation(location);
                        }
                    }

                }

            } catch (GeoCoordinatesNotFoundException gcnfEx) {
                throw gcnfEx;
            } catch (LocationNotFoundException lnfEx) {
                    throw lnfEx;    
            } catch (Exception ex) {
                logger.error(" Exception occured in setQueryRequest of Flash Deal Search : " + ex.getMessage());
                throw new SearchEngineException("Exception occured in instantiating Coordinates of lisitng Search ", ex);
            } catch (Throwable th) {
                logger.error(" Throwable occured in setQueryRequest of Flash Deal Search : " + th.getMessage());
                throw new SearchEngineException("Throwable occured in setQueryRequest of Flash Deal Search ", th);
            }
        }

    }

    public void handleQuery() throws SearchEngineException {
        
        super.handleQuery();
        logger.debug("Querying The Flash Deal Index");

        /** Querying The Server */
        FlashDealSearchManager flashDealSearchManager = new FlashDealSearchManager();
        
        alFlashDealSearchResultSet = flashDealSearchManager.execute(flashDealSearchRequestVO);
        
        /**
        int totalPages;
        int totalResults;
        
        totalResults = flashDealSearchManager.getTotalResults();
        totalPages = flashDealSearchManager.getTotalPages();

        setTotalResults(totalResults);
        setTotalPages(totalPages);
        **/
        //Search type based implementation
    }

    public BaseResponseVO getQueryResponse() {

        flashDealSearchResponseVO = new FlashDealSearchResponseVO();

        //Populate the response VO here

        return flashDealSearchResponseVO;

    }

    public List<BaseResponseVO> getQueryResponseResultSet() {

               return alFlashDealSearchResultSet;

    }
}
