package com.management.authservice.service.jwt;

import com.management.authservice.config.keystore.KeyProvider;
import com.management.authservice.dto.token.response.TokenValidationResponse;
import com.management.authservice.entity.user.User;
import com.management.authservice.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    private final KeyProvider keyProvider;
    private final long accessValidity;
    private final UserRepository userRepository;

    public JwtService(KeyProvider keyProvider, @Value("${jwt.access-token-validity-seconds}") long accessValidity,
                      UserRepository userRepository) {
        this.keyProvider = keyProvider;
        this.accessValidity = accessValidity;
        this.userRepository = userRepository;
    }

    public String generateAccessToken(String username, String role) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);

        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessValidity)))
                .signWith(keyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token);
    }

    public LocalDateTime getExpiry(String token) {
        Date exp = parseToken(token).getBody().getExpiration();
        return exp.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
    }


    public TokenValidationResponse validateToken(String token) {
        try {
            Jws<Claims> claims = parseToken(token);
            Claims claimsBody = claims.getBody();
            String username = claimsBody.getSubject();
            String role = claimsBody.get("role", String.class);
            LocalDateTime expiry = getExpiry(token);

            if (expiry.isBefore(LocalDateTime.now())) {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .message("Token has expired")
                        .expiryTime(expiry)
                        .build();
            }
            Optional<User> userDetails = userRepository.findByUsernameAndRoleWithActiveToken(username, role, token);

            TokenValidationResponse tokenValidationResponse;
            if (userDetails.isEmpty()) {
                tokenValidationResponse = TokenValidationResponse.builder()
                        .valid(false)
                        .message("No Active User found")
                        .build();
            } else {
                tokenValidationResponse = TokenValidationResponse.builder()
                        .valid(true)
                        .username(username)
                        .expiryTime(expiry)
                        .message("Token is valid")
                        .token(token)
                        .build();
            }
            return tokenValidationResponse;
        } catch (Exception ex) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Invalid token: " + ex.getMessage())
                    .build();
        }
    }

}
