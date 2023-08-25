package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.ResponseAdministratorDto;
import com.example.hotelgfl.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Optional<Administrator> findByEmail(String email);

    @Query("SELECT DISTINCT a FROM Administrator a LEFT JOIN FETCH a.roles r WHERE a.email = :email")
    Optional<Administrator> findByEmailFetchRoles(@Param("email") String email);

    @Query("SELECT new com.example.hotelgfl.dto.ResponseAdministratorDto(" +
            "a.rank, a.salary, a.firstName, a.lastName, a.email, a.passportId, a.phoneNumber) " +
            "FROM Administrator a WHERE a.email = :email")
    Optional<ResponseAdministratorDto> findAdministratorDtoByEmail(@Param("email") String email);

    @Query("SELECT new com.example.hotelgfl.dto.ResponseAdministratorDto(" +
            "a.rank, a.salary, a.firstName, a.lastName, a.email, a.passportId, a.phoneNumber) " +
            "FROM Administrator a")
    List<ResponseAdministratorDto> findAllAdministratorDtos();
}
