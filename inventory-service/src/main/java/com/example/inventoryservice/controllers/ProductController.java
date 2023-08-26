package com.example.inventoryservice.controllers;

import com.example.inventoryservice.config.ContextHolder;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.services.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = {"api/v1/products"})
public class ProductController {

    private final IProductService productService;
    private final ContextHolder contextHolder;


    /**
     * Create a new product
     *
     * @param request          The HttpServletRequest.
     * @param productCreateDto The product to create.
     * @return The response entity with product details.
     */
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))}),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/add")
    public ResponseEntity<ProductDto> add(
            HttpServletRequest request,
            @Valid @RequestBody ProductCreateDto productCreateDto) {

        this.logRequest(request, productCreateDto);

        var productDto = this.productService.addOne(productCreateDto);

//        return ResponseEntity.created(URI.create("/" + productDto.id())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneById(
            HttpServletRequest request,
            @PathVariable Long id) {

        this.logRequest(request, null);

        var productDto = this.productService.getOneById(id);

        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(HttpServletRequest request) {

        this.logRequest(request, null);

        var productsDto = this.productService.getAll();

        return ResponseEntity.ok(productsDto);
    }

    @PutMapping("/increase-stock")
    public ResponseEntity<Object> increaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        this.logRequest(request, productsQuantities);

        this.productService.increaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/decrease-stock")
    public ResponseEntity<Object> decreaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        this.logRequest(request, productsQuantities);

        this.productService.decreaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-ids")
    public ResponseEntity<Object> getByIds(
            HttpServletRequest request,
            @RequestParam List<Long> ids) {

        this.logRequest(request, ids);

        var productsDto = this.productService.getAllByIds(ids);

        return ResponseEntity.ok(productsDto);
    }

    private void logRequest(
            final HttpServletRequest request,
            final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), this.contextHolder.getCorrelationId(), this.contextHolder.getUsername(), obj);
    }
}