package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationServiceProperties {

    private JwtGenerator jwtGenerator;
    private RefreshTokenService refreshTokenService;

    @Getter
    @Setter
    public static class JwtGenerator
    {
        private String secret;
        private Long AccessTokenExpirationTime;
    }
    @Getter
    @Setter
    public static class RefreshTokenService
    {
        private Long RefreshTokenExpirationTime;
    }
}
