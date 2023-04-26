package com.ecommerce.ecommerce_backend.Controller;

import com.ecommerce.ecommerce_backend.Document.User;
import com.ecommerce.ecommerce_backend.Payloads.LoginResponse;
import com.ecommerce.ecommerce_backend.Repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class userController {

    @Autowired
    private userRepository userRepository;

    @GetMapping("/email/{email}")
    public Mono<User> isExist(@PathVariable String email){
        return userRepository.findByEmail(email);
    }

    @GetMapping("/me")
    public Mono<LoginResponse> getUser(@AuthenticationPrincipal User user, ServerWebExchange request){
        String authorization = request.getRequest().getHeaders().getFirst("Authorization");
        Mono<LoginResponse> map = Mono.just(user)
                .map(userData -> LoginResponse
                        .builder()
                        .userId(userData.getUserId())
                        .name(userData.getName())
                        .email(userData.getEmail())
                        .address("Noida")
                        .type("Admin")
                        .token(authorization)
                        .password(user.getPassword())
                        .build());
        return map;
    }

}
