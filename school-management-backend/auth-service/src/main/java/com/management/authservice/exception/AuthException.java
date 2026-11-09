package com.management.authservice.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Builder
public class AuthException extends RuntimeException {

    private Object body;
    private HttpStatus httpStatus;

    public AuthException(Object body, HttpStatus httpStatus) {
        super(body.toString());
        this.body = body;
        this.httpStatus = httpStatus;
    }
}
