package grsu.by.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("common.outbox")
public class OutboxProperties {
    private Producer producer;
    private Scheduler scheduler;

    @Getter
    @Setter
    public static class Producer {
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
