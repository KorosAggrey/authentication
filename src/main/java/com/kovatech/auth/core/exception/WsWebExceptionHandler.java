package com.kovatech.auth.core.exception;

import com.kovatech.auth.core.config.WsWrapperUtilityProperties;
import com.kovatech.auth.core.model.WsEncryptedPayload;
import com.kovatech.auth.core.service.WsStarterService;
import com.kovatech.auth.core.wrapper.WsWrapperService;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class WsWebExceptionHandler
        extends AbstractErrorWebExceptionHandler {
    private final WsWrapperService wrapperService;
    private final WsStarterService starterService;
    private final WsWrapperUtilityProperties properties;

    public WsWebExceptionHandler(WsErrorAttributes g, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer, WsWrapperService wrapperService, WsStarterService starterService, WsWrapperUtilityProperties properties) {
        super(g, new ResourceProperties(), applicationContext);
        this.starterService = starterService;
        this.properties = properties;
        setMessageWriters(serverCodecConfigurer.getWriters());
        setMessageReaders(serverCodecConfigurer.getReaders());
        this.wrapperService = wrapperService;
    }


    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);
        int status = ((Integer) errorPropertiesMap.get("status")).intValue();
        errorPropertiesMap.remove("status");
        String key = "";
        Object encryptedBody = null;
        if (this.wrapperService.securePayload(request.exchange().getRequest().getMethod().name(), request
                .headers().firstHeader("x-source-system"), request
                .headers().firstHeader("host"), this.properties.getSecuredResponseMethods(), this.properties
                .getExemptedSourceSystems(), this.properties.getInternalSourceSystems(), this.properties.getExternalHosts())) {
            key = this.wrapperService.generateKey();
            encryptedBody = new WsEncryptedPayload(this.wrapperService.encrypt(key, this.starterService.serialize(errorPropertiesMap)));
        }
        return ServerResponse.status((status == 401) ? HttpStatus.valueOf(status) : HttpStatus.valueOf(200))
                .contentType(MediaType.APPLICATION_JSON_UTF8).header("X-MessageID", new String[]{key
                }).body(BodyInserters.fromObject((encryptedBody == null) ? errorPropertiesMap : encryptedBody));
    }
}