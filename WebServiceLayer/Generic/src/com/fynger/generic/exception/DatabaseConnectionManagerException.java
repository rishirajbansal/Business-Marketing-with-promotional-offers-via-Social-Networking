/**
 * 
 */
package com.fynger.generic.exception;

import com.fynger.generic.exception.base.ExceptionDetail;

/**
 * @author Rishi
 *
 */
@SuppressWarnings("serial")
public class DatabaseConnectionManagerException extends ApplicationException{
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for DatabaseConnectionManagerException
     * @param msg - Message associated with the exception
     */
    public DatabaseConnectionManagerException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>DatabaseConnectionManagerException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public DatabaseConnectionManagerException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for DatabaseConnectionManagerException to set exceptionDetail
     */
    public DatabaseConnectionManagerException(ExceptionDetail exceptionDetail) {
            super(exceptionDetail);
    }
    
    /**
     * Returns the error message string of the exception object.
     *
     * @return  the error message string of this <code>Exception</code>
     *          object if it was <code>Exception</code> with an
     *          error message string; or <code>null</code> if it was
     *          <code>Exception</code> created} with no error message.
     *
     */
    public String getMessage() {
    	    	
        return error;
    }



}
