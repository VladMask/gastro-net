package grsu.by.entity;

import grsu.by.enums.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "header")
    private String header;
    @Column(name = "payload")
    private String payload;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @Column(name = "retry")
    private short retry;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public OutboxEvent(String header, String payload) {
        this.header = header;
        this.payload = payload;
        this.status = EventStatus.NEW;
    }
}
