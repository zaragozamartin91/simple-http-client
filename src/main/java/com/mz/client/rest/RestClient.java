package com.mz.client.rest;

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

public class RestClient {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private HttpUriRequest httpRequest;

    private SslConfig sslConfig;
    private HttpHost proxy;

    private RestClient(HttpUriRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public static RestClient newPost(String url) {
        return new RestClient(new HttpPost(url));
    }

    public static RestClient newGet(String url) {
        return new RestClient(new HttpGet(url));
    }

    public RestClient withHeader(String headerName, String headerValue) {
        httpRequest.setHeader(headerName, headerValue);
        return this;
    }

    public RestClient withContentType(String contentType) {
        return withHeader("content-type", contentType);
    }

    public RestClient withBasicAuth(String username, String password) {
        String rawCred = String.format("%s:%s", username, password);
        String credentials = Base64.getEncoder().encodeToString(rawCred.getBytes(UTF_8));
        return withHeader("Authorization", "Basic " + credentials);
    }

    public RestClient withBody(String body) {
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

    public RestClient withSslConfig(SslConfig sslConfig) {
        try {
            this.sslConfig = sslConfig;
            return this;
        } catch (Exception e) {
            throw new RestClientException("Error al asignar configuracion de SSL", e);
        }
    }

    public RestClient withProxy(String proxyUrl) {
        try {
            this.proxy = HttpHost.create(proxyUrl);
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al asignar configuracion de proxy " + proxyUrl, e);
        }
    }

    public RestResponse execute() {
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

            return new RestResponse(httpResponse, body);
        } catch (IOException e) {
            throw new RestClientException("Ocurrio un error al ejecutar http request", e);
        }
    }
}
