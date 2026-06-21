package grsu.by.scheduler;

import grsu.by.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final ReservationService reservationService;

    @Scheduled(fixedDelayString = "${reservation.scheduler.expiration-check-delay:300000}",
            initialDelayString = "${reservation.scheduler.expiration-initial-delay:60000}")
    public void expireOutdatedReservations() {
        log.info("Running reservation expiration check...");
        reservationService.expireOutdatedReservations();
    }
}