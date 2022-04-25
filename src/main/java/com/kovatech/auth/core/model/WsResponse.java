package com.kovatech.auth.core.model;

public class WsResponse {
    private WsHeader header;
    private Object body;

    public WsHeader getHeader() {
        return this.header;
    }

    public void setHeader(WsHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return this.body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public WsResponse() {
    }

    public WsResponse(WsHeader header, Object body) {
        this.header = header;
        this.body = body;
    }
}
