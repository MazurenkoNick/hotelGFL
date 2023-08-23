package com.example.hotelgfl.model;

import jakarta.persistence.*;

@Entity
@Table(name = "room_classes")
public class RoomClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}
