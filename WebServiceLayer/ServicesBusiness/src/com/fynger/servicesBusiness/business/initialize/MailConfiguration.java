/**
 * 
 */
package com.fynger.servicesBusiness.business.initialize;

import java.security.Security;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.MailConfigurationException;

/**
 * @author Rishi
 *
 */
public class MailConfiguration {
	
	public static LoggerManager logger = GenericUtility.getLogger(MailConfiguration.class.getName());
	
	private static PropertyManager propertyManager = PropertyManager.getPropertyManager();
	
	public static final String TRANSPORT_PROTOCOL 		= "mail.transport.protocol";
	public static final String SMTP_HOST 				= "mail.smtp.host";
	public static final String SMTP_PORT 				= "mail.smtp.port";
	public static final String SMTP_USER 				= "mail.smtp.user";
	public static final String SMTP_PASSWORD 			= "mail.smtp.password";
	public static final String SMTP_AUTH 				= "mail.smtp.auth";
	public static final String SMTP_FROM 				= "mail.smtp.from";
	public static final String SMTP_DEBUG 				= "mail.debug";
	public static final String SMTP_STARTTLS 			= "mail.smtp.starttls.enable";
	
	public static final String SMTP						= "smtp";
	
	private static MailConfiguration mailConfiguration = null;
	private static Properties prop = null;
	private static Properties mailSMTPProperties = null;
	
	private static String smtpHost = null; 
	private static String smtpPort = null;
	private static String smtpUser = null;
	private static String smtpPassword = null;
	
	private static String fromMailId = null;
	private static String debug = null;
	private static String enableStarttls = null;
	private static String authenticate = null;
	
	private static Authenticator authenticator;
	
	
	private MailConfiguration(){
		prop = propertyManager.getProperties(GenericConstants.EMAIL_PROPERTIES_FILE_NAME);
	}
	
	public static MailConfiguration createInstance(){
		
		if (mailConfiguration == null){
			mailConfiguration = new MailConfiguration();
		}
		
		return mailConfiguration;
	}
	
	public void configure(){
		setConfigParmsFromPropFile();
		
		authenticator = new SMTPAuthenticator();
	}
	
	public void setConfigParmsFromPropFile(){
		
		try{
			smtpHost = prop.getProperty(GenericConstants.MAIL_CONFIG_SMTP_HOST);
			if (smtpHost == null){
				logger.error("SMTP Host is not found in property file.");
				throw new MailConfigurationException("SMTP Host is not found in property file.");
			}
			
			smtpPort = prop.getProperty(GenericConstants.MAIL_CONFIG_SMTP_PORT);
			if (smtpPort == null){
				logger.error("SMTP Port is not found in property file.");
				throw new MailConfigurationException("SMTP Port is not found in property file.");
			}
			
			smtpUser = prop.getProperty(GenericConstants.MAIL_CONFIG_SMTP_USER);
			if (smtpUser == null){
				logger.error("SMTP User ID is not found in property file.");
				throw new MailConfigurationException("SMTP User ID is not found in property file.");
			}
			
			smtpPassword = prop.getProperty(GenericConstants.MAIL_CONFIG_SMTP_PASSWORD);
			if (smtpPassword == null){
				logger.error("SMTP Password is not found in property file.");
				throw new MailConfigurationException("SMTP Password is not found in property file.");
			}
			
			fromMailId = prop.getProperty(GenericConstants.MAIL_CONFIG_FROM_MAILID);
			if (fromMailId == null){
				logger.error("From Mail ID is not found in property file.");
				throw new MailConfigurationException("From Mail ID is not found in property file.");
			}
			
			debug = prop.getProperty(GenericConstants.MAIL_CONFIG_DEBUG);
			if (debug == null){
				logger.error("Debug is not found in property file.");
				throw new MailConfigurationException("Debug is not found in property file.");
			}
			
			enableStarttls = prop.getProperty(GenericConstants.MAIL_CONFIG_ENABLE_STARTTLS);
			if (enableStarttls == null){
				logger.error("enable sStarttls is not found in property file.");
				throw new MailConfigurationException("Debug is not found in property file.");
			}
			
			authenticate = prop.getProperty(GenericConstants.MAIL_CONFIG_AUTHENTICATE);
			if (authenticate == null){
				logger.error("authenticate is not found in property file.");
				throw new MailConfigurationException("Debug is not found in property file.");
			}
			
			configMailSMTPProperties(); 
		}
		catch(Exception ex){
			logger.error("Error while setting up the local variables from properties file: " + ex.getMessage());
			throw new MailConfigurationException("Error while setting up the local variables from properties file: " + ex.getMessage());
		}
		
	}
	
