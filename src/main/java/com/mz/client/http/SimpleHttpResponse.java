package com.mz.client.http;

import org.apache.http.HttpResponse;

import java.util.Locale;

/**
 * Simple http response representation.
 */
public class SimpleHttpResponse {
    private HttpResponse httpResponse;
    private SimpleHttpBody body;

    SimpleHttpResponse(HttpResponse httpResponse, String body) {
        this.httpResponse = httpResponse;
        this.body = new SimpleHttpBody(body);
    }

    /**
     * HTTP Status code
     *
     * @return status code.
     */
    public int getStatusCode() {
        return httpResponse.getStatusLine().getStatusCode();
    }

    /**
     * Response locale.
     *
     * @return locale.
     */
    public Locale getLocale() {
        return httpResponse.getLocale();
    }

    /**
     * Check if the response contains a specific http-header.
     *
     * @param headerName Header name.
     * @return True if the response contains said header.
     */
    public boolean containsHeader(String headerName) {
        return httpResponse.containsHeader(headerName);
    }

    /**
     * Get the response content type.
     *
     * @return Response content type (e.g. text/html; charset=utf-8)
     */
    public String getContentType() {
        return httpResponse.getEntity().getContentType().getValue();
    }

    /**
     * Get the response body.
     *
     * @return Response body.
     */
    public SimpleHttpBody getBody() {
        return body;
    }

    /**
     * Get the response body as a String.
     *
     * @return Response body value if present. Null if absent.
     */
    public String getBodyValue() {
        SimpleHttpBody b = getBody();
        return b == null ? null : b.value();
    }

    /**
     * Returns true if the response is OK (status code within 200 and 300)
     *
     * @return True if status code &gt;= 200 and &lt; 300.
     */
    public boolean isOk() {
        int statusCode = this.getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    public boolean is404() {
        return this.getStatusCode() == 404;
    }

    public boolean is4xx() {
        int statusCode = this.getStatusCode();
        return statusCode >= 400 && statusCode < 500;
    }

    public boolean is5xx() {
        int statusCode = this.getStatusCode();
        return statusCode >= 500 && statusCode < 600;
    }

    public StatusCode matchStatusCode() {
        return StatusCode.fromCode(this.getStatusCode());
    }

    @Override
    public String toString() {
        return "SimpleHttpResponse{" +
                "code=" + getStatusCode() +
                ", contentType='" + getContentType() +
                "', body='" + body + '\'' +
                '}';
    }
}
