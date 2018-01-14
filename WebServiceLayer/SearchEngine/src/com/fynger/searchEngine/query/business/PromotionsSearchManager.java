package com.fynger.searchEngine.query.business;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.query.utilities.ValidityManager;
import com.fynger.searchEngine.requests.vo.PromotionsSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.PromotionsSearchResponseVO;

public class PromotionsSearchManager {

    public static LoggerManager logger = GenericUtility.getLogger(PromotionsSearchManager.class.getName());
    
    private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
    
    private Set<String> set = new HashSet<String>();
    
    private int totalResults;
    private int totalPages;
    
    private static final String searchEngineUrl;
    private static final String searchEngineFilter;
    private static final String searchEngineSort;
    private static final String searchEngineDistanceField;
    private List<BaseResponseVO> alPromotionsSearchResponse;

    static {
    	Properties prop = propertyManager.getProperties(SearchEngineConstants.SEARCHENGINE_PROPERTIES_FILE_NAME);
        searchEngineUrl = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_URL);
        searchEngineFilter = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_FILTER);
        searchEngineSort = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_SORT);
        searchEngineDistanceField = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_DISTANCE_FIELD);
    }

    public List<BaseResponseVO> execute(PromotionsSearchRequestVO promotionsSearchRequestVO) throws SearchEngineException {       

        int resultSetCount = promotionsSearchRequestVO.getResultSetCount();
        int counter = SearchEngineConstants.COUNTER_INITIALIZATION;
        int analyzer_iteration = SearchEngineConstants.ANALYZER_INITIALIZATION;
        int entityId;

        String promotype, latString, lngString, storeName, address, city, pinCode, promotion, validtill, state, stateCode, entityIdString, validity;
        String keyword = promotionsSearchRequestVO.getKeyword();

        double latitude, longitude;

        double userLat = promotionsSearchRequestVO.getLatitude();
        double userLng = promotionsSearchRequestVO.getLongitude();

        boolean validityStatus;

        PromotionsSearchResponseVO promotionsSearchResponseVO;
        BaseResponseVO baseResponseVO;
        ValidityManager validityManager;

        alPromotionsSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server with different analyzers*/
            for (; analyzer_iteration < SearchEngineConstants.ANALYZER_COUNT_PROMOTIONS; analyzer_iteration++) {

                if (analyzer_iteration == SearchEngineConstants.PROMOTEXT_EXACT_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.PROMOTEXT_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.PROMOTEXT_EXACT_MATCH + "]");

                } else if (analyzer_iteration == SearchEngineConstants.PROMOTEXT_STANDARD_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.PROMOTEXT_STANDARD_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.PROMOTEXT_STANDARD_MATCH + "]");

                } else if (analyzer_iteration == SearchEngineConstants.PROMOTEXT_NGRAM_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.PROMOTEXT_NGRAM_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.PROMOTEXT_NGRAM_MATCH + "]");

                } else if (analyzer_iteration == SearchEngineConstants.STORENAME_PROMO_EXACT_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.STORENAME_PROMO_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.STORENAME_PROMO_EXACT_MATCH + "]");

                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_PROMO_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_PROMO + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_PROMO + "]");

                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_PROMO_SYNONYM_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_PROMO_SYNONYM + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_PROMO_SYNONYM + "]");

                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_PROMO_NGRAM_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_PROMO_NGRAM_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_PROMO_NGRAM_MATCH + "]");
                }

                /**Setting the Search Parameters */
                query.addFilterQuery(searchEngineFilter);
                query.set(SearchEngineConstants.SFIELD, searchEngineDistanceField);
                query.set(SearchEngineConstants.POINTS, userLat + SearchEngineConstants.COMMA + userLng);
                query.set(SearchEngineConstants.DISTANCE, SearchEngineConstants.SEARCH_ENGINE_RANGE);
                query.set(SearchEngineConstants.SORT_TYPE, searchEngineSort);

                QueryResponse rsp = server.query(query);
                SolrDocumentList docs = rsp.getResults();
                Iterator<SolrDocument> solrDocumentIterator = docs.iterator();
                
                while (solrDocumentIterator.hasNext()) {
                	
                    SolrDocument solr_document = solrDocumentIterator.next();
                    
                        if (!set.contains(solr_document.toString())) {
                        	
                        	if (counter >= ((resultSetCount * SearchEngineConstants.PAGING_SIZE) - SearchEngineConstants.PAGING_SIZE) && counter < (resultSetCount * SearchEngineConstants.PAGING_SIZE)) {                        		                        		                        
                        	
                            stateCode = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE_PROMO).toString();
                            
                            if (stateCode.equals(SearchEngineConstants.ACTIVE_TYPE)) {
                            	
                                promotionsSearchResponseVO = new PromotionsSearchResponseVO();
                                validityManager = new ValidityManager();                                
                                baseResponseVO = new BaseResponseVO();
                                
                                /** populating the results in case of an active promotion **/                                
                                address = solr_document.getFieldValue(SearchEngineConstants.STRING_ADDRESS).toString();
                                city = solr_document.getFieldValue(SearchEngineConstants.STRING_CITY).toString();
                                state = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE).toString();
                                pinCode = solr_document.getFieldValue(SearchEngineConstants.STRING_PIN).toString();
                                promotion = solr_document.getFieldValue(SearchEngineConstants.STRING_PROMOTEXT).toString();
                                validtill = solr_document.getFieldValue(SearchEngineConstants.STRING_VALIDTILL).toString();

                                validtill = validtill.substring(1, (validtill.length() - 1));
                                validityStatus = validityManager.getValidityStatus(validtill);
                                /** Filtering the results with expired validity **/
                                if (validityStatus) {
                                	
                                    validity = validityManager.getValidity();

                                    promotion = promotion.substring(1, (promotion.length() - 1));
                                    
                                    address = address.substring(1, (address.length() - 1));
                                    pinCode = pinCode.substring(1, (pinCode.length() - 1));
                                    city = city.substring(1, city.length() - 1);
                                    state = state.substring(1, state.length() - 1);

                                    promotype = solr_document.getFieldValue(SearchEngineConstants.STRING_PROMOTYPE).toString();

                                    /** setting lat/lng in case of both events and promotions **/
                                    if (promotype.equals(SearchEngineConstants.PROMOTION_TYPE)) {

                                        latString = solr_document.getFieldValue(SearchEngineConstants.STRING_LATITUDE).toString();
                                        lngString = solr_document.getFieldValue(SearchEngineConstants.STRING_LONGITUDE).toString();
                                        storeName = solr_document.getFieldValue(SearchEngineConstants.STRING_STORENAME).toString();
                                        entityIdString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDPROMOTION).toString();

                                        latitude = Double.parseDouble(latString.substring(0, latString.toString().length()));
                                        longitude = Double.parseDouble(lngString.substring(0, lngString.length()));
                                        entityId = Integer.parseInt(entityIdString.substring(1, (entityIdString.length() - 1)));
                                        storeName = storeName.substring(1, (storeName.length() - 1));
                                        promotype = SearchEngineConstants.STRING_PROMOTION;

                                        /** setting the name of the store in case the result in not an event **/
                                        promotionsSearchResponseVO.setName(storeName);
                                        promotionsSearchResponseVO.setEntityId(entityId);

                                    } else if (promotype.equals(SearchEngineConstants.EVENT_TYPE)) {                                        

                                        latString = solr_document.getFieldValue(SearchEngineConstants.STRING_LATITUDE).toString();
                                        lngString = solr_document.getFieldValue(SearchEngineConstants.STRING_LONGITUDE).toString();
                                        entityIdString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDEVENT).toString();
                                                                                
                                        latitude = Double.parseDouble(latString.substring(0, latString.toString().length()));
                                        longitude = Double.parseDouble(lngString.substring(0, lngString.length()));
                                        entityId = Integer.parseInt(entityIdString.substring(1, (entityIdString.length() - 1)));
                                        
                                        promotype = SearchEngineConstants.STRING_EVENT;
                                        promotionsSearchResponseVO.setEntityId(entityId);                                        
                                        

                                    } else {                                        
                                        logger.error(" Promotion Type Could Not Be Determined " );
                                        break;
                                    }

                                    //TODO: Promotions in case of cabs	                         
                                    promotionsSearchResponseVO.setAddress(address);
                                    promotionsSearchResponseVO.setCity(city);
                                    promotionsSearchResponseVO.setState(state);
                                    promotionsSearchResponseVO.setPinCode(pinCode);
                                    promotionsSearchResponseVO.setLatitude(latitude);
                                    promotionsSearchResponseVO.setLongitude(longitude);                                    
                                    promotionsSearchResponseVO.setPromotionText(promotion);
                                    promotionsSearchResponseVO.setPromotionType(promotype);
                                    promotionsSearchResponseVO.setValidityPeriod(validity);                                    

                                    baseResponseVO = (BaseResponseVO) promotionsSearchResponseVO;
                                    this.alPromotionsSearchResponse.add(baseResponseVO);
                                   
                                }
                            } else if (stateCode.equals(SearchEngineConstants.INACTIVE_TYPE)) {
                            } else {
                                throw new SearchEngineException("Promotion State Could Not Be Determined");
                            }
                        }
                        	set.add(solr_document.toString());
                    }// else if (counter >= resultSetCount * SearchEngineConstants.PAGING_SIZE) {
                       // break;
                   // }
                    counter++;
                }
                
            }

        } catch (SolrServerException ssEx) {
            logger.error(" Sole Server Exception occured in Querying the server : " + ssEx.getMessage());
            throw new SearchEngineException("Sole Server Exception occured in Querying the server  ", ssEx);
        } catch (MalformedURLException me) {
            logger.error(" Malformed URL Exception occured in Querying the server : " + me.getMessage());
            throw new SearchEngineException("Malformed URL Exception occured in Querying the server  ", me);
        } catch (Exception ex) {
            logger.error(" Exception occured in Querying the server : " + ex.getMessage());
            throw new SearchEngineException("Exception occured in Querying the server  ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Querying the server : " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Querying the server ", th);
        }
        return alPromotionsSearchResponse;

    }
    
    public int getTotalResults() {

    	totalResults = set.size();    	
        return totalResults;
    }

    public int getTotalPages() {

        totalPages = (int) (totalResults / SearchEngineConstants.PAGING_SIZE);

        if (!(totalResults % SearchEngineConstants.PAGING_SIZE == SearchEngineConstants.INT_ZERO)) {
            totalPages++;
        }

        return totalPages;
    }
    
}
