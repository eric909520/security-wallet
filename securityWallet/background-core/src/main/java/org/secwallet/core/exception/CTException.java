package org.secwallet.core.exception;

/**
 * Platform abstract superclass, other custom exceptions are based on this class
 */
public class CTException extends  RuntimeException{

    public CTException(String msg,Throwable throwable){
        super(msg,throwable);
    }

    public CTException(String msg){
        super(msg);
    }
}
