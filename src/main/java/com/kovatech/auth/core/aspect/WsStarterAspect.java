package com.kovatech.auth.core.aspect;

import com.kovatech.auth.core.annotation.WsExecutionTime;
import com.kovatech.auth.core.annotation.WsProcess;
import com.kovatech.auth.core.annotation.WsTransaction;
import com.kovatech.auth.core.config.WsStarterConfig;
import com.kovatech.auth.core.enums.WsProcessLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class WsStarterAspect {
    private final Map<String, String> process;

    public WsStarterAspect(WsStarterConfig starterConfig) {

        this.process = starterConfig.process();

    }

    @Before("@annotation(dxlProcess) && args(headers,..)")
    public void getProcess(WsProcess dxlProcess, Map<String, String> headers) {

        this.process.put(headers.get("x-correlation-conversationid"), dxlProcess.value());

    }

    @Before("@annotation(dxlTransaction) && args(requestRefId,..)")
    public void getTransaction(WsTransaction dxlTransaction, String requestRefId) {

        this.process.put(requestRefId, dxlTransaction.value());

    }

    @Around("@annotation(dxlExecutionTime)")
    public Object executionTime(ProceedingJoinPoint joinPoint, WsExecutionTime dxlExecutionTime) throws Throwable {

        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        WsProcessLogger.valueOf("INFO").log("".equals(dxlExecutionTime.process()) ? (
                methodSignature.getName() + ", process duration: " + executionTime + "ms") : (
                dxlExecutionTime.process() + ", process duration: " + executionTime + "ms"));

        return proceed;

    }

}