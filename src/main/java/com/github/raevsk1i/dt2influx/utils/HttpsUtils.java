package com.github.raevsk1i.dt2influx.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

@Slf4j
@EnableAsync
public final class HttpsUtils {

    public static synchronized HttpClient getHttpClient() {
        try {
            return HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(120))
                    .sslContext(createSSLContext())
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
        } catch (NoSuchAlgorithmException ex) {
            log.error("NoSuchAlgorithmException, idk", ex);
            return null;
        } catch (KeyManagementException ex) {
            log.error("error with key management, while creating ssl context with trust all", ex);
            return null;
        }
    }

    private static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);

        return sslContext;
    }
}
