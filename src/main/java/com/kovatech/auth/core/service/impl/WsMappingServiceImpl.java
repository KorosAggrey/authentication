package com.kovatech.auth.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.config.WsMapperProperties;
import com.kovatech.auth.core.config.WsStarterConfig;
import com.kovatech.auth.core.enums.WsProcessLogger;
import com.kovatech.auth.core.logging.WsLogManager;
import com.kovatech.auth.core.model.WsHeader;
import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.model.WsResponseDetails;
import com.kovatech.auth.core.service.WsMappingService;
import com.kovatech.auth.core.service.WsResponseMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
public class WsMappingServiceImpl implements WsMappingService, WsResponseMapper {
    private static final String CRS = "CRS";
    private static final String CRE = "CRE";
    private static final String CRW = "CRW";
    private final WsMapperProperties properties;
    private final Map<String, String> processes;
    private final Map<String, WsResponseDetails> responses;
    private final Map<String, String> errorMapperOverride;

    public WsMappingServiceImpl(WsStarterConfig starterConfig, WsMapperProperties properties) {
        this.responses = starterConfig.responseMessages();
        this.processes = starterConfig.process();
        this.properties = properties;
        this.errorMapperOverride = starterConfig.errorMapperOverride();
    }


    public WsResponseDetails getErrorMapper(String errorCode, String requestRefId, String processParams) {
        String code = (errorCode == null) ? "500" : errorCode;
        WsResponseDetails errorDetail = this.responses.get(code);
        if (this.properties.isErrorCodeOverrideEnabled() && errorDetail == null) {
            errorDetail = this.responses.get(this.errorMapperOverride.getOrDefault(processParams.split(",")[0], errorCode));
        }

        if (errorDetail == null) {
            errorDetail = String.valueOf(this.properties.getDefaultSuccessCode()).equals(code) ? new WsResponseDetails("CRS", this.properties.getDefaultSuccessCode(), "", "", "Success", this.properties.getDefaultSuccessMessage(), "") : new WsResponseDetails("CRW", this.properties.getDefaultResponseCode(), "", "", "Error Code Not Mapped", this.properties.getDefaultResponseMessage(), "");
            logMissedError(errorCode, requestRefId);
        }
        return errorDetail;
    }


    public Mono<WsResponse> setApiResponse(String errorCode, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        WsResponseDetails ed = getErrorMapper(errorCode, headers.get("x-correlation-conversationid"), processParams);
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return Mono.just(new WsResponse(new WsHeader(headers.get("x-correlation-conversationid"), ed.getResponseCode(), ed
                .getTechnicalMessage(), ed.getCustomerMessage(), String.valueOf(LocalDateTime.now())), responseBody));
    }


    public Mono<WsResponse> setApiResponse(WsResponseDetails ed, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return Mono.just(new WsResponse(new WsHeader(headers.get("x-correlation-conversationid"), ed.getResponseCode(), ed
                .getTechnicalMessage(), ed.getCustomerMessage(), String.valueOf(LocalDateTime.now())), responseBody));
    }


    public WsResponse setApiResponse(String processParams, String errorCode, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        WsResponseDetails ed = getErrorMapper(errorCode, headers.get("x-correlation-conversationid"), processParams);
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return new WsResponse(new WsHeader(headers.get("x-correlation-conversationid"), ed.getResponseCode(), ed
                .getTechnicalMessage(), ed.getCustomerMessage(), String.valueOf(LocalDateTime.now())), responseBody);
    }


    public WsResponse setApiResponse(String processParams, WsResponseDetails ed, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return new WsResponse(new WsHeader(headers.get("x-correlation-conversationid"), ed.getResponseCode(), ed
                .getTechnicalMessage(), ed.getCustomerMessage(), String.valueOf(LocalDateTime.now())), responseBody);
    }


    public Mono<Object> setTmfResponse(String errorCode, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        WsResponseDetails ed = getErrorMapper(errorCode, headers.get("x-correlation-conversationid"), processParams);
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return Mono.just(responseBody);
    }


    public Mono<Object> setTmfResponse(WsResponseDetails ed, Object responseBody, String processParams, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return Mono.just(responseBody);
    }


    public Object setTmfResponse(String processParams, String errorCode, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        WsResponseDetails ed = getErrorMapper(errorCode, headers.get("x-correlation-conversationid"), processParams);
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return responseBody;
    }


