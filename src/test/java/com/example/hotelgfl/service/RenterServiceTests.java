package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.RenterDto;
import com.example.hotelgfl.mapper.RenterMapper;
import com.example.hotelgfl.model.Renter;
import com.example.hotelgfl.repository.RenterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RenterServiceTests {

    @Mock
    private RenterRepository renterRepository;
    @Mock
    private RenterMapper renterMapper;
    @InjectMocks
    private RenterService renterService;

    @Test
    void updateTest() {
        Renter renter = new Renter("first", "last", "12345", "first@gmail.com", "12345");
        when(renterRepository.findByEmail(anyString(), any())).thenReturn(Optional.of(renter));
        RenterDto expected = new RenterDto("First", "Last", "54321", "54321", "last@gmail.com");

        renterService.update("first@gmail.com", expected);

        assertThat(renter.getEmail()).isEqualTo(expected.getEmail());
        assertThat(renter.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(renter.getLastName()).isEqualTo(expected.getLastName());
        assertThat(renter.getPassportId()).isEqualTo(expected.getPassportId());
        assertThat(renter.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
    }
}
