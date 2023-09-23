package com.example.hotelgfl.repository;

import com.example.hotelgfl.dto.ReceiptResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReceiptRepositoryTest {

    @Autowired
    private ReceiptRepository receiptRepository;

    @ParameterizedTest
    @MethodSource("findReceiptResponseByIdSource")
    void findReceiptResponseByIdTest(Long id, ReceiptResponse expected) {
        var actual = receiptRepository.findReceiptById(id, ReceiptResponse.class);
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> findReceiptResponseByIdSource() {
        return Stream.of(
                Arguments.of(
                        10L,
                        new ReceiptResponse(
                                10L,
                                LocalDateTime.of(2000, 2, 2, 0, 0),
                                LocalDateTime.of(2000, 2, 5, 0, 0),
                                10L,
                                449.9775,
                                "n@gmail.com"
                        )
                ),
                Arguments.of(
                        99L,
                        null
                )
        );
    }
}
