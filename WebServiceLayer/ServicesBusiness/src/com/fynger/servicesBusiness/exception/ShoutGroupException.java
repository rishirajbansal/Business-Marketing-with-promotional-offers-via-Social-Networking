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
public class ShoutGroupException extends ApplicationException {
	
	protected Throwable throwable = null;
	
	String error = "";
	
	String code = "";
	
	/**
     * Constructor for ShoutGroupException
     * @param msg - Message associated with the exception
     */
    public ShoutGroupException(String msg) {
    	super(msg);
    	error = msg;
    }
    
    /**
     * Constructor for ShoutGroupException
     * @param msg - Message associated with the exception
     */
    public ShoutGroupException(String code, String msg) {
    	super(msg);
    	error = msg;
    	this.code = code;
    }

    /**
     * Initializes a newly created <code>ShoutGroupException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public ShoutGroupException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for ShoutGroupException to set exceptionDetail
     */
    public ShoutGroupException(ExceptionDetail exceptionDetail) {
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
    
    public String getCode(){
    	return code;
    }



}
