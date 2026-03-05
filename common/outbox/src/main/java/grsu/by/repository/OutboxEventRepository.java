package grsu.by.repository;

import grsu.by.entity.OutboxEvent;
import grsu.by.enums.EventStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByStatusOrderByCreatedAt(EventStatus status, Limit limit);
}
