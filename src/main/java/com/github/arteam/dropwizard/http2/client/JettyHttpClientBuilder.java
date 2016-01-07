package com.github.arteam.dropwizard.http2.client;

import com.google.common.base.Strings;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.HttpCookieStore;

/**
 * Date: 11/26/15
 * Time: 10:22 AM
 * <p>
 * A builder for {@link HttpClient}. It builds an instance from an external
 * configuration and ties it to Dropwizard environment.
 *
 * @author Artem Prigoda
 */
public class JettyHttpClientBuilder {

    private Environment environment;
    private Http2ClientConfiguration configuration = new Http2ClientConfiguration();

    public JettyHttpClientBuilder(Environment environment) {
        this.environment = environment;
    }

    public JettyHttpClientBuilder using(Http2ClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public HttpClient build(String name) {
        ClientTransportFactory connectionFactoryBuilder = configuration.getConnectionFactoryBuilder();
        HttpClient httpClient = new HttpClient(
                connectionFactoryBuilder.httpClientTransport(environment.metrics(), name),
                connectionFactoryBuilder.sslContextFactory());
        httpClient.setConnectTimeout(configuration.getConnectionTimeout().toMilliseconds());
        httpClient.setIdleTimeout(configuration.getIdleTimeout().toMilliseconds());
        httpClient.setFollowRedirects(configuration.isFollowRedirects());
        httpClient.setCookieStore(new HttpCookieStore.Empty());
        if (!Strings.isNullOrEmpty(name)) {
            httpClient.setUserAgentField(new HttpField(HttpHeader.USER_AGENT, name));
        }
        if (!configuration.isStoreCookies()) {
            httpClient.setCookieStore(new HttpCookieStore.Empty());
        }
        environment.lifecycle().manage(httpClient);
        return httpClient;
    }

    public HttpClient build() {
        return build("");
    }
}