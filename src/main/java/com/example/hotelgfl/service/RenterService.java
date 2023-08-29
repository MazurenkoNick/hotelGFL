package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.mapper.RenterMapper;
import com.example.hotelgfl.model.Renter;
import com.example.hotelgfl.repository.RenterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenterService {
    
    private final RenterRepository renterRepository;
    private final RenterMapper renterMapper;

    @Transactional
    public RenterDto create(RenterDto renterDto) {
        Renter renter = renterMapper.dtoToEntity(renterDto);
        renterRepository.save(renter);
        return renterMapper.entityToDto(renter);
    }

    @Transactional
    public RenterDto remove(String email) {
        Renter renter = renterRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        renterRepository.delete(renter);
        return renterMapper.entityToDto(renter);
    }

    @Transactional
    public RenterDto update(String email, RenterDto renterDto) {
        Renter renter = renterRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        renter.setEmail(renterDto.getEmail());
        renter.setFirstName(renterDto.getFirstName());
        renter.setLastName(renterDto.getLastName());
        renter.setPassportId(renterDto.getPassportId());
        renter.setPhoneNumber(renterDto.getPhoneNumber());

        return renterMapper.entityToDto(renter);
    }

    public RenterDto getDto(String email) {
        return renterRepository.findRenterDtoByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Renter get(String email) {
        return renterRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Renter with the given email doesn't exist, email: " + email)
                ); // todo: add custom RenterNotFoundException
    }

    public List<RenterDto> getAll() {
        return renterRepository.findAllRenterDtos();
    }
}
