package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.ReservationDto;
import com.example.hotelgfl.dto.ReservationUpdateDto;
import com.example.hotelgfl.model.*;
import com.example.hotelgfl.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReservationServiceTests {

    @MockBean
    private RoomService roomService;
    @MockBean
    private RenterService renterService;
    @MockBean
    private AdministratorService administratorService;
    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    @InjectMocks
    private ReservationService reservationService;

    private final String admEmail = "adm@gmail.com";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(admEmail, "123")
        );
    }

    @ParameterizedTest
    @MethodSource("createReservationSource")
    void createValidReservationTest(ReservationDto expected) {
        var renter = new Renter();
        renter.setEmail(expected.getRenterEmail());
        var room = new Room();
        room.setRoomNumber(expected.getRoomNumber());
        var adm = new Administrator();
        adm.setEmail(admEmail);

        when(renterService.get(anyString())).thenReturn(renter);
        when(roomService.get(anyLong())).thenReturn(room);
        when(administratorService.get(anyString())).thenReturn(adm);
        when(roomService.isFree(anyLong(), any(), any())).thenReturn(true);

        var actual = reservationService.create(expected);

        assertThat(actual.getTo()).isEqualTo(expected.getTo());
        assertThat(actual.getFrom()).isEqualTo(expected.getFrom());
        assertThat(actual.getRoomNumber()).isEqualTo(expected.getRoomNumber());
        assertThat(actual.getRenterEmail()).isEqualTo(expected.getRenterEmail());
        assertThat(actual.getAdministratorEmail()).isEqualTo(admEmail);
    }

    @ParameterizedTest
    @MethodSource("createReservationSource")
    void createInvalidReservationTest(ReservationDto expected) {
        var renter = new Renter();
        renter.setEmail(expected.getRenterEmail());
        var room = new Room();
        room.setRoomNumber(expected.getRoomNumber());
        var adm = new Administrator();
        adm.setEmail(admEmail);

        when(renterService.get(anyString())).thenReturn(renter);
        when(roomService.get(anyLong())).thenReturn(room);
        when(administratorService.get(anyString())).thenReturn(adm);
        when(roomService.isFree(anyLong(), any(), any())).thenReturn(false);

        assertThatThrownBy(() -> reservationService.create(expected))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The room is not available during the given dates, " +
                        "room number: " + room.getRoomNumber() + ", from: " + expected.getFrom() + ", to: " + expected.getTo());
    }

    static Stream<Arguments> createReservationSource() {
        return Stream.of(
                Arguments.of(
                        new ReservationDto(
                                LocalDateTime.of(2022, 1, 1, 0, 0),
                                LocalDateTime.of(2022, 1, 4, 0, 0),
                                1L, "email@gmail.com"
                                )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("updateSource")
    void validUpdateDatesTest(Long id, ReservationUpdateDto expected) {
        var room = new Room();
        room.setRoomNumber(99L);
        var adm = new Administrator();
        adm.setEmail(admEmail);
        var renter = new Renter();
        renter.setEmail("old@gmail.com");
        var reservation = new Reservation(expected.getFrom(), expected.getTo(), adm, room, renter);
        reservation.setId(id);
        var updatedRenter = new Renter();
        updatedRenter.setEmail(expected.getRenterEmail());

        when(roomService.isFreeUpdate(anyLong(), anyLong(), any(), any())).thenReturn(true);
        when(renterService.get(anyString())).thenReturn(updatedRenter);
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        var actual = reservationService.update(id, expected);

        assertThat(actual.getRenterEmail()).isEqualTo(updatedRenter.getEmail());
        assertThat(actual.getTo()).isEqualTo(expected.getTo());
        assertThat(actual.getFrom()).isEqualTo(expected.getFrom());
    }

    static Stream<Arguments> updateSource() {
        return Stream.of(
                Arguments.of(
                        1L, new ReservationUpdateDto(
                                LocalDateTime.of(2022, 1, 2, 0, 0),
                                LocalDateTime.of(2022, 1, 5, 0, 0),
                                "renter@gmail.com"
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("checkoutTotalPriceSource")
    void checkoutTotalPriceTest(Long id, double expectedTotalSum, Reservation reservation) {
        when(reservationRepository.findByIdFetchDiscounts(anyLong())).thenReturn(Optional.of(reservation));
        var receipt = reservationService.checkout(id);

        assertThat(receipt.getTotalPrice()).isEqualTo(expectedTotalSum);
        assertThat(receipt.getId()).isEqualTo(reservation.getId());
        assertThat(receipt.getCheckIn()).isEqualTo(reservation.getFromDateTime());
        assertThat(receipt.getCheckOut()).isEqualTo(reservation.getToDateTime());
        assertThat(receipt.getRenterEmail()).isEqualTo(reservation.getRenter().getEmail());
    }

    static Stream<Arguments> checkoutTotalPriceSource() {
        Long id = 1L;
        var roomClass = new RoomClass("VIP");
        var room = new Room();
        room.setRoomNumber(99L);
        room.setDayPrice(100);
        room.setRoomClass(roomClass);

        var adm = new Administrator();
        adm.setEmail("adm@gmail.com");

        var renter = new Renter();
        renter.setEmail("old@gmail.com");
        renter.setDiscounts(new ArrayList<>());
        renter.addDiscount(new Discount(10, roomClass));

        var reservation = new Reservation(
                LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 11, 0, 0),
                adm, room, renter);
        reservation.setId(id);

        return Stream.of(
                Arguments.of(
                        id, 900, reservation
                )
        );
    }
}
