package com.fynger.servicesBusiness.integration.dataAccess.base;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

import com.fynger.searchEngine.query.utilities.ValidityManager;
import com.fynger.servicesBusiness.exception.DataAccessException;

public class DAOUtility {
	
	private DAOUtility() {
        
    }
	
	/**
	 * This encrypts a Password using MD5 Hash algorithm this is one way only,
	 * after using this one can compare two values by Hashing the other one
	 * and seeing if they are equal
	 *
	 * @param strPassword strPassword
	 *
	 * @return String
	 *
	*/
	public static String encryptPassword(String strPassword) {
		
		String strEncoded = new String("");

		try {			
			String input = strPassword;
			byte[] inputbytes = input.getBytes("UTF8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(inputbytes);

			byte[] digest = md5.digest();
			BASE64Encoder encoder = new BASE64Encoder();
			String base64 = encoder.encode(digest);
			strEncoded = base64.substring(0, base64.length() - 4);
		}
		catch(Exception e) {
			throw new DataAccessException("Error occured during the encryption of password");
		}
		return strEncoded;
	}
	
	public static String promotionValidityStringFormat(String date){
		
		ValidityManager validityManager = new ValidityManager();
		
		boolean flag = validityManager.getValidityStatus(date);
		
		if (flag){
			return validityManager.getValidity();
		}
		else{
			return null;
		}
		
	}

}
