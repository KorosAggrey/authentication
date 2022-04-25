package com.kovatech.auth.core.wrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.model.WsEncryptedPayload;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;


public class WsResponseWrapper
        extends ServerHttpResponseDecorator {
    private final ObjectMapper objectMapper;
    private final String xMessageId;
    private final ServerHttpResponse responseDecorator;
    private final boolean encryptResponse;
    private String responseBody;

    public WsResponseWrapper(ServerHttpResponse responseDecorator, boolean encryptResponse, String xMessageId) {
        super(responseDecorator);
        this.objectMapper = new ObjectMapper();
        this.xMessageId = xMessageId;
        this.responseDecorator = responseDecorator;
        this.encryptResponse = encryptResponse;
    }


    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (this.encryptResponse) {
            Flux.from(body).subscribe(dataBuffer -> {

                try {
                    Channels.newChannel(baos).write(dataBuffer.asByteBuffer());
                    this.responseBody = IOUtils.toString(baos.toByteArray(), "UTF-8");
                    DataBufferUtils.release(dataBuffer);
                } catch (IOException iOException) {
                    try {
                        baos.close();
                    } catch (IOException iOException1) {
                    }
                } finally {
                    try {
                        baos.close();
                    } catch (IOException iOException) {
                    }
                }


            }).dispose();
            String response = serializeResponse(new WsEncryptedPayload(encrypt(this.xMessageId, this.responseBody)));
            this.responseDecorator.setStatusCode(HttpStatus.OK);
            this.responseDecorator.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            this.responseDecorator.getHeaders().add("X-MessageID", this.xMessageId);
            this.responseDecorator.getHeaders().setContentLength(response.length());
            return this.responseDecorator.writeWith(Mono.just(this.responseDecorator.bufferFactory().wrap(response.getBytes())));
        }
        getHeaders().add("X-MessageID", "");
        return super.writeWith(body);
    }


    public String encrypt(String xMessageID, String responseBody) {
        /*Details details = AesCbcEncryptorDecryptor.encryptPayload(xMessageID, responseBody);
        return details.isSuccess() ? details.getEncryptedString() : "{}";*/
        return  responseBody;
    }

    public String serializeResponse(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            return "{}";
        }
    }
}