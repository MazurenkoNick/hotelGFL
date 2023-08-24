package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RenterRepository extends JpaRepository<Renter, Long> {

    Optional<Renter> findByEmail(String email);
}
