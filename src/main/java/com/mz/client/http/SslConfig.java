package com.mz.client.http;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SslConfig {
    private SSLConnectionSocketFactory sslsf;
    private SSLContext sslcontext;

    private SslConfig(SSLConnectionSocketFactory sslsf, SSLContext sslcontext) {
        this.sslsf = sslsf;
        this.sslcontext = sslcontext;
    }

    public static class SslConfigBuilder {
        private File truststore;
        private String truststorePassword;

        private File keystore;
        private String keystorePassword;
        private String keyPassword;

        private TrustStrategy trustStrategy = new TrustSelfSignedStrategy();
        private String[] supportedProtocols = { "TLSv1" };

        public SslConfig build() {
            try {
                SSLContextBuilder sslContextBuilder = SSLContexts.custom();
                if (truststore != null) {
                    sslContextBuilder.loadTrustMaterial(truststore, truststorePassword.toCharArray(), trustStrategy);
                }
                if (keystore != null) {
                    sslContextBuilder.loadKeyMaterial(keystore, keystorePassword.toCharArray(), keyPassword.toCharArray());
                }

                SSLContext sslcontext = sslContextBuilder.build();

                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslcontext,
                        supportedProtocols,
                        null,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

                return new SslConfig(sslsf, sslcontext);
            } catch (NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | IOException e) {
                throw new IllegalStateException(e);
            } catch (UnrecoverableKeyException e) {
                throw new IllegalStateException("Imposible agregar keystore " + keystore, e);
            }
        }

        public SslConfigBuilder withTruststore(File truststore, String truststorePassword) {
            this.truststore = truststore;
            this.truststorePassword = truststorePassword;
            return this;
        }

        public SslConfigBuilder withKeystore(File keystore, String keystorePassword, String keyPassword) {
            this.keystore = keystore;
            this.keystorePassword = keystorePassword;
            this.keyPassword = keyPassword;
            return this;
        }

        public SslConfigBuilder withKeystore(File keystore, String keystorePassword) {
            return withKeystore(keystore, keystorePassword, keystorePassword);
        }

        public SslConfigBuilder withSupportedProtocols(String[] supportedProtocols) {
            this.supportedProtocols = supportedProtocols;
            return this;
        }

        public SslConfigBuilder withTrustStrategy(TrustStrategy trustStrategy) {
            this.trustStrategy = trustStrategy;
            return this;
        }
    }

    public static SslConfigBuilder newConfig() {
        return new SslConfigBuilder();
    }

    SSLContext getSslcontext() {
        return sslcontext;
    }

    SSLConnectionSocketFactory getSslsf() {
        return sslsf;
    }
}
