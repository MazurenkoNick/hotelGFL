package com.example.hotelgfl.repository;

import com.example.hotelgfl.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
