package com.example.hotelgfl.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "renters")
public class Renter extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "renter", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Discount> discounts;
}
