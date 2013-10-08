package com.ht.dl645.exception;

public class TransportException extends Exception {
    private static final long serialVersionUID = -1;

    public TransportException() {
        super();
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransportException(String message) {
        super(message);
    }

    public TransportException(Throwable cause) {
        super(cause);
    }
}
