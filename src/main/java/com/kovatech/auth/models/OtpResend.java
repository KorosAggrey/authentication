package com.kovatech.auth.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class OtpResend {

    @NotBlank(message="Email address or phone number is required")
    private String identity;
}
