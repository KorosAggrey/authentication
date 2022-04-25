package com.kovatech.auth.controller;

import com.kovatech.auth.AuthApplication;
import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.models.SignUp;
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

}