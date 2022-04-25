package com.kovatech.auth.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WsBadRequestException
        extends RuntimeException {
    public WsBadRequestException() {
    }

    public WsBadRequestException(String message) {
        super(message);
    }

    public WsBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}