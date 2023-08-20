package com.example.inventoryservice.services;

import com.example.inventoryservice.controllers.ProductException;
import com.example.inventoryservice.dtos.IProductMapper;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.repository.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

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
        this.productCreate = ProductCreateDto
                .builder()
                .name("Trackpad")
                .description("Apple Magic Trackpad")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .build();

        this.product = Product.builder()
                .id(1L)
                .name(this.productCreate.name())
                .description(this.productCreate.description())
                .price(this.productCreate.price())
                .quantity(this.productCreate.quantity())
                .build();

        this.productDto = ProductDto.builder()
                .name(this.product.getName())
                .description(this.product.getDescription())
                .price(this.product.getPrice())
                .quantity(this.product.getQuantity())
                .build();
    }

    @Test
    void addOne_WhenProductDoesNotExist_ShouldSave() {
        //Arrange
        when(this.productRepository.existsByName(anyString()))
                .thenReturn(false);
        when(this.productMapper.toProduct(any(ProductCreateDto.class)))
                .thenReturn(this.product);
        when(this.productRepository.save(any(Product.class)))
                .thenReturn(this.product);
        when(this.productMapper.toDto(any(Product.class)))
                .thenReturn(this.productDto);

        //Act
        var productSaved = this.productService.addOne(this.productCreate);

        //Assert
        Assertions.assertEquals(this.productDto, productSaved);
        verify(this.productRepository).save(this.product);
    }

    @Test
    void addOne_WhenProductAlreadyExist_ShouldThrowProductException() {
        //Arrange
        when(this.productRepository.existsByName(anyString())).thenReturn(true);

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> this.productService.addOne(this.productCreate),
                ProductException.ALREADY_EXISTS_PRODUCT);

        verify(this.productRepository, never()).save(any(Product.class));
    }

    @Test
    void getAll_WhenSingleProduct_ShouldReturnList() {
        //Arrange
        var productsDtoList = Collections.singletonList(this.productDto);
        when(this.productRepository.findAll())
                .thenReturn(List.of(this.product));
        when(this.productMapper.toDto(anyList()))
                .thenReturn(productsDtoList);

        //Act
        var productsGet = this.productService.getAll();

        //Assert
        Assertions.assertEquals(productsDtoList.size(), productsGet.size());
        Assertions.assertEquals(productsDtoList, productsGet);
    }

    @Test
    void getOneById_WhenProductExist_ShouldReturnsProduct() {
        //Arrange
        when(this.productRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(this.product));
        when(this.productMapper.toDto(any(Product.class)))
                .thenReturn(this.productDto);

        //Act
        var productGet = this.productService.getOneById(anyLong());

        //Assert
        Assertions.assertEquals(this.productDto, productGet);
    }

    @Test
    void getOneById_WhenProductDoesNotExist_ShouldThrowProductException() {
        //Arrange
        when(this.productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> this.productService.getOneById(anyLong()),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }

    @Test
    void getAllByIds_WhenSingleProduct_ShouldReturnList() {
        //Arrange
        var productsListDto = List.of(this.productDto);
        when(this.productRepository.findAllById(anyList()))
                .thenReturn(List.of(this.product));
        when(this.productMapper.toDto(anyList()))
                .thenReturn(productsListDto);

        //Act
        var productsGet = this.productService.getAllByIds(anyList());

        //Assert
        Assertions.assertEquals(productsListDto.size(), productsGet.size());
        Assertions.assertEquals(productsListDto, productsGet);
    }

    @Test
    void increaseStock_WhenAllProductsExist_ShouldIncreaseTheQuantities() {
        //Arrange
        var initialQuantity = this.product.getQuantity(); // 100
        var quantityToIncrease = 1;

        var quantities = List.of(
                new ProductStockQuantityDto(this.product.getId(), quantityToIncrease)
        );

        when(this.productRepository.findAllById(anyList()))
                .thenReturn(List.of(this.product));

        //Act
        this.productService.increaseStock(quantities);

        //Assert
        Assertions.assertEquals(initialQuantity + quantityToIncrease, this.product.getQuantity());
    }


    @Test
    void increaseStock_WhenSomeGivenProductsDoesNotExist_ShouldThrowProductException() {
        //Arrange
        var initialQuantity = this.product.getQuantity(); // 100
        var quantityToIncrease = 1;

        var quantities = List.of(
                new ProductStockQuantityDto(this.product.getId(), quantityToIncrease)
        );

        when(this.productRepository.findAllById(anyList()))
                .thenReturn(Collections.emptyList());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> this.productService.increaseStock(quantities),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }


    @Test
    void decreaseStock_WhenAllGivenProductsExist_ShouldDecreaseTheQuantities() {
        //Arrange

        var initialQuantity = this.product.getQuantity(); // 100
        var quantityToDecrease = 99;

        var quantities = List.of(
                new ProductStockQuantityDto(this.product.getId(), quantityToDecrease)
        );

        when(this.productRepository.findAllById(anyList()))
                .thenReturn(List.of(this.product));

        //Act
        this.productService.decreaseStock(quantities);

        //Assert
        Assertions.assertEquals(initialQuantity - quantityToDecrease, this.product.getQuantity());
    }

    @Test
    void decreaseStock_WhenSomeGivenProductsDoesNotExist_ShouldThrowProductException() {
        //Arrange

        var quantityToDecrease = 99;

        var quantities = List.of(
                new ProductStockQuantityDto(this.product.getId(), quantityToDecrease)
        );

        when(this.productRepository.findAllById(anyList()))
                .thenReturn(Collections.emptyList());

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> this.productService.decreaseStock(quantities),
                ProductException.PRODUCT_DOES_NOT_EXIST);
    }

    @Test
    void decreaseStock_WhenDecreaseQuantityGreaterThanExisting_ShouldThrowProductException() {
        //Arrange

        // initialQuantity = 100
        var quantityToDecrease = 101;

        var quantities = List.of(
                new ProductStockQuantityDto(this.product.getId(), quantityToDecrease)
        );

        when(this.productRepository.findAllById(anyList()))
                .thenReturn(List.of(this.product));

        //Act and Assert
        Assertions.assertThrows(ProductException.class,
                () -> this.productService.decreaseStock(quantities),
                ProductException.QUANTITY_LOWER_ZERO);
    }
}