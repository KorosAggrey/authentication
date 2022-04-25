package com.kovatech.auth.core.model;

public class WsResponseDetails {
    private String errorCode;
    private int responseCode;
    private String serviceName;
    private String errorType;
    private String technicalMessage;
    private String customerMessage;
    private String errorSource;

    public WsResponseDetails() {
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getTechnicalMessage() {
        return this.technicalMessage;
    }

    public String getErrorSource() {
        return this.errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public void setTechnicalMessage(String technicalMessage) {
        this.technicalMessage = technicalMessage;
    }

    public String getCustomerMessage() {
        return this.customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public WsResponseDetails(String errorCode, int responseCode, String serviceName, String errorType, String technicalMessage, String customerMessage, String errorSource) {
        this.errorCode = errorCode;
        this.responseCode = responseCode;
        this.serviceName = serviceName;
        this.errorType = errorType;
        this.technicalMessage = technicalMessage;
        this.customerMessage = customerMessage;
        this.errorSource = errorSource;
    }
}
