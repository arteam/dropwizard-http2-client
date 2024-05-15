package com.github.arteam.dropwizard.http2.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.jetty.client.HttpClient;

/**
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class HttpBinProxyResource {

    private HttpClient httpClient;

    public HttpBinProxyResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @GET
    public String get() throws Exception {
        return httpClient.GET("https://httpbin.org/get").getContentAsString();
    }
}
