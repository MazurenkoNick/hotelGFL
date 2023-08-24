package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.model.Administrator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministratorMapper {

    AdministratorDto instanceToDto(Administrator instance);
    Administrator dtoToInstance(AdministratorDto administratorDto);
}
