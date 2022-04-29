package com.kovatech.auth.service;

import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.models.OtpResend;
import com.kovatech.auth.models.OtpVerification;
import com.kovatech.auth.models.SignUp;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 *
 * @author Aggrey
 */
public interface MsStarterService {

    Mono<WsResponse> signUpUser(Map<String, String> headers, Mono<SignUp> payload);

    Mono<WsResponse> otpVerification(Map<String, String> headers, Mono<OtpVerification> otp);

    Mono<WsResponse> resendOtp(Map<String, String> headers, Mono<OtpResend> payload);
}