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

    public WsMapperRepository(WsMapperProperties mapperProperties) {
        this.objectMapper = new ObjectMapper();
        this.mapperProperties = mapperProperties;
    }

    public Flux<WsResponseDetails> getResponses() {
        return Flux.just(new WsResponseDetails());
        //return this.hashOperations.values("DXL_MAP_" + this.mapperProperties.getMappingName()).map(err -> deserialize(err));
    }

    public Map<String, WsResponseDetails> getDxlStarterResponses() {
        Map<String,String> res = responses();
        Map<String,String> techRes = technicalResponses();
        Map<String,WsResponseDetails> newRes = new HashMap<>();
        for(String key: res.keySet()){
            WsResponseDetails var1 = new WsResponseDetails();
            String var2 = res.get(key);
            if(techRes.containsKey(key)){
                var2 = techRes.get(key);
            }
            var1.setCustomerMessage(res.get(key));
            var1.setErrorCode(key);
            int code = 400;
            if(isNumeric(key)) {
                 code = Integer.parseInt(key);
            }
            var1.setResponseCode(code);
            var1.setErrorType("Custom");
            var1.setServiceName("Starter");
            var1.setErrorSource("Starter Utility");
            var1.setTechnicalMessage(var2);
            res.get(key);
            newRes.put(key,var1);
        }
        return newRes;
    }

    public Map<String,String> responses(){
        Map<String,String> res = new HashMap<>();
        res.put("CRW100053","Request failed. Please try again later");
        res.put("408","Request failed, please try again later\"");
        res.put("CRW100050","Missing/Invalid headers");
        res.put("404","No records found");
        res.put("CRW100056","No handler");
        res.put("200","Request executed successfully");
        res.put("400","Request failed, please try again later");
        res.put("CRW100058","Sorry, the resource is being used by another entity");
        res.put("CRW100061","Sorry, request could not be processed. Failed validation");
        res.put("CRW100051","Sorry, requested resource could not be found");
        res.put("422","Service is temporarily unavailable");
        res.put("CRW100052","Request failed. Please try again later");
        res.put("CRE100054","Request failed, please try again later");
        res.put("203","User exist, proceed to verify your email address");
        res.put("204","Otp code invalid");
        res.put("205","Success");
        res.put("402","Password is not correct");
        return res;
    }

    public Map<String,String> technicalResponses(){
        Map<String,String> res = new HashMap<>();
        res.put("CRW100053","Invalid payload");
        res.put("408","Connection Timeout");
        res.put("CRW100050","Missing/Invalid headers");
        res.put("404","Not found");
        res.put("CRW100056","Sorry, requested resource path could not be found");
        res.put("200","Success");
        res.put("400","Bad Request");
        res.put("CRW100058","Bad Request");
        res.put("CRW100061","Validation Failed");
        res.put("CRW100051","Not found");
        res.put("CRW100052","Invalid payload");
        res.put("422","Bad Gateway");
        res.put("CRE100054","Internal Server Error");
        res.put("203","User exist, account not verified");
        res.put("204","Otp code not found");
        res.put("205","Success");
        res.put("402","Password is not correct");
        return res;
    }

    public static boolean isNumeric(String string) {
        int intValue;
        if(string == null || string.equals("")) {
            return false;
        }
        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    private WsResponseDetails deserialize(Object object) {
        try {
            return (WsResponseDetails) this.objectMapper.readValue(object.toString(), WsResponseDetails.class);
        } catch (IOException ex) {
            return new WsResponseDetails();
        }
    }
}