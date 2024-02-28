package com.example.reviewsservice.services;

import com.example.reviewsservice.controllers.ReviewException;
import com.example.reviewsservice.dtos.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceClient
        implements IOrderServiceClient {

    private final RestTemplate restTemplate;

    @Value("${api.order-service}")
    private String serviceUrl;

    @Override
    public Optional<OrderDto> getOrder(Long orderId) {
        var url = this.serviceUrl + "/users/me/" + orderId;
        ResponseEntity<OrderDto> response = null;
        try {
            response = this.restTemplate.getForEntity(url, OrderDto.class);

            if (!response.getStatusCode()
                         .is2xxSuccessful()) {
                log.error(response.getStatusCode()
                                  .toString());
            }

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            HttpStatusCode statusCode = null;

            if (response != null) {
                statusCode = response.getStatusCode();
            }

            if (statusCode != null && statusCode.is5xxServerError()) {
                log.error("OrderServiceClient - " + e.getMessage());
                throw new ReviewException(statusCode);
            }

            return Optional.empty();
        }
    }
}
