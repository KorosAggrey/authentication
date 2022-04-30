package com.kovatech.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dxl.ms")
public class MsConfigProperties {

    @Value("${dxl.basic.auth.jwtSecretKey}")
    private String staticIv;
    @Value("${dxl.basic.auth.jwtExpirationMs}")
    private String jwtExpirationTime;

    public String getStaticIv() {
        return staticIv;
    }

    public void setStaticIv(String staticIv) {
        this.staticIv = staticIv;
    }

    public String getJwtExpirationTime() {
        return jwtExpirationTime;
    }

    public void setJwtExpirationTime(String jwtExpirationTime) {
        this.jwtExpirationTime = jwtExpirationTime;
    }
}