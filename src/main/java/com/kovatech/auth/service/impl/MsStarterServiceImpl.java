package com.kovatech.auth.service.impl;

import com.kovatech.auth.component.MessagingService;
import com.kovatech.auth.config.GroupsConfig;
import com.kovatech.auth.core.model.WsResponse;
import com.kovatech.auth.core.model.WsResponseDetails;
import com.kovatech.auth.core.service.WsMappingService;
import com.kovatech.auth.core.service.WsResponseMapper;
import com.kovatech.auth.core.service.WsStarterService;
import com.kovatech.auth.datalayer.entities.User;
import com.kovatech.auth.datalayer.entities.UserGroups;
import com.kovatech.auth.models.OtpResend;
import com.kovatech.auth.models.OtpVerification;
import com.kovatech.auth.models.SignUp;
import com.kovatech.auth.repositories.UserGroupsRepository;
import com.kovatech.auth.repositories.UserRepository;
import com.kovatech.auth.service.MsStarterService;
import com.kovatech.auth.utils.Validations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WsStarterService starterService;

    @Autowired
    UserGroupsRepository userGroupsRepository;

    @Autowired
    MessagingService messagingService;

    @Autowired
    Validations validation;

    private final PasswordEncoder passwordEncoder;

    private final GroupsConfig groupsConfig;
    private final WsMappingService mappingService;

    public MsStarterServiceImpl(WsResponseMapper responseMapper, PasswordEncoder passwordEncoder, GroupsConfig groupsConfig, WsMappingService mappingService){
        this.responseMapper = responseMapper;
        this.passwordEncoder = passwordEncoder;
        this.groupsConfig = groupsConfig;
        this.mappingService = mappingService;
    }

    public Mono<WsResponse> signUpUser(Map<String, String> headers, Mono<SignUp> userDtoMono){
        return userDtoMono.flatMap(res -> {
            String msisdn = starterService.formatAndCompareMsisdn(res.getPhone(),res.getPhone(),false,false);
            res.setPhone(msisdn);
            return userRepository.findByMsisdn(msisdn)
                    .switchIfEmpty(getUserByEmail(res,headers))
                    .flatMap(userR -> {
                        if(userR.getPhone() == null){
                            Mono<User> created = createNewUser(res,headers);
                            return created.flatMap( createdUser ->{
                                messagingService.prepareVerificationMessage(createdUser);
                                WsResponseDetails responseDetails = mappingService.getErrorMapper(ERR_SUCCESS,
                                        headers.get(X_CONVERSATION_ID), TRANS_GET_RESPONSES);
                                responseDetails.setCustomerMessage("Registered successfully, proceed to verify your email address");
                                return responseMapper.setApiResponse(responseDetails, NULL, TRANS_GET_RESPONSES, ES, ES,
                                        ES, FALSE, headers);
                            });
                        }

                        if(userR.getIsActive() != 1){ //Not Active activate
                            WsResponseDetails responseDetails = mappingService.getErrorMapper("203",
                                    headers.get(X_CONVERSATION_ID), TRANS_GET_RESPONSES);
                            responseDetails.setCustomerMessage("User exist, proceed to verify your email address");
                            return responseMapper.setApiResponse(responseDetails, NULL, TRANS_GET_RESPONSES, ES, ES,
                                    ES, FALSE, headers);
                        }
                        return responseMapper.setApiResponse(ERR_SUCCESS, NULL, TRANS_GET_RESPONSES, ES, ES, ES, FALSE, headers);
                    });

        });
    }

    @Override
    public Mono<WsResponse> otpVerification(Map<String, String> headers, Mono<OtpVerification> otpPayload) {
        return otpPayload.flatMap(otp -> {
            return userRepository.getByVerificationCode(otp.getCode())
                    .defaultIfEmpty(new User())
                    .flatMap(userR -> {
                        if(userR.getPhone() != null) {
                            userR.setActivationCode("");
                            userR.setModifiedOn(LocalDateTime.now().toString());
                            userR.setIsActive(1);
                            userR.setActive(1);
                            userR.setModifiedBy(userR.getId());
                            userR.setAsUpdate();
                            userRepository.save(userR.setAsUpdate()).subscribe();
                            WsResponseDetails responseDetails = mappingService.getErrorMapper("205",
                                    headers.get(X_CONVERSATION_ID), TRANS_GET_RESPONSES);
                            responseDetails.setCustomerMessage("Account verified successfully, proceed to login");
                            return responseMapper.setApiResponse(responseDetails, NULL, TRANS_GET_RESPONSES, ES, ES,
                                    ES, FALSE, headers);
                        }
                        WsResponseDetails responseDetails = mappingService.getErrorMapper("204",
                                headers.get(X_CONVERSATION_ID), TRANS_GET_RESPONSES);
                        responseDetails.setCustomerMessage("Otp verification code not found");
                        return responseMapper.setApiResponse(responseDetails, NULL, TRANS_GET_RESPONSES, ES, ES,
                                ES, FALSE, headers);
                    });
            });
    }

    @Override
    public Mono<WsResponse> resendOtp(Map<String, String> headers, Mono<OtpResend> payload) {
        return payload.flatMap(req -> {
            Mono<User> user = Mono.just(new User());
            if(validation.isValidEmail(req.getIdentity())){
               user = userRepository.findByEmail(req.getIdentity());
           }else{
                String msisdn = starterService.formatAndCompareMsisdn(req.getIdentity(),req.getIdentity(),false,false);
                user = userRepository.findByMsisdn(msisdn);
           }
           return user.defaultIfEmpty(new User()).flatMap(res -> {
               if(res.getPhone() == null){
                   WsResponseDetails responseDetails = mappingService.getErrorMapper("404",
                           headers.get(X_CONVERSATION_ID), TRANS_GET_RESPONSES);
                   responseDetails.setCustomerMessage("User details not found");
                   return responseMapper.setApiResponse(responseDetails, NULL, TRANS_GET_RESPONSES, ES, ES,
                           ES, FALSE, headers);
               }
               System.out.println(starterService.serialize(res));
               return responseMapper.setApiResponse(ERR_SUCCESS, NULL, TRANS_GET_RESPONSES, ES, ES, ES, FALSE, headers);
           });
        });
    }

    private Mono<User> getUserByEmail(SignUp res, Map<String, String> headers) {
        return userRepository.findByEmail(res.getEmail().toLowerCase(Locale.ROOT))
                .defaultIfEmpty(new User())
                .flatMap( var1 ->{
                    return Mono.just(var1);
                });
    }

    private Mono<User> createNewUser(SignUp res, Map<String, String> headers) {
        User var1 = new User();
        UserGroups newGroup = new UserGroups();
        String firstName = "", lastName = "", middleName ="";
        Map<String,String> names = splitFullName(res.getFullName());
        var1.setFirstName(names.get("first"));
        var1.setMiddleName(names.get("middle"));
        var1.setLastName(names.get("last"));
        var1.setEmail(res.getEmail());
        var1.setPassword(passwordEncoder.encode(res.getPassword()));
        var1.setPhone(res.getPhone());
        var1.setCreatedOn(LocalDateTime.now().toString());
        var1.setModifiedOn(LocalDateTime.now().toString());
        var1.setPublicId(UUID.randomUUID().toString());
        Random rand = new Random(); //instance of random class
        int upperbound = 1000000;
        //generate random values from 0-24
        int intRandom = rand.nextInt(upperbound);
        var1.setActivationCode(String.valueOf(intRandom));
        var1.setAsNew();
        userRepository.save(var1).flatMap(reg ->{
            return groupsConfig.groupList().flatMap(gro ->{
                int groupId = 2;
                if(gro.containsKey("member")){
                    groupId = gro.get("member");
                }
                newGroup.setGroupId(groupId);
                newGroup.setUserId(reg.getId());
                newGroup.setCreatedOn(LocalDateTime.now().toString());
                newGroup.setCreatedBy(reg.getId());
                userGroupsRepository.save(newGroup.setAsNew()).subscribe();
                return Mono.just(var1);
            });
        }).subscribe();
        return Mono.just(var1);
    }

    private Map<String, String> splitFullName(String fullName) {
        Map<String,String> name = new HashMap<>();
        String[] split = fullName.split(" ");
        if(split.length > 2){
            name.put("first",split[0]);
            name.put("middle",split[1]);
            name.put("last",split[2]);
        }else if(split.length == 2){
            name.put("first",split[0]);
            name.put("middle","");
            name.put("last",split[1]);
        }
        return name;
    }

}