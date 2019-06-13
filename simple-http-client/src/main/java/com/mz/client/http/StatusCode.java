package com.mz.client.http;

import org.apache.http.HttpStatus;

/**
 * General and specific http status codes
 */
public enum StatusCode {
    /**
     * Specific 201 status code
     */
    CREATED(HttpStatus.SC_CREATED),
    /**
     * Specific 204 status code
     */
    NO_CONTENT(HttpStatus.SC_NO_CONTENT),
    /**
     * Specific 403 status code
     */
    FORBIDDEN(HttpStatus.SC_FORBIDDEN),
    /**
     * Specific 401 status code
     */
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED),
    /**
     * Specific 404 status code
     */
    NOT_FOUND(HttpStatus.SC_NOT_FOUND),

    /**
     * General status code. Ranges from 100 to 199 (inclusive)
     */
    S_1xx(100),
    /**
     * General status code. Ranges from 200 to 299 (inclusive)
     */
    S_2xx(200),
    /**
     * General status code. Ranges from 300 to 399 (inclusive)
     */
    S_3xx(300),
    /**
     * General status code. Ranges from 400 to 499 (inclusive)
     */
    S_4xx(400),
    /**
     * General status code. Ranges from 500 to 599 (inclusive)
     */
    S_5xx(500);

    private final int value;

    StatusCode(int code) { this.value = code; }

    public static StatusCode fromCode(int code) {
        for (StatusCode statusCode : StatusCode.values()) { if (statusCode.value == code) { return statusCode; } }

        int genCode = Double.valueOf(Math.floor(code / 100) * 100).intValue();
        for (StatusCode statusCode : StatusCode.values()) { if (statusCode.value == genCode) { return statusCode; } }

        throw new IllegalArgumentException("Invalid status code " + code);
    }
}
