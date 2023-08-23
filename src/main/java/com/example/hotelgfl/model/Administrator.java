package com.example.hotelgfl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "administrators")
public class Administrator extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    private Rank rank;

    @Column(name = "salary")
    private double salary;

    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}",
            message = "Must be minimum 6 characters, at least one letter and one number")
    @Column(name = "password")
    private String password;
}
