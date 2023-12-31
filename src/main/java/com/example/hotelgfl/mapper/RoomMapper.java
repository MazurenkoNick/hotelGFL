package com.example.hotelgfl.mapper;

import com.example.hotelgfl.dto.room.RoomDto;
import com.example.hotelgfl.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "roomClassName", source = "roomClass.name")
    RoomDto entityToDto(Room instance);

    Room dtoToEntity(RoomDto roomDto);
}
