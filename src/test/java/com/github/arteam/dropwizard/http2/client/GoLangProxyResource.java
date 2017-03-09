package com.github.arteam.dropwizard.http2.client;

import org.eclipse.jetty.client.HttpClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Artem Prigoda (a.prigoda)
 * @since 26.03.16
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class GoLangProxyResource {

    private HttpClient httpClient;

    public GoLangProxyResource(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @GET
    public String get() throws Exception {
        return httpClient.GET("https://nghttp2.org/httpbin/headers").getContentAsString();
    }
}
