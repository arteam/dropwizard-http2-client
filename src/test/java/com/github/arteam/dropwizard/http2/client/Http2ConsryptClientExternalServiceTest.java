package com.github.arteam.dropwizard.http2.client;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arteam.dropwizard.http2.client.transport.Http2ClientTransportFactory;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.logging.common.BootstrapLogging;
import org.conscrypt.OpenSSLProvider;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.util.component.LifeCycle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Security;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Date: 1/6/16
 * Time: 8:50 PM
 *
 * @author Artem Prigoda
 */
public class Http2ConsryptClientExternalServiceTest {

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
        Security.addProvider(new OpenSSLProvider());
    }

    private Environment environment = new Environment("default", new ObjectMapper(),
            Validators.newValidatorFactory(), new MetricRegistry(), ClassLoader.getSystemClassLoader(),
            new HealthCheckRegistry(), new Configuration());

    private HttpClient httpClient;

    @BeforeEach
    public void setUp() throws Exception {
        Http2ClientTransportFactory clientTransportFactory = new Http2ClientTransportFactory();
        clientTransportFactory.setJceProvider("Conscrypt");

        JettyClientConfiguration configuration = new JettyClientConfiguration();
        configuration.setConnectionFactoryBuilder(clientTransportFactory);
        httpClient = new JettyClientBuilder(environment)
                .using(configuration)
                .build();

        for (LifeCycle managedObject : environment.lifecycle().getManagedObjects()) {
            managedObject.start();
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        for (LifeCycle managedObject : environment.lifecycle().getManagedObjects()) {
            managedObject.stop();
        }
    }

    @Test
    public void testExternalService() throws Exception {
        ContentResponse contentResponse = httpClient.GET("https://httpbin.org/get");
        String content = contentResponse.getContentAsString();
        assertThat(content).contains("\"Host\": \"httpbin.org\"");
        assertThat(contentResponse.getVersion()).isEqualTo(HttpVersion.HTTP_2);
    }
}

