package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private int bedCount;

    @Column(name = "day_price", nullable = false)
    private double dayPrice;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_class_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private RoomClass roomClass;

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST)
    List<Reservation> reservations;

    public Room(Long roomNumber, int bedCount, double dayPrice, RoomClass roomClass) {
        this.roomNumber = roomNumber;
        this.bedCount = bedCount;
        this.dayPrice = dayPrice;
        this.roomClass = roomClass;
    }

    public void addReservation(Reservation reservation) {
        Room currentRoom = reservation.getRoom();
        if (currentRoom != null && currentRoom != this) {
            throw new IllegalArgumentException("Reservation already has a room!");
        }
        reservation.setRoom(this);
        reservations.add(reservation);
    }
}
