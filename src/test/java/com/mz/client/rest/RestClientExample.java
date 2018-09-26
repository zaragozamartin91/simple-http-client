package com.mz.client.rest;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class RestClientExample {
    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

//        new RestClientExample().executeBumeranPost();
//        new RestClientExample().executeGoogleGet();
//        new RestClientExample().executeWProxyAndSsl();
        new RestClientExample().executeWithRestClientWithProxyAndSsl();
    }

    private void executeGoogleGet() throws IOException {
        String url = "http://www.google.com";
        RestResponse restResponse = RestClient.newGet(url).execute();

        System.out.println(restResponse.getBody().get());
    }

    private void executeWithRestClientWithProxyAndSsl() {
        String uri = "https://httpbin.org";
        String proxyUrl = "http://192.168.2.10:8080";

        RestResponse response = RestClient.newGet(uri)
//                .withProxy(proxyUrl)
                .withSslConfig(SslConfig.create(new File("cert/mykeystore.jks"), "changeit"))
                .execute();

        System.out.println(response);
    }

    private void executeWProxyAndSsl() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(new File("cert/mykeystore.jks"), "changeit".toCharArray(), new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        //        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build()) {

            //            HttpHost proxy = new HttpHost("192.168.2.10", 8080, "http");
            HttpHost proxy = HttpHost.create("http://192.168.2.10:8080");

            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();

            String uri = "https://httpbin.org";
            HttpGet request = new HttpGet(uri);
            request.setConfig(config);

            System.out.println("Executing request " + request.getRequestLine() + " to " + uri + " via " + proxy);

            try (CloseableHttpResponse response = httpclient.execute(request)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }

    }

}