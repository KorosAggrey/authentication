package com.kovatech.auth.repositories;

import com.kovatech.auth.datalayer.entities.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    @Query("SELECT * FROM users WHERE phone =:msisdn LIMIT 1")
    Mono<User> findByMsisdn(String msisdn);

    @Query("SELECT * FROM users WHERE email =:email LIMIT 1")
    Mono<User> findByEmail(String email);
    @Query("SELECT * FROM users WHERE activation_code =:code LIMIT 1")
    Mono<User> getByVerificationCode(String code);
    @Query("SELECT * FROM users WHERE forgotten_password_code =:code LIMIT 1")
    Mono<User> getByForgotPasswordCode(String code);
}
