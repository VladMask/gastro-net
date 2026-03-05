package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "notification-service")
public class NotificationServiceProperties {
    private Consumer consumer;
    private Scheduler scheduler;
    private VerificationCode verificationCode;
    private CodeHasher codeHasher;

    @Getter
    @Setter
    public static class Consumer {
        private String kafkaTopic;
    }

    @Getter
    @Setter
    public static class Scheduler {
        private int fixedDelay;
        private int initialDelay;
        private boolean enabled;
        private String lockAtMostFor;
    }

    @Getter
    @Setter
    public static class VerificationCode {
        private int expirationTimeSeconds;
    }

    @Getter
    @Setter
    public static class CodeHasher {
        private String otpSecret;
    }
}
