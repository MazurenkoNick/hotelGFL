package com.example.hotelgfl.model;

import com.example.hotelgfl.repository.AdministratorRepository;
import com.example.hotelgfl.repository.RenterRepository;
import com.example.hotelgfl.repository.ReservationRepository;
import com.example.hotelgfl.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdministratorModelTests {

    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RenterRepository renterRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void testReservationAddedDuringCascade() {
        Administrator admin = administratorRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Room room = roomRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);

        Reservation expected = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(2), admin, room, renter);
        admin.addReservation(expected);

        em.flush();

        assertTrue(reservationRepository.findById(1L).isPresent());
        var actual = reservationRepository.findById(1L).get();
        assertEquals(expected.getAdministrator(), actual.getAdministrator());
        assertEquals(expected.getRoom(), actual.getRoom());
        assertEquals(expected.getRenter(), actual.getRenter());
        assertEquals(expected.getFromDateTime(), actual.getFromDateTime());
        assertEquals(expected.getToDateTime(), actual.getToDateTime());
    }
}
