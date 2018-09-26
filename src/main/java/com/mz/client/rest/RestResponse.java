package com.mz.client.rest;

import org.apache.http.HttpResponse;

import java.util.Locale;
import java.util.Optional;

public class RestResponse {
    private HttpResponse httpResponse;
    private String body;

    public RestResponse(HttpResponse httpResponse, String body) {
        this.httpResponse = httpResponse;
        this.body = body;
    }

    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    public Locale getLocale() {
        return httpResponse.getLocale();
    }

    public boolean containsHeader(String s) {
        return httpResponse.containsHeader(s);
    }

    public String getContentType() {
        return httpResponse.getEntity().getContentType().getValue();
    }

    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

    public boolean isOk() {
        int statusCode = this.getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    @Override public String toString() {
        return "RestResponse{" +
                "code=" + getStatusCode() +
                ", contentType='" + getContentType() +
                "', body='" + body + '\'' +
                '}';
    }
}
