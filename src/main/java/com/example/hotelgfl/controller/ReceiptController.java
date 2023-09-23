package com.example.hotelgfl.controller;

import com.example.hotelgfl.dto.receipt.ReceiptResponse;
import com.example.hotelgfl.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation/{id}/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable("id") Long id) {
        ReceiptResponse receipt = receiptService.get(id);
        return ResponseEntity.ok(receipt);
    }

    @DeleteMapping
    public ResponseEntity<ReceiptResponse> deleteReceipt(@PathVariable("id") Long id) {
        ReceiptResponse receipt = receiptService.remove(id);
        return ResponseEntity.ok(receipt);
    }
}
