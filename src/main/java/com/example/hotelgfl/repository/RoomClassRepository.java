package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.RoomClass;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoomClassRepository extends CrudRepository<RoomClass, Long> {

    Optional<RoomClass> findByName(String name);
}
