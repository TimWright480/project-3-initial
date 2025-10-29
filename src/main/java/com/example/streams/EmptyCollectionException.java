package com.example.streams;

public class EmptyCollectionException extends RuntimeException {

    public EmptyCollectionException() {
    }

    public EmptyCollectionException(String message) {
        super(message);
    }

    public EmptyCollectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyCollectionException(Throwable cause) {
        super(cause);
    }
}

