/**
 * 
 */
package com.fynger.generic.configManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.PropertyManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;

/**
 * @author Rishi Raj
 *
 */
public class PropertyManager extends Properties {
	

	private static final long serialVersionUID = 1L;

	public static LoggerManager logger = GenericUtility.getLogger(PropertyManager.class.getName());
	
	private static PropertyManager propertyManager = null;
	private static String propertiesFilePath = null;
	private static String commonPropertyFile = GenericConstants.COMMON_PROPERTIES_FILE_NAME;
	
	private Map<String,Properties> hmPropertiesContainer = new HashMap<String,Properties>();	
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	
	private PropertyManager() throws PropertyManagerException{
		
		try {
			load(commonPropertyFile);
		}
		catch(Exception e) {
			logger.error(" Exception occured while loading ProptertyFile ["+PropertyManager.commonPropertyFile+".properties] : "+e.getMessage());
			throw new PropertyManagerException("Property Manager failed to load common property file ["+PropertyManager.commonPropertyFile+".properties] : "+e.getMessage());
		}
	}
	
	public static PropertyManager createInstance(String propFilePath)  { 
		
		propertiesFilePath = propFilePath;
		
		if (propertyManager == null) {
			synchronized (lockObject) {
				propertyManager = new PropertyManager();
			}
		}

		return propertyManager;
	}
	
	public static PropertyManager getPropertyManager(){
		return propertyManager;
	}
	
	public synchronized void load(String propFile) throws Exception {
		
		Properties props = new Properties();
		
		try{
			if (propertiesFilePath.lastIndexOf(GenericConstants.FILE_SEPARATOR) != propertiesFilePath.length() - 1){
				propertiesFilePath = propertiesFilePath + GenericConstants.FILE_SEPARATOR;
			}
			
			FileInputStream fin = new FileInputStream(propertiesFilePath + propFile) ;
			props.load(fin);
			hmPropertiesContainer.put(propFile.toUpperCase(), props);
			fin.close();
		}
		catch (FileNotFoundException fe){
			logger.error("Exception occured while reading the property file :"+fe.getMessage());
			throw new PropertyManagerException("Exception occured while reading the property file ", fe);
		}
		
	}
	
	public Properties getProperties(String propFileName) throws PropertyManagerException{
		
		Properties pReturn = hmPropertiesContainer.get(propFileName.toUpperCase());
		
		if (pReturn == null) {
			//try to load the properties file for first time, add to hmPropertiesContainer and return value requested
			try {
				load(propFileName);
				pReturn = hmPropertiesContainer.get(propFileName.toUpperCase());
			}
			catch(Exception e) {
				logger.error("Exception loading ProptertyFile [ "+propFileName+" ] :"+ e.getMessage());
				throw new PropertyManagerException("Could not find/open file [ "+ propFileName + " ]", e);
			}
		}
		
		return pReturn;
		
	}
	
	public String getProperty(String propFileName, String key) throws PropertyManagerException {
		String value = "";
		Properties prop = hmPropertiesContainer.get(propFileName.toUpperCase());
		
		if (prop!=null) {
			value = (String)prop.get(key);
		}
		else {
			//try to load the properties file for first time, add to hmProperties and return value requested
			try {
				load(propFileName);
				prop = hmPropertiesContainer.get(propFileName.toUpperCase());
				
				if (prop!=null) {
					value = (String)prop.get(key);
				}
			}
			catch(Exception e) {
				logger.error("Exception loading ProptertyFile [ "+propFileName+" ] : "+ e.getMessage());
				throw new PropertyManagerException("Could not find/open file [ "+propFileName+" ]", e);
			}
		}
		
		return value;
	}

}
