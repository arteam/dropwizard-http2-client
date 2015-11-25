package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;
import org.eclipse.jetty.io.ClientConnectionFactory;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",
        defaultImpl = Http2ClientConnectionFactoryBuilder.class)
public interface ClientConnectionFactoryBuilder extends Discoverable {

    ClientConnectionFactory build();
}
