package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.administrator.AdministratorDto;
import com.example.hotelgfl.dto.administrator.AdministratorUpdateDto;
import com.example.hotelgfl.mapper.AdministratorMapper;
import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.repository.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final AdministratorMapper administratorMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdministratorDto create(AdministratorDto administratorDto) {
        Administrator admin = administratorMapper.dtoToEntity(administratorDto);
        String hashPassword = passwordEncoder.encode(administratorDto.getPassword());
        admin.setPassword(hashPassword);
        administratorRepository.save(admin);
        return administratorMapper.entityToDto(admin);
    }

    @Transactional
    public AdministratorDto delete(HttpSession session, String email) {
        if (getEmailFromSecurityContext().equals(email)) {
            session.invalidate();
        }
        Administrator administrator = administratorRepository.findByEmail(email, Administrator.class)
                .orElseThrow(EntityNotFoundException::new);
        administratorRepository.delete(administrator);
        return administratorMapper.entityToDto(administrator);
    }

    @Transactional
    public AdministratorDto update(String email, AdministratorUpdateDto administratorDto) {
        Administrator administrator = administratorRepository.findByEmail(email, Administrator.class)
                .orElseThrow(EntityNotFoundException::new);

        // if the user who updates the administrator and the administrator are the same people,
        // then update authenticated email using dto value
        String authenticatedEmail = getEmailFromSecurityContext();
        if (email.equals(authenticatedEmail)) {
            String newAuthenticatedEmail = administratorDto.getEmail();
            updateAuthenticationUsername(newAuthenticatedEmail);
        }
        updateRankAndSalary(administrator, administratorDto);
        administrator.setFirstName(administratorDto.getFirstName());
        administrator.setLastName(administratorDto.getLastName());
        administrator.setEmail(administratorDto.getEmail());
        administrator.setPassportId(administratorDto.getPassportId());
        administrator.setPhoneNumber(administratorDto.getPhoneNumber());

        return administratorMapper.entityToDto(administrator);
    }

    public <T> T get(String email, Class<T> dtoType) {
        return administratorRepository.findByEmail(email, dtoType)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Administrator get(String email) {
        return get(email, Administrator.class);
    }

    public <T> List<T> getAll(Class<T> dtoType) {
        return administratorRepository.findAllBy(dtoType);
    }

    private void updateAuthenticationUsername(String email) {
        Authentication oldAuthentication = getAuthentication();
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(
                email, oldAuthentication.getCredentials(), oldAuthentication.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(updatedAuthentication);
    }

    private void updateRankAndSalary(Administrator toUpdate, AdministratorUpdateDto dto) {
        if (hasRole("ADMIN")) {
            toUpdate.setRank(dto.getRank());
            toUpdate.setSalary(dto.getSalary());
        }
    }

    private boolean hasRole(String role) {
        return getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_" + role));
    }

    private String getEmailFromSecurityContext() {
        return getAuthentication().getName();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
