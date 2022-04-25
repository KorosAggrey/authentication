package com.kovatech.auth.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WsLogManager {
    public static final String LOG = "{}";
    public static final String LOG_FORMAT = "TransactionID={} | Process={} | ProcessDuration={} | Message={}";
    public static final String LOGGER_FORMAT = "TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}";
    public static final Logger logger = LoggerFactory.getLogger(WsLogManager.class);

    private WsLogManager() {
    }

    public static void warn(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload) {
        logger.warn("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload});
    }

    public static void warn(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload, String headers) {
        logger.warn("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={} | RequestHeaders={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload, headers});
    }

    public static void info(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload) {
        logger.info("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload});
    }

    public static void info(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload, String headers) {
        logger.info("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={} | RequestHeaders={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload, headers});
    }

    public static void error(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload) {
        logger.error("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload});
    }

    public static void starter(String requestId, String transactionType, String process, String processDuration, String msisdn, String username, String sourceSystem, String targetSystem, String response, int responseCode, String responseMsg, String errorDescription, String requestPayload, String responsePayload) {
        logger.error("TransactionID={} | TransactionType={} | Process={} | ProcessDuration={} | Msisdn={} | Username={} | SourceSystem={} | TargetSystem={}  | Response={} | ResponseCode={}  | ResponseMsg={} | ErrorDescription={} | RequestPayload={} | ResponsePayload={}", new Object[]{requestId, transactionType, process, processDuration, msisdn, username, sourceSystem, targetSystem, response, responseCode, responseMsg, errorDescription, requestPayload, responsePayload});
    }

    public static void starterInfo(String requestId, String process, String timeTaken, String message) {
        logger.info("TransactionID={} | Process={} | ProcessDuration={} | Message={}", new Object[]{requestId, process, timeTaken, message});
    }

    public static void starterWarn(String requestId, String process, String timeTaken, String message) {
        logger.warn("TransactionID={} | Process={} | ProcessDuration={} | Message={}", new Object[]{requestId, process, timeTaken, message});
    }

    public static void starterError(String requestId, String process, String timeTaken, String message) {
        logger.error("TransactionID={} | Process={} | ProcessDuration={} | Message={}", new Object[]{requestId, process, timeTaken, message});
    }
}