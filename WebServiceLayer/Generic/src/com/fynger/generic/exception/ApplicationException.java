/**
 * 
 */
package com.fynger.generic.exception;

import com.fynger.generic.common.GenericConstants;
import com.fynger.generic.exception.base.ExceptionDetail;

/**
 * @author Rishi
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException{
	
	protected Throwable throwable = null;
	
	private ExceptionDetail exceptionDetail;
	
	String error = "";
	
	/**
     * Constructor for ApplicationException
     * @param msg - Message associated with the exception
     */
    public ApplicationException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>ApplicationException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public ApplicationException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for ApplicationException to set exceptionDetail
     */
    public ApplicationException(ExceptionDetail exceptionDetail) {
            this.exceptionDetail = exceptionDetail ;
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
    	if (throwable == null) {
    		return GenericConstants.EMPTY_STRING;
        }
    	
    	return error;
    }
    
    public ExceptionDetail getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(ExceptionDetail exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

}
