package grsu.by.service.impl;

import grsu.by.entity.OutboxEvent;
import grsu.by.enums.EventStatus;
import grsu.by.repository.OutboxEventRepository;
import grsu.by.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventServiceImpl implements OutboxEventService {
    private final OutboxEventRepository repository;

    @Transactional
    @Override
    public OutboxEvent create(OutboxEvent event) {
        event.setStatus(EventStatus.NEW);
        return repository.save(event);
    }

    @Override
    public List<OutboxEvent> findByStatusOrderByCreatedAt(EventStatus eventStatus, Limit limit) {
        return repository.findByStatusOrderByCreatedAt(eventStatus, limit);
    }

    @Transactional
    @Override
    public void saveAll(List<OutboxEvent> events) {
        repository.saveAll(events);
    }
}
