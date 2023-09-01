package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@NoArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    public Receipt(Reservation reservation) {
        this.id = reservation.getId();
        this.checkIn = reservation.getFromDateTime();
        this.checkOut = reservation.getToDateTime();
        this.reservation = reservation;
        this.totalPrice = countTotalPrice(reservation);
    }

    private double countTotalPrice(Reservation reservation) {
        Duration duration = Duration.between(reservation.getFromDateTime(), reservation.getToDateTime());
        double daysSpent = (double) duration.toHours() / 24;
        return reservation.getRoom().getDayPrice() * daysSpent;
    }
}
