package com.github.arteam.dropwizard.http2.client;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
@Ignore("Takes a lot of time")
public class HttpBinProxyIntegrationTest {

    @Rule
    public DropwizardAppRule<GoLangProxyConfiguration> appRule = new
            DropwizardAppRule<>(GoLangProxyApplication.class,
            ResourceHelpers.resourceFilePath("test-golang-proxy.yml"));

    @Test
    public void tenureTest() throws Exception {
        JerseyClient jerseyClient = new JerseyClientBuilder().build();
        long start = System.currentTimeMillis();
        long now = start;
        int attempt = 1;
        while (now - start < 60000) {
            String content = jerseyClient.target("http://127.0.0.1:" + appRule.getLocalPort())
                    .request()
                    .get()
                    .readEntity(String.class);
            assertThat(content).contains("\"Host\": \"httpbin.org\"");
            now = System.currentTimeMillis();
            if (attempt++ % 5 == 0) { // 5 attempts reuse the same connection
                Thread.sleep(4000); // Force reopening the connection
                attempt = 1;
            }
        }
        jerseyClient.close();
    }
}
