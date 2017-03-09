package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Date: 11/26/15
 * Time: 10:10 AM
 * <p>
 * A {@link ClientTransportFactory} implementation that provides {@link HttpClientTransport}
 * as HTTP/2 and {@link SslContextFactory} from an external configuration.
 * <p>
 * Implements <b>h2</b> transport for {@link HttpClient}.
 *
 * @author Artem Prigoda
 */
@JsonTypeName("h2")
public class Http2ClientTransportFactory extends HttpsClientFactory implements ClientTransportFactory {
    private Executor executor = new QueuedThreadPool();
    private ByteBufferPool byteBufferPool = new MappedByteBufferPool();

    @Override
    public HttpClientTransport httpClientTransport() {
        // If we don't specify a connection factory, an SSL connection factory with
        // ALPN and HTTP/2 will be used by default. The configured SslContextFactory
        // will be passed from HttpClient.
        HTTP2Client http2Client = new HTTP2Client();
        http2Client.setExecutor(executor);
        http2Client.setByteBufferPool(byteBufferPool);
        return new HttpClientTransportOverHTTP2(http2Client);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("h2")
                .add("tls", super.toString())
                .toString();
    }
}
