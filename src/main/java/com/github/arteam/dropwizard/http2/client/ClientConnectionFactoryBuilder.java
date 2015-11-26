package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http2.client.HTTP2ClientConnectionFactory;
import org.eclipse.jetty.io.ClientConnectionFactory;

/**
 * Date: 11/26/15
 * Time: 10:11 AM
 * <p>
 * A service interface for discovering of builder for {@link ClientConnectionFactory}.
 * It allows dynamically plug-in different transports to {@link HttpClient}.
 *
 * @author Artem Prigoda
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        defaultImpl = Http2ClientConnectionFactoryBuilder.class)
public interface ClientConnectionFactoryBuilder extends Discoverable {

    ClientConnectionFactory build();
}
