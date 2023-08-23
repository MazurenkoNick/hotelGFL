package com.example.hotelgfl.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "room_num", nullable = false, unique = true)
    private Long roomNumber;

    @Column(name = "bed_num", nullable = false)
    private int bedNumber;

    @Column(name = "day_price", nullable = false)
    private double dayPrice;

    @Column(name = "is_free", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private boolean isFree;

    @ManyToOne
    @JoinColumn(name = "room_class_id", nullable = false)
    private RoomClass roomClass;

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<Reservation> reservations;
}
