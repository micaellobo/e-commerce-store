package com.example.orderservice.kafka;

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
        String data = null;
        try {
            data = this.objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var sendResultFuture = this.kafkaTemplate.send(
                this.topicOrderCreated,
                message.id().toString(),
                data
        );

        sendResultFuture.thenAccept(sendResult -> {
            if (sendResult != null) {
                System.out.println("Message sent successfully to topic: " + sendResult.getRecordMetadata().topic());
            } else {
                System.out.println("Message send failed.");
            }
        });

    }
}
