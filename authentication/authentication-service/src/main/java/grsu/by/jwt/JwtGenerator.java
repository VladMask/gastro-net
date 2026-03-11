package grsu.by.jwt;

import grsu.by.config.properties.AuthenticationServiceProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGenerator {
    private final String SECRET;
    private final Long ACCESS_TOKEN_EXPIRATION_TIME;

    public JwtGenerator(AuthenticationServiceProperties properties) {
        this.SECRET = properties.getJwtGenerator().getSecret();
        this.ACCESS_TOKEN_EXPIRATION_TIME = properties.getJwtGenerator().getAccessTokenExpirationTime();
    }

    public String generate(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
