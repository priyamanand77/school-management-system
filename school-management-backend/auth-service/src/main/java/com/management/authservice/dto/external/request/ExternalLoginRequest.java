package com.management.authservice.dto.external.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalLoginRequest {

    private String provider;      // e.g., "google", "facebook"

    /**
     * Provider ID token (preferred for Google: id_token).
     * Either idToken or accessToken must be provided.
     */
    private String idToken;

    /**
     * Provider access token (OAuth access token).
     * Either idToken or accessToken must be provided.
     */
    private String accessToken;
}