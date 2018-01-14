/**
 * 
 */
package com.fynger.generic.common;

/**
 * @author Rishi
 *
 */
public class GenericConstants {
	
	public static final String EMPTY_STRING										= "";
	public static final String STRING_TRUE										= "true";
	public static final String STRING_FALSE										= "false";
	
	public static final String PROPERTIES_FILE_PATH								= "PropertiesFilePath";
	public static final String CONFIGURATION_FILE_PATH							= "ConfigurationFilePath";
	
	public static final String FILE_SEPARATOR 									= System.getProperty("file.separator");
	public static final String PIPE_SEPARATOR 									= "|";
	
	public static final String COMMON_PROPERTIES_FILE_NAME 						= "common.properties";
	public static final String DATABASE_PROPERTIES_FILE_NAME 					= "database.properties";
	public static final String EMAIL_PROPERTIES_FILE_NAME 						= "emailConfig.properties";
	public static final String EMAIL_CONTENT_PROPERTIES_FILE_NAME 				= "emailContent.properties";
	
	/*
	 * Database configuration constants
	 */
	public static final String DATABASE_DRIVER									= "driver";
	public static final String DATABASE_URL										= "url";
	public static final String DATABASE_USERNAME								= "username";
	public static final String DATABASE_PASSWORD								= "password";
	public static final String DATABASE_AUTO_COMMIT								= "autoCommit";
	public static final String DATABASE_ACTIVE_MAX_POOL_SIZE					= "activeMaxPoolSize";
	
	/*
	 * Common Properties constants
	 */
	public static final String FILE_SERVER_HOST									= "file_server_host";
	public static final String FILE_SERVER_PICTURE_UPLOAD_PATH					= "file_server_picture_upload_path";
	public static final String FILE_SERVER_PICTURE_DOWNLOAD_PATH				= "file_server_picture_download_path";
	public static final String PROMOTION_VALIDITY_POOL_FREQUENCY				= "promotionValidityPoolFrequency";
	public static final String FLASHDEAL_VALIDITY_POOL_FREQUENCY				= "flashDealValidityPoolFrequency";
	public static final String PROMOTION_VALIDITY_DIFF							= "promotionValidityDiff";
	public static final String FLASHDEAL_VALIDITY_DIFF							= "flashDealValidityDiff";
	public static final String MAX_PAGE_RESULTS									= "maxPageResults";
	public static final String USER_FAVOURITES_REFRESH_FREQUENCY				= "userFavouritesRefreshFrequency";
	public static final String SHOUT_GROUP_MAX_PAGE_RESULTS						= "shoutGroupMaxPageResults";
	public static final String PLACE_GROUP_MAX_PAGE_RESULTS						= "placeGroupMaxPageResults";
	public static final String FLASHDEAL_RESPONSE_POOL_FREQUENCY				= "flashDealResponsePoolFrequency";
	
	
	/*
	 * Email Properties constants
	 */
	public static final String MAIL_CONFIG_SMTP_HOST							= "smtpHost";
	public static final String MAIL_CONFIG_SMTP_PORT							= "smtpPort";
	public static final String MAIL_CONFIG_SMTP_USER							= "smtpUser";
	public static final String MAIL_CONFIG_SMTP_PASSWORD						= "smtpPassword";
	public static final String MAIL_CONFIG_POP3_HOST							= "pop3Host";
	public static final String MAIL_CONFIG_POP3_PORT							= "pop3Port";
	public static final String MAIL_CONFIG_POP3_USER							= "pop3User";
	public static final String MAIL_CONFIG_POP3_PASSWORD						= "pop3Password";
	public static final String MAIL_CONFIG_FROM_MAILID							= "from_mailid";
	public static final String MAIL_CONFIG_DEBUG								= "mail_debug";
	public static final String MAIL_CONFIG_ENABLE_STARTTLS						= "enable_starttls";
	public static final String MAIL_CONFIG_AUTHENTICATE							= "authenticate";
	public static final String MAIL_CONFIG_CONTENT_TYPE							= "mailContentType";
	
	/*
	 * Email Content Properties constants
	 */
	public static final String MAIL_CONTENT_REGISTRATION_SUBJECT				= "registration.subject";
	public static final String MAIL_CONTENT_REGISTRATION_BODY					= "registration.body";
	
	public static final String MAIL_CONTENT_REGISTRATION_BODY_PLACEHOLDER_USERNAME					= "<username>";
	public static final String MAIL_CONTENT_REGISTRATION_BODY_PLACEHOLDER_EMAILID					= "<emailid>";
	
	public static final String MAIL_CONTENT_FLASHDEAL_RESPONSE_SUBJECT				= "flashDealResponse.subject";
	public static final String MAIL_CONTENT_FLASHDEAL_RESPONSE_BODY					= "flashDealResponse.body";
	
	public static final String MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_NAME				= "<name>";
	public static final String MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_ADDRESS			= "<address>";
	public static final String MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_PHONE			= "<phone>";
	public static final String MAIL_CONTENT_FLASHDEALRESPONSE_BODY_PLACEHOLDER_RESPONSETEXT		= "<responsetext>";
	


}
