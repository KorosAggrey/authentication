package com.kovatech.auth.core.exception;

import com.kovatech.auth.core.model.WsResponseDetails;
import com.kovatech.auth.core.service.WsMappingService;
import com.kovatech.auth.core.utils.WsStarterVariables;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


public class WsExceptionHandler {
    private final WsMappingService mappingService;

    public WsExceptionHandler(WsMappingService mappingService) {
        this.mappingService = mappingService;
    }

    @ExceptionHandler({WebClientResponseException.class})
    protected Mono<ResponseEntity> handleWebClientResponseException(WebClientResponseException ex, ServerHttpRequest request) {
        return setErrResponse("CRW100057", ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({WebClientRequestException.class})
    protected Mono<ResponseEntity> handleWebClientRequestException(WebClientRequestException ex, ServerHttpRequest request) {
        return setErrResponse(errorCodes().getOrDefault((ex.getCause() == null) ? ex.getRootCause().getClass().getSimpleName() :
                ex.getCause().getClass().getSimpleName(), "422"), ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({WsBadRequestException.class})
    protected Mono<ResponseEntity> handleBadRequest(WsBadRequestException ex, ServerHttpRequest request) {
        return setErrResponse(ex.getMessage(), ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({WsResourceNotFoundException.class})
    protected Mono<ResponseEntity> handleResourceNotFound(WsResourceNotFoundException ex, ServerHttpRequest request) {
        return setErrResponse("CRW100052", ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected Mono<ResponseEntity> handleSQLException(DataIntegrityViolationException ex, ServerHttpRequest request) {
        return setErrResponse("CRW100058", ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({WebExchangeBindException.class})
    protected Mono<ResponseEntity> handleWebExchangeBindException(WebExchangeBindException ex, ServerHttpRequest request) {
        return setErrResponse("CRW100061", ex.getBindingResult().getFieldError().getDefaultMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, true);
    }

    @ExceptionHandler({ServerWebInputException.class})
    protected Mono<ResponseEntity> handleServerWebInputException(ServerWebInputException ex, ServerHttpRequest request) {
        return setErrResponse("CRW100052", ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.OK, false);
    }

    @ExceptionHandler({Exception.class})
    protected Mono<ResponseEntity> handleAllExceptions(Exception ex, ServerHttpRequest request) {
        return setErrResponse("CRE100054", ex.getMessage(), WsStarterVariables.NULL, request
                .getHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, false);
    }


    protected Mono<ResponseEntity> setErrResponse(String errCode, String msg, Object body, HttpHeaders httpHeaders, HttpStatus status, boolean errorMessagePreferred) {
        WsResponseDetails errorDetail = this.mappingService.getErrorMapper(errCode, httpHeaders
                .getFirst("x-correlation-conversationid"), "");
        return Mono.just(new ResponseEntity(this.mappingService.setErrResponse(errorDetail, body, msg, errorMessagePreferred, httpHeaders
                .toSingleValueMap()), status));
    }

    private Map<String, String> errorCodes() {
        Map<String, String> errors = new HashMap<>(8);
        errors.put("AnnotatedConnectException", "502");
        errors.put("ConnectException", "502");
        errors.put("ReadTimeoutException", "408");
        errors.put("ConnectTimeoutException", "408");
        return errors;
    }
}