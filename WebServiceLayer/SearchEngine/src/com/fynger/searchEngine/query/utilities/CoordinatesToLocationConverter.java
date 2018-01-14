package com.fynger.searchEngine.query.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;

public class CoordinatesToLocationConverter {

    public static Logger logger = GenericUtility.getLogger(CoordinatesToLocationConverter.class.getName());
    private String locationString;
    private double lat = SearchEngineConstants.NaN;
    private double lng = SearchEngineConstants.NaN;

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double lat) {
        this.lat = lat;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double lng) {
        this.lng = lng;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.locationString;
    }

    /**
     * @return the locationStatus
     */
    public boolean getLocationStatus() {

    	  try {
              String URL = SearchEngineConstants.GEOCODER_REQUEST_PREFIX_FOR_XML + SearchEngineConstants.LATLNG_PREFIX + URLEncoder.encode(lat + SearchEngineConstants.COMMA + lng, SearchEngineConstants.TEXT_ENCODING) + SearchEngineConstants.SENSOR_FALSE;
                
              DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
              DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
              Document dom = documentBuilder.parse(URL);
              Element documentElement = dom.getDocumentElement();
              NodeList node = documentElement.getElementsByTagName(SearchEngineConstants.STRING_RESULT);                     
              
              if (node != null && node.getLength() > SearchEngineConstants.INT_ZERO) {            
              	locationString = ((Element) node.item(SearchEngineConstants.INT_ZERO)).getElementsByTagName(SearchEngineConstants.STRING_FORMATTED_ADDRESS).item(SearchEngineConstants.INT_ZERO).getTextContent();                                
              }
              if (GenericUtility.safeTrim(locationString).equals(GenericConstants.EMPTY_STRING)) {          
              	return false;
              }
          } catch (UnsupportedEncodingException ue) {
              logger.error(" Exception occured in instantiating Coordinates : " + ue.getMessage());
              throw new SearchEngineException("Exception occured in instantiatin Coordinates ", ue);
          } catch (Exception ex) {
              logger.error(" Exception occured in instantiating Coordinates : " + ex.getMessage());
              throw new SearchEngineException("Exception occured in instantiatin Coordinates ", ex);
          } catch (Throwable th) {
              logger.error(" Throwable occured in instantiatin Coordinates : " + th.getMessage());
              throw new SearchEngineException("Throwable occured in instantiatin Coordinates ", th);
          }

          return true;

    }
}
