package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.RoomDto;
import com.example.hotelgfl.mapper.RoomMapper;
import com.example.hotelgfl.model.Room;
import com.example.hotelgfl.model.RoomClass;
import com.example.hotelgfl.repository.RoomClassRepository;
import com.example.hotelgfl.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final RoomClassRepository roomClassRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomDto create(RoomDto roomDto) {
        Room room = roomDtoToRoomWithRoomClass(roomDto);
        roomRepository.save(room);
        return roomMapper.instanceToDto(room);
    }

    @Transactional
    public RoomDto remove(Long roomNumber) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(EntityNotFoundException::new);
        roomRepository.delete(room);
        return roomMapper.instanceToDto(room);
    }

    @Transactional
    public RoomDto update(Long roomNumber, RoomDto roomDto) {
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(EntityNotFoundException::new);
        RoomClass roomClass = getRoomClass(roomDto.getRoomClassName());
        room.setRoomClass(roomClass);
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setFree(roomDto.getIsFree());
        room.setBedCount(roomDto.getBedCount());
        room.setDayPrice(roomDto.getDayPrice());

        return roomMapper.instanceToDto(room);
    }

    /**
     * {@link Room} entity will be converted from the RoomDto.
     * If the roomClassName retrieved from the {@link RoomDto} is not blank,
     * then {@link RoomClass} entity will be retrieved from the db.
     * If the {@link RoomClass} entity with the given name doesn't exist,
     * it'll be created and persisted thanks to {@link jakarta.persistence.CascadeType.PERSIST}
     *
     * @param roomDto
     * @return {@link Room} with RoomClass mapped to it
     */
    private Room roomDtoToRoomWithRoomClass(RoomDto roomDto) {
        Room room = roomMapper.dtoToInstance(roomDto);
        String roomClassName = roomDto.getRoomClassName();

        if (roomClassName.isBlank()) {
            throw new IllegalArgumentException("`Room Class` name can't be blank!");
        }
        RoomClass roomClass = getRoomClass(roomClassName);

        room.setRoomClass(roomClass);
        return room;
    }

    private RoomClass getRoomClass(String roomClassName) {
        return roomClassRepository.findByName(roomClassName)
                .orElse(new RoomClass(roomClassName));
    }
}
