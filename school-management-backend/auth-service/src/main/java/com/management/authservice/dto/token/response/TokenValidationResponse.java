package com.management.authservice.dto.token.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenValidationResponse {
    private boolean valid;
    private String username;
    private LocalDateTime expiryTime;
    private String message;
    private String token;
}

