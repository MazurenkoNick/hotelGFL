package com.example.hotelgfl.service;

import com.example.hotelgfl.mapper.ReceiptMapper;
import com.example.hotelgfl.model.*;
import com.example.hotelgfl.repository.ReceiptRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReceiptServiceTests {

    @Mock
    private ReceiptRepository receiptRepository;
    @Mock
    private ReceiptMapper receiptMapper;
    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void removeTest() {
        Reservation reservation = new Reservation(
                LocalDateTime.of(2000, 2, 2, 0, 0),
                LocalDateTime.of(2000, 2, 5, 0, 0),
                new Administrator(), new Room(), new Renter());
        Receipt receipt = new Receipt(reservation, 999);
        reservation.setReceipt(receipt);

        when(receiptRepository.findById(anyLong())).thenReturn(Optional.of(receipt));

        receiptService.remove(anyLong());
        assertThat(reservation.getReceipt()).isNull();
    }
}
