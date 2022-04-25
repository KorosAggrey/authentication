package com.kovatech.auth.core.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.enums.WsProcessLogger;
import com.kovatech.auth.core.enums.WsWrapperUtilityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Pattern;


@Service
public class WsWrapperService {
    private final ObjectMapper objectMapper = new ObjectMapper();


    public boolean securePayload(String method, String sourceSystem, String host, String securedMethods, String exemptedSourceSystems, String internalSystems, String externalHosts) {
        return isInternalSystem(sourceSystem, host, externalHosts, internalSystems) ? Boolean.FALSE.booleanValue() : (
                (getHashSet(securedMethods, ",").contains(method) && !getHashSet(exemptedSourceSystems, ",").contains(sourceSystem)));
    }

    public boolean isInternalSystem(String sourceSystem, String host, String externalHosts, String internalSystems) {
        return (!getHashSet(externalHosts, ",").contains(host) &&
                getHashSet(internalSystems, ",").contains(sourceSystem));
    }

    public boolean isValid(String regex, String header) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(header).matches();
    }

    public HashSet<String> getHashSet(String text, String del) {
        if (text == null || "".equals(text)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(text.split(del)));
    }


    public String generateKey() {
        String key = "";
        for (int l = 0; l <= 2; l++) {
            key = key + ((l == 0) ? randomKey(16) : ("|" + randomKey(16)));
        }
        return key;
    }

    public String randomKey(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*@!$".charAt(rnd.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*@!$".length())));
        }
        return sb.toString();
    }

    public String encrypt(String xMessageId, String responseBody) {
        /*Details details = AesCbcEncryptorDecryptor.encryptPayload(xMessageId, responseBody);
        return details.isSuccess() ? details.getEncryptedString() : "{}";*/
        return responseBody;
    }

    public String serialize(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            WsProcessLogger.ERROR.log(ex.getMessage());
            return "{}";
        }
    }

    @Async
    public void log(String type, String requestId, String message) {
        WsWrapperUtilityLogger.valueOf(type).log(requestId, message);
    }
}