package com.mz.client.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;

/**
 * Simple Http client for http requests.
 */
public class SimpleHttpClient {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private HttpUriRequest httpRequest;

    private SslConfig sslConfig;
    private HttpHost proxy;

    private SimpleHttpClient(HttpUriRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * Create a Http Post request.
     *
     * @param url Request url.
     * @return New POST http request.
     */
    public static SimpleHttpClient newPost(String url) {
        return new SimpleHttpClient(new HttpPost(url));
    }

    /**
     * Create a Http Get request.
     *
     * @param url Request url.
     * @return New GET request.
     */
    public static SimpleHttpClient newGet(String url) {
        return new SimpleHttpClient(new HttpGet(url));
    }

    /**
     * Assign a header to the request.
     *
     * @param headerName  Name.
     * @param headerValue Value.
     * @return this.
     */
    public SimpleHttpClient withHeader(String headerName, String headerValue) {
        httpRequest.setHeader(headerName, headerValue);
        return this;
    }

    /**
     * Assign a content-type header.
     *
     * @param contentType Value (e.g. application/json)
     * @return this.
     */
    public SimpleHttpClient withContentType(String contentType) {
        return withHeader("content-type", contentType);
    }

    /**
     * Assign a basic-auth header.
     *
     * @param username Plain text Username.
     * @param password Plain text Password.
     * @return this.
     */
    public SimpleHttpClient withBasicAuth(String username, String password) {
        String rawCred = String.format("%s:%s", username, password);
        String credentials = Base64.getEncoder().encodeToString(rawCred.getBytes(UTF_8));
        return withHeader("Authorization", "Basic " + credentials);
    }

    /**
     * Assign a body to the request.
     *
     * @param body String body.
     * @return this.
     */
    public SimpleHttpClient withBody(String body) {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            try {
                HttpEntityEnclosingRequest httpEntityEnclosingRequest = (HttpEntityEnclosingRequest) this.httpRequest;
                httpEntityEnclosingRequest.setEntity(new StringEntity(body));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Body de request: " + body + " invalido", e);
            }
        } else {
            throw new IllegalStateException("No es posible asignar un body a este tipo de request");
        }

        return this;
    }

    /**
     * Assign a SSL config for HTTPS requests.
     *
     * @param sslConfig Ssl config.
     * @return this.
     */
    public SimpleHttpClient withSslConfig(SslConfig sslConfig) {
        try {
            this.sslConfig = sslConfig;
            return this;
        } catch (Exception e) {
            throw new HttpClientException("Error al asignar configuracion de SSL", e);
        }
    }

    /**
     * Set a proxy.
     *
     * @param proxyUrl Proxy url.
     * @return this.
     */
    public SimpleHttpClient withProxy(String proxyUrl) {
        try {
            this.proxy = HttpHost.create(proxyUrl);
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al asignar configuracion de proxy " + proxyUrl, e);
        }
    }

    /**
     * Execute the request.
     *
     * @return Response.
     */
    public SimpleHttpResponse execute() {
        try (CloseableHttpClient httpClient = Optional.ofNullable(sslConfig).isPresent() ?
                HttpClients.custom().setSSLSocketFactory(sslConfig.getSslsf()).build() :
                HttpClients.createDefault()) {

            if (Optional.ofNullable(proxy).isPresent()) {
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                ((HttpRequestBase) httpRequest).setConfig(config);
            }

            CloseableHttpResponse httpResponse = httpClient.execute(httpRequest);
            HttpEntity entity = httpResponse.getEntity();
            String body = entity == null ? null : EntityUtils.toString(entity);

            return new SimpleHttpResponse(httpResponse, body);
        } catch (IOException e) {
            throw new HttpClientException("Ocurrio un error al ejecutar http request", e);
        }
    }
}