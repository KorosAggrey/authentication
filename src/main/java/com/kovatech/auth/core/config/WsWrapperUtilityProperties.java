package com.kovatech.auth.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "dxl.wrapper.utility")
public class WsWrapperUtilityProperties {
    private String acceptedSourceSystems;
    private String acceptedApplications;
    private String requiredHeaders;
    private String headerValidation;
    private String removedHeaders;
    private String securedRequestMethods;
    private String securedResponseMethods;
    private String exemptedSourceSystems;
    private String exemptedRequestUri;
    private boolean debugModeEnabled;
    private String internalSourceSystems;
    private String externalHosts;

    public String getAcceptedSourceSystems() {
        return this.acceptedSourceSystems;
    }

    public void setAcceptedSourceSystems(String acceptedSourceSystems) {
        this.acceptedSourceSystems = acceptedSourceSystems;
    }

    public String getAcceptedApplications() {
        return this.acceptedApplications;
    }

    public void setAcceptedApplications(String acceptedApplications) {
        this.acceptedApplications = acceptedApplications;
    }

    public String getRequiredHeaders() {
        return this.requiredHeaders;
    }

    public void setRequiredHeaders(String requiredHeaders) {
        this.requiredHeaders = requiredHeaders;
    }

    public String getHeaderValidation() {
        return this.headerValidation;
    }

    public void setHeaderValidation(String headerValidation) {
        this.headerValidation = headerValidation;
    }

    public String getRemovedHeaders() {
        return this.removedHeaders;
    }

    public void setRemovedHeaders(String removedHeaders) {
        this.removedHeaders = removedHeaders;
    }

    public String getSecuredRequestMethods() {
        return this.securedRequestMethods;
    }

    public void setSecuredRequestMethods(String securedRequestMethods) {
        this.securedRequestMethods = securedRequestMethods;
    }

    public String getSecuredResponseMethods() {
        return this.securedResponseMethods;
    }

    public void setSecuredResponseMethods(String securedResponseMethods) {
        this.securedResponseMethods = securedResponseMethods;
    }

    public String getExemptedSourceSystems() {
        return this.exemptedSourceSystems;
    }

    public void setExemptedSourceSystems(String exemptedSourceSystems) {
        this.exemptedSourceSystems = exemptedSourceSystems;
    }

    public String getExemptedRequestUri() {
        return this.exemptedRequestUri;
    }

    public void setExemptedRequestUri(String exemptedRequestUri) {
        this.exemptedRequestUri = exemptedRequestUri;
    }

    public boolean isDebugModeEnabled() {
        return this.debugModeEnabled;
    }

    public void setDebugModeEnabled(boolean debugModeEnabled) {
        this.debugModeEnabled = debugModeEnabled;
    }

    public String getInternalSourceSystems() {
        return this.internalSourceSystems;
    }

    public void setInternalSourceSystems(String internalSourceSystems) {
        this.internalSourceSystems = internalSourceSystems;
    }

    public String getExternalHosts() {
        return this.externalHosts;
    }

    public void setExternalHosts(String externalHosts) {
        this.externalHosts = externalHosts;
    }
}