	private void configMailSMTPProperties(){
		mailSMTPProperties = new Properties();
		
		mailSMTPProperties.setProperty(TRANSPORT_PROTOCOL, SMTP);
		mailSMTPProperties.setProperty(SMTP_HOST, getSmtpHost());
		mailSMTPProperties.setProperty(SMTP_PORT, getSmtpPort());
		mailSMTPProperties.setProperty(SMTP_USER, getSmtpUser());
		mailSMTPProperties.setProperty(SMTP_PASSWORD, getSmtpPassword());
		mailSMTPProperties.setProperty(SMTP_AUTH, getAuthenticate());
		mailSMTPProperties.setProperty(SMTP_FROM, getFromMailId());
		mailSMTPProperties.setProperty(SMTP_DEBUG, getDebug());
		mailSMTPProperties.setProperty(SMTP_STARTTLS, getEnableStarttls());		
	}
	
	public class SMTPAuthenticator extends Authenticator {
		
        public PasswordAuthentication getPasswordAuthentication() {
        	
        	if (mailSMTPProperties.getProperty(MailConfiguration.SMTP_STARTTLS).equalsIgnoreCase(GenericConstants.STRING_TRUE)){
        		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());          		
        	}
        	
        	String username = mailSMTPProperties.getProperty(MailConfiguration.SMTP_USER);
        	String password =  mailSMTPProperties.getProperty(MailConfiguration.SMTP_PASSWORD);
        	return new PasswordAuthentication(username, password);
        }
    }

	/**
	 * @return the smtpHost
	 */
	public static String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * @param smtpHost the smtpHost to set
	 */
	public static void setSmtpHost(String smtpHost) {
		MailConfiguration.smtpHost = smtpHost;
	}

	/**
	 * @return the smtpPort
	 */
	public static String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort the smtpPort to set
	 */
	public static void setSmtpPort(String smtpPort) {
		MailConfiguration.smtpPort = smtpPort;
	}

	/**
	 * @return the smtpUser
	 */
	public static String getSmtpUser() {
		return smtpUser;
	}

	/**
	 * @param smtpUser the smtpUser to set
	 */
	public static void setSmtpUser(String smtpUser) {
		MailConfiguration.smtpUser = smtpUser;
	}

	/**
	 * @return the smtpPassword
	 */
	public static String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * @param smtpPassword the smtpPassword to set
	 */
	public static void setSmtpPassword(String smtpPassword) {
		MailConfiguration.smtpPassword = smtpPassword;
	}

	/**
	 * @return the fromMailId
	 */
	public static String getFromMailId() {
		return fromMailId;
	}

	/**
	 * @param fromMailId the fromMailId to set
	 */
	public static void setFromMailId(String fromMailId) {
		MailConfiguration.fromMailId = fromMailId;
	}

	/**
	 * @return the debug
	 */
	public static String getDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public static void setDebug(String debug) {
		MailConfiguration.debug = debug;
	}

	/**
	 * @return the enableStarttls
	 */
	public static String getEnableStarttls() {
		return enableStarttls;
	}

	/**
	 * @param enableStarttls the enableStarttls to set
	 */
	public static void setEnableStarttls(String enableStarttls) {
		MailConfiguration.enableStarttls = enableStarttls;
	}

	/**
	 * @return the authenticate
	 */
	public static String getAuthenticate() {
		return authenticate;
	}

	/**
	 * @param authenticate the authenticate to set
	 */
	public static void setAuthenticate(String authenticate) {
		MailConfiguration.authenticate = authenticate;
	}

	/**
	 * @return the mailSMTPProperties
	 */
	public static Properties getMailSMTPProperties() {
		return mailSMTPProperties;
	}

	/**
	 * @param mailSMTPProperties the mailSMTPProperties to set
	 */
	public static void setMailSMTPProperties(Properties mailSMTPProperties) {
		MailConfiguration.mailSMTPProperties = mailSMTPProperties;
	}

	/**
	 * @return the authenticator
	 */
	public static Authenticator getAuthenticator() {
		return authenticator;
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	public static void setAuthenticator(Authenticator authenticator) {
		MailConfiguration.authenticator = authenticator;
	}

	

}
