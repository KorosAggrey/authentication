package com.kovatech.auth.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class WsHeader {
    private String requestRefId;
    private int responseCode;
    private String responseMessage;
    private String customerMessage;
    private String timestamp;

    public String getRequestRefId() {
        return this.requestRefId;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public String getCustomerMessage() {
        return this.customerMessage;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public WsHeader() {
    }

    public WsHeader(String requestRefId, int responseCode, String responseMessage, String customerMessage, String timestamp) {
        this.requestRefId = requestRefId;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.customerMessage = customerMessage;
        this.timestamp = timestamp;
    }
}
