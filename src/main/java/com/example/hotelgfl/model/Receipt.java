package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@NoArgsConstructor
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    public Receipt(Reservation reservation, double totalPrice) {
        this.id = reservation.getId();
        this.checkIn = reservation.getFromDateTime();
        this.checkOut = reservation.getToDateTime();
        this.reservation = reservation;
        this.totalPrice = totalPrice;
    }
}
