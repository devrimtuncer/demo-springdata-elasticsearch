package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RestController
public class DemoController {

    private final BulkService bulkService;

    private int index = 0;

    private final List<String> DUMMY_ENDPOINTS = Arrays.asList("/login", "/logout", "/payment");
    private final List<String> DUMMY_TRANSACTION_IDS = Arrays.asList("I10A", "I20B", "I30C");

    public DemoController(BulkService bulkService) {
        this.bulkService = bulkService;
    }

    /**
     * Adds an item model to @{@link BulkService}
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/")
    public String index() throws Exception {
        final String endpoint = DUMMY_ENDPOINTS.get(new Random().nextInt(DUMMY_ENDPOINTS.size()));
        final String transactionId = DUMMY_TRANSACTION_IDS.get(new Random().nextInt(DUMMY_TRANSACTION_IDS.size()));

        ItemModel model = ItemModel.builder()
                .endpoint(endpoint)
                .transactionId(transactionId)
                .count(1)
                .timestamp(LocalDateTime.now())
                .build();

        bulkService.add(model);
        return "index: " + index++;
    }
}
