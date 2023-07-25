package com.example.inventoryservice.controllers;

import com.example.inventoryservice.dtos.IProductMapper;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.inventoryservice.controllers.ProductController.CONTROLLER_PATH;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {CONTROLLER_PATH})
public class ProductController {

    public static final String CONTROLLER_PATH = "api/v1/products";
    private final IProductService productService;
    private final IProductMapper productMapper;

    @PostMapping("/add")
    public ResponseEntity<Object> add(
            @RequestHeader("CorrelationID") String correlationId,
            @Valid @RequestBody ProductCreateDto productCreateDto) {
        log.info("GET - /add - {} - {}", productCreateDto, correlationId);

        var product = productService.add(productCreateDto);

        var productDto = productMapper.toDto(product);

//        return ResponseEntity.created(URI.create("/" + productDto.id())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAll(
            @RequestHeader("CorrelationID") String correlationId) {
        log.info("GET - / - {}", correlationId);

        var products = productService.getAll();

        var productDtos = productMapper.toDtos(products);

        return ResponseEntity.ok(productDtos);
    }


    @PatchMapping("/{productId}/increase-stock")
    public ResponseEntity<Object> increaseStock(
            @RequestHeader("CorrelationID") String correlationId,
            @PathVariable final Long productId,
            @Valid @RequestBody ProductStockQuantityDto productStockQuantityDto) {

        log.info("PATCH - {}/increase-stock - {} - {}", productId, productStockQuantityDto, correlationId);

        productService.increaseStock(productId, productStockQuantityDto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}/decrease-stock")
    public ResponseEntity<Object> decreaseStock(
            @RequestHeader("CorrelationID") String correlationId,
            @PathVariable final Long productId,
            @Valid @RequestBody ProductStockQuantityDto productStockQuantityDto) {

        log.info("PATCH - {}/decrease-stock - {} - {}", productId, productStockQuantityDto, correlationId);

        productService.decreaseStock(productId, productStockQuantityDto);

        return ResponseEntity.noContent().build();
    }
}