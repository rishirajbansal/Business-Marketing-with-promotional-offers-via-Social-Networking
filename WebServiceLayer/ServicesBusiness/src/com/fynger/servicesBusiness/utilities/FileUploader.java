/**
 * 
 */
package com.fynger.servicesBusiness.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.FileUploadFailedException;

/**
 * @author Rishi
 *
 */
public class FileUploader {
	
	public static LoggerManager logger = GenericUtility.getLogger(FileUploader.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	
	public void uploadFile(InputStream uploadedInputStream, String uploadedFileLocation){
		
		OutputStream out = null;
		
		logger.debug("Uploading the file : " + uploadedFileLocation);
		
		try{
			out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
			
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			
			out.flush();
		}
		catch (IOException fileEx){
			logger.error("IOException occur in file uploading : " + fileEx.getMessage());
			throw new FileUploadFailedException("IOException occur in file uploading : " + fileEx.getMessage());
		}
		catch (Exception ex){
			logger.error("Exception occur in file uploading : " + ex.getMessage());
			throw new FileUploadFailedException("Exception occur in file uploading : " + ex.getMessage());
		}
		finally{
			try{
				if (null != out){
					out.close();
				}
			}
			catch (IOException fileEx){
				logger.error("IOException occur in file uploading : " + fileEx.getMessage());
				throw new FileUploadFailedException("IOException occur in file uploading : " + fileEx.getMessage());
			}
			catch (Exception ex){
				logger.error("Exception occur in file uploading : " + ex.getMessage());
				throw new FileUploadFailedException("Exception occur in file uploading : " + ex.getMessage());
			}
		}
		
	}

	
	public String generateUserPictureUploadFilePath(String username, String fileExt){
		
		//String host = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_HOST);
		String picturePath = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_PICTURE_UPLOAD_PATH);
		
		String filePath = picturePath + username + "." + fileExt;
		
		return filePath;
		
	}
	
	public String generateUserPictureDownloadFilePath(String username, String fileExt){
		
		//String host = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_HOST);
		String picturePath = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_PICTURE_DOWNLOAD_PATH);
		
		String filePath = picturePath + username + "." + fileExt;
		
		return filePath;
		
	}
	
	public String generateUploadFilePath(String prefix, String fileName){
		
		//String host = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_HOST);
		String picturePath = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_PICTURE_UPLOAD_PATH);
		
		String filePath = picturePath + prefix + fileName;
		
		return filePath;
		
	}
	
	public String generateDownloadFilePath(String prefix, String fileName){
		
		//String host = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_HOST);
		String picturePath = propertyManager.getProperty(GenericConstants.COMMON_PROPERTIES_FILE_NAME, GenericConstants.FILE_SERVER_PICTURE_DOWNLOAD_PATH);
		
		String filePath = picturePath + prefix + fileName;
		
		return filePath;
		
	}
	
	
}
