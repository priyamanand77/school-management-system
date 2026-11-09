package com.management.authservice.dto.signup.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    @NotBlank(message = "username cannot be blank")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 100, message = "password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "firstName cannot be blank")
    @Size(max = 50, message = "firstName cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    @Size(max = 50, message = "lastName cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "role cannot be blank")
    private String role;
}

