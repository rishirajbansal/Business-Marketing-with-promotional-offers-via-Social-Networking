package com.fynger.searchEngine.query.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;

public class ValidityManager {

    public static Logger logger = GenericUtility.getLogger(ValidityManager.class.getName());
    private String validity;

    public boolean getValidityStatus(String validtill) {

        /** Converting the validity timestamp into number of days and hours from the current timpstamp*/
        logger.info("Checking the Validity of the promotion");

        Date today = new Date();

        long currentTimeMilliseconds = today.getTime();
        long validtillMilliseconds;
        long validityMilliseconds;

        int hours, days;

        try {

            /** Setting the format of the time stamp*/
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(validtill);

            validtillMilliseconds = parsedDate.getTime();

            validityMilliseconds = validtillMilliseconds - currentTimeMilliseconds;
            
            if (validityMilliseconds > SearchEngineConstants.INT_ZERO) {

                hours = (int) ((validityMilliseconds / (SearchEngineConstants.INT_MILLISECONDS * SearchEngineConstants.INT_SECONDS * SearchEngineConstants.INT_MINUTES)) % SearchEngineConstants.INT_HOURS);
                days = (int) (validityMilliseconds / (SearchEngineConstants.INT_MILLISECONDS * SearchEngineConstants.INT_SECONDS * SearchEngineConstants.INT_MINUTES * SearchEngineConstants.INT_HOURS));

                /** Setting the validity **/
                if (days == SearchEngineConstants.INT_ONE && hours == SearchEngineConstants.INT_ONE) {
                    validity = days + SearchEngineConstants.STRING_DAY_AND + hours + SearchEngineConstants.STRING_HOUR;
                } else if (days > SearchEngineConstants.INT_ONE && hours == SearchEngineConstants.INT_ONE) {
                    validity = days + SearchEngineConstants.STRING_DAYS_AND + hours + SearchEngineConstants.STRING_HOUR;
                } else if (days == SearchEngineConstants.INT_ONE && hours > SearchEngineConstants.INT_ONE) {
                    validity = days + SearchEngineConstants.STRING_DAY_AND + hours + SearchEngineConstants.STRING_HOURS;
                }
                else if (days == SearchEngineConstants.INT_ZERO && hours == SearchEngineConstants.INT_ONE) {
                    validity = hours + SearchEngineConstants.STRING_HOUR;
                }
                else if (days == SearchEngineConstants.INT_ZERO && hours > SearchEngineConstants.INT_ONE) {
                    validity = hours + SearchEngineConstants.STRING_HOURS;
                }
                else if (days == SearchEngineConstants.INT_ONE && hours == SearchEngineConstants.INT_ZERO) {
                    validity = days + SearchEngineConstants.STRING_DAY;
                }
                else if (days > SearchEngineConstants.INT_ONE && hours == SearchEngineConstants.INT_ZERO) {
                    validity = days + SearchEngineConstants.STRING_DAYS;
                }
                else {
                    validity = days + SearchEngineConstants.STRING_DAYS_AND + hours + SearchEngineConstants.STRING_HOURS;
                }

            } else {
               
               return false; 
            }

        
        } catch (Exception ex) {
            logger.error(" EngineException occured in Converting promotion validity timestamp into promotion validity string : " + ex.getMessage());
            throw new SearchEngineException("Exception occured in converting promotion validity timestamp into promotion validity string ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in Converting promotion validity timestamp into promotion validity string : " + th.getMessage());
            throw new SearchEngineException("Throwable occured in Converting promotion validity timestamp into promotion validity string : ", th);
        }

        return true;
    }

    public String getValidity() {
        return validity;
    }
}
