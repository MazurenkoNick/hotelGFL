package com.example.hotelgfl.service;

import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.model.Role;
import com.example.hotelgfl.repository.AdministratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DbUserDetailsService implements UserDetailsService {

    private final AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Administrator administrator = administratorRepository.findByEmailFetchRoles(email)
                .orElseThrow(EntityNotFoundException::new);

        String[] roleNames = retrieveRoleNames(administrator.getRoles());
        return User.withUsername(administrator.getEmail())
                .password(administrator.getPassword())
                .roles(roleNames)
                .build();
    }

    private String[] retrieveRoleNames(List<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toArray(String[]::new);
    }
}
