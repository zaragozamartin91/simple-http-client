package com.mz.client.rest;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SslConfig {
    private final SSLConnectionSocketFactory sslsf;
    private SSLContext sslcontext;

    public SslConfig(File keystore, String password, TrustStrategy trustStrategy, String[] supportedProtocols) {
        try {
            this.sslcontext = SSLContexts.custom()
                    .loadTrustMaterial(keystore, password.toCharArray(), trustStrategy)
                    .build();

            this.sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    supportedProtocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch (NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static SslConfig create(File keystore, String password) {
        return new SslConfig(keystore, password, new TrustSelfSignedStrategy(), new String[] { "TLSv1" });
    }

    public static SslConfig createWithCustomStrategy(File keystore, String password, TrustStrategy trustStrategy) {
        return new SslConfig(keystore, password, trustStrategy, new String[] { "TLSv1" });
    }

    public static SslConfig createWithCustomProtocols(File keystore, String password, String[] supportedProtocols) {
        return new SslConfig(keystore, password, new TrustSelfSignedStrategy(), supportedProtocols);
    }

    public static SslConfig createWithCustomStrategyAndProtocols(File keystore, String password, TrustStrategy trustStrategy, String[] supportedProtocols) {
        return new SslConfig(keystore, password, trustStrategy, supportedProtocols);
    }

    SSLContext getSslcontext() {
        return sslcontext;
    }

    SSLConnectionSocketFactory getSslsf() {
        return sslsf;
    }
}
