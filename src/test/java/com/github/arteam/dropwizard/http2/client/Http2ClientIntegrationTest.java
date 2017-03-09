package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClearClientTransportFactory;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClientTransportFactory;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Date: 11/20/15
 * Time: 4:17 PM
 *
 * @author Artem Prigoda
 */
public class Http2ClientIntegrationTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private final LifecycleEnvironment lifecycleEnvironment = mock(LifecycleEnvironment.class);
    private final Environment environment = mock(Environment.class);

    private LifeCycle lifeCycle;
    private MetricRegistry metricRegistry = new MetricRegistry();
    private ObjectMapper objectMapper = new ObjectMapper();

    @ClassRule
    public static final DropwizardAppRule<TestConfiguration> h2c = new DropwizardAppRule<>(TestApplication.class,
            ResourceHelpers.resourceFilePath("http2c-server.yml"));

    @ClassRule
    public static final DropwizardAppRule<TestConfiguration> h2 = new DropwizardAppRule<>(
            TestApplication.class, ResourceHelpers.resourceFilePath("http2-server.yml"),
            ConfigOverride.config("server.connector.keyStorePath",
                    ResourceHelpers.resourceFilePath("stores/h2_server.jks"))
    );

    @Before
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
        Mockito.doAnswer(invocation -> {
            lifeCycle = (LifeCycle) invocation.getArguments()[0];
            lifeCycle.start();
            return null;
        }).when(lifecycleEnvironment).manage(any(LifeCycle.class));
        when(environment.metrics()).thenReturn(metricRegistry);
    }

    @After
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
                .isEqualTo(objectMapper.readTree(FixtureHelpers.fixture("server_response.json")));
    }

    @Test
    public void testH2() throws Exception {
        JettyClientConfiguration h2conf = new JettyClientConfiguration();
        Http2ClientTransportFactory h2transport = new Http2ClientTransportFactory();
        h2transport.setTrustStorePath(ResourceHelpers.resourceFilePath("stores/h2_client.jts"));
        h2transport.setTrustStorePassword("h2_client");
        h2transport.setValidatePeers(false);
        h2conf.setConnectionFactoryBuilder(h2transport);

        HttpClient client = new JettyClientBuilder(environment)
                .using(h2conf)
                .build();
        String response = client.GET(String.format("https://127.0.0.1:%d/application/greet", h2.getLocalPort()))
                .getContentAsString();
        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree(FixtureHelpers.fixture("server_response.json")));
    }
}