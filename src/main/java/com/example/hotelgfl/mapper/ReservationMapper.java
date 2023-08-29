package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.ReservationDto;
import com.example.hotelgfl.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mappings({
            @Mapping(target = "from", source = "fromDateTime"),
            @Mapping(target = "to", source = "toDateTime"),
            @Mapping(target = "roomNumber", source = "room.roomNumber"),
            @Mapping(target = "renterEmail", source = "renter.email"),
    })
    ReservationDto entityToDto(Reservation reservation);

    @Mappings({
            @Mapping(source = "from", target = "fromDateTime"),
            @Mapping(source = "to", target = "toDateTime"),
    })
    Reservation dtoToEntity(ReservationDto reservationDto);
}
