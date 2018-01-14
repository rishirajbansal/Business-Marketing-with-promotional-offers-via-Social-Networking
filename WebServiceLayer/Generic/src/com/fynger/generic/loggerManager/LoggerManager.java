/**
 * 
 */
package com.fynger.generic.loggerManager;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * @author Rishi Raj
 *
 */
public class LoggerManager extends Logger{
	
	private Logger logger;
	
	private static String LOGGER_SYSTEM_DEFAULT = "FYNGER_CONSOLE";
	
	public LoggerManager (String category, String className) {
		super(category + "." + className);
		logger = Logger.getLogger(category + "." + className);
	}
	
	public LoggerManager (String className) {
		this(LOGGER_SYSTEM_DEFAULT, className);
		//super(className);
		//logger = Logger.getLogger(className);
	}
	
	private String format(Object arg) {
		return arg.toString().trim();
	}
	
	private String format(String methodName, Object arg) {
		return methodName + "() -> " + arg.toString().trim();
	}
	
	public void debug(Object arg0) {
		if (logger.isDebugEnabled()) {
			logger.debug(format(arg0));
		}
	}
	
	public void debug(String methodName, Object arg1) {
		if (logger.isDebugEnabled()) {
			logger.debug(format(methodName, arg1));
		}
	}
	
	public void info(Object arg0) {
		if (logger.isInfoEnabled()) {
			logger.info(format(arg0));
		}
	}
	
	public void warn(Object arg0) {
		logger.warn(format(arg0));
	}
	
	public void error(Object arg0) {
		logger.error(format(arg0));
	}
	
	public void error(String methodName, Object arg1) {
		logger.error(format(methodName, arg1));
	}
	
	public void fatal(Object arg0) {
		logger.fatal(format(arg0));
	}
	
	public void trace(Object arg0) {
		logger.trace(format(arg0));
	}
	
	public void error(Throwable e) {
		error(toStackTrace(e));
	}
	
	private String toStackTrace(Throwable e) {
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		return stackTrace.toString();
	}

    public boolean isDebugEnabled() {
		return logger.isDebugEnabled(); 
    }
    
    public boolean isInfoEnabled() {
		return logger.isInfoEnabled(); 
    }
    
    public boolean isTraceEnabled() {
		return logger.isTraceEnabled(); 
    }
    
    public void debug(Object arg0, Throwable t) {
		if (logger.isDebugEnabled()) 
			logger.debug(format(arg0), t);
	}
    
	public void info(Object arg0, Throwable t) {
		if (logger.isInfoEnabled()) 
			logger.info(format(arg0), t);
	}
	
	public void warn(Object arg0, Throwable t) {
		logger.warn(format(arg0), t);
	}
	
	public void error(Object arg0, Throwable t) {
		logger.error(format(arg0), t);
	}
	
	public void fatal(Object arg0, Throwable t) {
		logger.fatal(format(arg0), t);
	}
	
	public void trace(Object arg0, Throwable t) {
		logger.trace(format(arg0), t);
	}

}
