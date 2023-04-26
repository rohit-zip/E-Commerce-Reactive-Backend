package com.ecommerce.ecommerce_backend.Repository;

import com.ecommerce.ecommerce_backend.Document.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface userRepository extends ReactiveMongoRepository<User, String> {
    Mono<Boolean> existsByEmailIgnoreCase(String email);
    Mono<User> findByEmail(String email);
}
