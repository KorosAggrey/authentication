package com.kovatech.auth.core.wrapper;

import com.kovatech.auth.core.config.WsStarterConfig;
import com.kovatech.auth.core.config.WsStarterProperties;
import com.kovatech.auth.core.config.WsWrapperUtilityProperties;
import com.kovatech.auth.core.exception.WsBasicAuthException;
import com.kovatech.auth.core.model.WsEncryptedPayload;
import com.kovatech.auth.core.model.WsError;
import com.kovatech.auth.core.model.WsHeader;
import com.kovatech.auth.core.model.WsResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class WsWrapperUtility
        implements WebFilter {
    private final WsWrapperUtilityProperties wrapperProperties;
    private final WsWrapperService wrapperService;
    private final Map<String, String> processes;
    private final Map<String, String> credentials;
    private final boolean debugMode;

    public WsWrapperUtility(WsWrapperUtilityProperties wrapperProperties, WsStarterProperties starterProperties, WsWrapperService wrapperService, WsStarterConfig starterConfig) {
        this.wrapperProperties = wrapperProperties;
        this.wrapperService = wrapperService;
        this.processes = starterConfig.process();
        this.credentials = starterConfig.credentials();
        this.debugMode = "production".equals(starterProperties.getProfile()) ? false : wrapperProperties.isDebugModeEnabled();
    }


    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!exchange.getRequest().getURI().getPath().contains("swagger") &&
                !exchange.getRequest().getURI().getPath().contains("api-docs") && !exchange.getRequest().getURI().getPath().contains("actuator")) {

            getRequestAuth(exchange.getRequest().getHeaders().getFirst("Authorization"));
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String refId = headers.getFirst("x-correlation-conversationid");
            String key = this.wrapperService.generateKey();
            boolean encryptResponse = this.wrapperService.securePayload(exchange.getRequest().getMethod().name(), headers
                    .getFirst("x-source-system"), headers.getFirst("host"), this.wrapperProperties.getSecuredResponseMethods(), this.wrapperProperties
                    .getExemptedSourceSystems(), this.wrapperProperties.getInternalSourceSystems(), this.wrapperProperties
                    .getExternalHosts());
            List<WsError> errors = validationErrors(headers);
            if (!errors.isEmpty()) {
                return validationResponse(exchange.getResponse(), refId, encryptResponse, key, errors);
            }
            this.processes.put("_" + refId, LocalDateTime.now().toString());
            this.processes.put(refId, exchange.getRequest().getMethod() + ":" + exchange.getRequest().getURI().getPath());
            return chain.filter(exchangeDecorator(exchange, key, headers.getFirst("x-source-system"), headers
                    .getFirst("host"), encryptResponse));
        }
        return chain.filter(exchange);
    }


    private ServerWebExchangeDecorator exchangeDecorator(final ServerWebExchange exchange, final String key, final String source, final String host, final boolean encrypt) {
        return new ServerWebExchangeDecorator(exchange) {
            public ServerHttpRequest getRequest() {
                return new WsRequestWrapper(super.getRequest(), WsWrapperUtility.this.wrapperService.getHashSet(WsWrapperUtility.this.wrapperProperties.getRemovedHeaders(), ","), WsWrapperUtility.this
                        .wrapperService.securePayload(exchange.getRequest().getMethod().name(), source, host, WsWrapperUtility.this
                                .wrapperProperties.getSecuredRequestMethods(), WsWrapperUtility.this.wrapperProperties.getExemptedSourceSystems(), WsWrapperUtility.this
                                .wrapperProperties.getInternalSourceSystems(), WsWrapperUtility.this.wrapperProperties.getExternalHosts()));
            }

            public ServerHttpResponse getResponse() {
                return new WsResponseWrapper(super.getResponse(), encrypt, key);
            }
        };
    }


    private List<WsError> validationErrors(HttpHeaders headers) {
        String refId = (headers.getFirst("x-correlation-conversationid") == null) ? UUID.randomUUID().toString() : headers.getFirst("x-correlation-conversationid");
        List<WsError> errors = new ArrayList<>();
        for (String header : requiredHeaders(headers.getFirst("x-app"))) {
            Map<String, String> headersToValidate = getHeadersToValidate();
            Map<String, String> allowedSystems = getAllowedSystems(headers.getFirst("host"));
            if (headers.containsKey(header)) {
                if (allowedSystems.containsKey(header) &&
                        !this.wrapperService.getHashSet(allowedSystems.get(header), ",").contains(headers.getFirst(header))) {
                    getErrorLogs(headers, errors, header);
                    continue;
                }
                if (headersToValidate.containsKey(header) && !this.wrapperService.isValid(headersToValidate.get(header), headers
                        .getFirst(header)))
                    getErrorLogs(headers, errors, header);
                continue;
            }
            errors.add(new WsError(400, "Missing Request Header",
                    this.debugMode ? ("Missing '" + header + "' required header") : "Sorry, request could not be processed"));
            this.wrapperService.log("MISSING_HEADER", refId, "Missing '" + header + "' required header");
        }

        return errors;
    }

    private void getErrorLogs(HttpHeaders headers, List<WsError> errors, String header) {
        errors.add(new WsError(400, "Request Header Invalid",
                this.debugMode ? (
                        header + " '" + headers.getFirst(header) + "' is not allowed") : "Sorry, request could not be processed"));
        this.wrapperService.log("INVALID_HEADER", headers.getFirst("x-correlation-conversationid"), header +
                " '" + headers
                .getFirst(header) + "' is not allowed" + " from host '" + headers
                .getFirst("host") + "'");
    }


    private Mono<Void> validationResponse(ServerHttpResponse response, String requestRefId, boolean encryptResponse, String key, List<WsError> errors) {
        String body = this.wrapperService.serialize(new WsResponse(new WsHeader((requestRefId == null) ? UUID.randomUUID().toString() :
                requestRefId, ((WsError) errors.get(0)).getCode(), ((WsError) errors.get(0)).getError(), ((WsError) errors.get(0)).getMessage(),
                String.valueOf(LocalDateTime.now())), this.debugMode ? errors : null));
        return getResponseBody(response, encryptResponse, key, body);
    }

    private Mono<Void> getResponseBody(ServerHttpResponse response, boolean encryptResponse, String key, String body) {
        body = encryptResponse ? this.wrapperService.serialize(new WsEncryptedPayload(this.wrapperService.encrypt(key, body))) : body;
        response.getHeaders().add("X-MessageID", encryptResponse ? key : "");
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setContentLength(body.length());
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }


    private Mono<Void> sessionResponse(ServerHttpResponse response, String requestRefId, boolean encryptResponse, String key) {
        String body = this.wrapperService.serialize(new WsResponse(new WsHeader((requestRefId == null) ? UUID.randomUUID().toString() :
                requestRefId, 210, "Forbidden", "Sorry, device session is invalid",
                LocalDateTime.now().toString()), null));
        return getResponseBody(response, encryptResponse, key, body);
    }

    private HashSet<String> requiredHeaders(String app) {
        HashSet<String> requiredHeaders = new HashSet();
        requiredHeaders.addAll(this.wrapperService.getHashSet(this.wrapperProperties.getRequiredHeaders(), ","));
        return requiredHeaders;
    }

    private HashSet<String> validatedHeaders() {
        HashSet<String> requiredHeaders = new HashSet();
        requiredHeaders.addAll(this.wrapperService.getHashSet(this.wrapperProperties.getHeaderValidation(), ";"));
        return requiredHeaders;
    }

    private Map<String, String> getAllowedSystems(String host) {
        Map<String, String> headers = new HashMap<>();
        if (this.wrapperService.getHashSet(this.wrapperProperties.getExternalHosts(), ",").contains(host)) {
            if (this.wrapperProperties.getAcceptedSourceSystems() != null)
                headers.put("x-source-system", this.wrapperProperties.getAcceptedSourceSystems());
            if (this.wrapperProperties.getAcceptedApplications() != null)
                headers.put("x-app", this.wrapperProperties.getAcceptedApplications());
        } else {
            headers.put("x-source-system", this.wrapperProperties.getInternalSourceSystems());
        }
        return headers;
    }

    private Map<String, String> getHeadersToValidate() {
        Map<String, String> v = new HashMap<>();
        for (String h : validatedHeaders()) {
            String[] header = h.split("=");
            v.put(header[0], header[1]);
        }
        return v;
    }

    private void getRequestAuth(String auth) {
        if (auth != null && auth.toLowerCase().startsWith("basic")) {
            String[] user = getUser(auth);
            if (!this.credentials.containsKey(user[0]) || !this.credentials.getOrDefault(user[0], "").equals(user[1])) {
                throw new WsBasicAuthException("Sorry, you are not authorized to access the resource");
            }
        } else {
            throw new WsBasicAuthException("Sorry, you are not authorized to access the resource");
        }
    }

    private String[] getUser(String auth) {
        try {
            String base64Credentials = auth.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            return (new String(credDecoded, StandardCharsets.UTF_8)).split(":", 2);
        } catch (IllegalArgumentException ex) {
            throw new WsBasicAuthException("Sorry, you are not authorized to access the resource");
        }
    }
}