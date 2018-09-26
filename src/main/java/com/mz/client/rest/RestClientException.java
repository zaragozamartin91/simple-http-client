package com.mz.client.rest;

public class RestClientException extends RuntimeException {
    public RestClientException(String message) {
        super(message);
    }

    public RestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
