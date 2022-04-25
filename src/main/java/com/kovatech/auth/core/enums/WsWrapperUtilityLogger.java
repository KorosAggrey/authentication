package com.kovatech.auth.core.enums;


import com.kovatech.auth.core.logging.WsLogManager;

public enum WsWrapperUtilityLogger {
    INVALID_HEADER {
        public void log(String requestId, String message) {
            WsLogManager.starterWarn(requestId, "Header-Validation", "1ms", message);
        }
    },
    MISSING_HEADER {
        public void log(String requestId, String message) {
            WsLogManager.starterWarn(requestId, "Header-Validation", "1ms", message);
        }
    },
    INVALID_SESSION {
        public void log(String requestId, String message) {
            WsLogManager.starterWarn(requestId, "Session-Validation", "1ms", message);
        }
    },
    NOT_AUTHENTICATED {
        public void log(String requestId, String message) {
            WsLogManager.starterWarn(requestId, "User-Authentication", "1ms", message);
        }
    };

    public abstract void log(String paramString1, String paramString2);
}