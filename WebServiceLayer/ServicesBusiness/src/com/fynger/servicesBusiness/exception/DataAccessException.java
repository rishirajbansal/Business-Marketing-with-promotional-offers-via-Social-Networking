package com.fynger.servicesBusiness.exception;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.ExceptionDetail;

@SuppressWarnings("serial")
public class DataAccessException extends ApplicationException{
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for DataAccessException
     * @param msg - Message associated with the exception
     */
    public DataAccessException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>DataAccessException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public DataAccessException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for DataAccessException to set exceptionDetail
     */
    public DataAccessException(ExceptionDetail exceptionDetail) {
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
