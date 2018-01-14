/**
 * 
 */
package com.fynger.servicesBusiness.constants;

/**
 * @author Rishi
 *
 */
public class ExceptionConstants {
	
	/* Generic Data Access Exception details */
	public static final String CODE_DATA_ACCESS_EXCEPTION = "WS101";
	public static final String USERMESSAGE_DATA_ACCESS_EXCEPTION = "There was some problem in processing your request. Please try after some time.";
	
	/* Generic Business Exception details */
	public static final String CODE_BUSINESS_EXCEPTION = "WS102";
	public static final String USERMESSAGE_BUSINESS_EXCEPTION = "There was some problem in processing your request. Please try after some time.";
	
	/* Search Engines Exception details */
	public static final String CODE_SEARCH_ENGINE_EXCEPTION = "WS103";
	public static final String USERMESSAGE_SEARCH_ENGINE_EXCEPTION = "There was some problem in processing your request. Please try after some time.";
	
	
	/* User Name not found Exception details */
	public static final String CODE_USERNAME_NOT_FOUND = "WS111";
	public static final String USERMESSAGE_USERNAME_NOT_FOUND = "Username does not exist.";
	public static final String ERRORMESSAGE_USERNAME_NOT_FOUND = "No record found for the provided User name in the database.";
	
	/* Incorrect Password Exception details */
	public static final String CODE_INCORRECT_PASSWORD = "WS112";
	public static final String USERMESSAGE_INCORRECT_PASSWORD = "Password is incorrect. Please enter a valid password.";
	public static final String ERRORMESSAGE_INCORRECT_PASSWORD = "Record for provided User name exists but the password is incorrect.";
	
	/* Mandatory Fields Validation Exception details */
	public static final String CODE_MANDATORY_FIELD_VALIDATION = "WS113";
	public static final String USERMESSAGE_MANDATORY_FIELD_VALIDATION = "Following fields are mandatory : ";
	public static final String ERRORMESSAGE_MANDATORY_FIELD_VALIDATION = "Mandatory field validation check failed. Missing fields are : ";
	public static final String USERMESSAGE_LOCATION_MANDATORY_FIELD_VALIDATION = "Please enter a valid location or switch on your location settings";
	
	/* Length Fields Validation Exception details */
	public static final String CODE_LENGTH_FIELD_VALIDATION = "WS114";
	public static final String USERMESSAGE_LENGTH_FIELD_VALIDATION_USERNAME = "Username should be between 3-20 characters.";
	public static final String USERMESSAGE_LENGTH_FIELD_VALIDATION_PHONE = "Invalid number. Mobile number should be 10 digits.";
	public static final String USERMESSAGE_LENGTH_FIELD_VALIDATION_PASSWORD = "Password should be between 5-15 characters with at least one digit.";
	public static final String ERRORMESSAGE_LENGTH_FIELD_VALIDATION = "Length field validation check failed.";
	
	/* Syntax Fields Validation Exception details */
	public static final String CODE_SYNTAX_FIELD_VALIDATION = "WS115";
	public static final String USERMESSAGE_SYNTAX_FIELD_VALIDATION_USERNAME = "Please use only alphanumeric characters in the Username.";
	public static final String USERMESSAGE_SYNTAX_FIELD_VALIDATION_LOCATION_COORDINATES = "Invalid location coordinates format.";
	public static final String USERMESSAGE_SYNTAX_FIELD_VALIDATION_PHONE = "Mobile number  cannot start with a '0'.";
	public static final String USERMESSAGE_SYNTAX_FIELD_VALIDATION_EMAIL = "Invalid email format.";
	public static final String USERMESSAGE_SYNTAX_FIELD_VALIDATION_DOB = "Date of Birth format is not valid.";
	public static final String ERRORMESSAGE_SYNTAX_FIELD_VALIDATION = "Syntax field validation check failed.";
	
	/* User Name Exists Exception details */
	public static final String CODE_USERNAME_EXISTS = "WS116";
	public static final String USERMESSAGE_USERNAME_EXISTS = "The requested Username already exists. Please try a different username.";
	public static final String ERRORMESSAGE_USERNAME_EXISTS = "User name already exists in the database.";
	
