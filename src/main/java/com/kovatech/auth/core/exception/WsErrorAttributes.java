package com.kovatech.auth.core.exception;

import com.kovatech.auth.core.config.WsStarterConfig;
import com.kovatech.auth.core.enums.WsProcessLogger;
import com.kovatech.auth.core.model.WsHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.Map;


@Component
class WsErrorAttributes
        extends DefaultErrorAttributes {
    private final Map<String, String> processes;
    @Value("${dxl.response.mapper.default-error-params}")
    private String errorParams;

    public WsErrorAttributes(WsStarterConfig starterConfig) {
        this.processes = starterConfig.process();
    }

    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        String requestId = request.headers().firstHeader("x-correlation-conversationid");
        Map<String, Object> errorAttributes = super.getErrorAttributes(request, options);

        String response = "Not Found".equals(errorAttributes.get("error").toString()) ? ("Request Mapping '" + errorAttributes.getOrDefault("path", "") + "' Not Found") : errorAttributes.get("error").toString();

        errorAttributes.put("header", new WsHeader(requestId,
                Integer.valueOf(errorAttributes.get("status").toString()).intValue(), response,
                "401".equals(errorAttributes.get("status").toString()) ? "Full authentication is required to access the resource" :
                        "Sorry, request could not be processed", String.valueOf(LocalDateTime.now())));
        errorAttributes.put("body", null);
        WsProcessLogger.valueOf("ERROR").log(requestId, request.uri().getPath(), "1ms",
                (errorAttributes.get("message") == null) ? response : errorAttributes.get("message").toString());
        for (String param : this.errorParams.split(",")) {
            errorAttributes.remove(param);
        }
        this.processes.remove(requestId);
        this.processes.remove("_" + requestId);
        return errorAttributes;
    }
}