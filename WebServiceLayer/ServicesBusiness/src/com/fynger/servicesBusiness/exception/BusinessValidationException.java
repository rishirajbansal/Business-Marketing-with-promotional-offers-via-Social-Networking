package com.fynger.servicesBusiness.exception;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.ExceptionDetail;

@SuppressWarnings("serial")
public class BusinessValidationException extends ApplicationException{
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for BusinessValidationException
     * @param msg - Message associated with the exception
     */
    public BusinessValidationException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>BusinessValidationException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public BusinessValidationException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for BusinessValidationException to set exceptionDetail
     */
    public BusinessValidationException(ExceptionDetail exceptionDetail) {
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
