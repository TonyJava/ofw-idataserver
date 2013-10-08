package com.ht.dl645.service.serial;


public class SerialPortUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    public SerialPortUnavailableException(String details) {
        super(details);
    }
}
