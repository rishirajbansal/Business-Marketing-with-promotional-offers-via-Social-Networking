package com.fynger.notificationEngine.exception;

import com.fynger.generic.exception.ApplicationException;
import com.fynger.generic.exception.base.ExceptionDetail;


@SuppressWarnings("serial")
public class NotificationEngineException extends ApplicationException{
	
	protected Throwable throwable = null;
	
	String error = "";
	
	/**
     * Constructor for NotificationEngineException
     * @param msg - Message associated with the exception
     */
    public NotificationEngineException(String msg) {
    	super(msg);
    	error = msg;
    }

    /**
     * Initializes a newly created <code>NotificationEngineException</code> object.
     * @param	msg - the message associated with the Exception.
     * @param   cause - Throwable object
     */
    public NotificationEngineException(String msg, Throwable cause) {
    	super(msg, cause);
    }
    
    /**
     * Constructor for NotificationEngineException to set exceptionDetail
     */
    public NotificationEngineException(ExceptionDetail exceptionDetail) {
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
