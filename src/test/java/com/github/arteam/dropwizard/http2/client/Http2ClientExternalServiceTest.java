package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 1/6/16
 * Time: 8:50 PM
 *
 * @author Artem Prigoda
 */
public class Http2ClientExternalServiceTest {

    static {
        BootstrapLogging.bootstrap();
    }

    private Environment environment = new Environment("default", new ObjectMapper(),
            Validators.newValidator(), new MetricRegistry(), ClassLoader.getSystemClassLoader());

    private HttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        httpClient = new Http2ClientBuilder(environment).build();
        for (LifeCycle managedObject : environment.lifecycle().getManagedObjects()) {
            managedObject.start();
        }
    }

    @After
    public void tearDown() throws Exception {
        for (LifeCycle managedObject : environment.lifecycle().getManagedObjects()) {
            managedObject.stop();
        }
    }

    @Test
    public void testExternalService() throws Exception {
        String content = httpClient.GET("https://nghttp2.org/httpbin/headers")
                .getContentAsString();
        assertThat(content).contains("\"Host\": \"nghttp2.org:443\"");
    }
}

