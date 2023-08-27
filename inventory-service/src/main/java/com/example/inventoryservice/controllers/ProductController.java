package com.example.inventoryservice.controllers;

import com.example.inventoryservice.config.ContextHolder;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.services.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Product")
public class ProductController {

    private final IProductService productService;
    private final ContextHolder contextHolder;


    /**
     * Create a new product
     *
     * @param request          The HttpServletRequest.
     * @param productCreateDto The product to create.
     * @return The created product.
     */
    @Operation(
            summary = "Create a new product",
            responses = {
                    @ApiResponse(responseCode = "201", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))}),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PostMapping("/add")
    public ResponseEntity<ProductDto> add(
            HttpServletRequest request,
            @Valid @RequestBody ProductCreateDto productCreateDto) {

        this.logRequest(request, productCreateDto);

        var productDto = this.productService.addOne(productCreateDto);

//        return ResponseEntity.created(URI.create("/" + productDto.id())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    /**
     * Get the product by id
     *
     * @param request The HttpServletRequest.
     * @param id      The product id.
     * @return The product.
     */
    @Operation(
            summary = "Get the product by id",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))}),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getOneById(
            HttpServletRequest request,
            @PathVariable Long id) {

        this.logRequest(request, null);

        var productDto = this.productService.getOneById(id);

        return ResponseEntity.ok(productDto);
    }

    /**
     * Get all products
     *
     * @param request The HttpServletRequest.
     * @return List of products.
     */
    @Operation(
            summary = "Get all products",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))}),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAll(HttpServletRequest request) {

        this.logRequest(request, null);

        var productsDto = this.productService.getAll();

        return ResponseEntity.ok(productsDto);
    }

    /**
     * Increase the stock of products
     *
     * @param request            The HttpServletRequest.
     * @param productsQuantities The products and quantities to increase.
     * @return No content.
     */
    @Operation(
            summary = "Increase the stock of products",
            responses = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/increase-stock")
    public ResponseEntity<Object> increaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        this.logRequest(request, productsQuantities);

        this.productService.increaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    /**
     * Decrease the stock of products
     *
     * @param request            The HttpServletRequest.
     * @param productsQuantities The products and quantities to decrease.
     * @return No content.
     */
    @Operation(
            summary = "Decrease the stock of products",
            responses = {
                    @ApiResponse(responseCode = "204"),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @PutMapping("/decrease-stock")
    public ResponseEntity<Object> decreaseStock(
            HttpServletRequest request,
            @Valid @RequestBody List<ProductStockQuantityDto> productsQuantities) {

        this.logRequest(request, productsQuantities);

        this.productService.decreaseStock(productsQuantities);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get all products by ids
     *
     * @param request The HttpServletRequest.
     * @param ids     The products ids.
     * @return List of products.
     */
    @Operation(
            summary = "Get all products by ids",
            responses = {
                    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))}),
                    @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("/by-ids")
    public ResponseEntity<List<ProductDto>> getByIds(
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