	/* Forgot Password Verification Failed Exception details */
	public static final String CODE_FORGOT_PASSWORD_VERIFICATION_FAILED = "WS117";
	public static final String USERMESSAGE_FORGOT_PASSWORD_VERIFICATION_FAILED = "No record found for the entered username. Either the username or the date of birth is invalid.";
	public static final String ERRORMESSAGE_FORGOT_PASSWORD_VERIFICATION_FAILED = "User verification details for Forgot password FAILED. Not found the combination of user name & date of birth in database.";
	
	/* No search results found Exception details */
	public static final String CODE_NO_SEARCH_RESULTS_FOUND = "WS118";
	public static final String USERMESSAGE_NO_SEARCH_RESULTS_FOUND = "Sorry, we could not find any business matching your search criteria";
	public static final String ERRORMESSAGE_NO_SEARCH_RESULTS_FOUND = "Search Engine returned blank response for the Search.";
	
	/* No Reviews found Exception details */
	public static final String CODE_NO_REVIEWS_FOUND = "WS119";
	public static final String USERMESSAGE_NO_REVIEW_FOUND = "No reviews found. Be the first one to review this place.";
	public static final String ERRORMESSAGE_NO_REVIEW_FOUND = "No review record found in the database for this listing/promotion id.";
	
	/* Promotion Id Not Found Exception details */
	public static final String CODE_PROMOTION_ID_NOT_FOUND = "WS120";
	public static final String USERMESSAGE_PROMOTION_ID_NOT_FOUND = "Details not available for this promotion.";
	public static final String ERRORMESSAGE_PROMOTION_ID_NOT_FOUND = "Promotion id not found in the datbase, unable to retrieve the records.";
	
	/* Promotion Exception details */
	public static final String CODE_PROMOTION_EXCEPTION = "WS121";
	public static final String USERMESSAGE_PROMOTION_EXCEPTION = "There appears to be some problem in processing the promotion feature request. Please try after some time.";
	
	/* Geo Coordinates not found Exception details */
	public static final String CODE_GEO_COORDINATES_NOT_FOUND = "WS122";
	public static final String USERMESSAGE_GEO_COORDINATES_NOT_FOUND = "Invalid location. Please enter a valid location or use the 'Near Me' option.";
	
	/* Flash Deal Exception details */
	public static final String CODE_FLASHDEAL_EXCEPTION = "WS123";
	public static final String USERMESSAGE_FLASHDEAL_EXCEPTION = "There appears to be some problem in processing your Flash Deal request. Please try after some time.";
	
	/* No Flash Deal Responded Results found Exception details */
	public static final String CODE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND = "WS124";
	public static final String USERMESSAGE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND = "You do not have any Flash Deal responses yet.";
	public static final String ERRORMESSAGE_NO_FLASHDEAL_RESPONDED_RESULTS_FOUND = "Flash Deal Results are empty";
	
	/* File Upload Failed Exception details */
	public static final String CODE_FILE_UPLOAD_FAILED = "WS125";
	public static final String USERMESSAGE_FILE_UPLOAD_FAILED = "Picture upload failed";
	public static final String ERRORMESSAGE_FILE_UPLOAD_FAILED = "Upload of picture not successful. Please try after some time.";
	
	/* Favourites Exception details */
	public static final String CODE_FAVOURITES_EXCEPTION = "WS126";
	public static final String USERMESSAGE_FAVOURITES_EXCEPTION = "There appears to be some problem in processing your Favourite search request. Please try after some time.";
	
	/* Location Not found Exception details */
	public static final String CODE_LOCATION_NOT_FOUND = "WS127";
	public static final String USERMESSAGE_LOCATION_NOT_FOUND = "No matching merchants found around your current location. Please check your location settings.";
	
	/* Add Favourites Exception details */
	public static final String CODE_ADD_FAVOURITES_EXCEPTION = "WS128";
	public static final String USERMESSAGE_ADD_FAVOURITES_EXCEPTION = "There appears to be some problem in adding your favourite(s). Please try after some time.";
	
	/* No Favourites Found Exception details */
	public static final String CODE_NO_FAVOURITES_FOUND_EXCEPTION = "WS129";
	public static final String USERMESSAGE_NO_FAVOURITES_FOUND_EXCEPTION = "You do not have any favorites. Start adding them and enjoy direct feed to awesome offers, deals and promotions.";
	
