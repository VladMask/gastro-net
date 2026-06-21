package grsu.by.scheduler;

import grsu.by.kafka.OutboxEventProducer;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventScheduler {
    private final OutboxEventProducer outboxEventProducer;

    @Scheduled(
            initialDelayString = "${common.outbox.scheduler.initial-delay}",
            fixedDelayString = "${common.outbox.scheduler.fixed-delay}"
    )
    @SchedulerLock(name = "sendEventsToKafka")
    public void sendEventsToKafka() {
        outboxEventProducer.sendCreatedEvents();
    }
}
