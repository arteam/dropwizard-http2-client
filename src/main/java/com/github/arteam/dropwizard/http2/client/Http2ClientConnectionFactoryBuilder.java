package com.github.arteam.dropwizard.http2.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import org.eclipse.jetty.alpn.client.ALPNClientConnectionFactory;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http2.client.HTTP2ClientConnectionFactory;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.ClientConnectionFactory;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.io.ssl.SslClientConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.hibernate.validator.constraints.NotEmpty;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Date: 11/26/15
 * Time: 10:10 AM
 * <p>
 * A builder for {@link SslClientConnectionFactory} with {@link ALPNClientConnectionFactory}
 * and {@link HTTP2ClientConnectionFactory}.
 * <p>
 * <p>Provides <b>h2</b> transport for {@link HttpClient}.</p>
 *
 * @author Artem Prigoda
 */
@JsonTypeName("http2")
public class Http2ClientConnectionFactoryBuilder implements ClientConnectionFactoryBuilder {

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

    private List<String> supportedProtocols;
    private List<String> excludedProtocols;
    private List<String> supportedCipherSuites;
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

    @Override
    public ClientConnectionFactory build() {
        final ALPNClientConnectionFactory alpn = new ALPNClientConnectionFactory(executor,
                new HTTP2ClientConnectionFactory(), protocols);
        return new SslClientConnectionFactory(buildSslContextFactory(), byteBufferPool, executor, alpn);
    }

    /**
     * Builds a {@link SslClientConnectionFactory} instance from an external configuration.
     *
     * @return a configured {@link SslClientConnectionFactory}
     */
    protected SslContextFactory buildSslContextFactory() {
        final SslContextFactory factory = new SslContextFactory();
        if (keyStorePath != null) {
            factory.setKeyStorePath(keyStorePath);
        }

        if (keyStoreType.startsWith("Windows-")) {
            try {
                final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                factory.setKeyStore(keyStore);
            } catch (Exception e) {
                throw new IllegalStateException("Windows key store not supported", e);
            }
        } else {
            factory.setKeyStoreType(keyStoreType);
            factory.setKeyStorePassword(keyStorePassword);
        }

        if (keyStoreProvider != null) {
            factory.setKeyStoreProvider(keyStoreProvider);
        }

        if (trustStoreType.startsWith("Windows-")) {
            try {
                final KeyStore trustStore = KeyStore.getInstance(trustStoreType);
                trustStore.load(null, null);
                factory.setTrustStore(trustStore);
            } catch (Exception e) {
                throw new IllegalStateException("Windows trust store not supported", e);
            }
        } else {
            if (trustStorePath != null) {
                factory.setTrustStorePath(trustStorePath);
            }
            if (trustStorePassword != null) {
                factory.setTrustStorePassword(trustStorePassword);
            }
            factory.setTrustStoreType(trustStoreType);
        }

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
    public String toString() {
        return MoreObjects.toStringHelper("http2")
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
