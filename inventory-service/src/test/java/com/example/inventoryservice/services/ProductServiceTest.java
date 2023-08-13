package com.example.inventoryservice.services;

import com.example.inventoryservice.controllers.ProductException;
import com.example.inventoryservice.dtos.IProductMapper;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.repository.IProductRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductMapper productMapper;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private ProductCreateDto productCreate;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void beforeEach() {
        productCreate = ProductCreateDto
                .builder()
                .name("Trackpad")
                .description("Apple Magic Trackpad")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .build();

        product = Product.builder()
                .id(1L)
                .name(productCreate.name())
                .description(productCreate.description())
                .price(productCreate.price())
                .quantity(productCreate.quantity())
                .build();

        productDto = ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
    }

    @Test
    void addOne_WhenProductDoNotExist_ShouldSave() {
        //Arrange
        when(productRepository.existsByName(anyString())).thenReturn(false);
        when(productMapper.toProduct(any(ProductCreateDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        //Act
        var productSaved = productService.addOne(productCreate);

        //Assert
        Assertions.assertEquals(productDto, productSaved);
        verify(productRepository).save(product);
    }

    @Test
    void addOne_WhenProductAlreadyExist_ShouldThrowProductException() {
        //Arrange
        when(productRepository.existsByName(anyString())).thenReturn(true);

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> productService.addOne(productCreate),
                ProductException.ALREADY_EXISTS_PRODUCT);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAll_WhenSingleProduct_ShouldReturnList() {
        //Arrange
        var productsDtoList = Collections.singletonList(productDto);
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productMapper.toDto(anyList())).thenReturn(productsDtoList);

        //Act
        var productsGet = productService.getAll();

        //Assert
        Assertions.assertEquals(productsDtoList.size(), productsGet.size());
        Assertions.assertEquals(productsDtoList, productsGet);
    }

    @Test
    void getOneById_WhenProductExist_ShouldReturnsProduct() {
        //Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        //Act
        var productGet = productService.getOneById(anyLong());

        //Assert
        Assertions.assertEquals(productDto, productGet);
    }

    @Test
    void getOneById_WhenProductDoNotExist_ShouldThrowProductException() {
        //Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> productService.getOneById(anyLong()),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }

    @Test
    void getAllByIds_WhenSingleProduct_ShouldReturnList() {
        //Arrange
        var productsListDto = List.of(productDto);
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product));
        when(productMapper.toDto(anyList())).thenReturn(productsListDto);

        //Act
        var productsGet = productService.getAllByIds(anyList());

        //Assert
        Assertions.assertEquals(productsListDto.size(), productsGet.size());
        Assertions.assertEquals(productsListDto, productsGet);
    }

    @Test
    void increaseStock_WhenAllProductsExist_ShouldIncreaseTheQuantities() {
        //Arrange
        var initialQuantity = product.getQuantity(); // 100
        var quantityToIncrease = 1;

        var quantities = List.of(
                new ProductStockQuantityDto(product.getId(), quantityToIncrease)
        );

        var productsToUpdate = List.of(product);
        when(productRepository.findAllById(anyList())).thenReturn(productsToUpdate);

        //Act
        productService.increaseStock(quantities);

        //Assert
        Assertions.assertEquals(initialQuantity + quantityToIncrease, product.getQuantity());

    }


    @Test
    void increaseStock_WhenSomeGivenProductsDoNotExist_ShouldThrowProductException() {
        //Arrange
        var initialQuantity = product.getQuantity(); // 100
        var quantityToIncrease = 1;

        var quantities = List.of(
                new ProductStockQuantityDto(product.getId(), quantityToIncrease)
        );

        when(productRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> productService.increaseStock(quantities),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }


    @Test
    void decreaseStock_WhenAllGivenProductsExist_ShouldDecreaseTheQuantities() {
        //Arrange

        var initialQuantity = product.getQuantity(); // 100
        var quantityToDecrease = 99;

        var quantities = List.of(
                new ProductStockQuantityDto(product.getId(), quantityToDecrease)
        );

        var productsToUpdate = List.of(product);
        when(productRepository.findAllById(anyList())).thenReturn(productsToUpdate);

        //Act
        productService.decreaseStock(quantities);

        //Assert
        Assertions.assertEquals(initialQuantity - quantityToDecrease, product.getQuantity());
    }

    @Test
    void decreaseStock_WhenSomeGivenProductsDoNotExist_ShouldThrowProductException() {
        //Arrange

        var quantityToDecrease = 99;

        var quantities = List.of(
                new ProductStockQuantityDto(product.getId(), quantityToDecrease)
        );

        when(productRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> productService.decreaseStock(quantities),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }

    @Test
    void decreaseStock_WhenDecreaseQuantityGreaterThanExisting_ShouldThrowProductException() {
        //Arrange

        // initialQuantity = 100
        var quantityToDecrease = 101;

        var quantities = List.of(
                new ProductStockQuantityDto(product.getId(), quantityToDecrease)
        );

        var productsToUpdate = List.of(product);
        when(productRepository.findAllById(anyList())).thenReturn(productsToUpdate);

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> productService.decreaseStock(quantities),
                ProductException.QUANTITY_LOWER_ZERO);
    }
}