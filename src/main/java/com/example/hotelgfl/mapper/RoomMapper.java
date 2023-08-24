package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "roomClassName", source = "roomClass.name")
    RoomDto instanceToDto(Room instance);

    Room dtoToInstance(RoomDto roomDto);
}
