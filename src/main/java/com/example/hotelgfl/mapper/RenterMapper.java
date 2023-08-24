package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.model.Renter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RenterMapper {

    RenterDto instanceToDto(Renter instance);
    Renter dtoToInstance(RenterDto renterDto);
}
