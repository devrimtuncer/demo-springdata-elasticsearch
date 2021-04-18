package com.example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemModel {
    private final String endpoint;
    private final String transactionId;
    private final int count;
    private final String timestamp;
}
