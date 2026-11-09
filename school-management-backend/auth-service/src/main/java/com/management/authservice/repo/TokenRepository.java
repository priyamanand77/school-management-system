package com.management.authservice.repo;

import com.management.authservice.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    List<Token> findAllByUsernameAndRevokedFalse(String username);
}
