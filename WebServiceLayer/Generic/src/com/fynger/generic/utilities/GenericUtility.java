/**
 * 
 */
package com.fynger.generic.utilities;

import com.fynger.generic.loggerManager.LoggerManager;

/**
 * @author Rishi
 *
 */
public class GenericUtility {
	
	public static LoggerManager getLogger(String className) {
		return new LoggerManager(className);
	}
	
	/**
	 * safeTrim takes a String and trims the leading and trailing spaces and returs a it
	 * this method will return an empty string if the String passed is is ==null or is the string "null"
	 *
	 * @param s Sting string to trim leading and trailing spaces
	 * @return String
	 */
	public static String safeTrim(String s) {
		if ((s == null) || s.equals("null")) {
			return "";
		}
		else {
			return s.trim();
		}
	}

}
