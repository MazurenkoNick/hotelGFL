package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "rooms")
@NoArgsConstructor
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

    public Room(Long roomNumber, int becCount, double dayPrice, boolean isFree, RoomClass roomClass) {
        this.roomNumber = roomNumber;
        this.becCount = becCount;
        this.dayPrice = dayPrice;
        this.isFree = isFree;
        this.roomClass = roomClass;
    }

    public void addReservation(Reservation reservation) {
        Room currentRoom = reservation.getRoom();
        if (currentRoom != null && currentRoom != this) {
            // todo: add custom exception
            throw new IllegalArgumentException("Reservation already has a room!");
        }
        reservation.setRoom(this);
        reservations.add(reservation);
    }
}
