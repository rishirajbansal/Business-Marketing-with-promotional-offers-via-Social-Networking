/**
 * 
 */
package com.fynger.searchEngine.query.constants;


public class SearchEngineConstants {
	
	public static final String GEOCODER_REQUEST_PREFIX_FOR_XML					= "http://maps.google.com/maps/api/geocode/xml";
	public static final String ADDRESS_PREFIX								    = "?address=";
	public static final String LATLNG_PREFIX								    = "?latlng=";
	public static final String SENSOR_FALSE  									= "&sensor=false";
	public static final String SENSOR_TRUE  									= "&sensor=true";	
	public static final String TEXT_ENCODING    								= "UTF-8";
	public static final String GEOCODE_RESPONSE_LOCATION						= "/GeocodeResponse/result[1]/geometry/location/*";	
	public static final String LAT						                        = "lat";
	public static final String LNG						                        = "lng";
	public static final String COMMA						                    = ",";
	public static final double NaN                                              =  Float.NaN;
	public static final int PAGING_SIZE                                         =  10;
	public static final int ANALYZER_INITIALIZATION                             =  0;
	public static final int COUNTER_INITIALIZATION                              =  0;
	public static final double DOUBLE_NAN                                       =  0.0;
	public static final String STRING_NULL                                      =   null ;
	
	/*
	 * Search Engine configuration constants
	 */	
	public static final String SEARCHENGINE_PROPERTIES_FILE_NAME 				= "searchEngine.properties";
																				   
	
	public static final String SEARCH_ENGINE_URL                                =  "searchEngineUrl";
	public static final String SEARCH_ENGINE_URL_TEST                           =  "searchEngineUrl_test";
	public static final String SEARCH_ENGINE_FILTER                             =  "searchEngineFilter";
	public static final String SEARCH_ENGINE_SORT                               =  "searchEngineSort";
	public static final String SEARCH_ENGINE_DISTANCE_FIELD                     =  "searchEngineDistanceField";	
	public static final int SEARCH_ENGINE_RANGE                                 =  5;
	public static final int ANALYZER_COUNT_LISTING                              =  5;
	public static final int ANALYZER_COUNT_PROMOTIONS                           =  7;
	public static final int ANALYZER_COUNT_FLASH_DEAL                           =  4;
	public static final int ANALYZER_COUNT_FAVOURITES                           =  3;
	public static final int RADUIS_EARTH                                        =  6371;	
	public static final int NAME_EXACT_MATCH_ITERATION                          =  0;
	public static final int NAME_STANDARD_MATCH_ITERATION                       =  1;
	public static final int CATEGORIES_STANDARD_MATCH_ITERATION                 =  2;
	public static final int NAME_NGARM_MATCH_ITERATION                          =  3;
	public static final int CATEGORIES_SYNONYM_ITERATION                        =  4;
	
	public static final int PROMOTEXT_EXACT_MATCH_ITERATION                     =  0;
	public static final int PROMOTEXT_STANDARD_MATCH_ITERATION                  =  1;
	public static final int PROMOTEXT_NGRAM_MATCH_ITERATION                     =  2;
	public static final int STORENAME_PROMO_EXACT_MATCH_ITERATION               =  3;
	public static final int CATEGORIES_PROMO_ITERATION                          =  4;
	public static final int CATEGORIES_PROMO_SYNONYM_ITERATION                  =  5;
	public static final int CATEGORIES_PROMO_NGRAM_MATCH_ITERATION              =  6;

	public static final int PROMOTEXT_EXACT_MATCH_PLCAES_FAVOURITES_ITERATION   =  0;
	public static final int PROMOTEXT_STANDARD_MATCH_PLACES_FAVOURITES_ITERATION=  1;
	public static final int CATEGORIES_EXACT_MATCH_PLACES_FAVOURITES_ITERATION  =  2;	
	
	public static final int NAME_FLASH_DEAL_EXACT_MATCH_ITERATION               =  0;
	public static final int CATEGORIES_FLASH_DEAL_EXACT_MATCH_ITERATION         =  1;
	public static final int CATEGORIES_FLASH_DEAL_STANDARD_MATCH_ITERATION      =  2;
	public static final int CATEGORIES_FLASH_DEAL_SYNONYM_MATCH_ITERATION       =  3;
		
