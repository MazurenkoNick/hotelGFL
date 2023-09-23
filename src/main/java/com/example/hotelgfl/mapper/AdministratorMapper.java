package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.administrator.AdministratorDto;
import com.example.hotelgfl.model.Administrator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdministratorMapper {

    AdministratorDto entityToDto(Administrator instance);
    Administrator dtoToEntity(AdministratorDto administratorDto);
}
