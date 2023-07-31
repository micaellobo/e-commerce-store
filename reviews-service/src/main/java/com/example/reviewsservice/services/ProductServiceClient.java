package com.example.reviewsservice.services;

import com.example.reviewsservice.dtos.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceClient implements IProductServiceClient {

    private final RestTemplate restTemplate;

    @Value("${api.inventory-service}")
    private String serviceUrl;

    @Override
    public Optional<ProductDto> getProductById(final Long id) {
        try {
            var url = serviceUrl + "/" + id;

            var response = restTemplate.getForEntity(url, ProductDto.class);

            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return Optional.empty();
        }
    }
}
