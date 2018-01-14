package com.fynger.searchEngine.query.handlers;

import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.SearchEngineException;


public abstract class AbstractSearchQueryHandler implements SearchQueryHandler{

    private SearchQueryTypes searchQueryType;
    
    private int totalResults;
    private int totalPages;
    
    public void setQueryType(SearchQueryTypes searchQueryType){
        this.searchQueryType = searchQueryType;
    }

    public void handleQuery() throws SearchEngineException{
        
        //Common code for all search types
    }

    public String getQueryType(){
        return searchQueryType.toString();
    }

	/**
	 * @return the searchQueryType
	 */
	public SearchQueryTypes getSearchQueryType() {
		return searchQueryType;
	}

	/**
	 * @param searchQueryType the searchQueryType to set
	 */
	public void setSearchQueryType(SearchQueryTypes searchQueryType) {
		this.searchQueryType = searchQueryType;
	}

	/**
	 * @return the totalResults
	 */
	public int getTotalResults() {
		return totalResults;
	}

	/**
	 * @param totalResults the totalResults to set
	 */
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

}
