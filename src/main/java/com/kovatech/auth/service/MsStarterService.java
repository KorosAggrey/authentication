package com.kovatech.auth.service;

import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.models.*;
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

    Mono<WsResponse> login(Map<String, String> headers, Mono<Login> payload);

    Mono<WsResponse> forgotPassword(Map<String, String> headers, Mono<OtpResend> payload);

    Mono<WsResponse> confirmCode(Map<String, String> headers, Mono<OtpVerification> payload);

    Mono<WsResponse> setNewPassword(Map<String, String> headers, Mono<ResetPassword> payload);
}