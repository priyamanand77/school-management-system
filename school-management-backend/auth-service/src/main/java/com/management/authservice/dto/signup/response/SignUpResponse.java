package com.management.authservice.dto.signup.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean enabled;
}
