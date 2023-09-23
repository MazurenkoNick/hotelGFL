package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.receipt.ReceiptResponse;
import com.example.hotelgfl.dto.reservation.ReservationDto;
import com.example.hotelgfl.dto.reservation.ReservationResponseDto;
import com.example.hotelgfl.dto.reservation.ReservationUpdateDto;
import com.example.hotelgfl.mapper.ReceiptMapper;
import com.example.hotelgfl.mapper.ReservationMapper;
import com.example.hotelgfl.model.*;
import com.example.hotelgfl.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomService roomService;
    private final RenterService renterService;
    private final AdministratorService administratorService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final ReceiptMapper receiptMapper;

    @Transactional // todo: think about blocking insertion during update and update during insertion of the reservation
    public ReservationResponseDto create(ReservationDto reservationDto) {
        Renter renter = renterService.get(reservationDto.getRenterEmail());
        Room room = roomService.get(reservationDto.getRoomNumber());
        Administrator administrator = administratorService.get(getAuthentication().getName());
        LocalDateTime from = reservationDto.getFrom();
        LocalDateTime to = reservationDto.getTo();

        if (roomService.isFree(room.getRoomNumber(), from.toLocalDate(), to.toLocalDate())) {
            Reservation reservation = reservationMapper.dtoToEntity(reservationDto);
            reservation.setAdministrator(administrator);
            reservation.setRoom(room);
            reservation.setRenter(renter);
            reservationRepository.save(reservation);

            return reservationMapper.entityToResponseDto(reservation);
        }
        throw new IllegalArgumentException("The room is not available during the given dates, " +
                "room number: " + room.getRoomNumber() + ", from: " + from + ", to: " + to);
    }

    @Transactional // todo: think about blocking insertion during update and update during insertion of the reservation
    public ReservationResponseDto update(Long id, ReservationUpdateDto reservationDto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        assertUpdatable(reservation);

        updateReservationRenter(reservation, reservationDto.getRenterEmail());
        updateReservationDates(reservation, reservationDto.getFrom(), reservationDto.getTo());

        return reservationMapper.entityToResponseDto(reservation);
    }

    @Transactional
    public ReceiptResponse checkout(Long id) {
        return checkout(id, null);
    }

    @Transactional
    public ReceiptResponse checkout(Long id, LocalDateTime checkoutDateTime) {
        Reservation reservation = reservationRepository.findByIdFetchDiscounts(id)
                .orElseThrow(EntityNotFoundException::new);
        assertUpdatable(reservation);

        // update reservation's final dates (if the guest decides to check out earlier than expected)
        if (checkoutDateTime != null) {
            if (checkoutDateTime.toLocalDate().isAfter(reservation.getToDateTime().toLocalDate()) ||
                checkoutDateTime.isBefore(reservation.getFromDateTime())) {
                throw new IllegalArgumentException(
                        "Can't make the check-out when the check-out date is after the interval of the reservation" +
                                "or the check-out date is before the check-in date, id: " + id
                );
            }
            updateReservationDates(reservation, reservation.getFromDateTime(), checkoutDateTime);
        }
        // create a receipt for the reservation and save it
        double totalPrice = countTotalPrice(reservation);
        reservation.createReceipt(totalPrice);
        return receiptMapper.entityToReceiptResponse(reservation.getReceipt());
    }

    @Transactional
    public ReservationResponseDto remove(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        reservationRepository.delete(reservation);
        return reservationMapper.entityToResponseDto(reservation);
    }

    public List<ReservationResponseDto> getAll() {
        return reservationRepository.getAllReservationResponseDtos();
    }

    public List<ReservationResponseDto> getAllNonCheckedOut() {
        return reservationRepository.getAllReservationResponseDtosNonCheckedOut();
    }

    public ReservationResponseDto get(Long id) {
        return reservationRepository.getReservationResponseDtoById(id);
    }

    private double countTotalPrice(Reservation reservation) {
        RoomClass roomClass = reservation.getRoom().getRoomClass();
        Renter renter = reservation.getRenter();
        Duration duration = Duration.between(reservation.getFromDateTime(), reservation.getToDateTime());

        double daysSpent = (double) duration.toHours() / 24;
        double discountPercent = renter.useDiscount(roomClass);
        return (reservation.getRoom().getDayPrice() * daysSpent) * (100 - discountPercent) / 100;
    }

    private void updateReservationDates(Reservation reservation, LocalDateTime from, LocalDateTime to) {
        Room room = reservation.getRoom();
        boolean isAllowedToUpdate = roomService.isFreeUpdate(
                reservation.getId(), room.getRoomNumber(),
                from.toLocalDate(), to.toLocalDate()
        );
        if (isAllowedToUpdate) {
            reservation.setFromDateTime(from);
            reservation.setToDateTime(to);
        }
        else {
            throw new IllegalArgumentException("The room is not available during the given dates, " +
                    "room number: " + room.getRoomNumber() + ", from: " + from + ", to: " + to);
        }
    }

    private void updateReservationRenter(Reservation reservation, String newRenterEmail) {
        Renter oldRenter = reservation.getRenter();
        if (oldRenter != null && !oldRenter.getEmail().equals(newRenterEmail)) {
            Renter newRenter = renterService.get(newRenterEmail);
            reservation.setRenter(newRenter);
        }
    }

    private void assertUpdatable(Reservation reservation) {
        if (reservation.getReceipt() != null) {
            throw new IllegalArgumentException(
                    "The reservation is not updatable anymore. The receipt already exists for the reservation, id: "
                            + reservation.getId());
        }
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
