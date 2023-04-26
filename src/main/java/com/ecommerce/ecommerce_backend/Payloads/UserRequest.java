package com.ecommerce.ecommerce_backend.Payloads;

import com.ecommerce.ecommerce_backend.Document.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotEmpty(message = "Please enter your Name")
    @Size(min = 2, max = 20, message = "Please enter correct Name")
    private String name;

    @NotEmpty(message = "Please enter your Email")
    @Email(message = "Please enter correct Email")
    private String email;

    @NotEmpty(message = "Enter Password")
    @Size(min = 4, max = 40)
    private String password;
    private List<Role> roles;

}
