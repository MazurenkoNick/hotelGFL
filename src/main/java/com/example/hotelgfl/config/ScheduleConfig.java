package com.example.hotelgfl.config;

import com.example.hotelgfl.dto.reservation.ReservationResponseDto;
import com.example.hotelgfl.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Log4j2
public class ScheduleConfig {

    private final ReservationService reservationService;

    @Scheduled(fixedRate = 3_600_000)
    public void forceCheckout() {
        log.info("Check out all the guests if the reservation is expired");
        List<ReservationResponseDto> reservations = reservationService.getAllNonCheckedOut();
        LocalDateTime now = LocalDateTime.now();

        for (ReservationResponseDto reservation : reservations) {
            if (now.isAfter(reservation.getTo())) {
                try {
                    reservationService.checkout(reservation.getId());
                // the guest with the current reservation may be concurrently checked out
                } catch (IllegalArgumentException ignored) {}
            }
        }
    }
}
