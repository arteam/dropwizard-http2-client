# Dropwizard HTTP/2 client [![Build Status](https://travis-ci.org/arteam/dropwizard-http2-client.svg?branch=master)](https://travis-ci.org/arteam/dropwizard-http2-client)
Dropwizard Integration with the Jetty HTTP/1.1 and HTTP/2 client 

* Provides the ability to configure the client from a Dropwizard config.
* Instruments the client and register the metrics in the Dropwizard's `MetricsRegistry`
* The client is managed by the Dropwizard's environment and correctly cleans resources.

Example of the usage:
```yaml
h2Client:
  connectionTimeout: 1s
  idleTimeout: 2s
```

```java
private Http2ClientConfiguration h2Client;

@JsonProperty
public Http2ClientConfiguration getH2Client() {
    return h2Client;
}

@JsonProperty
public void setH2Client(Http2ClientConfiguration h2Client) {
        this.h2Client = h2Client;
}
```

```java
HttpClient httpClient = new Http2ClientBuilder(environment)
                .using(configuration.getH2Client())
                .build("dropwizard-http2-golang");
```

