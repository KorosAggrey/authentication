package com.kovatech.auth.core.config;

import com.kovatech.auth.core.enums.WsProcessLogger;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration
@ConditionalOnProperty(name = {"dxl.starter.web-client-enabled"}, havingValue = "true")
public class WsWebClientConfig {
    private final WsStarterProperties starterProperties;

    public WsWebClientConfig(WsStarterProperties starterProperties) {
        this.starterProperties = starterProperties;
    }

    @Bean
    public WebClient webClient() throws SSLException {
        WsProcessLogger.valueOf("INFO").log("Initializing DxL starter web client");

        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext)).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(this.starterProperties.getConnectTimeout())).responseTimeout(Duration.ofMillis(this.starterProperties.getReadTimeout())).doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(this.starterProperties.getReadTimeout(), TimeUnit.MILLISECONDS)).addHandlerLast(new WriteTimeoutHandler(this.starterProperties.getReadTimeout(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}