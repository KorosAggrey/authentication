package com.kovatech.auth.core.service;

import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.model.WsResponseDetails;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface WsResponseMapper {
  Mono<WsResponse> setApiResponse(String errorCode, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  Mono<WsResponse> setApiResponse(WsResponseDetails errorDetail, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  WsResponse setApiResponse(String processParams, String errorCode, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  WsResponse setApiResponse(String processParams, WsResponseDetails errorDetail, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  Mono<Object> setTmfResponse(String errorCode, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  Mono<Object> setTmfResponse(WsResponseDetails errorDetail, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  Object setTmfResponse(String processParams, String errorCode, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);

  Object setTmfResponse(String processParams, WsResponseDetails errorDetail, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers);
}