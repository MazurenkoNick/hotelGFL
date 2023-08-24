package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.mapper.AdministratorMapper;
import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.repository.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AdministratorMapper administratorMapper;

    @Transactional
    public AdministratorDto create(AdministratorDto administratorDto) {
        Administrator admin = administratorMapper.dtoToInstance(administratorDto);
        Administrator persistedAdmin = administratorRepository.save(admin);
        return administratorMapper.instanceToDto(persistedAdmin);
    }

    @Transactional
    public AdministratorDto delete(String email) {
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        administratorRepository.delete(administrator);
        return administratorMapper.instanceToDto(administrator);
    }
}
