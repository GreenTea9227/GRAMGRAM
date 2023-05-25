package com.ll.gramgram.base.exceptionHandler;

public class AccessDeniedByException extends RuntimeException {

    public AccessDeniedByException() {
        super();
    }

    public AccessDeniedByException(String message) {
        super(message);
    }

    public AccessDeniedByException(Throwable cause) {
        super(cause);
    }
}
