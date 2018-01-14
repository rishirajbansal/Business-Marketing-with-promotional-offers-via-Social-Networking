/**
 * 
 */
package com.fynger.servicesBusiness.utilities;

/**
 * @author Rishi
 *
 */
public class DateValidator {
	
	private static final String	YYYY_VALIDATION_REGEXP								= "\\d\\d\\d\\d";

	private static final String	MM_VALIDATION_REGEXP								= "(0[1-9]|1[012])";

	private static final String	DD_VALIDATION_REGEXP								= "(0[1-9]|[12][0-9]|3[01])";
	
	private static final String	SLASH_SEPARATOR_VALIDATION_REGEXP					= "[/]";
	
	/**
	 * Regular expression for mm/dd/yyyy date validation.
	 */
	public static final String	FORMAT_MM_DD_YYYY									= MM_VALIDATION_REGEXP
																						+ SLASH_SEPARATOR_VALIDATION_REGEXP
																						+ DD_VALIDATION_REGEXP
																						+ SLASH_SEPARATOR_VALIDATION_REGEXP
																						+ YYYY_VALIDATION_REGEXP;
	
	public static boolean validate(String dateString, String format) {
		return dateString.matches(format);
	}

}
