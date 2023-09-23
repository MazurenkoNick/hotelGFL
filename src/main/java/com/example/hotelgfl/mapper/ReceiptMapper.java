package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.receipt.ReceiptResponse;
import com.example.hotelgfl.model.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mappings({
            @Mapping(target = "reservationId", source = "reservation.id"),
            @Mapping(target = "reservationRenterEmail", source = "reservation.renter.email")
    })
    ReceiptResponse entityToReceiptResponse(Receipt receipt);
}
