package com.github.arteam.dropwizard.http2.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.glassfish.jersey.server.ChunkedOutput;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 1/4/16
 * Time: 5:01 PM
 *
 * @author Artem Prigoda
 */
@Produces("application/json")
@Path("/")
public class TestResource {

    private ExecutorService executor = Executors.newCachedThreadPool();

    @GET
    @Path("/greet")
    public Greeting greet() {
        return new Greeting("HTTP/2 server", "Hello, World!");
    }

    @GET
    @Path("/greet-chunk")
    public ChunkedOutput<String> greetChunk() throws InterruptedException {
        final ChunkedOutput<String> output = new ChunkedOutput<>(String.class);

        executor.execute(() -> {
            try (ChunkedOutput<String> out = output) {
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500L);
                    out.write("Hello world, I'm a HTTP/2 chunk server");
                }
            } catch (IOException ignored) {
                throw new RuntimeException("Error writing output");
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        });

        Thread.sleep(500L);
        return output;
    }
}
