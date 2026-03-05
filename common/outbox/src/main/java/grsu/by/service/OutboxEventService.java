package grsu.by.service;

import grsu.by.entity.OutboxEvent;
import grsu.by.enums.EventStatus;
import org.springframework.data.domain.Limit;

import java.util.List;

public interface OutboxEventService {
    OutboxEvent create(OutboxEvent event);
    List<OutboxEvent> findByStatusOrderByCreatedAt(EventStatus eventStatus, Limit limit);
    void saveAll(List<OutboxEvent> events);
}
