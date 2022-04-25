package com.kovatech.auth.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WsBasicAuthException
        extends RuntimeException {
    public WsBasicAuthException() {
    }

    public WsBasicAuthException(String message) {
        super(message);
    }

    public WsBasicAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}