package com.kovatech.auth.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WsErrorResponse {
    protected HeaderResponse header;
    private List<ErrorDetail> body;

    public WsErrorResponse() {
    }

    public HeaderResponse getHeader() {
        return this.header;
    }

    public void setHeader(HeaderResponse header) {
        this.header = header;
    }

    public List<ErrorDetail> getBody() {
        return this.body;
    }

    public void setBody(List<ErrorDetail> body) {
        this.body = body;
    }

    public class HeaderResponse {
        @JsonProperty("requestRefId")
        private String requestRefId;
        @JsonProperty("responseCode")
        private int responseCode;
        @JsonProperty("responseMessage")
        private String responseMessage;
        @JsonProperty("customerMessage")
        private String customerMessage;
        @JsonProperty("timestamp")
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

        public HeaderResponse(String requestRefId, int responseCode, String responseMessage, String customerMessage, String timestamp) {
            this.requestRefId = requestRefId;
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.customerMessage = customerMessage;
            this.timestamp = timestamp;
        }
    }

    public class ErrorDetail {
        @JsonProperty("attribute")
        private String key;
        @JsonProperty("message")
        private String value;

        public ErrorDetail(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
