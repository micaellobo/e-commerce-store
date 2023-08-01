package com.example.orderservice.services;

import com.example.orderservice.controllers.OrderException;
import com.example.orderservice.dtos.*;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderProduct;
import com.example.orderservice.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IProductServiceClient productServiceClient;

    @Override
    public OrderDto add(final OrderCreateDto orderCreateDto, final Long userId) {
        var productsOrder = getOrderProducts(orderCreateDto);

        var order = Order.builder()
                .userId(userId)
                .products(productsOrder)
                .build();

        var orderSaved = orderRepository.save(order);

        return orderMapper.toDto(orderSaved);
    }

    private Set<OrderProduct> getOrderProducts(final OrderCreateDto orderCreateDto) {
        var productsId = orderCreateDto.products()
                .stream()
                .map(OrderProductCreateDto::productId)
                .collect(Collectors.toSet());

        var productById = productServiceClient.getProductById(productsId);

        return orderCreateDto.products()
                .stream()
                .map(orderProductCreateDtoToOrderProduct(productById))
                .collect(Collectors.toSet());
    }

    private static Function<OrderProductCreateDto, OrderProduct> orderProductCreateDtoToOrderProduct(final Map<Long, ProductDto> productById) {
        return orderProductCreateDto -> {
            var productDto = Optional.ofNullable(productById.get(orderProductCreateDto.productId()))
                    .orElseThrow(() -> new OrderException(OrderException.PRODUCT_DOES_NOT_EXIST));

            if (orderProductCreateDto.quantity() > productDto.quantity()){
                throw new OrderException(OrderException.STOCK_NOT_AVAILABLE);
            }

            return OrderProduct.builder()
                    .price(productDto.price())
                    .quantity(orderProductCreateDto.quantity())
                    .build();

        };
    }
}
