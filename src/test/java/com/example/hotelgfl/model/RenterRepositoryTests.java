package com.example.hotelgfl.model;

import com.example.hotelgfl.repository.*;
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
public class RenterRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RenterRepository renterRepository;
    @Autowired
    private RoomClassRepository roomClassRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void testReservationAddedDuringCascade() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Room room = roomRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Administrator admin = administratorRepository.findById(10L).orElseThrow(EntityNotFoundException::new);

        Reservation expected = new Reservation(LocalDateTime.now(), LocalDateTime.now().plusDays(2), admin, room, renter);
        renter.addReservation(expected);

        em.flush();

        assertTrue(reservationRepository.findById(1L).isPresent());
        var actual = reservationRepository.findById(1L).get();
        assertEquals(expected.getAdministrator(), actual.getAdministrator());
        assertEquals(expected.getRoom(), actual.getRoom());
        assertEquals(expected.getRenter(), actual.getRenter());
        assertEquals(expected.getFromDateTime(), actual.getFromDateTime());
        assertEquals(expected.getToDateTime(), actual.getToDateTime());
    }

    @Test
    @Transactional
    void testDiscountAddedDuringCascade() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        RoomClass roomClass = roomClassRepository.findById(2L).orElseThrow(EntityNotFoundException::new);
        System.out.println(roomClass.getName());

        Discount expectedDiscount = new Discount(25, roomClass, renter);
        renter.addDiscount(expectedDiscount);

        em.flush();

        assertTrue(discountRepository.findById(1L).isPresent());
        var actualDiscount = discountRepository.findById(1L).get();
        assertEquals(expectedDiscount.getRenter(), actualDiscount.getRenter());
        assertEquals(expectedDiscount.getPercent(), actualDiscount.getPercent());
        assertEquals(expectedDiscount.getRoomClass(), actualDiscount.getRoomClass());
    }

    @Test
    @Transactional
    void testDiscountDeletedDuringOrphanRemoval() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Long discountId = renter.getDiscounts().get(0).getId();
        renter.getDiscounts().remove(0);

        em.flush();

        assertFalse(discountRepository.findById(discountId).isPresent());
    }

    @Test
    @Transactional
    void testDiscountAndRenterDeletedUsingCascade() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Long renterId = renter.getId();
        Long discountId = renter.getDiscounts().get(0).getId();
        Long reservationId = renter.getReservations().get(0).getId();

        em.remove(renter);

        assertFalse(renterRepository.findById(renterId).isPresent());
        assertFalse(discountRepository.findById(discountId).isPresent());
        assertTrue(reservationRepository.findById(reservationId).isPresent());
    }
}
