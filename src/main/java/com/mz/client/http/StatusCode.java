package com.mz.client.http;

import org.apache.http.HttpStatus;

public enum StatusCode {
    OK(HttpStatus.SC_OK),
    CREATED(HttpStatus.SC_CREATED),
    NO_CONTENT(HttpStatus.SC_NO_CONTENT),
    FORBIDDEN(HttpStatus.SC_FORBIDDEN),
    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED),
    S_1xx(100),
    S_2xx(200),
    S_3xx(300),
    S_4xx(400),
    S_5xx(500);

    private final int value;

    StatusCode(int code) {
        this.value = code;
    }

    public static StatusCode fromCode(int code) {
        for (StatusCode statusCode : StatusCode.values()) { if (statusCode.value == code) { return statusCode; } }

        int ccode = Double.valueOf(Math.floor(code / 100) * 100).intValue();
        for (StatusCode statusCode : StatusCode.values()) { if (statusCode.value == ccode) { return statusCode; } }

        throw new IllegalArgumentException("Invalid status code " + code);
    }
}
