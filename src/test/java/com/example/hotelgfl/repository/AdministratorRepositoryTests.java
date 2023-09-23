package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.administrator.ResponseAdministratorDto;
import com.example.hotelgfl.model.Rank;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AdministratorRepositoryTests {

    @Autowired
    private AdministratorRepository administratorRepository;

    @ParameterizedTest
    @MethodSource("findAdministratorDtoByEmailSource")
    void findAdministratorDtoByEmailTest(String email, ResponseAdministratorDto expected) {
        Optional<ResponseAdministratorDto> actual = administratorRepository.findByEmail(email, ResponseAdministratorDto.class);
        assertThat(actual.orElse(null)).isEqualTo(expected);
    }

    static Stream<Arguments> findAdministratorDtoByEmailSource() {
        return Stream.of(
                Arguments.of(
                        "n1@gmail.com",
                        new ResponseAdministratorDto(Rank.JUNIOR, 9999, "First", "Last",
                                "n1@gmail.com", "1234530", "3102928")
                ),
                Arguments.of(
                        "nonExisted@gmail.com",
                        null
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findAllAdministratorDtosSource")
    void findAllAdministratorDtosTest(List<ResponseAdministratorDto> expected) {

    }

    static Stream<Arguments> findAllAdministratorDtosSource() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new ResponseAdministratorDto(Rank.JUNIOR, 9999, "First", "Last",
                                        "n1@gmail.com", "1234530", "3102928"),
                                new ResponseAdministratorDto(Rank.MIDDLE, 15000, "Ivan", "Ivanenko",
                                        "n2@gmail.com", "1234531", "3102929"),
                                new ResponseAdministratorDto(Rank.SENIOR, 20000, "Peter", "Petrenko",
                                        "n3@gmail.com", "1234532", "3102930"),
                                new ResponseAdministratorDto(Rank.JUNIOR, 10000, "Mykola", "Mykolenko",
                                        "n4@gmail.com", "1234533", "3102931"),
                                new ResponseAdministratorDto(Rank.MIDDLE, 13500, "Viktor", "Viktorov",
                                        "n5@gmail.com", "1234534", "3102932")
                        )
                )
        );
    }
}
