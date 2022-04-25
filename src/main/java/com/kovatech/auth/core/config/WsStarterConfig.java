package com.kovatech.auth.core.config;

import com.kovatech.auth.core.enums.WsProcessLogger;
import com.kovatech.auth.core.model.WsResponseDetails;
import com.kovatech.auth.core.repository.WsMapperRepository;
import com.kovatech.auth.core.security.AesCbcEncryptorDecryptor;
import com.kovatech.auth.core.service.WsStarterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Configuration
public class WsStarterConfig {
    //private final WsMapperRepository mapperRepository;
    private final WsStarterService starterService;
    private final Map<String, String> processes;
    private final WsBasicAuthProperties authProperties;
    private final WsWrapperUtilityProperties utilityProperties;
    private final WsMapperProperties mapperProperties;
    private final AesCbcEncryptorDecryptor aes;
    private final WsMapperRepository mapperRepository;

    public WsStarterConfig(WsStarterService starterService, WsMapperRepository mapperRepository,WsWrapperUtilityProperties utilityProperties, WsBasicAuthProperties authProperties, WsMapperProperties mapperProperties, AesCbcEncryptorDecryptor aes) {
        this.mapperRepository = mapperRepository;
        this.starterService = starterService;
        this.authProperties = authProperties;
        this.aes = aes;
        this.processes = new HashMap<>();
        this.utilityProperties = utilityProperties;
        this.mapperProperties = mapperProperties;
    }

    @Bean(name = {"WsErrorMapper"})
    @ConditionalOnProperty(name = {"dxl.response.mapper.enabled"}, havingValue = "true")
    @RefreshScope
    public Map<String, WsResponseDetails> responseMessages() {
        Map<String, WsResponseDetails> responses = this.mapperRepository.getDxlStarterResponses();
        for(String key: responses.keySet()){
            if (key != null) {
                responses.put(key, responses.get(key));
            }
        }
        return responses;
    }

    @Bean(name = {"WsCredentials"})
    @RefreshScope
    public Map<String, String> credentials() {
        SecretKey secKey = null;
        final String secretKey = authProperties.getStaticIv();
        Map<String, String> credentials = new HashMap(2);
        Iterator var2 = this.authProperties.getUsers().entrySet().iterator();
        while(var2.hasNext()) {
            Map.Entry<String, String> kv = (Map.Entry)var2.next();
            String encrypted = (String)kv.getValue();
            String decryptedString = aes.decrypt(encrypted, secretKey) ;
            credentials.put((String)kv.getKey(), decryptedString);
        }
        return credentials;
    }

    @Bean(name = {"WsProcesses"})
    public Map<String, String> process() {
        return this.processes;
    }

    @Bean(name = {"WsInternalSystems"})
    @RefreshScope
    public Map<String, Boolean> internalSystems() {
        Map<String, Boolean> systems = new HashMap<>(5);
        for (String src : this.starterService.getHashSet(this.utilityProperties.getAcceptedApplications(), ",")) {
            systems.put(src, Boolean.TRUE);
        }
        return systems;
    }

    @Bean(name = {"WsErrorMapperOverride"})
    @RefreshScope
    public Map<String, String> errorMapperOverride() {
        Map<String, String> errors = new HashMap<>(16);

        for (String err : this.mapperProperties.getErrorCodeOverride()) {
            String[] details = err.split(";");
            errors.put(details[0] + details[2], details[1]);
        }
        return errors;
    }
}