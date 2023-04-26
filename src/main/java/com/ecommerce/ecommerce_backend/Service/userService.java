package com.ecommerce.ecommerce_backend.Service;

import com.ecommerce.ecommerce_backend.Document.User;
import com.ecommerce.ecommerce_backend.Payloads.ApiResponse;
import com.ecommerce.ecommerce_backend.Payloads.UserRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

public interface userService {
    Mono<User> registerUser(Mono<User> userMono);
    Mono<UserRequest> getUserById(String userId);
    Flux<UserRequest> getRangedUsers(Double min, Double max);
    Flux<UserRequest> getAllUsers();
    Mono<UserRequest> updateUsers(Mono<UserRequest> userRequest, String userId);
    Mono<ApiResponse> deleteUser(String userId);
}
