package com.kovatech.auth.core.model;

public class WsEncryptedPayload {
    private String response;

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public WsEncryptedPayload() {
    }

    public WsEncryptedPayload(String response) {
        this.response = response;
    }
}
