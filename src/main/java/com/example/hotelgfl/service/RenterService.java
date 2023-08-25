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
        Renter renter = renterMapper.dtoToInstance(renterDto);
        renterRepository.save(renter);
        return renterMapper.instanceToDto(renter);
    }

    @Transactional
    public RenterDto remove(String email) {
        Renter renter = renterRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        renterRepository.delete(renter);
        return renterMapper.instanceToDto(renter);
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

        return renterMapper.instanceToDto(renter);
    }

    public RenterDto get(String email) {
        return renterRepository.findRenterDtoByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<RenterDto> getAll() {
        return renterRepository.findAllRenterDtos();
    }
}
