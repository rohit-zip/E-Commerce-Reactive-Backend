package com.ecommerce.ecommerce_backend.Controller;

import com.ecommerce.ecommerce_backend.Document.User;
import com.ecommerce.ecommerce_backend.Exceptions.ApiException;
import com.ecommerce.ecommerce_backend.Payloads.LoginRequest;
import com.ecommerce.ecommerce_backend.Payloads.LoginResponse;
import com.ecommerce.ecommerce_backend.Repository.userRepository;
import com.ecommerce.ecommerce_backend.Security.AuthenticationManager;
import com.ecommerce.ecommerce_backend.Security.JwtHelper;
import com.ecommerce.ecommerce_backend.Security.TokenProvider;
import com.ecommerce.ecommerce_backend.Service.userService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/v1")
@Slf4j
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private userRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private userService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/register")
    public Mono<User> signUp(@RequestBody Mono<User> request) {
        Mono<User> serverResponseMono = userService.registerUser(request);
        return serverResponseMono;
    }

    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody Mono<LoginRequest> request) {
        return request.flatMap(login -> userRepository.findByEmail(login.getEmail())
                .flatMap(user -> {
                    if (passwordEncoder.matches(login.getPassword(), user.getPassword())) {
                        return Mono.just(LoginResponse
                                .builder()
                                .name(user.getName())
                                .email(user.getEmail())
                                .token(tokenProvider.generateToken(user))
                                .userId(user.getUserId())
                                .password(user.getPassword())
                                .address("Noida")
                                .type("admin")
                                .build()).log();
                    } else {
                        return Mono.error(new ApiException("Invalid credentials", HttpStatus.BAD_REQUEST));
                    }
                }).switchIfEmpty(Mono.error(new ApiException("User does not exist", HttpStatus.BAD_REQUEST))).log());
    }

    @GetMapping("/validate-token")
    public Mono<Boolean> validateToken(ServerWebExchange request){
        String authorization = request.getRequest().getHeaders().getFirst("Authorization");
        Mono<String> token = Mono.just(authorization).cast(String.class);
        return token.map(t-> tokenProvider.getUsernameFromToken(t))
                .flatMap(email-> userRepository.existsByEmailIgnoreCase(email))
                .onErrorReturn(false);
    }

    @GetMapping("/checking")
    public Mono<String> checking(ServerWebExchange request){
        return Mono.just("Rohit Parihar");
    }
}
