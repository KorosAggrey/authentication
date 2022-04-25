package com.kovatech.auth.core.model;

public class WsError {
    private int code;
    private String error;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WsError() {
    }

    public WsError(int code, String error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }
}
