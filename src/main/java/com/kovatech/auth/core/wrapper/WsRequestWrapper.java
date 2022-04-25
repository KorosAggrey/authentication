package com.kovatech.auth.core.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class WsRequestWrapper
        extends ServerHttpRequestDecorator {
    private final ObjectMapper objectMapper;
    private final ServerHttpRequest requestDecorator;
    private final Set<String> removedHeaders;
    private final boolean decryptRequest;

    public WsRequestWrapper(ServerHttpRequest requestDecorator, Set<String> removedHeaders, boolean decryptRequest) {
        super(requestDecorator);
        this.objectMapper = new ObjectMapper();
        this.requestDecorator = requestDecorator;
        this.removedHeaders = removedHeaders;
        this.decryptRequest = decryptRequest;
    }


    public Flux<DataBuffer> getBody() {
        if (this.decryptRequest) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            return super.getBody().doOnNext(dataBuffer -> {

                try {
                    Channels.newChannel(baos).write(dataBuffer.asByteBuffer());

                    String body = decrypt(this.requestDecorator.getHeaders().getFirst("x-correlation-conversationid"), this.requestDecorator.getHeaders().getFirst("x-messageid"), IOUtils.toString(baos.toByteArray(), "UTF-8"));

                    ByteArrayInputStream input = new ByteArrayInputStream(body.getBytes());
                    Channels.newChannel(input).read(dataBuffer.asByteBuffer());
                } catch (IOException iOException) {
                    try {
                        baos.flush();

                        baos.close();
                    } catch (IOException iOException1) {
                    }
                } finally {
                    try {
                        baos.flush();
                        baos.close();
                    } catch (IOException iOException) {
                    }
                }

            });
        }

        return super.getBody();
    }


    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        for (Map.Entry<String, List<String>> header : super.getHeaders().entrySet()) {
            if (!this.removedHeaders.contains(header.getKey().toLowerCase())) {
                headers.put(header.getKey().toLowerCase(), header.getValue());
            }
        }
        return headers;
    }

    public String decrypt(String requestRefId, String xMessageID, String body) {
        /*RequestData data;
        try {
            data = this.objectMapper.readValue(body, RequestData.class);
        } catch (IOException ex) {
            return "{}";
        }
        ResponsePayload payload = null;
        if (data.getData() != null) {
            payload = AesCbcEncryptorDecryptor.decryptPayload(requestRefId, xMessageID, data
                    .getData());
        } else {
            return "{}";
        }
        if (payload != null) {
            return (payload.getApiHeaderResponse().getResponseCode().intValue() == 200) ? payload.getResponseBodyObject().toString() : "{}";
        }
        return "{}";*/
        return body;
    }
}