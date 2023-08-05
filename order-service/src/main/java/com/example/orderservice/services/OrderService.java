package com.example.orderservice.services;

import com.example.orderservice.controllers.OrderException;
import com.example.orderservice.dtos.*;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderProduct;
import com.example.orderservice.repository.IOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IProductServiceClient productServiceClient;

    @Transactional
    @Override
    public OrderDto add(final OrderCreateDto orderCreateDto, final Long userId) {

        var order = new Order();
        var productsOrder = getOrderProducts(orderCreateDto, order);

        order.setUserId(userId);
        order.setProducts(productsOrder);

        var orderSaved = orderRepository.save(order);

        var hasStockUpdated = productServiceClient.updateStock(orderCreateDto.products());

        if (!hasStockUpdated) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new OrderException(OrderException.ERROR_UPDATE_STOCK);
        }

        return orderMapper.toDto(orderSaved);
    }

    private List<OrderProduct> getOrderProducts(final OrderCreateDto orderCreateDto, final Order order) {
        var productsId = orderCreateDto.products()
                .stream()
                .map(OrderProductCreateDto::productId)
                .collect(Collectors.toSet());

        var productById = productServiceClient.getProductById(productsId);

        return orderCreateDto.products()
                .stream()
                .map(orderProductCreateDtoToOrderProduct(productById, order))
                .toList();
    }

    private static Function<OrderProductCreateDto, OrderProduct> orderProductCreateDtoToOrderProduct(final Map<Long, ProductDto> productById, final Order order) {
        return orderProductCreateDto -> {
            var productDto = Optional.ofNullable(productById.get(orderProductCreateDto.productId()))
                    .orElseThrow(() -> new OrderException(OrderException.PRODUCT_DOES_NOT_EXIST));

            if (orderProductCreateDto.quantity() > productDto.quantity()) {
                throw new OrderException(OrderException.STOCK_NOT_AVAILABLE);
            }

            return OrderProduct.builder()
                    .price(productDto.price())
                    .productId(orderProductCreateDto.productId())
                    .quantity(orderProductCreateDto.quantity())
                    .order(order)
                    .build();
        };
    }
}
