package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClearClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClientTransportFactory;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.logging.common.BootstrapLogging;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Date: 11/20/15
 * Time: 4:17 PM
 *
 * @author Artem Prigoda
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class Http2ClientIntegrationTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private final LifecycleEnvironment lifecycleEnvironment = mock(LifecycleEnvironment.class);
    private final Environment environment = mock(Environment.class);

    private LifeCycle lifeCycle;
    private MetricRegistry metricRegistry = new MetricRegistry();
    private ObjectMapper objectMapper = new ObjectMapper();

    public static final DropwizardAppExtension<TestConfiguration> h2c = new DropwizardAppExtension<>(TestApplication.class,
            ResourceHelpers.resourceFilePath("http2c-server.yml"));

    public static final DropwizardAppExtension<TestConfiguration> h2 = new DropwizardAppExtension<>(
            TestApplication.class, ResourceHelpers.resourceFilePath("http2-server.yml"),
            ConfigOverride.config("server.connector.keyStorePath",
                    ResourceHelpers.resourceFilePath("stores/h2_server.jks"))
    );

    @BeforeEach
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
        Mockito.doAnswer(invocation -> {
            lifeCycle = (LifeCycle) invocation.getArguments()[0];
            lifeCycle.start();
            return null;
        }).when(lifecycleEnvironment).manage(any(LifeCycle.class));
        when(environment.metrics()).thenReturn(metricRegistry);
    }

    @AfterEach
    public void tearDown() throws Exception {
        lifeCycle.stop();
    }

    @Test
    public void testH2c() throws Exception {
        JettyClientConfiguration configuration = new JettyClientConfiguration();
        configuration.setConnectionFactoryBuilder(new Http2ClearClientTransportFactory());
        final HttpClient client = new JettyClientBuilder(environment)
                .using(configuration)
                .build();
        String response = client.GET(String.format("http://127.0.0.1:%d/application/greet", h2c.getLocalPort()))
                .getContentAsString();
        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree(getClass().getResource("/server_response.json")));
    }

    @Test
    public void testH2() throws Exception {
        JettyClientConfiguration h2conf = new JettyClientConfiguration();
        Http2ClientTransportFactory h2transport = new Http2ClientTransportFactory();
        h2transport.setTrustStorePath(ResourceHelpers.resourceFilePath("stores/h2_client.jts"));
        h2transport.setTrustStorePassword("h2_client");
        h2transport.setValidatePeers(false);
        h2transport.setTrustAll(true);
        h2transport.setEndpointIdentificationAlgorithm(null);
        h2conf.setConnectionFactoryBuilder(h2transport);

        HttpClient client = new JettyClientBuilder(environment)
                .using(h2conf)
                .build();
        String response = client.GET(String.format("https://127.0.0.1:%d/application/greet", h2.getLocalPort()))
                .getContentAsString();
        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree(getClass().getResource("/server_response.json")));
    }
}