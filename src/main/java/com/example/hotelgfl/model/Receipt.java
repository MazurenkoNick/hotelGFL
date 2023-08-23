package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
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

}
