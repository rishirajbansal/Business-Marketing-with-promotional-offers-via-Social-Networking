package com.fynger.servicesBusiness.constants;

public class BusinessConstants {
	
	public static final String RESPONSE_SUCCESS										= "true";
	public static final String RESPONSE_FALSE										= "false";
	
	/* User Login Service constants */
	public static final String USER_LOGIN_FIELD_LOCATIONCOORDINATES					= "Location Coordinates";
	public static final String USER_LOGIN_FIELD_FBEMAIL								= "FB Email";
	public static final String USER_LOGIN_FIELD_FYNGERUSER							= "Fynger User Flag";
	public static final String USER_LOGIN_FIELD_FYNGERUSER_NAME						= "Fynger User Name";
	public static final String USER_LOGIN_FIELD_FYNGERUSER_PASSWORD					= "Fynger User Password";
	
	/* User Profile Service constants */
	public static final String USER_PROFILE_FIELD_USERNAME							= "User Name";
	public static final String USER_PROFILE_FIELD_PASSWORD							= "Password";
	public static final String USER_PROFILE_FIELD_DATEOFBIRTH						= "Date of Birth";
	public static final String USER_PROFILE_FIELD_EMAIL								= "Email";
	public static final String USER_PROFILE_FIELD_PHONE								= "Phone";
	
	public static final int USER_PROFILE_FIELD_USERNAME_LENGTH_VALIDATION			= 20;
	public static final int USER_PROFILE_FIELD_PASSWORD_LENGTH_VALIDATION			= 15;
	public static final int USER_PROFILE_FIELD_PHONE_LENGTH_VALIDATION				= 10;
	
	public static final String ALPHANUMERIC_SYNTAX_REGEX							= "[A-Za-z0-9]*";
	public static final String EMAIL_SYNTAX_REGEX									= "^\"?[\\w^\\.\\-\\+]?[\\w\\-\\.\\+]*?[\\w^\\.\\-\\+]\"?@[\\w]?[\\w\\-]*?[\\w]?\\.?[\\w\\-]*?\\.[a-zA-Z]{2,}$";
	public static final String LOCATION_COORDINATES_SYNTAX_REGEX					= "[\\d\\.]+\\|[\\d\\.]+";

	/* Common Search Services constants*/
	public static final String SEARCH_FIELD_SEARCHTEXT								= "Search Text";
	
	/* Listing Search Service constants */
	public static final String LISTING_SEARCH_FIELD_LOCATION						= "Location or Latitude/Longitude";
	public static final String LISTING_SEARCH_FIELD_KEYWORD							= "keyword";
	public static final String LISTING_SEARCH_FIELD_LAT_LONG						= "Latitude/Longitude";
	
	/* Promotion Search Service constants */
	public static final String PROMOTION_SEARCH_FIELD_ENTITYID						= "Entity Id";
	public static final String PROMOTION_SEARCH_FIELD_ENTITYTYPE					= "Entity Type";
	public static final String PROMOTION_SEARCH_FIELD_LOCATION						= "Location or Latitude/Longitude";
	public static final String PROMOTION_SEARCH_FIELD_KEYWORD						= "keyword";
	
	/* Flash Deal Search Service constants */
	public static final String FLASHDEAL_SEARCH_FIELD_EXPIRYPERIOD					= "Expiry Period";
	public static final String FLASHDEAL_SEARCH_FIELD_FLASHDEALID					= "Flash Deal Id";
	
	/* Favourites Search Service constants */
	public static final String FAVOURITES_SEARCH_FIELD_GROUPTYPE					= "Group Type";
	public static final String FAVOURITES_SEARCH_FIELD_KEYWORD						= "Keyword";
	public static final String FAVOURITES_SEARCH_FIELD_PROMOTIONID					= "Promotion Id";
	
	/* Shout Group Service Constants */
	public static final String SHOUT_GROUP_FIELD_LATITUDE							= "Latitude";
	public static final String SHOUT_GROUP_FIELD_LONGITUDE							= "Longitude";
	public static final String SHOUT_GROUP_FIELD_POSTID								= "Post Id";
	public static final String SHOUT_GROUP_FIELD_POSTTEXT							= "Post Text";
	public static final String SHOUT_GROUP_FIELD_SHOUT_GROUP_ID						= "Shout Group Id";
	public static final String SHOUT_GROUP_FIELD_POLL_QUESTION						= "Poll Question";
	public static final String SHOUT_GROUP_FIELD_POST_REPLY							= "Post Reply";
	public static final String SHOUT_GROUP_FIELD_POLLID								= "Poll Id";
	public static final String SHOUT_GROUP_FIELD_SELECTED_POLLOPTION				= "Selected Poll Option";
	
	public static final String SHOUT_GROUP_POST_PICTURE_UPLOAD_PREFIX				= "SG_post_";
	
	/* Place Group Service Constants */
	public static final String PLACE_GROUP_FIELD_LATITUDE							= "Latitude";
	public static final String PLACE_GROUP_FIELD_LONGITUDE							= "Longitude";
	public static final String PLACE_GROUP_FIELD_PLACE_NAME							= "Place Name";
	public static final String PLACE_GROUP_FIELD_PLACE_MAP_ID						= "Place Map Id";
	public static final String PLACE_GROUP_FIELD_POSTID								= "Post Id";
	public static final String PLACE_GROUP_FIELD_POSTTEXT							= "Post Text";
	public static final String PLACE_GROUP_FIELD_PLACE_GROUP_ID						= "Place Group Id";
	public static final String PLACE_GROUP_FIELD_POLL_QUESTION						= "Poll Question";
	public static final String PLACE_GROUP_FIELD_POST_REPLY							= "Post Reply";
	public static final String PLACE_GROUP_FIELD_POLLID								= "Poll Id";
	public static final String PLACE_GROUP_FIELD_SELECTED_POLLOPTION				= "Selected Poll Option";
	
	public static final String PLACE_GROUP_POST_PICTURE_UPLOAD_PREFIX				= "PG_post_";
	
	/* Refresh Location Service Constants */
	public static final String REFRESH_LOCATION_FIELD_LATITUDE						= "Latitude";
	public static final String REFRESH_LOCATION_FIELD_LONGITUDE						= "Longitude";
	
	/* Email Constants */
	public static final String EMAIL_EVENT_REGISTRATION								= "REGISTRATION"; 
	public static final String EMAIL_EVENT_FLASHDEAL_RESPONSE						= "FLASHDEAL_RESPONSE";
	
	/* Date Format constants */
	public static final String DATEFORMAT_SHOUT_PLACE								= "MMM dd, yyyy hh:mmaaa";
	
}	
