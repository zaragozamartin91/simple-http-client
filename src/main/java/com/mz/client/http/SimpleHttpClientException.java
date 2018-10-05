package com.mz.client.http;

public class SimpleHttpClientException extends RuntimeException {
    public SimpleHttpClientException(String message) {
        super(message);
    }

    public SimpleHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
