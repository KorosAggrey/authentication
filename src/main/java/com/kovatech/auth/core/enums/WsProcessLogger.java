package com.kovatech.auth.core.enums;


import com.kovatech.auth.core.logging.WsLogManager;

public enum WsProcessLogger {
    INFO {
        public void log(String requestId, String process, String timeTaken, String message) {
            WsLogManager.starterInfo(requestId, process, timeTaken, message);
        }

        public void log(String message) {
            WsLogManager.logger.info("{}", message);
        }
    },
    WARN {
        public void log(String requestId, String process, String timeTaken, String message) {
            WsLogManager.starterWarn(requestId, process, timeTaken, message);
        }

        public void log(String message) {
            WsLogManager.logger.warn("{}", message);
        }
    },
    ERROR {
        public void log(String requestId, String process, String timeTaken, String message) {
            WsLogManager.starterError(requestId, process, timeTaken, message);
        }

        public void log(String message) {
            WsLogManager.logger.error("{}", message);
        }
    };

    public abstract void log(String paramString1, String paramString2, String paramString3, String paramString4);

    public abstract void log(String paramString);
}