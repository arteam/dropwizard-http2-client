package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import org.eclipse.jetty.client.HttpClient;

/**
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
public class GoLangProxyApplication extends Application<GoLangProxyConfiguration> {
    @Override
    public void run(GoLangProxyConfiguration configuration, Environment environment) throws Exception {
        HttpClient httpClient = new JettyClientBuilder(environment)
                .using(configuration.getH2Client())
                .build("dropwizard-http2-golang");
        environment.jersey().register(new HttpBinProxyResource(httpClient));
        environment.healthChecks().register("fake", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });
    }
}
