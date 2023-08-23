package com.example.hotelgfl.model;

import com.example.hotelgfl.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        Assertions.assertTrue(reservationRepository.findById(1L).isPresent());
        var actual = reservationRepository.findById(1L).get();
        Assertions.assertEquals(expected.getAdministrator(), actual.getAdministrator());
        Assertions.assertEquals(expected.getRoom(), actual.getRoom());
        Assertions.assertEquals(expected.getRenter(), actual.getRenter());
        Assertions.assertEquals(expected.getFromDateTime(), actual.getFromDateTime());
        Assertions.assertEquals(expected.getToDateTime(), actual.getToDateTime());
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

        Assertions.assertTrue(discountRepository.findById(1L).isPresent());
        var actualDiscount = discountRepository.findById(1L).get();
        Assertions.assertEquals(expectedDiscount.getRenter(), actualDiscount.getRenter());
        Assertions.assertEquals(expectedDiscount.getPercent(), actualDiscount.getPercent());
        Assertions.assertEquals(expectedDiscount.getRoomClass(), actualDiscount.getRoomClass());
    }

    @Test
    @Transactional
    void testDiscountDeletedDuringOrphanRemoval() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Long discountId = renter.getDiscounts().get(0).getId();
        renter.getDiscounts().remove(0);

        em.flush();

        Assertions.assertFalse(discountRepository.findById(discountId).isPresent());
    }

    @Test
    @Transactional
    void testDiscountAndRenterDeletedUsingCascade() {
        Renter renter = renterRepository.findById(10L).orElseThrow(EntityNotFoundException::new);
        Long renterId = renter.getId();
        Long discountId = renter.getDiscounts().get(0).getId();
        Long reservationId = renter.getReservations().get(0).getId();

        em.remove(renter);

        Assertions.assertFalse(renterRepository.findById(renterId).isPresent());
        Assertions.assertFalse(discountRepository.findById(discountId).isPresent());
        Assertions.assertTrue(reservationRepository.findById(reservationId).isPresent());
    }
}
