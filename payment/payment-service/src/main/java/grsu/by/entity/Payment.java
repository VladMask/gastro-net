package grsu.by.entity;

import grsu.by.enums.PaymentMethod;
import grsu.by.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "restaurant_id")
    private Long restaurantId;
    @Column(name = "amount", precision = 14, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}