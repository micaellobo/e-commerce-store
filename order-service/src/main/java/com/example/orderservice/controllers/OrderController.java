package com.example.orderservice.controllers;

import com.example.orderservice.dtos.OrderCreateDto;
import com.example.orderservice.services.IOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {"api/v1/orders"})
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<Object> add(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @RequestHeader("userId") Long userId,
            @RequestHeader("username") String username,
            @Valid @RequestBody OrderCreateDto orderCreateDto) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, orderCreateDto);

        var orderDto = orderService.add(orderCreateDto, userId);

        return ResponseEntity.ok(orderDto);
    }
}