	/* Delete Favourites Exception details */
	public static final String CODE_DELETE_FAVOURITES_EXCEPTION = "WS130";
	public static final String USERMESSAGE_DELETE_FAVOURITES_EXCEPTION = "There appears to be some problem in deleting your  favourite(s). Please try after some time.";
	
	/* No Favourites Results Found Exception details */
	public static final String CODE_NO_FAVOURITES_RESULTS_FOUND_EXCEPTION = "WS131";
	public static final String USERMESSAGE_NO_FAVOURITES_RESULTS_FOUND_EXCEPTION = "No active promotions matching your favourites list at present.";
	
	/* Page Count Exceeded Exception details */
	public static final String CODE_PAGE_COUNT_EXCEEDED_EXCEPTION = "WS132";
	public static final String USERMESSAGE_PAGE_COUNT_EXCEEDED_EXCEPTION = "No more results found.";
	
	/* FB Login Exception details */
	public static final String CODE_FB_LOGIN_EXCEPTION = "WS133";
	public static final String USERMESSAGE_FB_LOGIN_EXCEPTION = "There appears to be some problem in logging you via Facebook. Please try again or use your Fynger credentials if you have them.";
	
	/* User Picture file upload exception details */
	public static final String RESPONSE_MESSAGE_FILE_UPLOAD_SUCCESS = "SUCCESS: Your picture has been uploaded successfully."; 
	public static final String RESPONSE_MESSAGE_FILE_UPLOAD_FAILED = "FAILED: ";
	
	/* Listing Id Not Found Exception details */
	public static final String CODE_LISTING_ID_NOT_FOUND = "WS134";
	public static final String USERMESSAGE_LISTING_ID_NOT_FOUND = "Details not available for this listing.";
	public static final String ERRORMESSAGE_LISTING_ID_NOT_FOUND = "Listing id not found in the datbase, unable to retrieve the records.";
	
	/* Shout Group Exception Details */
	public static final String USERMESSAGE_SHOUT_GROUP_GENERAL_MESSAGE = "Problem occurred in Shout feature. Please try after some time.";
	
	public static final String CODE_SHOUT_GROUP_LOAD_DETAILS = "WS135";
	public static final String USERMESSAGE_SHOUT_GROUP_LOAD_DETAILS = "Problem occurred in loading the selected shout group details. Please try after some time.";
	
	public static final String CODE_CITY_GEO_DETAILS = "WS136";
	public static final String CODE_SHOUT_GROUP_DETAILS = "WS137";
	
	public static final String CODE_SHOUT_GROUP_VIEW_POST_RESPONSES = "WS138";
	public static final String USERMESSAGE_SHOUT_GROUP_VIEW_POST_RESPONSES = "Problem occurred in loading the post responses. Please try after some time.";

	/* Shout Group - No Shout Group found */
	public static final String USERMESSAGE_SHOUT_GROUP_NO_RECORD_FOUND = "There are no posts/polls associated with the shout group. Be the first one to create a wall for this shout group.";
	
	/* Shout Group - No Post & Poll Data Found Exception Details */
	public static final String CODE_SHOUT_GROUP_NO_POST_POLL_DATA_FOUND_EXCEPTION = "WS139";
	public static final String USERMESSAGE_SHOUT_GROUP_NO_POST_POLL_DATA_FOUND_EXCEPTION = "No Post and Poll data found for this shout group. Be first to create one.";
	
	/* Shout Group - No Post Reply Data found exception */
	public static final String CODE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION = "WS140";
	public static final String USERMESSAGE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION = "No Post Reply data found for this Post. Be first to create one.";
	public static final String USERMESSAGE_SHOUT_GROUP_NO_POST_REPLY_DATA_FOUND = "There are no replies to this post. Be the first one to reply";
	
	/* Shout Group - Create Post exception */
	public static final String CODE_SHOUT_GROUP_CREATE_POST_EXCEPTION = "WS141";
	public static final String USERMESSAGE_SHOUT_GROUP_CREATE_POST_EXCEPTION = "There appears to be some problem in uploading your post. Please try after some time.";
	
