/**
 * 
 */
package com.fynger.servicesController.exception;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.ExceptionDetail;

/**
 * @author Rishi
 *
 */
@SuppressWarnings("serial")
public class BadRequestException extends ApplicationException {
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for BadRequestException
     * @param msg - Message associated with the exception
     */
    public BadRequestException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>BadRequestException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public BadRequestException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for BadRequestException to set exceptionDetail
     */
    public BadRequestException(ExceptionDetail exceptionDetail) {
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
