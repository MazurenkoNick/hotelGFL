package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.AdministratorDto;
import com.example.hotelgfl.model.Administrator;
import com.example.hotelgfl.model.Rank;
import com.example.hotelgfl.repository.AdministratorRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdministratorServiceTests {

    @MockBean
    private AdministratorRepository administratorRepository;

    @Autowired
    @InjectMocks
    private AdministratorService administratorService;

    @ParameterizedTest
    @MethodSource("createPasswordSource")
    void createPasswordTest(AdministratorDto administratorDto) {
        Administrator adm = new Administrator();
        adm.setPassword(administratorDto.getPassword());
        adm.setPassportId(administratorDto.getPassportId());
        adm.setPhoneNumber(administratorDto.getPhoneNumber());
        adm.setEmail(administratorDto.getEmail());
        adm.setFirstName(administratorDto.getFirstName());
        adm.setLastName(administratorDto.getLastName());
        adm.setRank(administratorDto.getRank());
        adm.setSalary(administratorDto.getSalary());

        var actual = administratorService.create(administratorDto);

        assertThat(actual.getPassword()).startsWith("$2a$10$");
    }

    static Stream<Arguments> createPasswordSource() {
        return Stream.of(
                Arguments.of(
                    new AdministratorDto(Rank.JUNIOR, 9999.99, "Qwerty123", "First",
                            "Last", "n@gmail.com", "1234567", "06642452")
                )
        );
    }

    // todo: add update test with security context & add delete test to check that session is invalidated
}
