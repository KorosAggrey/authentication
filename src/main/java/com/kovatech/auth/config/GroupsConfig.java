package com.kovatech.auth.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.service.WsStarterService;
import com.kovatech.auth.datalayer.entities.Groups;
import com.kovatech.auth.repositories.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GroupsConfig {

    private final GroupsRepository groupsRepository;
    @Autowired
    private WsStarterService starterService;

    public GroupsConfig(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @Bean
    public Mono<Map<String, Integer>> groupList() {
        Map<String,Integer> var1 = new HashMap<>();
         return groupsRepository.findAll()
                .collect(Collectors.toCollection(ArrayList::new))
                .flatMap( res ->{
                    for (Groups group: res) {
                        var1.put(group.getName().toLowerCase(Locale.ROOT),group.getId());
                    }
                    return Mono.just(var1);
                });
    }
}
