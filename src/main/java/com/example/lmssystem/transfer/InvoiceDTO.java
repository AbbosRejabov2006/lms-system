package com.example.lmssystem.transfer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public record InvoiceDTO(LocalDateTime date,Double amount,Long userId,Long groupId,String status) {
}
