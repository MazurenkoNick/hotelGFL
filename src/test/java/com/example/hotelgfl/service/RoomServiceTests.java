package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.room.RoomDto;
import com.example.hotelgfl.mapper.RoomMapper;
import com.example.hotelgfl.model.Room;
import com.example.hotelgfl.model.RoomClass;
import com.example.hotelgfl.repository.RoomClassRepository;
import com.example.hotelgfl.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomServiceTests {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomClassRepository roomClassRepository;
    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomService roomService;

    @Test
    void updateTest() {
        Room room = new Room(1L, 1, 1, new RoomClass("NONE"));
        when(roomRepository.findByRoomNumber(anyLong())).thenReturn(Optional.of(room));
        when(roomClassRepository.findByName(anyString())).thenReturn(Optional.ofNullable(null));
        RoomDto expected = new RoomDto(999L, 4, 999.99, "VIP");

        roomService.update(999L, expected);

        assertThat(room.getRoomClass().getName()).isEqualTo(expected.getRoomClassName());
        assertThat(room.getRoomNumber()).isEqualTo(expected.getRoomNumber());
        assertThat(room.getBedCount()).isEqualTo(expected.getBedCount());
        assertThat(room.getDayPrice()).isEqualTo(expected.getDayPrice());
    }

    @Test
    void getAllFreeTestThrows() {
        assertThatThrownBy(() -> roomService.getAllFree(
                LocalDate.of(2000, 2, 5),
                LocalDate.of(2000, 2, 2)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("`from` date can't be after `to` date");
    }
}
