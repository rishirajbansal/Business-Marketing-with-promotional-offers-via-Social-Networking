package com.fynger.notificationEngine.utilities;

public class NotificationStatusString {
	
	/**
	 * Returns a string of either Success Or Failure based upon the boolean input
	 *
	 * @param s Boolean boolean to convert
	 * @return String
	 */
	
	public static String statusString(boolean status) {
		
		String statusString;
		
		if(status == true){
			statusString = "Success";
		}else{
			statusString = "Failure";
		}		
		
		return statusString;
	}
	

}
