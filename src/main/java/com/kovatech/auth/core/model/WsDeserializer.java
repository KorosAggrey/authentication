package com.kovatech.auth.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WsDeserializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WsDeserializer() {
    }

    public T get(String record, Class<T> type) {
        try {
            return this.objectMapper.readValue(record, type);
        } catch (IOException var4) {
            return (T) type.getClass();
        }
    }
}
