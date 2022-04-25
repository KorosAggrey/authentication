package com.kovatech.auth.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "dxl.response.mapper")
public class WsMapperProperties {
    private int defaultSuccessCode;
    private int defaultResponseCode;
    private String defaultSuccessMessage;
    private String defaultResponseMessage;
    private String mappingName;
    private String starterMappingName;
    private boolean errorCodeOverrideEnabled;
    private List<String> errorCodeOverride;

    public int getDefaultSuccessCode() {

        return this.defaultSuccessCode;
    }

    public void setDefaultSuccessCode(int defaultSuccessCode) {

        this.defaultSuccessCode = defaultSuccessCode;
    }

    public int getDefaultResponseCode() {
        return this.defaultResponseCode;
    }

    public void setDefaultResponseCode(int defaultResponseCode) {
        this.defaultResponseCode = defaultResponseCode;
    }

    public String getDefaultSuccessMessage() {
        return this.defaultSuccessMessage;
    }

    public void setDefaultSuccessMessage(String defaultSuccessMessage) {
        this.defaultSuccessMessage = defaultSuccessMessage;
    }

    public String getDefaultResponseMessage() {
        return this.defaultResponseMessage;
    }

    public void setDefaultResponseMessage(String defaultResponseMessage) {
        this.defaultResponseMessage = defaultResponseMessage;
    }

    public String getMappingName() {
        return this.mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getStarterMappingName() {
        return this.starterMappingName;
    }

    public void setStarterMappingName(String starterMappingName) {

        this.starterMappingName = starterMappingName;
    }

    public boolean isErrorCodeOverrideEnabled() {
        return this.errorCodeOverrideEnabled;
    }

    public void setErrorCodeOverrideEnabled(boolean errorCodeOverrideEnabled) {

        this.errorCodeOverrideEnabled = errorCodeOverrideEnabled;
    }

    public List<String> getErrorCodeOverride() {
        return this.errorCodeOverride;
    }

    public void setErrorCodeOverride(List<String> errorCodeOverride) {

        this.errorCodeOverride = errorCodeOverride;
    }
}