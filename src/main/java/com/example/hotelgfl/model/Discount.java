package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@Getter
@Setter
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "percent", nullable = false)
    private double percent;

    @ManyToOne
    @JoinColumn(name = "room_class_id", nullable = false)
    private RoomClass roomClass;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private Renter renter;

    public Discount(double percent, RoomClass roomClass, Renter renter) {
        this.percent = percent;
        this.roomClass = roomClass;
        this.renter = renter;
    }

    public Discount(double percent, RoomClass roomClass) {
        this.percent = percent;
        this.roomClass = roomClass;
    }
}
