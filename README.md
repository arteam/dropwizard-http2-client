# Dropwizard HTTP/2 client
[![Build Status](https://travis-ci.org/arteam/dropwizard-http2-client.svg?branch=master)](https://travis-ci.org/arteam/dropwizard-http2-client)
[![Bintray](https://img.shields.io/bintray/v/arteam/maven/dropwizard-http2-client.svg?maxAge=2592000)]()

Dropwizard Integration with the Jetty HTTP/1.1 and HTTP/2 client

* Provides the ability to configure the client from a Dropwizard config.
* Instruments the client and register the metrics in the Dropwizard's `MetricsRegistry`
* The client is managed by the Dropwizard's environment and correctly cleans resources.

## Example of the usage:

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
# Call `httpClient.start()` if you want to use it before the server starts up.                
```

## Runtime dependencies

Don't forget to add an `alpn-boot` library to JVM’s bootpath:
```
-Xbootclasspath/p:/${user.home}/.m2/repository/org/mortbay/jetty/alpn/alpn-boot/${alpn-boot.version}/alpn-boot-${alpn-boot.version}.jar
```

The correct library version depends on a JVM version. Consult the Jetty ALPN [guide](http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html) for the reference.

## Examples of configurations

* HTTP/2

```yml
connectionTimeout: 1s
idleTimeout: 5m
connectionFactory:
  type : h2
  keyStorePath:       client.jks
  keyStorePassword:   http2_client
  trustStorePath:     servers.jks
  trustStorePassword: http2_server
  supportedProtocols:
    - 'TLSv1.2'
  supportedCipherSuites:
    - 'TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256'
```

* HTTP/2 Clear Text

```yml
connectionTimeout: 600ms
idleTimeout: 3m
connectionFactory:
  type : h2c
```

## Maven dependency

```xml
<dependency>
    <groupId>com.github.arteam</groupId>
    <artifactId>dropwizard-http2-client</artifactId>
    <version>0.1</version>
</dependency>
```

## Availability

Artifact are available in the [JCenter] (https://bintray.com/bintray/jcenter) repository

````xml
<repositories>
    <repository>
        <id>jcenter</id>
        <name>bintray</name>
        <url>http://jcenter.bintray.com</url>
  </repository>
</repositories>
````