    public Object setTmfResponse(String processParams, WsResponseDetails ed, Object responseBody, String requestPayload, String responsePayload, String errorMessage, boolean logHeaders, Map<String, String> headers) {
        logApiRequest(ed, processParams, LocalDateTime.now(), requestPayload, responsePayload, errorMessage, logHeaders, headers);
        return responseBody;
    }


    public WsResponse setErrResponse(WsResponseDetails errorDetail, Object responseBody, String errorMessage, boolean errorMessagePreferred, Map<String, String> headers) {
        WsResponse apiResponse = new WsResponse(new WsHeader(headers.get("x-correlation-conversationid"), errorDetail.getResponseCode(), errorDetail.getTechnicalMessage(), errorMessagePreferred ? errorMessage : errorDetail.getCustomerMessage(), String.valueOf(LocalDateTime.now())), responseBody);
        logApiRequest(errorDetail, "", LocalDateTime.now(), "", "", errorMessage, false, headers);

        return apiResponse;
    }


    public void logApiRequest(WsResponseDetails error, String processParams, LocalDateTime start, String requestPayload, String responsePayload, String errMsg, boolean logHeaders, Map<String, String> headers) {
        String requestId = headers.get("x-correlation-conversationid");
        String[] params = getProcessParams(processParams, header(this.processes.getOrDefault(requestId, "WEBFLUX_MS_STARTER,WEBFLUX_MS_STARTER,WEBFLUX_MS_STARTER")));
        LocalDateTime startTime = LocalDateTime.parse(this.processes.getOrDefault("_" + requestId, start.toString()));
        this.processes.remove(requestId);
        this.processes.remove("_" + requestId);
        if (error.getErrorCode().startsWith("CRS") || error.getResponseCode() < 300) {
            WsLogManager.info(header(headers.getOrDefault("x-correlation-conversationid", "")), params[0], getParam(params, 1),
                    processDuration(startTime), header(headers.getOrDefault("x-msisdn", "")),
                    header(headers.getOrDefault("x-identity", "")),
                    header(headers.getOrDefault("x-source-system", "")),
                    getParam(params, 2), error.getTechnicalMessage(), error.getResponseCode(), error.getCustomerMessage(), errMsg, requestPayload, responsePayload,
                    logHeaders ? headers.toString() : "");
        } else if (error.getErrorCode().startsWith("CRE") || (error.getResponseCode() >= 500 && error.getResponseCode() < 600)) {
            WsLogManager.error(header(headers.getOrDefault("x-correlation-conversationid", "")), params[0], getParam(params, 1),
                    processDuration(startTime), header(headers.getOrDefault("x-msisdn", "")),
                    header(headers.getOrDefault("x-identity", "")),
                    header(headers.getOrDefault("x-source-system", "")),
                    getParam(params, 2), error.getTechnicalMessage(), error
                            .getResponseCode(), error.getCustomerMessage(), errMsg, requestPayload, responsePayload);
        } else {
            WsLogManager.warn(header(headers.getOrDefault("x-correlation-conversationid", "")), params[0], getParam(params, 1),
                    processDuration(startTime), header(headers.getOrDefault("x-msisdn", "")),
                    header(headers.getOrDefault("x-identity", "")),
                    header(headers.getOrDefault("x-source-system", "")),
                    getParam(params, 2), error.getTechnicalMessage(), error.getResponseCode(), error.getCustomerMessage(), errMsg, requestPayload, responsePayload);
        }
    }


    private String[] getProcessParams(String processParams, String params) {
        return "".equals(processParams) ? params.split(",") : processParams.split(",");
    }

    private String header(String name) {
        return (name == null) ? "" : name;
    }

    private String getParam(String[] params, int key) {
        return (params.length > key) ? params[key] : params[0];
    }

    private String processDuration(LocalDateTime startTime) {
        return Duration.between(startTime, LocalDateTime.now()).toMillis() + "ms";
    }

    private void logMissedError(String errorCode, String requestRefId) {
        WsProcessLogger.valueOf("WARN").log(requestRefId, "Fetch Mapped Response/Error", "1ms", "Response/Error code '" + errorCode + "' not found. Response/error not mapped");
    }
}