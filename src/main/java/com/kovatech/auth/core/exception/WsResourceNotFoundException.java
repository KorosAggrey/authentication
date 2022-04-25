package com.kovatech.auth.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class WsResourceNotFoundException
        extends RuntimeException {
    public WsResourceNotFoundException() {
    }

    public WsResourceNotFoundException(String message) {
        super(message);
    }

    public WsResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}