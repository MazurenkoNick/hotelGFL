package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.ReceiptResponse;
import com.example.hotelgfl.model.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mapping(target = "reservationId", source = "reservation.id")
    ReceiptResponse entityToReceiptResponse(Receipt receipt);
}
