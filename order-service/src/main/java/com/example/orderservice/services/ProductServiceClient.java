package com.example.orderservice.services;

import com.example.orderservice.dtos.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Map<Long, ProductDto> getProductById(final Set<Long> ids) {
        try {
            var idsStringList = ids.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            var url = serviceUrl + "?ids=" + idsStringList;

            var response = restTemplate.getForEntity(url, ProductDto[].class);

            return Arrays.stream(Objects.requireNonNull(response.getBody()))
                    .collect(Collectors.toMap(ProductDto::id, productDto -> productDto));
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return new HashMap<>();
        }
    }

    @Override
    public boolean updateStock(final Set<Long> ids) {
        try {
            var url = serviceUrl + "/stock/decrease";

            var response = restTemplate.getForEntity(url, ProductDto[].class);

            return response.getStatusCode().is1xxInformational();
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return false;
        }
    }
}
