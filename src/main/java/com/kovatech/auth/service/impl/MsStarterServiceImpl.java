package com.kovatech.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.service.WsResponseMapper;
import com.kovatech.auth.models.SignUp;
import com.kovatech.auth.service.MsStarterService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.AssertTrue;
import java.util.Map;

import static com.kovatech.auth.core.utils.WsStarterVariables.*;
import static com.kovatech.auth.utils.MsStarterVariables.ERR_SUCCESS;
import static com.kovatech.auth.utils.MsStarterVariables.TRANS_GET_RESPONSES;


/**
 *
 * @author Aggrey
 */
@Service
public class MsStarterServiceImpl implements MsStarterService {
    private final WsResponseMapper responseMapper;

    public MsStarterServiceImpl(WsResponseMapper responseMapper){
        this.responseMapper = responseMapper;
    }

    public Mono<WsResponse> signUpUser(Map<String, String> headers, Mono<SignUp> userDtoMono){
        return userDtoMono.flatMap(res ->{
            return responseMapper.setApiResponse(ERR_SUCCESS, NULL, TRANS_GET_RESPONSES, ES, ES, ES, FALSE, headers);
        });
    }

}