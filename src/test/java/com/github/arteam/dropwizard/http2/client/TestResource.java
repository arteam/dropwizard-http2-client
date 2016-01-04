package com.github.arteam.dropwizard.http2.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Date: 1/4/16
 * Time: 5:01 PM
 *
 * @author Artem Prigoda
 */
@Produces("application/json")
@Path("greet")
public class TestResource {

    @GET
    public Greeting greet() {
        return new Greeting("HTTP/2 server", "Hello, World!");
    }
}
