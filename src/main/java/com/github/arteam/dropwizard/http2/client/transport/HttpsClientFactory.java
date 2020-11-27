package com.github.arteam.dropwizard.http2.client.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.Nullable;
import java.util.List;

public class HttpsClientFactory {
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
    private List<String> supportedCipherSuites = ImmutableList.of("TLS_ECDHE.*");
    private List<String> excludedCipherSuites;

    private boolean validateCerts = true;
    private boolean validatePeers = true;
    private String jceProvider;

    private boolean trustAll = false;

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
    public String getJceProvider() {
        return jceProvider;
    }

    @JsonProperty
    public void setJceProvider(String jceProvider) {
        this.jceProvider = jceProvider;
    }

    @JsonProperty
    public boolean isTrustAll() {
        return trustAll;
    }

    @JsonProperty
    public void setTrustAll(boolean trustAll) {
        this.trustAll = trustAll;
    }

    @Nullable
    public SslContextFactory sslContextFactory() {
        SslContextFactory factory = new SslContextFactory.Client(trustAll);
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
        if (jceProvider != null) {
            factory.setProvider(jceProvider);
        }

        return factory;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("httpsFactory")
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
                .add("jceProvider", jceProvider)
                .toString();
    }
}
