package com.kovatech.auth.exception;

import com.kovatech.auth.core.exception.WsExceptionHandler;
import com.kovatech.auth.core.service.WsMappingService;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author aggrey
 */
@RestControllerAdvice
public class MsExceptionHandler extends WsExceptionHandler {
    
    public MsExceptionHandler(WsMappingService mappingService) {
        super(mappingService);
    }
    
}