package com.example.hotelgfl.model;

import jakarta.persistence.*;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "percent", nullable = false)
    private double percent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "room_class_id")
    private RoomClass roomClass;

    @ManyToOne
    @JoinColumn(name = "renter_id", foreignKey = @ForeignKey(name = "renter_discounts_fk"), nullable = false)
    private Renter renter;
}
