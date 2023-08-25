package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RenterRepository extends JpaRepository<Renter, Long> {

    Optional<Renter> findByEmail(String email);

    @Query("SELECT new com.example.hotelgfl.dto.RenterDto(" +
            "r.firstName, r.lastName, r.passportId, r.phoneNumber, r.email) " +
            "FROM Renter r " +
            "WHERE r.email = :email")
    Optional<RenterDto> findRenterDtoByEmail(String email);

    @Query("SELECT new com.example.hotelgfl.dto.RenterDto(" +
            "r.firstName, r.lastName, r.passportId, r.phoneNumber, r.email) " +
            "FROM Renter r")
    List<RenterDto> findAllRenterDtos();
}
