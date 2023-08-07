package com.example.inventoryservice.controllers;

import com.example.inventoryservice.config.CustomContextHolder;
import com.example.inventoryservice.dtos.IProductMapper;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.services.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {"api/v1/products"})
public class ProductController {

    private final IProductService productService;
    private final IProductMapper productMapper;
    private final CustomContextHolder contextHolder;

    @PostMapping("/add")
    public ResponseEntity<Object> add(
            HttpServletRequest request,
            @Valid @RequestBody ProductCreateDto productCreateDto) {

        logRequest(request, productCreateDto);

        var product = productService.add(productCreateDto);

        var productDto = productMapper.toDto(product);

//        return ResponseEntity.created(URI.create("/" + productDto.id())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneById(
            HttpServletRequest request,
            @PathVariable Long id) {

        logRequest(request, null);

        var product = productService.getOneBy(id);

        var productDto = productMapper.toDto(product);

        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(HttpServletRequest request) {

        logRequest(request, null);

        var products = productService.getAll();

        var productsDto = productMapper.toDto(products);

        return ResponseEntity.ok(productsDto);
    }

    @PutMapping("/increase-stock")
    public ResponseEntity<Object> increaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        logRequest(request, productsQuantities);

        productService.increaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/decrease-stock")
    public ResponseEntity<Object> decreaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        logRequest(request, productsQuantities);

        productService.decreaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-ids")
    public ResponseEntity<Object> getByIds(
            HttpServletRequest request,
            @RequestParam List<Long> ids) {

        logRequest(request, ids);

        var products = productService.getByIds(ids);

        var productsDto = productMapper.toDto(products);

        return ResponseEntity.ok(productsDto);
    }

    private void logRequest(final HttpServletRequest request, final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), contextHolder.getCorrelationId(), contextHolder.getUsername(), obj);
    }
}