package com.sebosystem.exception;

public class SeboException extends Exception {

    private static final long serialVersionUID = 1L;

    public SeboException() {
        super();
    }

    public SeboException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SeboException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeboException(String message) {
        super(message);
    }

    public SeboException(Throwable cause) {
        super(cause);
    }

}
