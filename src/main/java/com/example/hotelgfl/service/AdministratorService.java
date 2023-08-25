package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.mapper.AdministratorMapper;
import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.repository.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        administratorRepository.save(admin);
        return administratorMapper.instanceToDto(admin);
    }

    @Transactional
    public AdministratorDto delete(String email) {
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        administratorRepository.delete(administrator);
        return administratorMapper.instanceToDto(administrator);
    }

    @Transactional
    public AdministratorDto update(String email, AdministratorDto administratorDto) {
        Administrator administrator = administratorRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        // if the user who updates the administrator and the administrator are the same people,
        // then update authenticated email using dto value
        String authenticatedEmail = getEmailFromSecurityContext();
        if (email.equals(authenticatedEmail)) {
            String newAuthenticatedEmail = administratorDto.getEmail();
            updateAuthenticationUsername(newAuthenticatedEmail);
        }
        administrator.setRank(administratorDto.getRank());
        administrator.setSalary(administratorDto.getSalary());
        administrator.setPassword(administratorDto.getPassword());
        administrator.setFirstName(administratorDto.getFirstName());
        administrator.setLastName(administratorDto.getLastName());
        administrator.setEmail(administratorDto.getEmail());
        administrator.setPassportId(administratorDto.getPassportId());
        administrator.setPhoneNumber(administratorDto.getPhoneNumber());

        return administratorMapper.instanceToDto(administrator);
    }

    private void updateAuthenticationUsername(String email) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication oldAuthentication = context.getAuthentication();
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                email, oldAuthentication.getCredentials(), oldAuthentication.getAuthorities()
        );
        context.setAuthentication(updatedAuthentication);
    }

    private String getEmailFromSecurityContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
}
