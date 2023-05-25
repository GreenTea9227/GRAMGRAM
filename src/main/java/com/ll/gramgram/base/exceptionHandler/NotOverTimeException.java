package com.ll.gramgram.base.exceptionHandler;

public class NotOverTimeException extends RuntimeException {
    public NotOverTimeException() {
        super();
    }

    public NotOverTimeException(String message) {
        super(message);
    }

    public NotOverTimeException(Throwable cause) {
        super(cause);
    }
}
