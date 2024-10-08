package com.github.arteam.dropwizard.http2.client;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 1/4/16
 * Time: 5:00 PM
 *
 * @author Artem Prigoda
 */
public class TestApplication extends Application<TestConfiguration> {
    @Override
    public void run(TestConfiguration configuration, Environment environment) throws Exception {
        environment.healthChecks().register("default", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                return Result.healthy();
            }
        });
        ExecutorService executor = Executors.newCachedThreadPool();
        environment.jersey().register(new TestResource(executor));
        environment.lifecycle().manage(new Managed() {
            @Override
            public void stop() throws Exception {
                executor.shutdown();
            }
        });
    }
}
