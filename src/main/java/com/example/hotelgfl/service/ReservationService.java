package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.ReservationDto;
import com.example.hotelgfl.mapper.ReservationMapper;
import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.model.Renter;
import com.example.hotelgfl.model.Reservation;
import com.example.hotelgfl.model.Room;
import com.example.hotelgfl.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final RoomService roomService;
    private final RenterService renterService;
    private final AdministratorService administratorService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Transactional // todo: think about isolation level
    public ReservationDto create(ReservationDto reservationDto) {
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

            return reservationMapper.entityToDto(reservation);
        }
        throw new IllegalArgumentException("The room is not available during the given dates, " +
                "room number: " + room.getRoomNumber() + ", from: " + from + ", to: " + to);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
