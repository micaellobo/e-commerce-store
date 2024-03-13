package com.example.orderservice.kafka;

import com.example.orderservice.controllers.OrderException;
import com.example.orderservice.dtos.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer
        implements IOrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.kafka.topics.order-created}")
    private String topicOrderCreated;

    @Override
    public void sendOrderCreate(OrderDto message) {
        var data = this.serializeData(message);

        var sendResultFuture = this.kafkaTemplate.send(
                this.topicOrderCreated,
                message.id().toString(),
                data
        );

        sendResultFuture.thenAccept(sendResult -> {
            if (sendResult == null) {
                throw new OrderException(OrderException.ERROR_PUBLISH_ORDER_CREATED);
            }
        });
    }

    private String serializeData(final OrderDto message) {
        try {
            return this.objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new OrderException(OrderException.ERROR_PUBLISH_ORDER_CREATED);
        }
    }
}
