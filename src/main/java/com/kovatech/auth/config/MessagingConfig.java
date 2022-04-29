package com.kovatech.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dxl.ms")
public class MessagingConfig {
    @Value("${dxl.ms.sendgridApiKey}")
    private String sendGridKey;

    @Value("${dxl.ms.sendingEmail}")
    private String sendingEmail;

    @Value("${dxl.ms.sendingId}")
    private int sendingId;

    public String getSendGridKey() {
        return sendGridKey;
    }

    public void setSendGridKey(String sendGridKey) {
        this.sendGridKey = sendGridKey;
    }

    public String getSendingEmail() {
        return sendingEmail;
    }

    public int getSendingId() {
        return sendingId;
    }

    public void setSendingId(int sendingId) {
        this.sendingId = sendingId;
    }

    public void setSendingEmail(String sendingEmail) {
        this.sendingEmail = sendingEmail;
    }
}
