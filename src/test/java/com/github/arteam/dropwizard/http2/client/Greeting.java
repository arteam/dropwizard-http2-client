package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Date: 1/4/16
 * Time: 5:02 PM
 *
 * @author Artem Prigoda
 */
public class Greeting {

    public final String author;
    public final String message;

    public Greeting(String author, String message) {
        this.author = author;
        this.message = message;
    }
}
