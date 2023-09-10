package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.RoomDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @ParameterizedTest
    @MethodSource("findRoomDtoByNumberSource")
    void findRoomDtoByNumberTest(Long number, RoomDto expected) {
        Optional<RoomDto> actual = roomRepository.findRoomDtoByNumber(number);
        assertThat(actual.orElse(null)).isEqualTo(expected);
    }

    static Stream<Arguments> findRoomDtoByNumberSource() {
        return Stream.of(
                Arguments.of(1L, new RoomDto(1L, 1, 199.99, "STANDARD")),
                Arguments.of(2L, new RoomDto(2L, 3, 499.99, "STANDARD")),
                Arguments.of(10L, null)
        );
    }

    @ParameterizedTest
    @MethodSource("findAllRoomDtosSource")
    void findAllRoomDtosTest(List<RoomDto> expected) {
        List<RoomDto> actual = roomRepository.findAllRoomDtos();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> findAllRoomDtosSource() {
        return Stream.of(
                Arguments.of(List.of(
                        new RoomDto(1L, 1, 199.99, "STANDARD"),
                        new RoomDto(2L, 3, 499.99, "STANDARD"),
                        new RoomDto(3L, 2, 349.99, "STANDARD"),
                        new RoomDto(4L, 3, 499.99, "PRESIDENTIAL"),
                        new RoomDto(5L, 3, 499.99, "PRESIDENTIAL")
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("findAllFreeRoomDtosSource")
    void findAllFreeRoomDtosTest(List<RoomDto> expected) {
        List<RoomDto> actual = roomRepository.findAllFreeRoomDtos();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> findAllFreeRoomDtosSource() {
        return Stream.of(
                Arguments.of(List.of(
                        new RoomDto(1L, 1, 199.99, "STANDARD"),
                        new RoomDto(2L, 3, 499.99, "STANDARD"),
                        new RoomDto(3L, 2, 349.99, "STANDARD"),
                        new RoomDto(4L, 3, 499.99, "PRESIDENTIAL"),
                        new RoomDto(5L, 3, 499.99, "PRESIDENTIAL")
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("findRoomIfFreeSource")
    void findRoomIfFreeTest(Long roomNumber, LocalDate from, LocalDate to, RoomDto expected) {
        Optional<RoomDto> actual = roomRepository.findRoomIfFree(roomNumber, from, to);
        assertThat(actual.orElse(null)).isEqualTo(expected);
    }

    static Stream<Arguments> findRoomIfFreeSource() {
        return Stream.of(
                Arguments.of(
                        1L, LocalDate.of(2000, 2, 1), LocalDate.of(2000, 2, 1),
                        new RoomDto(1L, 1, 199.99, "STANDARD")
                ), // free
                Arguments.of(
                        1L, LocalDate.of(2000, 2, 1), LocalDate.of(2000, 2, 3),
                        null
                ) // taken on these dates
        );
    }
}
