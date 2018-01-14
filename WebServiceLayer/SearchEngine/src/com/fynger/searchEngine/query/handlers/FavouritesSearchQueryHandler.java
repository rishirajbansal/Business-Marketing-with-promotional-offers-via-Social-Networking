package com.fynger.searchEngine.query.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.business.FavouriteSearchManager;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.GeoCoordinatesNotFoundException;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.utilities.SearchUtility;
import com.fynger.searchEngine.requests.vo.BaseRequestVO;
import com.fynger.searchEngine.requests.vo.FavouritesSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FavouritesSearchResponseVO;

public class FavouritesSearchQueryHandler extends AbstractSearchQueryHandler {

    public static Logger logger = GenericUtility.getLogger(FavouritesSearchQueryHandler.class.getName());
    
    private FavouritesSearchRequestVO favouritesSearchRequestVO;
    private FavouritesSearchResponseVO favouritesSearchResponseVO;
    
    private List<BaseResponseVO> alFavouritesSearchResultSet;
    private List<String> categories;
    private List<String> brands;
    private List<String> places;

    public void setQueryRequest(BaseRequestVO baseRequestVO) {

        double latitude, longitude;

        if (getQueryType().equals(SearchQueryTypes.Favourite_resultset.toString())) {
            favouritesSearchRequestVO = (FavouritesSearchRequestVO) baseRequestVO;

            /** Assigning the value of the lists to the instance variables **/
            categories = favouritesSearchRequestVO.getCategories();
            brands = favouritesSearchRequestVO.getBrands();
            places = favouritesSearchRequestVO.getPlaces();

            latitude = favouritesSearchRequestVO.getLatitude();
            longitude = favouritesSearchRequestVO.getLongitude();

            if (latitude == SearchEngineConstants.DOUBLE_NAN || longitude == SearchEngineConstants.DOUBLE_NAN) {
                logger.debug("Geo Coordinates not present or Invalid Geo Coordinates present in the request object for Favourites Search");
                throw new GeoCoordinatesNotFoundException("Geo Coordinates not present or Invalid Geo Coordinates present in the request object");
            }
        }
    }

    public void handleQuery() throws SearchEngineException {
        super.handleQuery();

        logger.debug("Querying The Promotion Index For Favourites");

        Iterator<String> iterPlaces = places.iterator();
        Iterator<String> iterBrands = brands.iterator();
        Iterator<String> iterCategories = categories.iterator();

        alFavouritesSearchResultSet = new ArrayList<BaseResponseVO>();

        FavouriteSearchManager favouriteSearchManager = new FavouriteSearchManager();

        String keyword;

        /** iterating over the places list for exact match results**/
        while (iterPlaces.hasNext()) {
            keyword = iterPlaces.next().toString();
            keyword = SearchUtility.formatKeyword(keyword);

            alFavouritesSearchResultSet.addAll(favouriteSearchManager.executePlace(keyword, favouritesSearchRequestVO));
        }

        /** iterating over the brands list for exact match results**/
        while (iterBrands.hasNext()) {
            keyword = iterBrands.next().toString();
            keyword = SearchUtility.formatKeyword(keyword);

            alFavouritesSearchResultSet.addAll(favouriteSearchManager.executeBrandCategory(keyword, favouritesSearchRequestVO));
        }

        /** iterating over the categories list for exact match results**/
        while (iterCategories.hasNext()) {
            keyword = iterCategories.next().toString();
            keyword = SearchUtility.formatKeyword(keyword);

            alFavouritesSearchResultSet.addAll(favouriteSearchManager.executeBrandCategory(keyword, favouritesSearchRequestVO));
        }

        /** reiterating over the brands list for standard match results**/
        iterBrands = brands.iterator();
        while (iterBrands.hasNext()) {
            keyword = iterBrands.next().toString();
            keyword = SearchUtility.formatKeyword(keyword);

            alFavouritesSearchResultSet.addAll(favouriteSearchManager.executeStandardMatchBrandCategory(keyword, favouritesSearchRequestVO));
        }

        /** reiterating over the categories list for standard match results**/
        iterCategories = categories.iterator();
        while (iterCategories.hasNext()) {
            keyword = iterCategories.next().toString();
            keyword = SearchUtility.formatKeyword(keyword);

            alFavouritesSearchResultSet.addAll(favouriteSearchManager.executeStandardMatchBrandCategory(keyword, favouritesSearchRequestVO));
        }

    }

    public BaseResponseVO getQueryResponse() {

        favouritesSearchResponseVO = new FavouritesSearchResponseVO();
        //Populate the response VO here

        return favouritesSearchResponseVO;

    }

    public List<BaseResponseVO> getQueryResponseResultSet() {

        //Collection list for listing search result set        
        //Populate the response VO and result set list here

        return alFavouritesSearchResultSet;

    }
}
