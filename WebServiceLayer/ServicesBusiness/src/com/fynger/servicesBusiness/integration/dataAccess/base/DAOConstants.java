/**
 * 
 */
package com.fynger.servicesBusiness.integration.dataAccess.base;

/**
 * @author Rishi
 *
 */
public class DAOConstants {
	
	public static final String TABLE_USER_LOGIN_COLUMN_LOCATION_COORDINATES 				= "location_coordinates";
	public static final String TABLE_USER_LOGIN_COLUMN_FBEMAIL				 				= "fbemail";
	public static final String TABLE_USER_LOGIN_COLUMN_USERNAME				 				= "username";
	
	public static final int LOGIN_FBUSER_YES												= 1;													
	
	public static final String TABLE_USER_PROFILE_COLUMN_FULLNAME 							= "full_name";
	public static final String TABLE_USER_PROFILE_COLUMN_DATEOFBIRTH 						= "date_of_birth";
	public static final String TABLE_USER_PROFILE_COLUMN_EMAIL 								= "email";
	public static final String TABLE_USER_PROFILE_COLUMN_PHONE 								= "phone";
	public static final String TABLE_USER_PROFILE_COLUMN_ZIPCODE	 						= "zipcode";
	public static final String TABLE_USER_PROFILE_COLUMN_GENDER 							= "gender";
	public static final String TABLE_USER_PROFILE_COLUMN_PICTUREPATH	 					= "picture_path";
	
	public static final String TABLE_PROMOTION_COLUMN_IDPROMOTION 							= "idpromotion";
	public static final String TABLE_PROMOTION_COLUMN_IDPROMOBASIC 							= "idpromobasic";
	public static final String TABLE_PROMOTION_COLUMN_IDBASICSTORE 							= "idbasicstore";
	
	public static final String TABLE_PROMOBASIC_COLUMN_PROMOTYPE 							= "promotype";
	public static final String TABLE_PROMOBASIC_COLUMN_SUMMARY 								= "summary";
	public static final String TABLE_PROMOBASIC_COLUMN_MMURL 								= "mmurl";
	public static final String TABLE_PROMOBASIC_COLUMN_MMTYPE 								= "mmtype";
	public static final String TABLE_PROMOBASIC_COLUMN_STARTSAT 							= "startsat";
	public static final String TABLE_PROMOBASIC_COLUMN_VALIDTILL							= "validtill";
	
	public static final String TABLE_BASICSTORE_COLUMN_NAME									= "name";
	public static final String TABLE_BASICSTORE_COLUMN_ADDRESS								= "address";
	public static final String TABLE_BASICSTORE_COLUMN_CONTACT								= "contact";
	public static final String TABLE_BASICSTORE_COLUMN_CITY									= "city";
	public static final String TABLE_BASICSTORE_COLUMN_STATE								= "state";
	public static final String TABLE_BASICSTORE_COLUMN_LATITUDE								= "latitude";
	public static final String TABLE_BASICSTORE_COLUMN_LONGITUDE							= "longitude";
		
	public static final String TABLE_TOPCAT_COLUMN_CATEGORYNAME								= "categoryName";
	
	public static final String TABLE_EVENTAB_COLUMN_ADDRESS									= "address";
	public static final String TABLE_EVENTAB_COLUMN_CONTACT									= "contact";
	public static final String TABLE_EVENTAB_COLUMN_CITY									= "city";
	public static final String TABLE_EVENTAB_COLUMN_STATE									= "state";
	public static final String TABLE_EVENTAB_COLUMN_LATITUDE								= "latitude";
	public static final String TABLE_EVENTAB_COLUMN_LONGITUDE								= "longitude";
	public static final String TABLE_EVENTAB_COLUMN_IDPROMOBASIC 							= "idpromobasic";
	
	public static final String TABLE_MERCHANT_RATING_LIKES_COLUMN_RATING					= "rating";
	public static final String TABLE_MERCHANT_RATING_LIKES_COLUMN_LIKESCOUNT				= "likes_count";
	public static final String TABLE_MERCHANT_RATING_LIKES_COLUMN_LISTING_ASSOCIATION		= "listing_association";
	
	public static final String TABLE_MERCHANT_REVIEWS_COLUMN_USERNAME						= "username";
	public static final String TABLE_MERCHANT_REVIEWS_COLUMN_USER_PICTURE_PATH				= "user_picture_path";
	public static final String TABLE_MERCHANT_REVIEWS_COLUMN_REVIEW_TEXT					= "review_text";
	
	public static final int PROMOTION_BASIC_PROMO_TYPE_PROMOTION							= 0;
	public static final int PROMOTION_BASIC_PROMO_TYPE_EVENT								= 1;	
	public static final String PROMOTION_BASIC_PROMO_TYPE_PROMOTION_STRING					= "Promotion";
	public static final String PROMOTION_BASIC_PROMO_TYPE_EVENT_STRING						= "Event";
	public static final int PROMOTION_BASIC_STATUS_ACTIVE									= 1;
	public static final int PROMOTION_BASIC_STATUS_INACTIVE									= 0;
	public static final String PROMOTION_BASIC_MULTIMEDIA_TYPE_IMAGE						= "Image";
	public static final String PROMOTION_BASIC_MULTIMEDIA_TYPE_VIDEO						= "Video";
	
