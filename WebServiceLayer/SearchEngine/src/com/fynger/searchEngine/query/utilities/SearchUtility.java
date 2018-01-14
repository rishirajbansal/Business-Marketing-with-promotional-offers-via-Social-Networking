package com.fynger.searchEngine.query.utilities;

public class SearchUtility {
	
	/**
	 * formatKeyword takes a String and formats it by adding double quotes to the beginning and ending of the string 
	 * and thereby formating it as per the search engine and returns it	 
	 *
	 * @param s Sting string to format
	 * @return String
	 */
	
	public static String formatKeyword(String keyword) {
		
		keyword = "\""+keyword+"\"";				
		return keyword;
	}

}
