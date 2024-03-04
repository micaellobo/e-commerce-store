package com.example.orderservice.kafka;

import com.example.orderservice.dtos.OrderDto;

public interface IOrderEventProducer {
    void sendOrderCreate(OrderDto message);

}
