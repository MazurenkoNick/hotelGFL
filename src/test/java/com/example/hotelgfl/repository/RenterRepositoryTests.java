package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.RenterDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RenterRepositoryTests {

    @Autowired
    private RenterRepository renterRepository;

    @ParameterizedTest
    @CsvSource({
            "n@gmail.com, First, Last, 1234543, 2902928",
            "m@gmail.com, Inokentii, Inokentiev, 1324543, 2902929"
    })
    public void testFindRenterDtoByEmailTest(String email, String firstName, String lastName, String passportId, String phoneNumber) {
        Optional<RenterDto> optionalRenterDto = renterRepository.findRenterDtoByEmail(email);
        assertThat(optionalRenterDto).isPresent();
        RenterDto renterDto = optionalRenterDto.get();
        assertThat(renterDto.getFirstName()).isEqualTo(firstName);
        assertThat(renterDto.getLastName()).isEqualTo(lastName);
        assertThat(renterDto.getPassportId()).isEqualTo(passportId);
        assertThat(renterDto.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @ParameterizedTest
    @MethodSource("testFindAllRenterDtosSource")
    public void testFindAllRenterDtosTest(List<RenterDto> expected) {
        List<RenterDto> actual = renterRepository.findAllRenterDtos();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    static Stream<Arguments> testFindAllRenterDtosSource() {
        return Stream.of(
                Arguments.of(
                        List.of(
                               new RenterDto("First", "Last", "1234543",
                                       "2902928", "n@gmail.com"),
                               new RenterDto("Inokentii", "Inokentiev", "1324543",
                                       "2902929", "m@gmail.com"),
                                new RenterDto("Alex", "Shaldenko", "2134543",
                                        "2902930", "l@gmail.com"),
                                new RenterDto("Peter", "Parker", "2314543",
                                        "2902931", "k@gmail.com")
                        )
                )
        );
    }
}
