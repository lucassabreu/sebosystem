package com.sebosystem.exception;

/**
 * A {@link Exception} extension for the SeboSystem, supports translation on the
 * front end
 * 
 * @author Lucas dos Santos Abreu {@code lucas.s.abreu@gmail.com}
 * @author Fabrício Felisbino {@code fabricio.felisbino@outlook.com}
 */
public class SeboException extends Exception {

    private static final long serialVersionUID = -4936115403603282067L;

    private Object[] parameters;

    /**
     * Create a exception based on the message and parameters <br/>
     * {@inheritDoc}
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     * @param parameters
     */
    public SeboException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... parameters) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.parameters = parameters;
    }

    /**
     * Create a exception based on the message and parameters <br/>
     * {@inheritDoc}
     * 
     * @param message
     * @param cause
     * @param parameters
     */
    public SeboException(String message, Throwable cause, Object... parameters) {
        super(message, cause);
        this.parameters = parameters;
    }

    /**
     * Create a exception based on the message and parameters <br/>
     * {@inheritDoc}
     * 
     * @param message
     * @param parameters
     */
    public SeboException(String message, Object... parameters) {
        super(message);
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     * 
     * @param cause
     */
    public SeboException(Throwable cause) {
        super(cause);
        this.parameters = new Object[] {};
    }

    /**
     * Retrieve the parameters attached to the {@link SeboException}
     * 
     * @return
     */
    public Object[] getParamenters() {
        return this.parameters;
    }

}
