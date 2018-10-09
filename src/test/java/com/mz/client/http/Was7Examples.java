package com.mz.client.http;

import java.io.File;
import java.util.regex.Pattern;

public class Was7Examples {
    public static void main(String[] args) {
        enableSslDebug();

//        getWasAdminConsole();

        postWasHelloWorldService();

    }

    private static void postWasHelloWorldService() {
        String truststore = new File("was7_cert/truststore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        String truststorePassword = "changeit";

        String keystore = new File("was7_cert/keystore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        String keystorePassword = "changeit";

        String uri = "https://wks0393.accusysargbsas.local:9443/hello/HelloWorldService";

        SslConfig sslConfig = SslConfig.newConfig()
                .withTruststore(new File(truststore), truststorePassword)
                .withKeystore(new File(keystore), keystorePassword)
                .build();

        String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.ex.mz/\"> <soapenv:Header/> <soapenv:Body> <ws:salute> <!--Optional:--> <arg0>martin</arg0> </ws:salute> </soapenv:Body></soapenv:Envelope>";

        SimpleHttpResponse response = SimpleHttpClient.newPost(uri)
                .withSslConfig(sslConfig)
                .withContentType("application/xml")
                .withBody(body)
                .execute();

        System.out.println(response);
    }

    private static void getWasAdminConsole() {
        String truststore = new File("was7_cert/truststore.jks").getAbsolutePath().replaceAll(Pattern.quote("\\"), "/");
        String truststorePassword = "changeit";

        //        setTruststore(truststore, truststorePassword);

        String uri = "https://wks0393.accusysargbsas.local:9043/ibm/console/logon.jsp";

        SimpleHttpResponse response = SimpleHttpClient.newGet(uri)
                .withSslConfig(SslConfig.newConfig().withTruststore(new File(truststore), truststorePassword).build())
                .withContentType("application/xml")
                .execute();

        System.out.println(response);
    }

    private static void setKeystores(String keystore, String keystorePassword, String truststore, String truststorePassword) {
        setKeystore(keystore, keystorePassword);
        setTruststore(truststore, truststorePassword);
    }

    private static void setTruststore(String truststore, String truststorePassword) {
        System.setProperty("javax.net.ssl.trustStore ", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword ", truststorePassword);
    }

    private static void setKeystore(String keystore, String keystorePassword) {
        System.setProperty("javax.net.ssl.keyStore", keystore);
        System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
    }

    private static void enableSslDebug() {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("javax.net.debug", "ssl");
    }
}
