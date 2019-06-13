package com.mz.client.http;

import org.junit.Test;

import static org.junit.Assert.*;

public class StatusCodeTest {

    @Test
    public void fromCode() {
        assertEquals(StatusCode.fromCode(201), StatusCode.CREATED);
        assertEquals(StatusCode.fromCode(204), StatusCode.NO_CONTENT);
        assertEquals(StatusCode.fromCode(200), StatusCode.S_2xx);
        assertEquals(StatusCode.fromCode(210), StatusCode.S_2xx);

        assertEquals(StatusCode.fromCode(403), StatusCode.FORBIDDEN);
        assertEquals(StatusCode.fromCode(404), StatusCode.NOT_FOUND);
        assertEquals(StatusCode.fromCode(401), StatusCode.UNAUTHORIZED);
        assertEquals(StatusCode.fromCode(420), StatusCode.S_4xx);
        assertEquals(StatusCode.fromCode(400), StatusCode.S_4xx);

        assertEquals(StatusCode.fromCode(500), StatusCode.S_5xx);
        assertEquals(StatusCode.fromCode(599), StatusCode.S_5xx);
    }
}