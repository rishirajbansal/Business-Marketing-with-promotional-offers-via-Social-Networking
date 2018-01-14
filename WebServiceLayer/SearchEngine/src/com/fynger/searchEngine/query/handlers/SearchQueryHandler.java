
package com.fynger.searchEngine.query.handlers;

import java.util.List;

import com.fynger.searchEngine.query.enums.SearchQueryTypes;
import com.fynger.searchEngine.query.exception.SearchEngineException;
import com.fynger.searchEngine.requests.vo.BaseRequestVO;
import com.fynger.searchEngine.responses.vo.BaseResponseVO;


public interface SearchQueryHandler {

    public void setQueryRequest(BaseRequestVO baseRequestVO);

    public void setQueryType(SearchQueryTypes searchQueryType);

    public void handleQuery() throws SearchEngineException;

    public BaseResponseVO getQueryResponse();

    public List<BaseResponseVO> getQueryResponseResultSet();
    
    public int getTotalPages();
    
    public int getTotalResults();

}
