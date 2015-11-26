package com.github.arteam.dropwizard.http2.client;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.util.Duration;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class Http2ClientConfigurationTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private Http2ClientConfiguration load(String configLocation) {
        try {
            return new ConfigurationFactory<>(Http2ClientConfiguration.class,
                    Validators.newValidator(),
                    Jackson.newObjectMapper(), "dw-http2-client")
                    .build(new File(Resources.getResource(configLocation).toURI()));
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    @Test
    public void testHttp2() {
        final Http2ClientConfiguration conf = load("http2-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(5));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.seconds(1));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(Http2ClientTransportFactory.class);
        final Http2ClientTransportFactory http2 = (Http2ClientTransportFactory)
                conf.getConnectionFactoryBuilder();

        assertThat(http2.getProtocols()).containsOnly("h2", "h2-17", "h2-16", "h2-15", "h2-14");

        assertThat(http2.getKeyStorePath()).isEqualTo("client.jks");
        assertThat(http2.getKeyStorePassword()).isEqualTo("http2_client");
        assertThat(http2.getKeyStoreType()).isEqualTo("JKS");

        assertThat(http2.getTrustStorePath()).isEqualTo("servers.jks");
        assertThat(http2.getTrustStorePassword()).isEqualTo("http2_server");
        assertThat(http2.getTrustStoreType()).isEqualTo("JKS");

        assertThat(http2.getSupportedProtocols()).containsOnly("TLSv1.2");
        assertThat(http2.getSupportedCipherSuites()).containsOnly("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
    }

    @Test
    public void testHttp2c() {
        final Http2ClientConfiguration conf = load("http2c-client.yml");
        assertThat(conf.getIdleTimeout()).isEqualTo(Duration.minutes(3));
        assertThat(conf.getConnectionTimeout()).isEqualTo(Duration.milliseconds(600));

        assertThat(conf.getConnectionFactoryBuilder())
                .isInstanceOf(Http2ClearClientTransportFactory.class);
    }
}
