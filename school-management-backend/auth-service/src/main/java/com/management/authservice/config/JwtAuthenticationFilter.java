package com.management.authservice.config;


import com.management.authservice.dto.token.response.TokenValidationResponse;
import com.management.authservice.service.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            // Verify user exists
            TokenValidationResponse tokenValidationResponse = jwtService.validateToken(token);
            if (!tokenValidationResponse.isValid()) {
                filterChain.doFilter(request, response);
                return;
            }

            // Build Authentication and set context
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(tokenValidationResponse.getUsername(), null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException | IllegalArgumentException ex) {
            // Invalid or expired token -> clear context and continue (endpoint security will reject)
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
