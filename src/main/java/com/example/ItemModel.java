package com.example;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ItemModel {
    private final String endpoint;
    private final String transactionId;
    private final int count;
    private final LocalDateTime timestamp;
}
