package com.example.orderservice.services;

import com.example.orderservice.config.ContextHolder;
import com.example.orderservice.controllers.OrderException;
import com.example.orderservice.dtos.*;
import com.example.orderservice.models.Order;
import com.example.orderservice.repository.IOrderRepository;
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
class OrderServiceTest {

    @Mock
    IOrderRepository orderRepository;
    @Mock
    IOrderMapper orderMapper;
    @Mock
    IProductServiceClient productServiceClient;
    @Mock
    ContextHolder contextHolder;
    @InjectMocks
    OrderService orderService;
    private ProductDto productDto;

    @BeforeEach
    void beforeEach() {

        this.productDto = ProductDto
                .builder()
                .id(1L)
                .name("Trackpad")
                .description("Apple Magic Trackpad")
                .price(BigDecimal.valueOf(99.99))
                .quantity(100)
                .build();

        lenient().when(this.contextHolder.getUserId())
                .thenReturn(1L);
        lenient().when(this.contextHolder.getUsername())
                .thenReturn("JhonDoe");
    }

    @Test
    void addOne_WhenValidOrder_ShouldCreateTheOrder() {
        //Arrange
        var orderProductCreate = OrderProductCreateDto
                .builder()
                .productId(this.productDto.id())
                .quantity(20)
                .build();

        var orderCreateDto = OrderCreateDto.builder()
                .products(List.of(orderProductCreate))
                .build();

        when(this.productServiceClient.getProductById(anyList()))
                .thenReturn(Map.of(this.productDto.id(), this.productDto));
        when(this.productServiceClient.updateStock(orderCreateDto.products()))
                .thenReturn(true);
        when(this.orderRepository.save(any(Order.class)))
                .thenReturn(new Order());
        when(this.orderMapper.toDto(any(Order.class)))
                .thenReturn(OrderDto.builder().build());

        //Act
        var orderCreated = this.orderService.addOne(orderCreateDto);

        //Assert
        Assertions.assertNotNull(orderCreated);
    }

    @Test
    void addOne_WhenProductDoesNotExist_ShouldThrowOrderException() {
        //Arrange
        var orderProductCreate = OrderProductCreateDto
                .builder()
                .productId(this.productDto.id())
                .quantity(this.productDto.quantity() + 1)
                .build();

        var orderCreateDto = OrderCreateDto.builder()
                .products(List.of(orderProductCreate))
                .build();

        when(this.productServiceClient.getProductById(anyList()))
                .thenReturn(Collections.emptyMap());

        //Act and Assert
        Assertions.assertThrows(OrderException.class,
                () -> this.orderService.addOne(orderCreateDto),
                OrderException.PRODUCT_DOES_NOT_EXIST);
    }

    @Test
    void addOne_WhenProductDoesNotHaveStock_ShouldThrowOrderException() {
        //Arrange
        var orderProductCreate = OrderProductCreateDto
                .builder()
                .productId(this.productDto.id())
                .quantity(this.productDto.quantity() + 1)
                .build();

        var orderCreateDto = OrderCreateDto.builder()
                .products(List.of(orderProductCreate))
                .build();

        when(this.productServiceClient.getProductById(anyList()))
                .thenReturn(Map.of(this.productDto.id(), this.productDto));

        //Act and Assert
        Assertions.assertThrows(OrderException.class,
                () -> this.orderService.addOne(orderCreateDto),
                OrderException.STOCK_NOT_AVAILABLE);
    }

    @Test
    void addOne_WhenStockNotUpdated_ShouldThrowOrderException() {
        //Arrange
        var orderProductCreate = OrderProductCreateDto
                .builder()
                .productId(this.productDto.id())
                .quantity(20)
                .build();

        var orderCreateDto = OrderCreateDto.builder()
                .products(List.of(orderProductCreate))
                .build();

        when(this.productServiceClient.getProductById(anyList()))
                .thenReturn(Map.of(this.productDto.id(), this.productDto));
        when(this.orderRepository.save(any(Order.class)))
                .thenReturn(new Order());
        when(this.productServiceClient.updateStock(orderCreateDto.products()))
                .thenReturn(false);

        //Act and Assert
        Assertions.assertThrows(OrderException.class,
                () -> this.orderService.addOne(orderCreateDto),
                OrderException.ERROR_UPDATE_STOCK);
    }

    @Test
    void getOne_WhenOrderExists_ShouldReturnOrder() {
        //Arrange
        when(this.orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Order()));
        when(this.orderMapper.toDto(any(Order.class)))
                .thenReturn(OrderDto.builder().build());

        //Act
        var orderGet = this.orderService.getOne(anyLong());

        //Assert
        Assertions.assertNotNull(orderGet);
    }

    @Test
    void getOne_WhenOrderDoesNotExists_ShouldReturnOrder() {
        //Arrange
        when(this.orderRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //Act and Assert
        Assertions.assertThrows(OrderException.class,
                () -> this.orderService.getOne(anyLong()),
                OrderException.ORDER_DOES_NOT_EXIST);
    }
}
