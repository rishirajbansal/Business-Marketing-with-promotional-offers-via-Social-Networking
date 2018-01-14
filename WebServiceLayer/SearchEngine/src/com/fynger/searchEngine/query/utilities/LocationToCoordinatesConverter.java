package com.fynger.searchEngine.query.utilities;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fynger.generic.utilities.GenericUtility;
import com.fynger.searchEngine.query.constants.SearchEngineConstants;
import com.fynger.searchEngine.query.exception.SearchEngineException;


public class LocationToCoordinatesConverter {
	
	public static Logger logger = GenericUtility.getLogger(LocationToCoordinatesConverter.class.getName());
    
    private double lat = SearchEngineConstants.NaN;
    private double lng = SearchEngineConstants.NaN;

    public boolean setLocation(String location) {        
    	
        try {
            URL url = new URL(SearchEngineConstants.GEOCODER_REQUEST_PREFIX_FOR_XML + SearchEngineConstants.ADDRESS_PREFIX + URLEncoder.encode(location, SearchEngineConstants.TEXT_ENCODING) + SearchEngineConstants.SENSOR_FALSE);

            /** prepare an HTTP connection  */
            logger.info("Making Http Connection to Retrieve Geo Coordinates...");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Document geocoderResultDocument = null;

            try {
                /** open the connection and get results as InputSource. */
                conn.connect();
                InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());

                logger.info("Connection Successful...");
                /** read result and parse into XML Document */
                geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
            } finally {
                conn.disconnect();
            }

            /** prepare XPath */
            XPath xpath = XPathFactory.newInstance().newXPath();

            /** extract the result */
            NodeList resultNodeList = null;
            resultNodeList = (NodeList) xpath.evaluate(SearchEngineConstants.GEOCODE_RESPONSE_LOCATION, geocoderResultDocument, XPathConstants.NODESET);

            for (int i = 0; i < resultNodeList.getLength(); ++i) {
                Node node = resultNodeList.item(i);
                if (SearchEngineConstants.LAT.equals(node.getNodeName())) {
                    lat = Float.parseFloat(node.getTextContent());
                }
                if (SearchEngineConstants.LNG.equals(node.getNodeName())) {
                    lng = Float.parseFloat(node.getTextContent());
                }
            }
            
            if (Double.isNaN(lat)  || Double.isNaN(lng)) {
            	return false;
            }
            

        } catch (Exception ex) {
            logger.error(" Exception occured in instantiating Coordinates : " + ex.getMessage());
            throw new SearchEngineException("Exception occured in instantiatin Coordinates ", ex);
        } catch (Throwable th) {
            logger.error(" Throwable occured in instantiatin Coordinates : " + th.getMessage());
            throw new SearchEngineException("Throwable occured in instantiatin Coordinates ", th);
        }
        return true;
        
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }

}