	public static final int FLASHDEAL_REQUEST_RESPONDED_YES									= 1;
	public static final int FLASHDEAL_REQUEST_RESPONDED_NO									= 0;
	public static final int FLASHDEAL_REQUEST_STATUS_ACTIVE									= 1;
	public static final int FLASHDEAL_REQUEST_STATUS_INACTIVE								= 0;
	public static final int FLASHDEAL_REQUEST_NOTIFIED_YES									= 1;
	public static final int FLASHDEAL_REQUEST_NOTIFIED_NO									= 0;
	
	public static final int FLASHDEAL_RESPONDED_EMAILED_NO									= 0;
	public static final int FLASHDEAL_RESPONDED_EMAILED_YES									= 1;
	public static final int FLASHDEAL_RESPONDED_NOTIFIED_NO									= 0;
	public static final int FLASHDEAL_RESPONDED_NOTIFIED_YES								= 1;
	
	public static final String TABLE_FLASHDEAL_COLUMN_IDFLASHDEAL							= "idflashdeal";
	public static final String TABLE_FLASHDEAL_COLUMN_IDMERCHANT							= "idmreg";
	public static final String TABLE_FLASHDEAL_COLUMN_USERNAME								= "username";
	public static final String TABLE_FLASHDEAL_COLUMN_RESPONSETEXT							= "responseText";
	public static final String TABLE_FLASHDEAL_COLUMN_REQUESTCATEGORY						= "reqcat";
	public static final String TABLE_FLASHDEAL_COLUMN_REQUESTLOCATION						= "reqloc";
	public static final String TABLE_FLASHDEAL_COLUMN_RESPONDEDTIMESTAMP					= "respts";
	
	public static final String TABLE_USER_FAVOURITES_COLUMN_USERNAME						= "username";
	public static final String TABLE_USER_FAVOURITES_COLUMN_CATEGORIES						= "categories";
	public static final String TABLE_USER_FAVOURITES_COLUMN_BRANDS							= "brands";
	public static final String TABLE_USER_FAVOURITES_COLUMN_PLACES							= "places";
	
	public static final String TABLE_USER_FAVOURITES_PROMOTIONS_COLUMN_PROMOTIONSIDS		= "promotion_ids";
	
	public static final String TABLE_CITY_GEO_COLUMN_CITYID									= "city_id";
	public static final String TABLE_CITY_GEO_COLUMN_CITYNAME								= "city_name";
	public static final String TABLE_CITY_GEO_COLUMN_CITYADDRESS							= "city_center_address";
	public static final String TABLE_CITY_GEO_COLUMN_CITYCENTER_LATITUDE					= "city_center_latitude";
	public static final String TABLE_CITY_GEO_COLUMN_CITYCENTER_LONGITUDE					= "city_center_longitude";
	
	public static final String TABLE_SHOUTGROUP_COLUMN_SHOUT_GROUP_ID						= "shout_group_id";
	public static final String TABLE_SHOUTGROUP_COLUMN_NAME									= "name";
	public static final String TABLE_SHOUTGROUP_COLUMN_DESCRIPTION							= "description";
	public static final String TABLE_SHOUTGROUP_COLUMN_ADDRESS								= "address";
	public static final String TABLE_SHOUTGROUP_COLUMN_CITYID								= "city_ID";
	public static final String TABLE_SHOUTGROUP_COLUMN_LATITUDE								= "latitude";
	public static final String TABLE_SHOUTGROUP_COLUMN_LONGITUDE							= "longitude";
	
	public static final String TABLE_SHOUTGROUP_MAPPING_COLUMN_SHOUT_GROUP_ID				= "shout_group_id";
	public static final String TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_ID					= "entity_id";
	public static final String TABLE_SHOUTGROUP_MAPPING_COLUMN_ENTITY_TYPE					= "entity_type";
	
	public static final int SHOUTGROUP_MAPPING_ENTITY_TYPE_POST								= 1;
	public static final int SHOUTGROUP_MAPPING_ENTITY_TYPE_POLL								= 2;
	
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_POSTID							= "post_id";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_SHOUTGROUP_ID					= "shout_group_id";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_POST_TEXT						= "post_text";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_CREATED_USERNAME				= "created_username";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_POST_LATITUDE					= "post_latitude";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_POST_LONGITUDE					= "post_longitude";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_POST_IMAGE_PATH					= "post_image_path";
	public static final String TABLE_SHOUTGROUP_POST_COLUMN_CREATED_TIMESTAMP				= "created_on";
	
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLLID							= "poll_id";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_SHOUTGROUP_ID					= "shout_group_id";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_CREATED_USERNAME				= "created_username";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_LATITUDE					= "poll_latitude";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_LONGITUDE					= "poll_longitude";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_QUESTION					= "poll_question";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_1					= "poll_option_1";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_2					= "poll_option_2";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_3					= "poll_option_3";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_4					= "poll_option_4";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1				= "poll_option_count_1";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2				= "poll_option_count_2";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3				= "poll_option_count_3";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4				= "poll_option_count_4";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_POLL_TOTAL_COUNT				= "poll_total_count";
	public static final String TABLE_SHOUTGROUP_POLL_COLUMN_CREATED_TIMESTAMP				= "created_on";
	
