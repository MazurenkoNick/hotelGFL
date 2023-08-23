package com.example.hotelgfl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "[A-Z][a-z]+",
            message = "Must start with a capital letter followed by one or more lowercase letters")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "passport_id", nullable = false, unique = true)
    private String passportId;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_num", nullable = false, unique = true)
    private String phoneNumber;

    public User(String firstName, String lastName, String passportId, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
