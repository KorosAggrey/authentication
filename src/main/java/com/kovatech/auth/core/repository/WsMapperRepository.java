package com.kovatech.auth.core.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.config.WsMapperProperties;
import com.kovatech.auth.core.model.WsResponseDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Repository
public class WsMapperRepository {
    private final WsMapperProperties mapperProperties;
    private final ObjectMapper objectMapper;
   // private final ReactiveHashOperations hashOperations;

    public WsMapperRepository(/*ReactiveRedisTemplate reactiveRedisTemplate,*/ WsMapperProperties mapperProperties) {
        this.objectMapper = new ObjectMapper();
       // this.hashOperations = reactiveRedisTemplate.opsForHash();
        this.mapperProperties = mapperProperties;
    }

    public Flux<WsResponseDetails> getResponses() {
        return Flux.just(new WsResponseDetails());
        //return this.hashOperations.values("DXL_MAP_" + this.mapperProperties.getMappingName()).map(err -> deserialize(err));
    }

    public Map<String, WsResponseDetails> getDxlStarterResponses() {
        Map<String,String> res = responses();
        Map<String,WsResponseDetails> newRes = new HashMap<>();
        for(String key: res.keySet()){
            WsResponseDetails var1 = new WsResponseDetails();
            var1.setCustomerMessage(res.get(key));
            var1.setErrorCode(key);
            var1.setResponseCode(400);
            var1.setErrorType("Custom");
            var1.setServiceName("Starter");
            var1.setErrorSource("Starter Utility");
            var1.setTechnicalMessage(res.get(key));
            res.get(key);
            newRes.put(key,var1);
        }
        return newRes;
    }

    public Map<String,String> responses(){
        Map<String,String> res = new HashMap<>();
        res.put("CRW100053","Invalid payload");
        res.put("408","Connection Timeout");
        res.put("CRW100050","Missing/Invalid headers");
        res.put("404","No records found");
        res.put("CRW100056","Sorry, requested resource path could not be found");
        res.put("200","Request submitted successful");
        res.put("400","Bad Request");
        res.put("CRW100058","Sorry, the resource is being used by another entity");
        res.put("CRW100061","Validation Failed");
        res.put("CRW100051","Sorry, requested resource could not be found");
        res.put("422","Service is temporarily unavailable");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        res.put("CRW100053","Invalid payload");
        return res;
    }

    private WsResponseDetails deserialize(Object object) {
        try {
            return (WsResponseDetails) this.objectMapper.readValue(object.toString(), WsResponseDetails.class);
        } catch (IOException ex) {
            return new WsResponseDetails();
        }
    }
}