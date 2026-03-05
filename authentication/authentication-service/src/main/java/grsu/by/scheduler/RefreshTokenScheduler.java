package grsu.by.scheduler;

import grsu.by.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenScheduler {
    private final RefreshTokenService service;

    @Scheduled(cron = "${scheduler.cron.interval}")
    public void deleteExpiredTokens() {
        log.info("Delete expired refresh tokens");
        service.deleteExpiredTokens();
    }

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}")
    public void deleteExpiredTokensOnStartup() {
        log.info("Delete expired refresh tokens using fixed delay");
        service.deleteExpiredTokens();
    }

    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    public void deleteExpiredTokensRate() {
        log.info("Delete expired refresh tokens using fixedRate");
        service.deleteExpiredTokens();
    }

}
