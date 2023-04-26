package com.ecommerce.ecommerce_backend.Payloads;

import com.ecommerce.ecommerce_backend.Document.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    private String userId;
    private String name;
    private String email;
    private String password;
    private String type;
    private String address;
}
