package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "room_num", nullable = false, unique = true)
    private Long roomNumber;

    @Column(name = "bed_count", nullable = false)
    private int becCount;

    @Column(name = "day_price", nullable = false)
    private double dayPrice;

    @Column(name = "is_free", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private boolean isFree;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_class_id", nullable = false)
    private RoomClass roomClass;

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<Reservation> reservations;
}
