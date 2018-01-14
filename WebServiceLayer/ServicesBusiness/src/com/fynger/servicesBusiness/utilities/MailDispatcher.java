/**
 * 
 */
package com.fynger.servicesBusiness.utilities;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.business.initialize.MailConfiguration;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.base.DAOFactory;
import com.fynger.servicesBusiness.integration.dataAccess.dao.IEmailLogDAO;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.EmailLogData;
import com.fynger.servicesBusiness.objects.MailContent;

/**
 * @author Rishi
 *
 */
public class MailDispatcher {
	
	public static LoggerManager logger = GenericUtility.getLogger(MailDispatcher.class.getName());
	
	public static DAOFactory daoFactory = null;
	
	
	public static final String MAIL_SUCCESS_MSG 					= 	"Mail Sent successfully";
	public static final String MAIL_DISPATCH_STATUS_SUCCESS 		=	"SUCCESS";
	public static final String MAIL_DISPATCH_STATUS_FAILED 			=	"FAILED";
	
	
	private static Authenticator authenticator;
	private static Properties mailProperties;
	
	private MailContent mailContent;
	String dispatchMsg;
	boolean dispatchErr;
	
	public MailDispatcher(){		
		
	}
		
	static {
		daoFactory= DAOFactory.getDAOFactory(DAOFactory.SQL);
		
		mailProperties = MailConfiguration.getMailSMTPProperties();
		
		authenticator = MailConfiguration.getAuthenticator();
	}
	
	public boolean dispatchMail(MailContent mailContent){
		
		boolean flag = false;
		
		try{
			this.mailContent = mailContent;
			
			sendMail();
			
			saveMailDispatchStatus();
			
			flag = true;
		}
		catch(DataAccessException daEx){
			logger.error("DataAccessException occurred in DAO layer : " + daEx.getMessage());
		}
		catch(Exception ex){
			logger.error("Exception occurred : " + ex.getMessage());
		}
		catch(Throwable th){
			logger.error("Throwable occurred : " + th.getMessage());
		}
		
		return flag;
		
	}
	
	public void sendMail(){
		
		try{
			Session mailSession = Session.getInstance(mailProperties, authenticator);
	        
	        MimeMessage message = new MimeMessage(mailSession);
	        
	        message.setFrom(new InternetAddress(MailConfiguration.getFromMailId()));
	       
	        message.setRecipients(MimeMessage.RecipientType.TO, parse(mailContent.getToMailId(), false));

	        message.setSubject(mailContent.getSubject());
	        
		    MimeBodyPart messageBodyPart =  new MimeBodyPart();
		    messageBodyPart.setContent(new String(mailContent.getBody()), mailContent.getContentType());

		    Multipart multipart = new MimeMultipart();
		    multipart.addBodyPart(messageBodyPart);
		    
		    message.setContent(multipart);
		    
		    Transport.send(message);
			
		}
		catch(AddressException aEx){
			dispatchMsg = "A wrongly formatted address is encountered. An error is detected on position : " + aEx.getPos();
			dispatchMsg = dispatchMsg + " Exception message : " + aEx.getMessage();
			dispatchErr = true;
		}
		catch(AuthenticationFailedException afEx){
			dispatchMsg = "An authentication failure occurred probably due to bad user name or password. ";
			dispatchMsg = dispatchMsg + " Exception message : " + afEx.getMessage();
			dispatchErr = true;
		}
		catch(SendFailedException sfEx){
			dispatchMsg = "Message cannot be sent or could not be sent on some of the mailids due to invalid addresses or may be some other reason";
			
			Address[] validSentAddresses = sfEx.getValidSentAddresses();
			Address[] validUnsentAddresses = sfEx.getValidUnsentAddresses();
			Address[] invalidAddresses = sfEx.getInvalidAddresses();
			String validSentAddressesString = validSentAddresses != null ? InternetAddress.toString(validSentAddresses) : "EMPTY" ;
			String validUnsentAddressesString = validUnsentAddresses != null ? InternetAddress.toString(validUnsentAddresses) : "EMPTY" ;
			String invalidAddressesString = invalidAddresses != null ? InternetAddress.toString(invalidAddresses) : "EMPTY" ;
			
			dispatchMsg = dispatchMsg + " Valid Addresses on which the mail sent successfully are : " + validSentAddressesString;
			dispatchMsg = dispatchMsg + " Valid Addresses on which the mail could not be sent successfully are : " + validUnsentAddressesString;
			dispatchMsg = dispatchMsg + " Invalid Addresses on which the mail could not be sent are : " + invalidAddressesString;
			dispatchMsg = dispatchMsg + " Exception message : " + sfEx.getMessage();
			
			dispatchErr = true;
		}
		catch(MessagingException mEx){
			dispatchMsg = "A Messaging Exception occured. Cause could be : " + mEx.getCause();
			dispatchMsg = dispatchMsg + " Exception message : " + mEx.getMessage();
			dispatchErr = true;
		}
		catch (Exception ex){
			dispatchMsg = "An Exception occured. "; 
			dispatchMsg = dispatchMsg + " Exception message : " + ex.toString();
			dispatchErr = true;
		}
		
	}
	
	private void saveMailDispatchStatus(){
		EmailLogData emailLogData = new EmailLogData();
		
		emailLogData.setUsername(mailContent.getUsername());
		emailLogData.setEvent(mailContent.getEvent());
		
		if (dispatchErr){
			emailLogData.setComments(dispatchMsg);
			emailLogData.setDispatchStatus(MAIL_DISPATCH_STATUS_FAILED);
		}
		else{
			emailLogData.setComments(MAIL_SUCCESS_MSG);
			emailLogData.setDispatchStatus(MAIL_DISPATCH_STATUS_SUCCESS);
			emailLogData.setSentTime(new java.sql.Timestamp(System.currentTimeMillis()));
		}
		
		IEmailLogDAO emailLogDAO = daoFactory.getEmailLogDAO();
		
		boolean flag = emailLogDAO.storeEmailLog(emailLogData);
		
		if (flag){
			logger.debug("Email Dispatch log record is saved successfully in the database for username : " + mailContent.getUsername());
		}
		else{
			logger.error("Email Dispatch log record is FAILED to save in the database for username : " + mailContent.getUsername());
		}
	}
	
	private Address[] parse(String address, boolean restrict) throws AddressException {
		if (address.trim().endsWith(",")) {
			address = address.trim();
			address = address.substring(0,address.length()-1);
		}
		return InternetAddress.parse(address, restrict);
	}
	
}
