package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "renters")
@Getter
@Setter
public class Renter extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "renter", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Discount> discounts;

    @OneToMany(mappedBy = "renter")
    private List<Reservation> reservations;
}
