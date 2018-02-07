package com.github.arteam.dropwizard.http2.client;

import com.github.arteam.dropwizard.http2.client.transport.Http2ClearClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.HttpClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.HttpsClientTransportFactory;
import com.google.common.io.Resources;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.util.Duration;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class JettyClientConfigurationTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private JettyClientConfiguration load(String configLocation) {
        try {
            return new YamlConfigurationFactory<>(JettyClientConfiguration.class,
                    Validators.newValidator(),
                    Jackson.newObjectMapper(), "dw-http-client")
                    .build(new File(Resources.getResource(configLocation).toURI()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Test
    public void testHttp2() {
        final JettyClientConfiguration conf = load("http2-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(5));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.seconds(1));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(Http2ClientTransportFactory.class);
        final Http2ClientTransportFactory http2 = (Http2ClientTransportFactory)
                conf.getConnectionFactoryBuilder();

        assertThat(http2.getKeyStorePath()).isEqualTo("client.jks");
        assertThat(http2.getKeyStorePassword()).isEqualTo("http2_client");
        assertThat(http2.getKeyStoreType()).isEqualTo("JKS");

        assertThat(http2.getTrustStorePath()).isEqualTo("servers.jks");
        assertThat(http2.getTrustStorePassword()).isEqualTo("http2_server");
        assertThat(http2.getTrustStoreType()).isEqualTo("JKS");

        assertThat(http2.getSupportedProtocols()).containsOnly("TLSv1.2");
        assertThat(http2.getSupportedCipherSuites()).containsOnly("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
        assertThat(http2.getJceProvider()).isEqualTo("SUN");
    }

    @Test
    public void testHttp2c() {
        final JettyClientConfiguration conf = load("http2c-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(3));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.milliseconds(600));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(Http2ClearClientTransportFactory.class);
    }

    @Test
    public void testHttps() {
        final JettyClientConfiguration conf = load("https-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(5));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.seconds(1));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(HttpsClientTransportFactory.class);
        final HttpsClientTransportFactory https = (HttpsClientTransportFactory)
                conf.getConnectionFactoryBuilder();

        assertThat(https.getKeyStorePath()).isEqualTo("client.jks");
        assertThat(https.getKeyStorePassword()).isEqualTo("http2_client");
        assertThat(https.getKeyStoreType()).isEqualTo("JKS");

        assertThat(https.getTrustStorePath()).isEqualTo("servers.jks");
        assertThat(https.getTrustStorePassword()).isEqualTo("http2_server");
        assertThat(https.getTrustStoreType()).isEqualTo("JKS");

        assertThat(https.getSupportedProtocols()).containsOnly("TLSv1.2");
        assertThat(https.getSupportedCipherSuites()).containsOnly("TLS_ECDHE.*");
        assertThat(https.getJceProvider()).isEqualTo("SUN");
    }

    @Test
    public void testHttp() {
        final JettyClientConfiguration conf = load("http-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(3));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.milliseconds(600));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(HttpClientTransportFactory.class);
    }
}