	public static final String NAME_EXACT_MATCH                                 =  "Name_exact_match:";
	public static final String NAME_STANDARD_MATCH                              =  "Name:";
	public static final String NAME_NGRAM_MATCH                                 =  "Name_ngram_match:";
	public static final String CATEGORIES_STANDARD_MATCH                        =  "Categories:";
	public static final String CATEGORIES_SYNONYM_MATCH                         =  "Categories_synonym:";
	public static final String PROMOTEXT_EXACT_MATCH                            =  "promotext_exact_match:";
	public static final String PROMOTEXT_STANDARD_MATCH                         =  "promotext:";
	public static final String PROMOTEXT_NGRAM_MATCH                            =  "promotext_ngram_match:";
	public static final String STORENAME_PROMO_EXACT_MATCH                      =  "storeName_promo_exact_match:";
	public static final String CATEGORIES_PROMO                                 =  "categories_promo:";
	public static final String CATEGORIES_PROMO_SYNONYM                         =  "categories_promo_synonym:";
	public static final String CATEGORIES_PROMO_NGRAM_MATCH                     =  "categories_promo_ngram_match:";
	public static final String NAME_FLASH_DEAL_EXACT_MATCH                      =  "name_flashDeal_exact_match:";
	public static final String CATEGORIES_FLASH_DEAL_STANDARD_MATCH             =  "categories_flashDeal:";
	public static final String CATEGORIES_FLASH_DEAL_EXACT_MATCH                =  "categories_flashDeal_exact_match:";
	public static final String CATEGORIES_FLASH_DEAL_SYNONYM_MATCH              =  "categories_flashDeal_synonym_match:";
	public static final String NAME_FAVOURITES_EXACT_MATCH                      =  "storeName_promo_exact_match:";
	public static final String CATEGORIES_EXACT_MATCH_PLACES_FAVOURITES         =  "categories_promo_exact_match:";
	public static final String SFIELD                                           =  "sfield";
	public static final String POINTS                                           =  "pt";
	public static final String DISTANCE                                         =  "d";
	public static final String SORT_TYPE                                        =  "sort";
	public static final String PROMOTION_TYPE                                   =  "[0]";
	public static final String EVENT_TYPE                                       =  "[1]";
	public static final String STRING_PROMOTION                                 =  "Promotion";
	public static final String STRING_EVENT                                     =  "Event";
	public static final String ACTIVE_TYPE                                      =  "[1]";
	public static final String INACTIVE_TYPE                                    =  "[0]";
	
	/**Search Engine Fields*/
	public static final String STRING_NAME                                      =  "Name";
	public static final String STRING_ADDRESS                                   =  "Address";
	public static final String STRING_PHONE                                     =  "Phone";
	public static final String STRING_MOBILE                                    =  "Mobile";
	public static final String STRING_CATEGORIES                                =  "Categories";
	public static final String STRING_LATITUDE                                  =  "location_0_coordinate";
	public static final String STRING_LONGITUDE                                 =  "location_1_coordinate";
	public static final String STRING_CITY                                      =  "City";
	public static final String STRING_STATE                                     =  "State";
	public static final String STRING_IDLISTING                                 =  "idlisting";
	public static final String STRING_PIN                                       =  "Pin";
	public static final String STRING_STORENAME                                 =  "storeName_promo";
	public static final String STRING_PROMOTEXT                                 =  "promotext";
	public static final String STRING_PROMOTYPE                                 =  "promotype";
	public static final String STRING_STARTSAT                                  =  "startsat_promo";
	public static final String STRING_VALIDTILL                                 =  "validtill_promo";
	public static final String STRING_FORMATTED_ADDRESS                         =  "formatted_address";
	public static final String STRING_STATE_PROMO                               =  "state_promo";
	public static final String STRING_IDPROMOTION                               =  "idpromotion" ;
	public static final String STRING_IDEVENT                                   =  "ideventtab" ;
	public static final String STRING_IDMREG                                    =  "idmreg";
	public static final String STRING_IDBASICSTORE                              =  "idbasicstore" ;
	public static final String STRING_DAYS_AND                                  =  " Days and " ;
	public static final String STRING_DAY_AND                                   =  " Day and " ;
	public static final String STRING_HOURS                                     =  " Hours " ;
	public static final String STRING_HOUR                                      =  " Hour " ;
	public static final String STRING_DAYS                                  	=  " Days " ;
	public static final String STRING_DAY                                   	=  " Day " ;
	public static final String STRING_RESULT                                    =  "result" ;
	
	public static final int INT_MILLISECONDS                                    = 1000 ;
	public static final int INT_SECONDS                                         = 60 ;
	public static final int INT_HOURS                                           = 24 ;
	public static final int INT_MINUTES                                         = 60 ;
	public static final int INT_ZERO                                            = 0 ;
	public static final int INT_ONE                                             = 1 ;
	
	
	
}
