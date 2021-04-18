package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class BulkService {

    private BulkProcessor processor;

    private final String index = "actions";

    public BulkService(RestHighLevelClient client) {
        this.processor = bulkProcessor(client);
    }

    public void add(ItemModel itemModel) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        IndexRequest one = new IndexRequest(index)
                .source(objectMapper.writeValueAsString(itemModel), XContentType.JSON);
        processor.add(one);
    }

    public BulkProcessor bulkProcessor(RestHighLevelClient client) {
        BulkProcessor.Builder builder =
                BulkProcessor.builder(bulkConsumer(client), bulkProcessorListener());

        builder.setBulkActions(10); // Execute the bulk every 10 requests. Default is 1000.
        builder.setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB)); // Flush the bulk every 1mb whatever the number of requests. Default is 5mb.
        builder.setFlushInterval(TimeValue.timeValueSeconds(10L)); // Flush the bulk every 10 seconds whatever the number of requests. Default is not set.

        builder.setConcurrentRequests(1); // 1 concurrent request is allowed to be executed while accumulating new bulk requests. Default is 1.

        builder.setBackoffPolicy(BackoffPolicy
                .constantBackoff(TimeValue.timeValueSeconds(1L), 1)); // Retry request one more time after 1 second if failed. Default is exponential backoff with 8 retries and a start delay of 50ms.

        return builder.build();
    }

    public BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer(RestHighLevelClient client) {
        return (request, bulkListener) ->
                client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
    }

    public BulkProcessor.Listener bulkProcessorListener() {
        return new BulkProcessor.Listener() {

            @Override
            public void beforeBulk(long l, BulkRequest bulkRequest) {
                System.out.println("BulkProcessor.Listener : beforeBulk : numberOfActions = " + bulkRequest.numberOfActions());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
                System.out.println("BulkProcessor.Listener : afterBulk : hasFailures = " + bulkResponse.hasFailures());
            }

            @Override
            public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
                System.out.println("BulkProcessor.Listener : afterBulk failed : throwable = " + throwable);
            }
        };
    }
}