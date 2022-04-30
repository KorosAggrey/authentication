package com.kovatech.auth.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JwtDto {

    private String token;
    private String type;
    private String id;
    private String name;
    private String email;
    private List<String> roles;
}
