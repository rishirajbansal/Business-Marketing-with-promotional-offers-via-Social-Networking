/**
 * 
 */
package com.fynger.servicesBusiness.exception;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.ExceptionDetail;

/**
 * @author Rishi
 *
 */
@SuppressWarnings("serial")
public class ForgotPasswordVerificationFailedException extends ApplicationException {
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for ForgotPasswordVerificationFailedException
     * @param msg - Message associated with the exception
     */
    public ForgotPasswordVerificationFailedException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>ForgotPasswordVerificationFailedException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public ForgotPasswordVerificationFailedException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for ForgotPasswordVerificationFailedException to set exceptionDetail
     */
    public ForgotPasswordVerificationFailedException(ExceptionDetail exceptionDetail) {
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
