package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RenterRepository extends JpaRepository<Renter, Long> {

    <T> Optional<T> findByEmail(String email, Class<T> type);

    <T> List<T> findAllBy(Class<T> type);
}
