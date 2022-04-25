package com.kovatech.auth.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "dxl.starter")
public class WsStarterProperties {
    private String staticIv;
    private String appVersion;
    private int connectTimeout;
    private int readTimeout;
    private boolean sslVerifierEnabled;
    private String msisdnPrefix;
    private int msisdnLength;
    private String swaggerTitle;
    private String swaggerDescription;
    private String developerName;
    private String developerEmail;
    @Value("${spring.profiles.active}")
    private String profile;

    public String getStaticIv() {
        return this.staticIv;
    }

    public void setStaticIv(String staticIv) {
        this.staticIv = staticIv;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isSslVerifierEnabled() {
        return this.sslVerifierEnabled;
    }

    public void setSslVerifierEnabled(boolean sslVerifierEnabled) {
        this.sslVerifierEnabled = sslVerifierEnabled;
    }

    public String getMsisdnPrefix() {
        return this.msisdnPrefix;
    }

    public void setMsisdnPrefix(String msisdnPrefix) {
        this.msisdnPrefix = msisdnPrefix;
    }

    public int getMsisdnLength() {
        return this.msisdnLength;
    }

    public void setMsisdnLength(int msisdnLength) {
        this.msisdnLength = msisdnLength;
    }

    public String getProfile() {
        return this.profile;
    }

    public String getSwaggerTitle() {
        return this.swaggerTitle;
    }

    public void setSwaggerTitle(String swaggerTitle) {
        this.swaggerTitle = swaggerTitle;
    }

    public String getSwaggerDescription() {
        return this.swaggerDescription;
    }

    public void setSwaggerDescription(String swaggerDescription) {
        this.swaggerDescription = swaggerDescription;
    }

    public String getDeveloperName() {
        return this.developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getDeveloperEmail() {
        return this.developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }
}