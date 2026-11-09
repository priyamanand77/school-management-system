package com.management.authservice.config.keystore;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt.keystore")
@Data
public class KeyStoreProperties {
    private String path;
    private String password;
    private String alias;
    private String keyPassword;
}
