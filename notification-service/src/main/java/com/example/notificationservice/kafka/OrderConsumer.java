package com.example.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumer {

    @KafkaListener(id = "order-consumer",
                   topics = "${api.kafka.topics.order-created}",
                   groupId = "order-group")
    public void listen(
            String value,
            @Header(KafkaHeaders.RECEIVED_TOPIC)
            String topic,
            @Header(KafkaHeaders.RECEIVED_KEY)
            String key
    ) {
        log.info("CONSUMED EVENT FROM TOPIC: {} KEY = {} VALUE = {}", topic, key, value);
    }

}
