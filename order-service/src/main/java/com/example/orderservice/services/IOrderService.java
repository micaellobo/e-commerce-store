package com.example.orderservice.services;

import com.example.orderservice.dtos.OrderCreateDto;
import com.example.orderservice.dtos.OrderDto;

public interface IOrderService{
    OrderDto addOne(OrderCreateDto orderCreateDto);

    OrderDto getOne(Long orderId);
}
