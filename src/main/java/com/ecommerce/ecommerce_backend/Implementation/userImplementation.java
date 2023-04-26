package com.ecommerce.ecommerce_backend.Implementation;

import com.ecommerce.ecommerce_backend.Document.Role;
import com.ecommerce.ecommerce_backend.Document.User;
import com.ecommerce.ecommerce_backend.Exceptions.ApiException;
import com.ecommerce.ecommerce_backend.Payloads.ApiResponse;
import com.ecommerce.ecommerce_backend.Payloads.UserRequest;
import com.ecommerce.ecommerce_backend.Repository.userRepository;
import com.ecommerce.ecommerce_backend.Service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class userImplementation implements userService {
    @Autowired
    private userRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Mono<User> registerUser(Mono<User> userRequest) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("admin", "all access"));
        Mono<User> userAlreadyExists = userRequest.map(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return user;
        }).flatMap(user -> userRepository.findByEmail(user.getEmail()).log()
                .flatMap(userData -> Mono.error(new ApiException("User already exists in User Controller", HttpStatus.BAD_REQUEST)))
                .switchIfEmpty(userRepository.save(User
                        .builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles)
                        .build()))
                .cast(User.class));
        return userAlreadyExists;
    }

    @Override
    public Mono<UserRequest> getUserById(String userId) {
        return null;
    }

    @Override
    public Flux<UserRequest> getRangedUsers(Double min, Double max) {
        return null;
    }

    @Override
    public Flux<UserRequest> getAllUsers() {
        return null;
    }

    @Override
    public Mono<UserRequest> updateUsers(Mono<UserRequest> userRequest, String userId) {
        return null;
    }

    @Override
    public Mono<ApiResponse> deleteUser(String userId) {
        return null;
    }

}
