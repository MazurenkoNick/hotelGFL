package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Optional<Administrator> findByEmail(String email);
    @Query("SELECT DISTINCT a FROM Administrator a LEFT JOIN FETCH a.roles r WHERE a.email = :email")
    Optional<Administrator> findByEmailFetchRoles(@Param("email") String email);
}
