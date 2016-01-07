package com.github.arteam.dropwizard.http2.client.transport;

import com.codahale.metrics.MetricRegistry;
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
import org.eclipse.jetty.io.ssl.SslClientConnectionFactory;
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
public class Http2ClientTransportFactory implements ClientTransportFactory {

    private Executor executor = new QueuedThreadPool();
    private ByteBufferPool byteBufferPool = new MappedByteBufferPool();
    private List<String> protocols = Arrays.asList("h2", "h2-17", "h2-16", "h2-15", "h2-14");

    private String keyStorePath;
    private String keyStorePassword;

    @NotEmpty
    private String keyStoreType = "JKS";
    private String keyStoreProvider;

    private String trustStorePath;
    private String trustStorePassword;

    @NotEmpty
    private String trustStoreType = "JKS";
    private String trustStoreProvider;

    private List<String> supportedProtocols = ImmutableList.of("TLSv1.2");
    private List<String> excludedProtocols;
    private List<String> supportedCipherSuites = ImmutableList.of("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
    private List<String> excludedCipherSuites;

    private boolean validateCerts = true;
    private boolean validatePeers = true;


    @JsonProperty
    public boolean isValidatePeers() {
        return validatePeers;
    }

    @JsonProperty
    public void setValidatePeers(boolean validatePeers) {
        this.validatePeers = validatePeers;
    }

    @JsonProperty
    public boolean isValidateCerts() {
        return validateCerts;
    }

    @JsonProperty
    public void setValidateCerts(boolean validateCerts) {
        this.validateCerts = validateCerts;
    }

    @JsonProperty
    public List<String> getExcludedCipherSuites() {
        return excludedCipherSuites;
    }

    @JsonProperty
    public void setExcludedCipherSuites(List<String> excludedCipherSuites) {
        this.excludedCipherSuites = excludedCipherSuites;
    }

    @JsonProperty
    public List<String> getSupportedCipherSuites() {
        return supportedCipherSuites;
    }

    @JsonProperty
    public void setSupportedCipherSuites(List<String> supportedCipherSuites) {
        this.supportedCipherSuites = supportedCipherSuites;
    }

    @JsonProperty
    public List<String> getExcludedProtocols() {
        return excludedProtocols;
    }

    @JsonProperty
    public void setExcludedProtocols(List<String> excludedProtocols) {
        this.excludedProtocols = excludedProtocols;
    }

    @JsonProperty
    public List<String> getSupportedProtocols() {
        return supportedProtocols;
    }

    @JsonProperty
    public void setSupportedProtocols(List<String> supportedProtocols) {
        this.supportedProtocols = supportedProtocols;
    }

    @JsonProperty
    public String getTrustStoreProvider() {
        return trustStoreProvider;
    }

    @JsonProperty
    public void setTrustStoreProvider(String trustStoreProvider) {
        this.trustStoreProvider = trustStoreProvider;
    }

    @JsonProperty
    public String getTrustStoreType() {
        return trustStoreType;
    }

    @JsonProperty
    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    @JsonProperty
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    @JsonProperty
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    @JsonProperty
    public String getTrustStorePath() {
        return trustStorePath;
    }

    @JsonProperty
    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    @JsonProperty
    public String getKeyStoreProvider() {
        return keyStoreProvider;
    }

    @JsonProperty
    public void setKeyStoreProvider(String keyStoreProvider) {
        this.keyStoreProvider = keyStoreProvider;
    }

    @JsonProperty
    public String getKeyStoreType() {
        return keyStoreType;
    }

    @JsonProperty
    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    @JsonProperty
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    @JsonProperty
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    @JsonProperty
    public String getKeyStorePath() {
        return keyStorePath;
    }

    @JsonProperty
    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    @JsonProperty
    public List<String> getProtocols() {
        return protocols;
    }

    @JsonProperty
    public void setProtocols(List<String> protocols) {
        this.protocols = protocols;
    }

    @Nullable
    public SslContextFactory sslContextFactory() {
        SslContextFactory factory = new SslContextFactory();
        if (keyStorePath != null) {
            factory.setKeyStorePath(keyStorePath);
        }

        factory.setKeyStoreType(keyStoreType);

        if (keyStorePassword != null) {
            factory.setKeyStorePassword(keyStorePassword);
        }

        if (keyStoreProvider != null) {
            factory.setKeyStoreProvider(keyStoreProvider);
        }

        if (trustStorePath != null) {
            factory.setTrustStorePath(trustStorePath);
        }

        if (trustStorePassword != null) {
            factory.setTrustStorePassword(trustStorePassword);
        }

        factory.setTrustStoreType(trustStoreType);

        if (trustStoreProvider != null) {
            factory.setTrustStoreProvider(trustStoreProvider);
        }

        factory.setValidateCerts(validateCerts);
        factory.setValidatePeerCerts(validatePeers);

        if (supportedProtocols != null) {
            factory.setIncludeProtocols(Iterables.toArray(supportedProtocols, String.class));
        }

        if (excludedProtocols != null) {
            factory.setExcludeProtocols(Iterables.toArray(excludedProtocols, String.class));
        }

        if (supportedCipherSuites != null) {
            factory.setIncludeCipherSuites(Iterables.toArray(supportedCipherSuites, String.class));
        }

        if (excludedCipherSuites != null) {
            factory.setExcludeCipherSuites(Iterables.toArray(excludedCipherSuites, String.class));
        }

        return factory;
    }

    @Override
    public HttpClientTransport httpClientTransport(MetricRegistry metricRegistry, @Nullable String name) {
        // If we don't specify a connection factory, an SSL connection factory with
        // ALPN and HTTP/2 will be used by default. The configured SslContextFactory
        // will be passed from HttpClient.
        HTTP2Client http2Client = new HTTP2Client();
        http2Client.setExecutor(executor);
        http2Client.setByteBufferPool(byteBufferPool);
        return new InstrumentedHttpClientTransportOverHttp2(http2Client, metricRegistry, name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("h2")
                .add("protocols", protocols)
                .add("keyStorePath", keyStorePath)
                .add("keyStorePassword", keyStorePassword)
                .add("keyStoreType", keyStoreType)
                .add("keyStoreProvider", keyStoreProvider)
                .add("trustStorePath", trustStorePath)
                .add("trustStorePassword", trustStorePassword)
                .add("trustStoreType", trustStoreType)
                .add("trustStoreProvider", trustStoreProvider)
                .add("supportedProtocols", supportedProtocols)
                .add("excludedProtocols", excludedProtocols)
                .add("supportedCipherSuites", supportedCipherSuites)
                .add("excludedCipherSuites", excludedCipherSuites)
                .add("validateCerts", validateCerts)
                .add("validatePeers", validatePeers)
                .toString();
    }
}
