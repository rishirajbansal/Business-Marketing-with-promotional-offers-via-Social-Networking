package com.fynger.servicesBusiness.utilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.RandomStringUtils;

import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.constants.BusinessConstants;

public class Utility {
	
	public static LoggerManager logger = GenericUtility.getLogger(Utility.class.getName());
	
	public static final int RADUIS_EARTH			= 6371;
	
	
	public static double calculateLocationCoordinatesDistance(double srcLat, double srcLng, double destLat, double destLng){
		
		double dLat, dLng, srcLatRadians, destLatRadians, distEq1, distEq2, distance;
		
		dLat = Math.toRadians(destLat - srcLat);
		dLng = Math.toRadians(destLng - srcLng);
		
		srcLatRadians = Math.toRadians(srcLat);
		destLatRadians = Math.toRadians(destLat);
		
		distEq1 = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(srcLatRadians) * Math.cos(destLatRadians);
		distEq2 = 2 * Math.atan2(Math.sqrt(distEq1), Math.sqrt(1 - distEq1));
		distance = RADUIS_EARTH * distEq2;
		
		return distance;
	}
	
	public static String formatShoutAndPlaceTimestamp(Timestamp timestamp){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(BusinessConstants.DATEFORMAT_SHOUT_PLACE);
		
		return dateFormat.format(timestamp);
		
	}
	
	public static String generateFBPassword(){
		
		String fbPassword = "FB" + RandomStringUtils.randomAlphanumeric(20);
				
		return fbPassword;
	}

}
