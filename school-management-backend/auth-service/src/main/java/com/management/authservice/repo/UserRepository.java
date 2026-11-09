package com.management.authservice.repo;


import com.management.authservice.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    /**
     * Find a user by username and role, with non-revoked token matching the provided token value.
     * Uses JPQL with JOIN FETCH to eagerly load filtered tokens.
     *
     * @param username the username to search for
     * @param role the role of the user
     * @param token the token value to match
     * @return Optional containing the User with filtered tokens, or empty if not found
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "JOIN FETCH u.tokens t " +
           "WHERE u.username = :username " +
           "AND u.role = :role " +
           "AND t.token = :token " +
           "AND t.revoked = false")
    Optional<User> findByUsernameAndRoleWithActiveToken(
            @Param("username") String username,
            @Param("role") String role,
            @Param("token") String token
    );


    /**
     * Find a user by username and role with optional tokens.
     * Returns user even if no tokens exist. Filters to non-revoked tokens only.
     *
     * @param username the username to search for
     * @param role the role of the user
     * @return Optional containing the User with non-revoked tokens, or empty if user not found
     */
    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.tokens t " +
            "WHERE u.username = :username " +
            "AND u.role = :role " +
            "AND (t.revoked = false OR t IS NULL)")
    Optional<User> findByUsernameAndRole(
            @Param("username") String username,
            @Param("role") String role
    );

}