	public static final String TABLE_SHOUTGROUP_POST_REPLY_COLUMN_POSTID					= "post_id";
	public static final String TABLE_SHOUTGROUP_POST_REPLY_COLUMN_REPLIED_USERNAME			= "replied_username";
	public static final String TABLE_SHOUTGROUP_POST_REPLY_COLUMN_REPLIED_TEXT				= "replied_text";
	public static final String TABLE_SHOUTGROUP_POST_REPLY_COLUMN_CREATED_TIMESTAMP			= "created_on";
	
	public static final String TABLE_PLACEGROUP_COLUMN_PLACE_GROUP_ID						= "place_group_id";
	
	public static final String TABLE_PLACEGROUP_MAPPING_COLUMN_SHOUT_GROUP_ID				= "place_group_id";
	public static final String TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_ID					= "entity_id";
	public static final String TABLE_PLACEGROUP_MAPPING_COLUMN_ENTITY_TYPE					= "entity_type";
	
	public static final int PLACEGROUP_MAPPING_ENTITY_TYPE_POST								= 1;
	public static final int PLACEGROUP_MAPPING_ENTITY_TYPE_POLL								= 2;
	
	public static final String TABLE_PLACEGROUP_POST_COLUMN_POSTID							= "post_id";
	public static final String TABLE_PLACEGROUP_POST_COLUMN_PLACEGROUP_ID					= "place_group_id";
	public static final String TABLE_PLACEGROUP_POST_COLUMN_POST_TEXT						= "post_text";
	public static final String TABLE_PLACEGROUP_POST_COLUMN_CREATED_USERNAME				= "created_username";
	public static final String TABLE_PLACEGROUP_POST_COLUMN_POST_IMAGE_PATH					= "post_image_path";
	public static final String TABLE_PLACEGROUP_POST_COLUMN_CREATED_TIMESTAMP				= "created_on";
	
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLLID							= "poll_id";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_PLACEGROUP_ID					= "place_group_id";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_CREATED_USERNAME				= "created_username";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_QUESTION					= "poll_question";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_1					= "poll_option_1";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_2					= "poll_option_2";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_3					= "poll_option_3";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_4					= "poll_option_4";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_1				= "poll_option_count_1";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_2				= "poll_option_count_2";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_3				= "poll_option_count_3";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_OPTION_COUNT_4				= "poll_option_count_4";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_POLL_TOTAL_COUNT				= "poll_total_count";
	public static final String TABLE_PLACEGROUP_POLL_COLUMN_CREATED_TIMESTAMP				= "created_on";
	
	public static final String TABLE_PLACEGROUP_POST_REPLY_COLUMN_POSTID					= "post_id";
	public static final String TABLE_PLACEGROUP_POST_REPLY_COLUMN_REPLIED_USERNAME			= "replied_username";
	public static final String TABLE_PLACEGROUP_POST_REPLY_COLUMN_REPLIED_TEXT				= "replied_text";
	public static final String TABLE_PLACEGROUP_POST_REPLY_COLUMN_CREATED_TIMESTAMP			= "created_on";
	
	public static final String TABLE_FLASHDEAL_RESPONDED_COLUMN_ID							= "id";
	public static final String TABLE_FLASHDEAL_RESPONDED_COLUMN_USERNAME					= "username";
	public static final String TABLE_FLASHDEAL_RESPONDED_COLUMN_IDFLASHDEAL					= "idflashdeal";
	public static final String TABLE_FLASHDEAL_RESPONDED_COLUMN_RESPONSETEXT				= "responsetext";
	public static final String TABLE_FLASHDEAL_RESPONDED_COLUMN_MERCHANTNAME				= "merchantstorename";
	
	public static final String TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_REGISTRATION_ID			= "device_reg_id";
	public static final String TABLE_USER_MOBILE_AUTH_COLUMN_DEVICE_TYPE					= "device_type";
	
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_ID								= "idnotification_log";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_USERNAME						= "username";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_EVENT							= "event";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_DISPATCH_STATUS				= "dispatch_status";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_COMMENTS						= "comments";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_TEXT							= "text";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_DEVICE_REG_ID					= "device_reg_id";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_RETRIED_COUNT					= "retried_count";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_SENT_TIME						= "sent_time";
	public static final String TABLE_NOTIFICATION_LOG_COLUMN_CREATED_ON						= "created_on";

}
