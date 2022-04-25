package com.kovatech.auth.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WsSwaggerConfig {
    private final WsStarterProperties properties;

    public WsSwaggerConfig(WsStarterProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return (new OpenAPI())
                .components(new Components())
                .info((new Info())
                        .title(this.properties.getSwaggerTitle())
                        .description(this.properties.getSwaggerDescription())
                        .version(this.properties.getAppVersion())
                        .contact((new Contact()).name(this.properties.getDeveloperName()).email(this.properties.getDeveloperEmail())));
    }
}