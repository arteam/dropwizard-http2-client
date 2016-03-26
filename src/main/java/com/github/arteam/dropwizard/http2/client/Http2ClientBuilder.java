package com.github.arteam.dropwizard.http2.client;

import com.github.arteam.dropwizard.http2.client.transport.ClientTransportFactory;
import com.google.common.base.Strings;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.HttpCookieStore;

import javax.annotation.Nullable;

/**
 * Date: 11/26/15
 * Time: 10:22 AM
 * <p>
 * A builder for {@link HttpClient}. It builds an instance from an external
 * configuration and ties it to Dropwizard environment.
 *
 * @author Artem Prigoda
 */
public class Http2ClientBuilder {

    private Environment environment;
    private Http2ClientConfiguration configuration = new Http2ClientConfiguration();

    public Http2ClientBuilder(Environment environment) {
        this.environment = environment;
    }

    public Http2ClientBuilder using(Http2ClientConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public HttpClient build(@Nullable String name) {
        ClientTransportFactory connectionFactoryBuilder = configuration.getConnectionFactoryBuilder();
        HttpClient httpClient = new InstrumentedHttpClient(connectionFactoryBuilder.httpClientTransport(),
                connectionFactoryBuilder.sslContextFactory(), environment.metrics(), name);
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
        return build(null);
    }
}
