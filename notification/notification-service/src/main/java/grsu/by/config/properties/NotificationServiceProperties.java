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
}
