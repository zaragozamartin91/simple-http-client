package com.mz.client.http;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.regex.Pattern;

public class SimpleHttpClientExample {
    public static void main(String[] args) throws Exception {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("javax.net.debug", "ssl");

        //        new SimpleHttpClientExample().executeGoogleGet();
        //                new SimpleHttpClientExample().executeWProxyAndSsl();
        new SimpleHttpClientExample().executeSoapWithClientCert();
        //        new SimpleHttpClientExample().connectToWeb();
    }
    //https://192.168.99.100:9443/hello/HelloWorldService/HelloWorldService.wsdl

    private void executeGoogleGet() throws IOException {
        String url = "http://www.google.com";
        SimpleHttpResponse simpleHttpResponse = SimpleHttpClient.newGet(url).execute();

        System.out.println(simpleHttpResponse.getBody().value());
    }

    private void executeSoapWithClientCert() {
        String keystore = new File("cert/was-keystore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        String keystorePassword = "changeit";

        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        System.setProperty("javax.net.ssl.trustStore ", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword ", keystorePassword);

        //        String uri = "https://192.168.99.100:9443/hello/HelloWorldService";
        String uri = "https://localhost:9443/hello/HelloWorldService";
        //        https://localhost:9443/hello/HelloWorldService

        SimpleHttpResponse response = SimpleHttpClient.newPost(uri)
                .withSslConfig(SslConfig.newConfig().withTruststore(new File(keystore), keystorePassword).build())
                .withBody(
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.ex.mz/\">  <soapenv:Header/>  <soapenv:Body>  <ws:salute>  <arg0>Martin</arg0>  </ws:salute> </soapenv:Body></soapenv:Envelope>")
                .withContentType("application/xml")
                .execute();

        System.out.println(response);
    }

    public HttpURLConnection connectToWeb() {
        String uri = "https://192.168.99.100:9443/hello/HelloWorldService";

        String keystore = new File("cert/was-keystore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        String keystorePassword = "changeit";

        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        System.setProperty("javax.net.ssl.trustStore ", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword ", keystorePassword);

        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.connect();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return connection;
    }

    private void executeWithSimpleHttpClientWithProxyAndSsl() {
        String uri = "https://httpbin.org";
        String proxyUrl = "http://192.168.2.10:8080";

        File truststore = new File("cert/mykeystore.jks");
        String truststorePassword = "changeit";

        SimpleHttpResponse response = SimpleHttpClient.newGet(uri)
                .withProxy(proxyUrl)
                .withSslConfig(SslConfig.newConfig().withTruststore(truststore, truststorePassword).build())
                .execute();

        System.out.println(response);
    }

    private void executeWProxyAndSsl() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // Trust own CA and all self-signed certs
        String keystore = new File("cert/was-keystore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        ;
        String keystorePass = "changeit";

        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePass);
        System.setProperty("javax.net.ssl.trustStore ", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword ", keystorePass);

        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(new File(keystore), keystorePass.toCharArray(), new TrustSelfSignedStrategy())
                .build();

        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
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