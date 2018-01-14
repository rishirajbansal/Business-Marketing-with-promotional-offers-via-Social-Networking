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
import com.fynger.searchEngine.requests.vo.FlashDealSearchRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;
import com.fynger.searchEngine.responses.vo.FlashDealSearchResponseVO;


public class FlashDealSearchManager {

    public static Logger logger = GenericUtility.getLogger(FlashDealSearchManager.class.getName());
    private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
    
    /**
    private int totalResults;
    private int totalPages;
    **/
    private Set<String> set = new HashSet<String>();
    
    private static final String searchEngineUrl;
    private static final String searchEngineFilter;
    private static final String searchEngineDistanceField;
    private List<BaseResponseVO> alFlashDealSearchResponse;

    static {
        	 Properties prop = propertyManager.getProperties(SearchEngineConstants.SEARCHENGINE_PROPERTIES_FILE_NAME);
             searchEngineUrl = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_URL);
        	 searchEngineFilter = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_FILTER);        	 
        	 searchEngineDistanceField = prop.getProperty(SearchEngineConstants.SEARCH_ENGINE_DISTANCE_FIELD);
    }

    public List<BaseResponseVO> execute(FlashDealSearchRequestVO flashDealSearchRequestVO) throws SearchEngineException {

    	/**
        int resultSetCount = flashDealSearchRequestVO.getResultSetCount();
        int counter = SearchEngineConstants.COUNTER_INITIALIZATION;
        **/
        int analyzer_iteration = SearchEngineConstants.ANALYZER_INITIALIZATION;

        int mregid, idbasicstore;

        double Lat = flashDealSearchRequestVO.getLatitude();
        double Lng = flashDealSearchRequestVO.getLongitude();

        String keyword = flashDealSearchRequestVO.getKeyword();        
        String mregidString, idbasicstoreString;
        String locationString = flashDealSearchRequestVO.getLocation();

        FlashDealSearchResponseVO flashDealSearchResponseVO;
        BaseResponseVO baseResponseVO;

        alFlashDealSearchResponse = new ArrayList<BaseResponseVO>();

        try {
            SolrServer server = new CommonsHttpSolrServer(searchEngineUrl);
            SolrQuery query = new SolrQuery();

            /** Querying the server with different analyzers*/
            for (; analyzer_iteration < SearchEngineConstants.ANALYZER_COUNT_FLASH_DEAL; analyzer_iteration++) {

                if (analyzer_iteration == SearchEngineConstants.NAME_FLASH_DEAL_EXACT_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.NAME_FLASH_DEAL_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.NAME_FLASH_DEAL_EXACT_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_FLASH_DEAL_EXACT_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_FLASH_DEAL_EXACT_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_FLASH_DEAL_EXACT_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_FLASH_DEAL_STANDARD_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_FLASH_DEAL_STANDARD_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_FLASH_DEAL_STANDARD_MATCH + "]");
                } else if (analyzer_iteration == SearchEngineConstants.CATEGORIES_FLASH_DEAL_SYNONYM_MATCH_ITERATION) {
                    query.setQuery(SearchEngineConstants.CATEGORIES_FLASH_DEAL_SYNONYM_MATCH + keyword);
                    logger.debug("Configuring the server for the analyzer [" + SearchEngineConstants.CATEGORIES_FLASH_DEAL_SYNONYM_MATCH + "]");
                }

                /**Setting the Search Parameters */
                query.addFilterQuery(searchEngineFilter);
                query.set(SearchEngineConstants.SFIELD, searchEngineDistanceField);
                query.set(SearchEngineConstants.POINTS, Lat + SearchEngineConstants.COMMA + Lng);
                query.set(SearchEngineConstants.DISTANCE, SearchEngineConstants.SEARCH_ENGINE_RANGE);


                QueryResponse rsp = server.query(query);
                SolrDocumentList docs = rsp.getResults();
                Iterator<SolrDocument> solrDocumentIterator = docs.iterator();

                while (solrDocumentIterator.hasNext()) {

                    SolrDocument solr_document = solrDocumentIterator.next();

                    if (!set.contains(solr_document.toString())) {

                        set.add(solr_document.toString());
                        /**
                        if (counter >= ((resultSetCount * SearchEngineConstants.PAGING_SIZE) - SearchEngineConstants.PAGING_SIZE) && counter < (resultSetCount * SearchEngineConstants.PAGING_SIZE)) {
						**/
                            /** Populating the results from the search engine **/
                            mregidString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDMREG).toString();
                            idbasicstoreString = solr_document.getFieldValue(SearchEngineConstants.STRING_IDBASICSTORE).toString();

                            /** Parsing the populated results **/
                            mregid = Integer.parseInt(mregidString.substring(1, mregidString.length() - 1));
                            idbasicstore = Integer.parseInt(idbasicstoreString.substring(1, idbasicstoreString.length() - 1));

                            flashDealSearchResponseVO = new FlashDealSearchResponseVO();
                            baseResponseVO = new BaseResponseVO();

                            flashDealSearchResponseVO.setMerchantId(mregid);
                            flashDealSearchResponseVO.setStoreId(idbasicstore);
                            flashDealSearchResponseVO.setLocationString(locationString);

                            baseResponseVO = (BaseResponseVO) flashDealSearchResponseVO;
                            alFlashDealSearchResponse.add(baseResponseVO);

                    /**    
                     *   }
                     
                        counter++;
                    */
                    }
                }
            }

        } catch (SolrServerException ssEx) {
            logger.error(" Solr Server Exception occured in Querying the server for a Flash Deal Search: " + ssEx.getMessage());
            throw new SearchEngineException("Solr Server Exception occured in Querying the server for a Flash Deal Search  ", ssEx);
        } catch (MalformedURLException me) {
            logger.error(" Malformed URL Exception occured in Querying the server for a Flash Deal Search : " + me.getMessage());
            throw new SearchEngineException("Malformed URL Exception occured in Querying the server for a Flash Deal Search  ", me);
        } catch (Exception ex) {
            logger.error(" Exception occured in Querying the server for a Flash Deal Search: " + ex.getMessage());
            throw new SearchEngineException("Exception occured in Querying the server for a Flash Deal Search ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Querying the server for a Flash Deal Search: " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Querying the server for a Flash Deal Search", th);
        }

        return alFlashDealSearchResponse;
    }
    
    /**
	 * @return the Total Number of Results
	 */
    /**
    public int getTotalResults() {

        totalResults = set.size();
        return totalResults;
    }
    **/
    /**
	 * @return the Total Number of Results
	 */
    /**
    public int getTotalPages() {

        totalPages = (int) (totalResults / SearchEngineConstants.PAGING_SIZE);

        if (!(totalResults % SearchEngineConstants.PAGING_SIZE == SearchEngineConstants.INT_ZERO)) {
            totalPages++;
        }

        return totalPages;
    }
    **/
}
