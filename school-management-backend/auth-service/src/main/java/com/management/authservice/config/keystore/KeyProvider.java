package com.management.authservice.config.keystore;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Component
@Data
@RequiredArgsConstructor
public class KeyProvider {
    private final KeyStoreProperties props;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        Resource res = new ClassPathResource(props.getPath().replace("classpath:", ""));
        try (InputStream is = res.getInputStream()) {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(is, props.getPassword().toCharArray());
            Key key = ks.getKey(props.getAlias(), props.getKeyPassword().toCharArray());
            if (key instanceof PrivateKey) {
                privateKey = (PrivateKey) key;
                Certificate cert = ks.getCertificate(props.getAlias());
                publicKey = cert.getPublicKey();
            } else {
                throw new IllegalStateException("No private key found in keystore");
            }
        }
    }
}
