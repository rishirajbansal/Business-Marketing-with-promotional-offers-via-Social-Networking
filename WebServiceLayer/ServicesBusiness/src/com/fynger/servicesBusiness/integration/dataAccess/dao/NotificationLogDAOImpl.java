package com.fynger.servicesBusiness.integration.dataAccess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fynger.generic.database.DatabaseConnectionManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;
import com.fynger.servicesBusiness.exception.DataAccessException;
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.NotificationLogData;

public class NotificationLogDAOImpl implements INotificationLogDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(NotificationLogDAOImpl.class.getName());
	
	private static final String SQL_INSERT_NOTIFICATION_LOG_DETAILS  = "INSERT into notification_log (username, event, dispatch_status, comments,text,device_reg_id, device_type, retried_count,sent_time,created_on)" +
																		" values( ?, ?, ?, ?, ?, ?, ?, ?, ?,NOW())";
	

	@Override
	public boolean storeNotificationLog(NotificationLogData notificationLogData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			ps = con.prepareStatement(SQL_INSERT_NOTIFICATION_LOG_DETAILS);
			ps.setString(1, notificationLogData.getUserName());
			ps.setString(2, notificationLogData.getEvent());
			ps.setString(3, notificationLogData.getDispatchStatus());
			ps.setString(4, notificationLogData.getComments());
			ps.setString(5, notificationLogData.getText());
			ps.setString(6, notificationLogData.getDeviceRegId());			
			ps.setInt(7, notificationLogData.getDeviceType());
			ps.setInt(8, notificationLogData.getRetriedCount());
			ps.setTimestamp(9, notificationLogData.getSentTime());			
						
			logger.debug("[storeNotificationLog()-QUERY 1] : " + ps.toString());					
		
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				throw new DataAccessException("storeNotificationLog() -> Failed to insert record for notification log ");
			}
			else{
				logger.debug("notification log is saved successfully");
				flag = true;
				con.commit();
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("storeNotificationLog", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("storeNotificationLog() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("storeNotificationLog", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("storeNotificationLog() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("storeNotificationLog() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

}
