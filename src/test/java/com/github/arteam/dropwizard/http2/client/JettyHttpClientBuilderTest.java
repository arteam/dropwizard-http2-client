package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.FixtureHelpers;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
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
public class JettyHttpClientBuilderTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private final LifecycleEnvironment lifecycleEnvironment = mock(LifecycleEnvironment.class);
    private final Environment environment = mock(Environment.class);

    private LifeCycle lifeCycle;
    private MetricRegistry metricRegistry = new MetricRegistry();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Rule
    public DropwizardAppRule<TestConfiguration> appRule = new DropwizardAppRule<>(TestApplication.class,
            ResourceHelpers.resourceFilePath("http2c-server.yml"));

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
        Http2ClientConfiguration configuration = new Http2ClientConfiguration();
        configuration.setConnectionFactoryBuilder(new Http2ClearClientTransportFactory());
        final HttpClient client = new JettyHttpClientBuilder(environment)
                .using(configuration)
                .build();
        String response = client.GET(String.format("http://127.0.0.1:%d/application/greet", appRule.getLocalPort()))
                .getContentAsString();
        assertThat(objectMapper.readTree(response))
                .isEqualTo(objectMapper.readTree(FixtureHelpers.fixture("server_response.json")));
    }
}