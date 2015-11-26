package com.github.arteam.dropwizard.http2.client;

import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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

    @Before
    public void setUp() throws Exception {
        when(environment.lifecycle()).thenReturn(lifecycleEnvironment);
        Mockito.doAnswer(invocation -> {
            lifeCycle = (LifeCycle) invocation.getArguments()[0];
            lifeCycle.start();
            return null;
        }).when(lifecycleEnvironment).manage(any(LifeCycle.class));
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
        String response = client.GET("http://127.0.0.1:8445/hello-world")
                .getContentAsString();
        System.out.println(response);
    }
}