	/* Shout Group - Create Poll exception */
	public static final String CODE_SHOUT_GROUP_CREATE_POLL_EXCEPTION = "WS142";
	public static final String USERMESSAGE_SHOUT_GROUP_CREATE_POLL_EXCEPTION = "There appears to be some problem in uploading your poll. Please try after some time.";

	/* Shout Group - Post Reply exception */
	public static final String CODE_SHOUT_GROUP_POST_REPLY_EXCEPTION = "WS143";
	public static final String USERMESSAGE_SHOUT_GROUP_POST_REPLY_EXCEPTION = "There appears to be some problem in uploading your reply. Please try after some time.";
	
	/* Shout Group - Poll Reply exception */
	public static final String CODE_SHOUT_GROUP_POLL_REPLY_EXCEPTION = "WS144";
	public static final String USERMESSAGE_SHOUT_GROUP_POLL_REPLY_EXCEPTION = "There appears to be some problem in uploading your vote. Please try after some time.";
	
	/* Place Group - No Place Group found */
	public static final String USERMESSAGE_PLACE_GROUP_NO_PLACE_RECORD_FOUND = "There are no posts/polls associated with the place. Be the first one to create a wall for this place";
	
	/* Place Group - No Post Reply Data found exception */
	public static final String CODE_PLACE_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION = "WS145";
	public static final String USERMESSAGE_PLACET_GROUP_NO_POST_REPLY_DATA_FOUND_EXCEPTION = "No Post Reply data found for this Post. Be first to create one.";
	public static final String USERMESSAGE_PLACE_GROUP_NO_POST_REPLY_DATA_FOUND = "There are no replies to this post. Be the first one to reply";
	
	/* Place Group - Create Post exception */
	public static final String CODE_PLACE_GROUP_CREATE_POST_EXCEPTION = "WS146";
	public static final String USERMESSAGE_PLACE_GROUP_CREATE_POST_EXCEPTION = "There appears to be some problem in uploading your post. Please try after some time.";
	
	/* Place Group - Create Poll exception */
	public static final String CODE_PLACE_GROUP_CREATE_POLL_EXCEPTION = "WS147";
	public static final String USERMESSAGE_PLACE_GROUP_CREATE_POLL_EXCEPTION = "There appears to be some problem in uploading your poll. Please try after some time.";
	
	/* Place Group - Post Reply exception */
	public static final String CODE_PLACE_GROUP_POST_REPLY_EXCEPTION = "WS148";
	public static final String USERMESSAGE_PLACE_GROUP_POST_REPLY_EXCEPTION = "There appears to be some problem in uploading your reply. Please try after some time.";
	
	/* Place Group - Poll Reply exception */
	public static final String CODE_PLACE_GROUP_POLL_REPLY_EXCEPTION = "WS149";
	public static final String USERMESSAGE_PLACE_GROUP_POLL_REPLY_EXCEPTION = "Problem occured in the execution of Poll Reply. Please try after some time.";
	
	/* Refresh Location exception */
	public static final String CODE_REFRESH_LOCATION_EXCEPTION = "WS150";
	public static final String USERMESSAGE_REFRESH_LOCATION_EXCEPTION = "There appears to be some problem in uploading your vote. Please try after some time.";
	
	/* Blank coordinates exception */
	public static final String ERRORMESSAGE_SYNTAX_FIELD_VALIDATION_LAT_LONG = "Latitude/Longitude coordinates are (0,0) blank coordinates";
	
	/* Openfire Registration Exception details */
	public static final String CODE_OPEN_REGISTRATION_EXCEPTION = "WS151";
	public static final String USERMESSAGE_OPEN_REGISTRATION_EXCEPTION = "You are registered successfully with Fynger but there appears to be some problem in registering with chat server.";
	
	/* Notification Engine Exception details */
	public static final String CODE_NOTIFICATION_ENGINE_EXCEPTION = "WS152";
	public static final String USERMESSAGE_NOTIFICATION_ENGINE_EXCEPTION = "There appears to be some problem in sending the notification.";
	
	/* Favourites Already Exists Exception details */
	public static final String CODE_FAVOURITES_ALREADY_EXISTS_EXCEPTION = "WS153";
	public static final String USERMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION = "The requested item has already been added to your favourites list";
	public static final String ERRORMESSAGE_FAVOURITES_ALREADY_EXISTS_EXCEPTION = "Favourite item already exists in the database.";
	

}

