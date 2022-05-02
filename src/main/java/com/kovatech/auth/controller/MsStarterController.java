package com.kovatech.auth.controller;

import com.kovatech.auth.AuthApplication;
import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.models.*;
import com.kovatech.auth.service.MsStarterService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

/**
 *
 * @author Aggrey
 */
@RestController
@RequestMapping("/api/v1")
public class MsStarterController {

    private final MsStarterService starterService;
    public MsStarterController(MsStarterService starterService){
        this.starterService = starterService;
    }

    @PostMapping("/signUp")
    public Mono<WsResponse> signUpUser(@RequestHeader Map<String, String> headers,
                                       @Valid @RequestBody Mono<SignUp> payload){
        return starterService.signUpUser(headers,payload);
    }

    @PostMapping("/otpVerification")
    public Mono<WsResponse> otpVerification(@RequestHeader Map<String,String> headers,
                                            @Valid @RequestBody Mono<OtpVerification> otp){
        return starterService.otpVerification(headers,otp);
    }

    @PostMapping("/resendOtp")
    public Mono<WsResponse> resendOtp(@RequestHeader Map<String,String> headers,
                                      @Valid @RequestBody Mono<OtpResend> payload){
        return starterService.resendOtp(headers,payload);
    }

    @PostMapping("/login")
    public Mono<WsResponse> login(@RequestHeader Map<String,String> headers,
                                  @Valid @RequestBody Mono<Login> payload){
        return starterService.login(headers,payload);
    }

    @PostMapping("/forgotPassword")
    public Mono<WsResponse> forgotPassword(@RequestHeader Map<String,String> headers,
                                           @Valid @RequestBody Mono<OtpResend> payload){
        return starterService.forgotPassword(headers,payload);
    }

    @PostMapping("/confirmCode")
    public Mono<WsResponse> confirmCode(@RequestHeader Map<String,String> headers,
                                        @Valid @RequestBody Mono<OtpVerification> payload){
        return starterService.confirmCode(headers,payload);
    }

    @PostMapping("/setPassword")
    public Mono<WsResponse> setNewPassword(@RequestHeader Map<String,String> headers,
                                        @Valid @RequestBody Mono<ResetPassword> payload){
        return starterService.setNewPassword(headers,payload);
    }
}