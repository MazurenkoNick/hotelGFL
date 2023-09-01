package com.example.hotelgfl.service;

import com.example.hotelgfl.dto.ReceiptResponse;
import com.example.hotelgfl.mapper.ReceiptMapper;
import com.example.hotelgfl.model.Receipt;
import com.example.hotelgfl.model.Reservation;
import com.example.hotelgfl.repository.ReceiptRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptResponse get(Long id) {
        return receiptRepository.findReceiptResponseById(id);
    }

    @Transactional
    public ReceiptResponse remove(Long id) {
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Reservation reservation = receipt.getReservation();
        reservation.setReceipt(null);
        return receiptMapper.entityToReceiptResponse(receipt);
    }
}
