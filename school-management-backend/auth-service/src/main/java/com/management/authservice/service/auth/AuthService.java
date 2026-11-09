package com.management.authservice.service.auth;

import com.management.authservice.dto.login.request.LoginRequest;
import com.management.authservice.dto.login.response.AuthResponse;
import com.management.authservice.dto.signup.request.SignUpRequest;
import com.management.authservice.dto.signup.response.SignUpResponse;
import com.management.authservice.dto.token.response.TokenValidationResponse;
import com.management.authservice.entity.token.Token;
import com.management.authservice.entity.token.TokenType;
import com.management.authservice.entity.user.User;
import com.management.authservice.exception.AuthException;
import com.management.authservice.repo.TokenRepository;
import com.management.authservice.repo.UserRepository;
import com.management.authservice.service.jwt.JwtService;
import com.management.authservice.util.Mapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepo;
    private final TokenRepository tokenRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, TokenRepository tokenRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.tokenRepo = tokenRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public SignUpResponse signup(SignUpRequest req) {
        log.info("SignUp Request: {}", req.getUsername());

        try {
            if (userRepo.findByUsername(req.getUsername()).isPresent()) {
                throw new BadCredentialsException("Username already exists");
            }
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setEmail(req.getEmail());
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setRole(req.getRole().toUpperCase());
            user.setEnabled(true);
            User savedUser = userRepo.save(user);
            return Mapper.map(savedUser, SignUpResponse.class);
        } catch (BadCredentialsException e) {
            log.error("SignUp client Error:", e);
            throw new AuthException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("SignUp server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AuthResponse login(LoginRequest req) {
        log.info("Login Request: {}", req);
        try {
            User user = userRepo.findByUsernameAndRole(req.getUsername(),req.getRole())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            List<Token> allByUsernameAndRevokedFalse = user.getTokens().stream().filter(t -> !t.isRevoked()).toList();

            if (!allByUsernameAndRevokedFalse.isEmpty()) {
                List<Token> oldRecords = allByUsernameAndRevokedFalse.stream().peek(x -> x.setRevoked(true)).toList();
                tokenRepo.saveAll(oldRecords);
            }

            String access = jwtService.generateAccessToken(user.getUsername(), req.getRole());
            saveToken(user, access, user.getUsername(), TokenType.ACCESS, jwtService.getExpiry(access));
            return AuthResponse.builder().accessToken(access).build();
        } catch (BadCredentialsException e) {
            log.error("login client Error:", e);
            throw new AuthException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("login server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public AuthResponse refresh(String refreshToken) {
        log.info("Refresh Request: {}", refreshToken);
        try {
            Jws<Claims> claims = jwtService.parseToken(refreshToken);
            Claims claimsBody = claims.getBody();
            String username = claimsBody.getSubject();
            String role = claimsBody.get("role", String.class);

            Token stored = tokenRepo.findByToken(refreshToken)
                    .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
            if (stored.isRevoked() || stored.getExpiry().isBefore(LocalDateTime.now())) {
                throw new BadCredentialsException("Refresh token revoked or expired");
            }
            // revoke old refresh token
            stored.setRevoked(true);
            tokenRepo.save(stored);

            String newAccess = jwtService.generateAccessToken(username, role);
            saveToken(stored.getUser(), newAccess, username, TokenType.REFRESH, jwtService.getExpiry(newAccess));
            return AuthResponse.builder().accessToken(newAccess).build();
        } catch (BadCredentialsException e) {
            log.error("refresh client Error:", e);
            throw new AuthException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("refresh server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void logout(String token) {
        log.info("Logout Request: {}", token);
        try {
            tokenRepo.findByToken(token).ifPresent(t -> {
                t.setRevoked(true);
                tokenRepo.save(t);
            });
        } catch (Exception e) {
            log.error("logout server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void saveToken(User user, String tokenStr, String username, TokenType type, LocalDateTime expiry) {
        log.info("Saving token for user: {}, type: {}, expiry: {}", username, type, expiry);
        try {
            Token t = new Token();
            t.setToken(tokenStr);
            t.setUsername(username);
            t.setTokenType(type);
            t.setExpiry(expiry);
            t.setRevoked(false);
            t.setUser(user);
            tokenRepo.save(t);
        } catch (Exception e) {
            log.error("save server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenValidationResponse validateToken(String token) {
        log.info("Validating token for user");
        try {
            return jwtService.validateToken(token);
        } catch (Exception e) {
            log.error("validate server Error: ", e);
            throw new AuthException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
