package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import org.eclipse.jetty.http2.client.HTTP2ClientConnectionFactory;
import org.eclipse.jetty.io.ClientConnectionFactory;

@JsonTypeName("http2c")
public class Http2ClearClientConnectionFactoryBuilder implements ClientConnectionFactoryBuilder {

    @Override
    public ClientConnectionFactory build() {
        return new HTTP2ClientConnectionFactory();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("http2c")
                .toString();
    }
}
