package grsu.by.utils;

import grsu.by.config.properties.NotificationServiceProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class CodeHasher {
    private final String secret;
    private static final String ALGORITHM = "HmacSHA256";

    public CodeHasher(NotificationServiceProperties properties) {
        this.secret = properties.getCodeHasher().getOtpSecret();
    }

    public String hash(String data) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM);
            mac.init(secretKeySpec);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing OTP", e);
        }
        return Base64.getEncoder().encodeToString(
                mac.doFinal(data.getBytes())
        );
    }

}
