package com.example.orderservice.controllers;

import com.example.orderservice.config.CustomContextHolder;
import com.example.orderservice.config.RequiresAuthentication;
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
    private final CustomContextHolder contextHolder;

    @PostMapping("users/me")
    @RequiresAuthentication
    public ResponseEntity<Object> add(
            HttpServletRequest request,
            @Valid @RequestBody OrderCreateDto orderCreateDto) {

        this.logRequest(request, orderCreateDto);

        var orderDto = this.orderService.addOne(orderCreateDto);

        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("users/me/{orderId}")
    @RequiresAuthentication
    public ResponseEntity<Object> getOne(
            HttpServletRequest request,
            @PathVariable Long orderId) {

        this.logRequest(request, null);

        var orderDto = this.orderService.getOne(orderId);

        return ResponseEntity.ok(orderDto);
    }

    private void logRequest(final HttpServletRequest request, final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), this.contextHolder.getCorrelationId(), this.contextHolder.getUsername(), obj);
    }
}
