/**
 * 
 */
package com.fynger.servicesBusiness.integration.searchEngine.facade;

import com.fynger.searchEngine.query.handlers.FavouritesSearchQueryHandler;
import com.fynger.searchEngine.query.handlers.FlashDealSearchQueryHandler;
import com.fynger.searchEngine.query.handlers.ListingSearchQueryHandler;
import com.fynger.searchEngine.query.handlers.PromotionsSearchQueryHandler;
import com.fynger.searchEngine.query.handlers.SearchQueryHandler;

/**
 * @author Rishi
 *
 */
public class SearchFacade {
	
	public static SearchQueryHandler getListingSearchHandler(){		
		return new ListingSearchQueryHandler();
	}
	
	public static SearchQueryHandler getPromotionSearchHandler(){		
		return new PromotionsSearchQueryHandler();
	}
	
	public static SearchQueryHandler getFlashDealSearchHandler(){		
		return new FlashDealSearchQueryHandler();
	}
	
	public static SearchQueryHandler getFavouritesSearchHandler(){		
		return new FavouritesSearchQueryHandler();
	}

}
