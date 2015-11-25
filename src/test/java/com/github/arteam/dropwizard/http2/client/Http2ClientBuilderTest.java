package com.github.arteam.dropwizard.http2.client;

import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.MetaData;
import org.eclipse.jetty.http2.api.Session;
import org.eclipse.jetty.http2.api.Stream;
import org.eclipse.jetty.http2.api.server.ServerSessionListener;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.frames.DataFrame;
import org.eclipse.jetty.http2.frames.HeadersFrame;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.FuturePromise;
import org.eclipse.jetty.util.Promise;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Date: 11/20/15
 * Time: 4:17 PM
 *
 * @author Artem Prigoda
 */
public class Http2ClientBuilderTest {

    @Test
    public void testH2c() throws Exception {
        Http2ClientConfiguration configuration = new Http2ClientConfiguration();
        configuration.setConnectionFactoryBuilder(new Http2ClientConnectionFactoryBuilder());
        final HTTP2Client client = new Http2ClientBuilder()
                .using(configuration)
                .build();
        final String hostname = "127.0.0.1";
        final int port = 8445;

        final FuturePromise<Session> sessionPromise = new FuturePromise<>();
        client.connect(new InetSocketAddress(hostname, port), new ServerSessionListener.Adapter(), sessionPromise);
        final Session session = sessionPromise.get(5, TimeUnit.SECONDS);

        final MetaData.Request request = new MetaData.Request("GET",
                new HttpURI("http://" + hostname + ":" + port + "/hello-world"),
                HttpVersion.HTTP_2, new HttpFields());
        CountDownLatch latch = new CountDownLatch(1);
        session.newStream(new HeadersFrame(request, null, true), new Promise.Adapter<>(), new Stream.Listener.Adapter() {
            @Override
            public void onHeaders(Stream stream, HeadersFrame frame) {
                System.out.println(frame);
            }

            @Override
            public void onData(Stream stream, DataFrame frame, Callback callback) {
                System.out.println(frame);
                callback.succeeded();
                if (frame.isEndStream()) {
                    latch.countDown();
                }
                super.onData(stream, frame, callback);
            }
        });
        latch.await();

        client.stop();
    }
}