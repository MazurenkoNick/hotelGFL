package com.example.hotelgfl.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
