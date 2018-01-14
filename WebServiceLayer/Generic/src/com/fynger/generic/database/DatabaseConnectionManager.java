/**
 * 
 */
package com.fynger.generic.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.configManager.PropertyManager;
import com.fynger.generic.exception.DatabaseConnectionManagerException;
import com.fynger.generic.loggerManager.LoggerManager;
import com.fynger.generic.utilities.GenericUtility;


/**
 * @author Rishi
 *
 */
public class DatabaseConnectionManager {
	
	public static LoggerManager logger = GenericUtility.getLogger(DatabaseConnectionManager.class.getName());
	
	private static PropertyManager propertyManager 	= PropertyManager.getPropertyManager();
	
	private static final String driver;
	private static final String url;
	private static final String username;
	private static final String password;
	private static final String autoCommit;
	private static final String maxActivePoolSize;
	
	private static DataSource dataSource;
	
	private static DatabaseConnectionManager dbConManager = null;
	
	/** Object used for locking. */
	private static Object lockObject = new Object();
	
	static{
		Properties prop = propertyManager.getProperties(GenericConstants.DATABASE_PROPERTIES_FILE_NAME);
		
		driver = prop.getProperty(GenericConstants.DATABASE_DRIVER);
		url = prop.getProperty(GenericConstants.DATABASE_URL);
		username = prop.getProperty(GenericConstants.DATABASE_USERNAME);
		password = prop.getProperty(GenericConstants.DATABASE_PASSWORD);
		autoCommit = prop.getProperty(GenericConstants.DATABASE_AUTO_COMMIT);
		maxActivePoolSize = prop.getProperty(GenericConstants.DATABASE_ACTIVE_MAX_POOL_SIZE);
		
	}
	
	
	private void setupDataSource() throws DatabaseConnectionManagerException{
		
		try{
			logger.info("Loading the database driver...");
			Class.forName(driver);
			
			logger.info("Configuring the connection factory...");
			ConnectionFactory conFactory = new DriverManagerConnectionFactory(url, username, password);
			
			GenericObjectPool connectionPool = new GenericObjectPool();
			connectionPool.setMaxActive(Integer.parseInt(maxActivePoolSize));
			
			PoolableConnectionFactory poolConFactory = new PoolableConnectionFactory(conFactory, connectionPool, null, null, false, Boolean.parseBoolean(autoCommit));
			
			logger.info("Creating datasource...");
			dataSource = new PoolingDataSource(connectionPool);
		}
		catch(ClassNotFoundException cnfEx){
			logger.error(" ClassNotFoundException occured during setup Data source : " + cnfEx.getMessage());
			throw new DatabaseConnectionManagerException("ClassNotFoundException occured duting setup Data source ", cnfEx);
		}
		catch(Exception ex){
			logger.error(" Exception occured during setup Data source : " + ex.getMessage());
			throw new DatabaseConnectionManagerException("Exception occured duting setup Data source ", ex);
		}
		
	}
	
	public static synchronized Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	public static void instantiate() throws DatabaseConnectionManagerException{
		try{
			if (null == dbConManager) {
				synchronized(lockObject){
					dbConManager = new DatabaseConnectionManager();
					dbConManager.setupDataSource();
				}
			}
		}
		catch(Exception ex){
			logger.error(" Exception occured in instantiating : " + ex.getMessage());
			throw new DatabaseConnectionManagerException("Exception occured in in instantiatin ", ex);
		}
		catch(Throwable th){
			logger.error(" Throwable occured in instantiatin : " + th.getMessage());
			throw new DatabaseConnectionManagerException("Throwable occured in instantiatin ", th);
		}
	}
	
	public static synchronized void returnConnection(Connection con) throws DatabaseConnectionManagerException{
		try{
			if (null != con){
				con.close();
			}
		}
		catch(SQLException sqlEx){
			logger.error(" SQLException occured in  returnConnection  : " + sqlEx.getMessage());
			throw new DatabaseConnectionManagerException("SQLException occured in returnConnection ", sqlEx);
		}
	}
	
	public static synchronized void clearResources(Statement stmt, ResultSet rs) throws DatabaseConnectionManagerException{
		try{
			if (null != stmt){
				stmt.close();
				stmt = null;
			}
			if (null != rs){
				rs.close();
				rs = null;
			}
		}
		catch(SQLException sqlEx){
			logger.error(" SQLException occured in clearResources  : " + sqlEx.getMessage());
			throw new DatabaseConnectionManagerException("SQLException occured in clearResources ", sqlEx);
		}
	}
	
	public static synchronized void clearResources(Statement... stmts) throws DatabaseConnectionManagerException{
		try{
			for (Statement stmt : stmts){
				if (null != stmt){
					stmt.close();
					stmt = null;
				}
			}
		}
		catch(SQLException sqlEx){
			logger.error(" SQLException occured in clearResources  : " + sqlEx.getMessage());
			throw new DatabaseConnectionManagerException("SQLException occured in clearResources ", sqlEx);
		}
	}
	
	public static synchronized void clearResources(ResultSet... sets) throws DatabaseConnectionManagerException{
		try{
			for (ResultSet rs : sets){
				if (null != rs){
					rs.close();
					rs = null;
				}
			}
		}
		catch(SQLException sqlEx){
			logger.error(" SQLException occured in clearResources  : " + sqlEx.getMessage());
			throw new DatabaseConnectionManagerException("SQLException occured in clearResources ", sqlEx);
		}
	}
	
	public static boolean testDBConnection() throws DatabaseConnectionManagerException{
		Connection con = null;
		
		try{
			con = DatabaseConnectionManager.getConnection();
			
			DatabaseMetaData metaData = con.getMetaData();
			
			if (null != metaData){
				logger.info("Database product Name: " + metaData.getDatabaseProductName());
				logger.info("Database product Version: " + metaData.getDatabaseProductVersion());
				
				logger.info("Test DB connection is successful.");
				
				return true;
			}
			else{
				logger.error("Test DB connection failed.");
				
				return false;
			}
			
		}
		catch(SQLException sqlEx){
			logger.error(" SQLException occured in testDBConnection  : " + sqlEx.getMessage());
			return false;
		}
		finally{
			DatabaseConnectionManager.returnConnection(con);
		}
	}

}
