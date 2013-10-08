package com.ht.dl645.exception;

import com.ht.dl645.msg.request.AbstractDL645Request;
import com.ht.dl645.msg.response.AbstractDL645Response;

public class ErrorResponseException extends Exception {
    private static final long serialVersionUID = -1;

    private AbstractDL645Request originalRequest;
    private AbstractDL645Response errorResponse;
    
    public ErrorResponseException(String message, AbstractDL645Request originalRequest, AbstractDL645Response errorResponse) {
    	super(message);
        this.originalRequest = originalRequest;
        this.errorResponse = errorResponse;
    }

    public AbstractDL645Response getErrorResponse() {
        return errorResponse;
    }

    public AbstractDL645Request getOriginalRequest() {
        return originalRequest;
    }
}
