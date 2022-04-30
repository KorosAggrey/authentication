package com.kovatech.auth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class Login {

    @NotBlank(message = "Email address or phone number is required")
    private String identity;

    @NotBlank(message = "Password is required")
    private String password;
}
