package com.fynger.searchEngine.query.business;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.requests.vo.FavouritesSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FavouritesSearchResponseVO;

public class FavouriteSearchManager {

    public static Logger logger = GenericUtility.getLogger(FavouriteSearchManager.class.getName());    
    private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
    
    private Set<String> set = new HashSet<String>();
    private static final String searchEngineUrl;
    private static final String searchEngineFilter;
    private static final String searchEngineSort;
    private static final String searchEngineDistanceField;
    
    private List<BaseResponseVO> alFavouritesSearchResponse;

    static {
        Properties prop = propertyManager.getProperties(SearchEngineConstants.SEARCHENGINE_PROPERTIES_FILE_NAME);
        searchEngineUrl = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_URL);
        searchEngineFilter = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_FILTER);  
        searchEngineSort = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_SORT);
        searchEngineDistanceField = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_DISTANCE_FIELD);
    }
    
    /** to carry out an exact match search in the places list **/
    public List<BaseResponseVO> executePlace(String keyword, FavouritesSearchRequestVO favouritesSearchRequestVO) throws SearchEngineException {

        int promotionId;

        double Lat = favouritesSearchRequestVO.getLatitude();
        double Lng = favouritesSearchRequestVO.getLongitude();

        String promotionIdString, stateCode;

        FavouritesSearchResponseVO favouritesSearchResponseVO;
        BaseResponseVO baseResponseVO;

        alFavouritesSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server for the exact match of Places */
            query.setQuery(SearchEngineConstants.NAME_FAVOURITES_EXACT_MATCH + keyword);

            query.addFilterQuery(searchEngineFilter);
            query.set(SearchEngineConstants.SFIELD, searchEngineDistanceField);
            query.set(SearchEngineConstants.POINTS, Lat + SearchEngineConstants.COMMA + Lng);
            query.set(SearchEngineConstants.DISTANCE, SearchEngineConstants.SEARCH_ENGINE_RANGE);
            query.set(SearchEngineConstants.SORT_TYPE, searchEngineSort);

            QueryResponse rsp = server.query(query);
            SolrDocumentList docs = rsp.getResults();
            Iterator<SolrDocument> solrDocumentIterator = docs.iterator();

            while (solrDocumentIterator.hasNext()) {
            	
                SolrDocument solr_document = solrDocumentIterator.next();
                
                if (!set.contains(solr_document.toString())) {
                    set.add(solr_document.toString());

                    stateCode = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE_PROMO).toString();

                    /** filtering out inactive promotions **/
                    if (stateCode.equals(SearchEngineConstants.ACTIVE_TYPE)) {

                        favouritesSearchResponseVO = new FavouritesSearchResponseVO();

                        promotionIdString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDPROMOTION).toString();
                        promotionId = Integer.parseInt(promotionIdString.substring(1, (promotionIdString.length() - 1)));

                        favouritesSearchResponseVO.setPromotionId(promotionId);

                        baseResponseVO = (BaseResponseVO) favouritesSearchResponseVO;
                        this.alFavouritesSearchResponse.add(baseResponseVO);
                    }

                }

            }

        } catch (SolrServerException ssEx) {
            logger.error(" Solr Server Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ssEx.getMessage());
            throw new SearchEngineException("Solr Server Exception occured in Querying the server for Favourites Search  ", ssEx);
        } catch (MalformedURLException me) {
            logger.error(" Malformed URL Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search : " + me.getMessage());
            throw new SearchEngineException("Malformed URL Exception occured in Querying the server for Favourites Search  ", me);
        } catch (Exception ex) {
            logger.error(" Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ex.getMessage());
            throw new SearchEngineException("Exception occured in Querying the server for Favourites Search ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Querying the server for Favourites Search", th);
        }

        return alFavouritesSearchResponse;

    }
    /** to carry out a promotion text exact and standard match and a categories exact match in the brands and the categories
     *  list **/
    public List<BaseResponseVO> executeBrandCategory(String keyword, FavouritesSearchRequestVO favouritesSearchRequestVO) throws SearchEngineException {

        int promotionId;
        int analyzer_iteration = SearchEngineConstants.ANALYZER_INITIALIZATION;
        double Lat = favouritesSearchRequestVO.getLatitude();
        double Lng = favouritesSearchRequestVO.getLongitude();

        String promotionIdString, stateCode;

        FavouritesSearchResponseVO favouritesSearchResponseVO;
        BaseResponseVO baseResponseVO;

        alFavouritesSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server for brands categories */
            for (; analyzer_iteration < SearchEngineConstants.ANALYZER_COUNT_FAVOURITES; analyzer_iteration++) {

                if (analyzer_iteration == SearchEngineConstants.PROMOTEXT_EXACT_MATCH_PLCAES_FAVOURITES_ITERATION) {
                    query.setQuery(SearchEngineConstants.PROMOTEXT_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.PROMOTEXT_EXACT_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.PROMOTEXT_STANDARD_MATCH_PLACES_FAVOURITES_ITERATION) {
                    query.setQuery(SearchEngineConstants.PROMOTEXT_STANDARD_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.PROMOTEXT_STANDARD_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_EXACT_MATCH_PLACES_FAVOURITES_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_EXACT_MATCH_PLACES_FAVOURITES + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_EXACT_MATCH_PLACES_FAVOURITES + "]");
                }


                query.addFilterQuery(searchEngineFilter);
                query.set(SearchEngineConstants.SFIELD, searchEngineDistanceField);
                query.set(SearchEngineConstants.POINTS, Lat + SearchEngineConstants.COMMA + Lng);
                query.set(SearchEngineConstants.DISTANCE, SearchEngineConstants.SEARCH_ENGINE_RANGE);
                query.set(SearchEngineConstants.SORT_TYPE, searchEngineSort);

                QueryResponse rsp = server.query(query);
                SolrDocumentList docs = rsp.getResults();
                Iterator<SolrDocument> solrDocumentIterator = docs.iterator();

                while (solrDocumentIterator.hasNext()) {

                    SolrDocument solr_document = solrDocumentIterator.next();

                    if (!set.contains(solr_document.toString())) {                        
                        set.add(solr_document.toString());

                        stateCode = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE_PROMO).toString();
                        
                        /** filtering out the inactive promotions **/
                        if (stateCode.equals(SearchEngineConstants.ACTIVE_TYPE)) {
                            favouritesSearchResponseVO = new FavouritesSearchResponseVO();
                            promotionIdString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDPROMOTION).toString();
                            promotionId = Integer.parseInt(promotionIdString.substring(1, (promotionIdString.length() - 1)));

                            favouritesSearchResponseVO.setPromotionId(promotionId);

                            baseResponseVO = (BaseResponseVO) favouritesSearchResponseVO;
                            this.alFavouritesSearchResponse.add(baseResponseVO);
                        }

                    }

                }
            }
        } catch (SolrServerException ssEx) {
            logger.error(" Solr Server Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ssEx.getMessage());
            throw new SearchEngineException("Solr Server Exception occured in Querying the server for Favourites Search  ", ssEx);
        } catch (MalformedURLException me) {
            logger.error(" Malformed URL Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search : " + me.getMessage());
            throw new SearchEngineException("Malformed URL Exception occured in Querying the server for Favourites Search  ", me);
        } catch (Exception ex) {
            logger.error(" Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ex.getMessage());
            throw new SearchEngineException("Exception occured in Querying the server for Favourites Search ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Querying the server for Favourites Search", th);
        }
              
        return alFavouritesSearchResponse;
    }
    /** to carry out a standard match search in the categories and brand list **/
    public List<BaseResponseVO> executeStandardMatchBrandCategory(String keyword, FavouritesSearchRequestVO favouritesSearchRequestVO) throws SearchEngineException {

        int promotionId;

        double Lat = favouritesSearchRequestVO.getLatitude();
        double Lng = favouritesSearchRequestVO.getLongitude();

        String promotionIdString, stateCode;

        FavouritesSearchResponseVO favouritesSearchResponseVO;
        BaseResponseVO baseResponseVO;

        alFavouritesSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server for brands and categories */
            query.setQuery(SearchEngineConstants.CATEGORIES_PROMO + keyword);

            query.addFilterQuery(searchEngineFilter);
            query.set(SearchEngineConstants.SFIELD, searchEngineDistanceField);
            query.set(SearchEngineConstants.POINTS, Lat + SearchEngineConstants.COMMA + Lng);
            query.set(SearchEngineConstants.DISTANCE, SearchEngineConstants.SEARCH_ENGINE_RANGE);
            query.set(SearchEngineConstants.SORT_TYPE, searchEngineSort);

            QueryResponse rsp = server.query(query);
            SolrDocumentList docs = rsp.getResults();
            Iterator<SolrDocument> solrDocumentIterator = docs.iterator();

            while (solrDocumentIterator.hasNext()) {

                SolrDocument solr_document = solrDocumentIterator.next();

                if (!set.contains(solr_document.toString())) {

                    set.add(solr_document.toString());                    
                    stateCode = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE_PROMO).toString();                    
                    if (stateCode.equals(SearchEngineConstants.ACTIVE_TYPE)) {

                        favouritesSearchResponseVO = new FavouritesSearchResponseVO();
                        promotionIdString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDPROMOTION).toString();
                        promotionId = Integer.parseInt(promotionIdString.substring(1, (promotionIdString.length() - 1)));

                        favouritesSearchResponseVO.setPromotionId(promotionId);

                        baseResponseVO = (BaseResponseVO) favouritesSearchResponseVO;
                        this.alFavouritesSearchResponse.add(baseResponseVO);
                    }

                }

            }

        } catch (SolrServerException ssEx) {
            logger.error(" Solr Server Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ssEx.getMessage());
            throw new SearchEngineException("Solr Server Exception occured in Querying the server for Favourites Search  ", ssEx);
        } catch (MalformedURLException me) {
            logger.error(" Malformed URL Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search : " + me.getMessage());
            throw new SearchEngineException("Malformed URL Exception occured in Querying the server for Favourites Search  ", me);
        } catch (Exception ex) {
            logger.error(" Exception occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + ex.getMessage());
            throw new SearchEngineException("Exception occured in Querying the server for Favourites Search ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Querying the server for query : '" + keyword + "' in Favourites Search: " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Querying the server for Favourites Search", th);
        }

        return alFavouritesSearchResponse;

    }
}