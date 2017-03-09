package com.github.arteam.dropwizard.http2.client.names;

import org.eclipse.jetty.client.api.Request;

import static com.codahale.metrics.MetricRegistry.name;

public class NameStrategies {
    public static NameStrategy HOST = Request::getHost;

    public static NameStrategy METHOD = Request::getMethod;

    public static NameStrategy FULL = request -> name(request.getHost(), request.getMethod(), request.getPath());

    public static NameStrategy prefixedStrategy(String prefix, NameStrategy strategy) {
        return request -> name(prefix, strategy.nameFor(request));
    }

    public static NameStrategy prefixedStrategy(Class<?> clazz, String prefix, NameStrategy strategy) {
        return request -> name(clazz, prefix, strategy.nameFor(request));
    }
}
