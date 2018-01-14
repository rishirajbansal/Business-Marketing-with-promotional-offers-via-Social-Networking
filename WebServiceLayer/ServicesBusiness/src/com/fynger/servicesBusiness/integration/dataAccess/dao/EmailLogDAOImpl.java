/**
 * 
 */
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
import com.fynger.servicesBusiness.integration.dataAccess.dataObjects.EmailLogData;

/**
 * @author Rishi
 *
 */
public class EmailLogDAOImpl implements IEmailLogDAO {
	
	public static LoggerManager logger = GenericUtility.getLogger(EmailLogDAOImpl.class.getName());
	
	private static final String SQL_INSERT_EMAIL_LOG	=	"INSERT INTO email_log(username, event, dispatch_status, comments, sent_time, created_on) VALUES(?, ?, ?, ?, ?, NOW())";


	@Override
	public boolean storeEmailLog(EmailLogData emailLogData) throws DataAccessException {
		
		boolean flag = false;
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			ps = con.prepareStatement(SQL_INSERT_EMAIL_LOG);
			
			ps.setString(1, emailLogData.getUsername());
			ps.setString(2, emailLogData.getEvent());
			ps.setString(3, emailLogData.getDispatchStatus());
			ps.setString(4, emailLogData.getComments());
			ps.setTimestamp(5, emailLogData.getSentTime());
			
			logger.debug("[storeEmailLog()-QUERY] : " + ps.toString());
			
			int rowsInserted = ps.executeUpdate();
			
			if (rowsInserted <= 0){
				throw new DataAccessException("storeEmailLog() -> Failed to insert record for email log ");
			}
			else{
				logger.debug("Email log is saved successfully");
				flag = true;
				con.commit();
			}
			
		}
		catch(SQLException sqlEx){
			logger.error("storeEmailLog", "SQLException occurred in DAO layer : " + sqlEx.getMessage());
			throw new DataAccessException("storeEmailLog() -> SQLException occurred in DAO layer : " + sqlEx.getMessage());
		}
		catch(Exception ex){
			logger.error("storeEmailLog", "Exception occurred in DAO layer : " + ex.getMessage());
			throw new DataAccessException("storeEmailLog() -> Exception occurred in DAO layer : " + ex.getMessage());
		}
		finally{
			try {
				DatabaseConnectionManager.returnConnection(con);
				DatabaseConnectionManager.clearResources(ps, rs);
			} 
			catch (DatabaseConnectionManagerException dcmEx) {
				throw new DataAccessException("storeEmailLog() -> DatabaseConnectionManagerException occured during closing resources ", dcmEx);
			}
		}
		
		return flag;

	}

}
