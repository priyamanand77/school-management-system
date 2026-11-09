package com.management.authservice.dto.login.request;

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
public class LoginRequest {
    @NotBlank(message = "username cannot be blank")
    @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 100, message = "password must be between 6 and 100 characters")
    private String password;

    @NotBlank(message = "role cannot be blank")
    private String role;
}
