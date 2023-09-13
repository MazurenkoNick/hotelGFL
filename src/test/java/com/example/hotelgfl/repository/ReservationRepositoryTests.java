package com.example.hotelgfl.repository;

import com.example.hotelgfl.config.ScheduleConfig;
import com.example.hotelgfl.dto.ReservationResponseDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationRepositoryTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @ParameterizedTest
    @MethodSource("getAllSource")
    void getAllTest(List<ReservationResponseDto> expected) {
        List<ReservationResponseDto> actual = reservationRepository.getAllReservationResponseDtos();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> getAllSource() {
        return Stream.of(
                Arguments.of(
                        List.of(
                            new ReservationResponseDto(10L,
                                    LocalDateTime.of(2000, 2, 2, 0, 0, 0),
                                    LocalDateTime.of(2000, 2, 5, 0, 0, 0),
                                    1L,
                                    "n@gmail.com",
                                    "n1@gmail.com"),
                                new ReservationResponseDto(11L,
                                        LocalDateTime.of(2000, 2, 3, 0, 0, 0),
                                        LocalDateTime.of(2000, 2, 7, 0, 0, 0),
                                        2L,
                                        "m@gmail.com",
                                        "n2@gmail.com"),
                                new ReservationResponseDto(12L,
                                        LocalDateTime.of(2000, 2, 1, 0, 0, 0),
                                        LocalDateTime.of(2000, 2, 3, 0, 0, 0),
                                        3L,
                                        "l@gmail.com",
                                        "n3@gmail.com"),
                                new ReservationResponseDto(13L,
                                        LocalDateTime.of(2000, 2, 4, 0, 0, 0),
                                        LocalDateTime.of(2000, 2, 8, 0, 0, 0),4L,
                                        "l@gmail.com",
                                        "n4@gmail.com")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getByIdSource")
    void getByIdTest(Long id, ReservationResponseDto expected) {
        var actual = reservationRepository.getReservationResponseDtoById(id);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> getByIdSource() {
        return Stream.of(
                Arguments.of(
                        10L, new ReservationResponseDto(10L,
                                LocalDateTime.of(2000, 2, 2, 0, 0, 0),
                                LocalDateTime.of(2000, 2, 5, 0, 0, 0),
                                1L,
                                "n@gmail.com",
                                "n1@gmail.com")
                ),
                Arguments.of(
                        11L, new ReservationResponseDto(11L,
                                LocalDateTime.of(2000, 2, 3, 0, 0, 0),
                                LocalDateTime.of(2000, 2, 7, 0, 0, 0),
                                2L,
                                "m@gmail.com",
                                "n2@gmail.com")
                )
        );
    }

    /**
     * make sure {@link ScheduleConfig#forceCheckout()} was run before this test.
     */
    @ParameterizedTest
    @MethodSource("getAllNonCheckedOutSource")
    void getAllNonCheckedOutTest(List<ReservationResponseDto> expected) {
        List<ReservationResponseDto> actual = reservationRepository.getAllReservationResponseDtosNonCheckedOut();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> getAllNonCheckedOutSource() {
        return Stream.of(
                Arguments.of(
                        List.of()
                )
        );
    }
}
