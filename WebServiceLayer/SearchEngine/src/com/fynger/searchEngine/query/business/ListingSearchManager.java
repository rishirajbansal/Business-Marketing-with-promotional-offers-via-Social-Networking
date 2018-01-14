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
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.requests.vo.ListingSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.ListingSearchResponseVO;

public class ListingSearchManager {
    
    public static Logger logger = GenericUtility.getLogger(ListingSearchManager.class.getName());
    private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
    
    private int totalResults;
    private int totalPages;

    private Set<String> set = new HashSet<String>();
    
    private static final String searchEngineUrl;
    private static final String searchEngineFilter;
    private static final String searchEngineSort;
    private static final String searchEngineDistanceField;
    private List<BaseResponseVO> alListingSearchResponse;

    static {
        	 Properties prop = propertyManager.getProperties(SearchEngineConstants.SEARCHENGINE_PROPERTIES_FILE_NAME);
             searchEngineUrl = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_URL);
        	 searchEngineFilter = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_FILTER);
        	 searchEngineSort = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_SORT);
        	 searchEngineDistanceField = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_DISTANCE_FIELD);
    }

    public List<BaseResponseVO> execute(ListingSearchRequestVO listingSearchRequestVO) throws SearchEngineException {

        int resultSetCount = listingSearchRequestVO.getResultSetCount();
        int counter = SearchEngineConstants.COUNTER_INITIALIZATION;
        int analyzer_iteration = SearchEngineConstants.ANALYZER_INITIALIZATION;
        int idlisting;        

        String keyword = listingSearchRequestVO.getKeyword();
        String name, address, phone, categories, latString, lngString, city, mobile, state, idlistingString, pinCode;

        double storeLat, storeLng;

        double userLat = listingSearchRequestVO.getLatitude();
        double userLng = listingSearchRequestVO.getLongitude();

        ListingSearchResponseVO listingSearchResponseVO;
        BaseResponseVO baseResponseVO;

        alListingSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server with different analyzers*/
            for (; analyzer_iteration < SearchEngineConstants.ANALYZER_COUNT_LISTING; analyzer_iteration++) {

                if (analyzer_iteration == SearchEngineConstants.NAME_EXACT_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.NAME_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.NAME_EXACT_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.NAME_STANDARD_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.NAME_STANDARD_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.NAME_STANDARD_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_STANDARD_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_STANDARD_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_STANDARD_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.NAME_NGARM_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.NAME_NGRAM_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.NAME_NGRAM_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_SYNONYM_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_SYNONYM_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_SYNONYM_MATCH + "]");
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
                    	
                        set.add(solr_document.toString());
                        
                        if (counter >= ((resultSetCount * SearchEngineConstants.PAGING_SIZE) - SearchEngineConstants.PAGING_SIZE) && counter < (resultSetCount * SearchEngineConstants.PAGING_SIZE)) {
                        	
                            name = solr_document.getFieldValue(SearchEngineConstants.STRING_NAME).toString();
                            address = solr_document.getFieldValue(SearchEngineConstants.STRING_ADDRESS).toString();
                            phone = solr_document.getFieldValue(SearchEngineConstants.STRING_PHONE).toString();
                            mobile = solr_document.getFieldValue(SearchEngineConstants.STRING_MOBILE).toString();
                            categories = solr_document.getFieldValue(SearchEngineConstants.STRING_CATEGORIES).toString();
                            latString = solr_document.getFieldValue(SearchEngineConstants.STRING_LATITUDE).toString();
                            lngString = solr_document.getFieldValue(SearchEngineConstants.STRING_LONGITUDE).toString();
                            city = solr_document.getFieldValue(SearchEngineConstants.STRING_CITY).toString();
                            state = solr_document.getFieldValue(SearchEngineConstants.STRING_STATE).toString();
                            idlistingString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDLISTING).toString();
                            pinCode = solr_document.getFieldValue(SearchEngineConstants.STRING_PIN).toString();

                            /** Parsing Results */
                            city = city.substring(1, city.length() - 1);
                            name = name.substring(1, (name.length() - 1));
                            address = address.substring(1, (address.length() - 1));
                            state = state.substring(1, (state.length() - 1));
                            phone = phone.substring(1, (phone.length() - 1));
                            mobile = mobile.substring(1, (mobile.length() - 1));
                            pinCode = pinCode.substring(1, (pinCode.length() - 1));
                            categories = categories.substring(1, (categories.length() - 1));
                            storeLat = Double.parseDouble(latString.substring(0, latString.toString().length()));
                            storeLng = Double.parseDouble(lngString.substring(0, lngString.length()));
                            idlisting = Integer.parseInt(idlistingString.substring(1, (idlistingString.length() - 1)));


                            listingSearchResponseVO = new ListingSearchResponseVO();
                            baseResponseVO = new BaseResponseVO();

                            listingSearchResponseVO.setName(name);
                            listingSearchResponseVO.setAddress(address);
                            listingSearchResponseVO.setCity(city);
                            listingSearchResponseVO.setMobile(mobile);
                            listingSearchResponseVO.setState(state);
                            listingSearchResponseVO.setPhone(phone);
                            listingSearchResponseVO.setCategories(categories);
                            listingSearchResponseVO.setLatitude(storeLat);
                            listingSearchResponseVO.setLongitude(storeLng);
                            listingSearchResponseVO.setListingId(idlisting);
                            listingSearchResponseVO.setPinCode(pinCode);


                            baseResponseVO = (BaseResponseVO) listingSearchResponseVO;
                            alListingSearchResponse.add(baseResponseVO);

                        }
                        counter++;
                    }
     
                }

            }
            
        } catch (SolrServerException ssEx) {
            logger.error(" Sole Server Exception occured in Querying the server : " + ssEx.getMessage());
            throw new SearchEngineException("Solr Server Exception occured in Querying the server  ", ssEx);
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

        return alListingSearchResponse;

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
