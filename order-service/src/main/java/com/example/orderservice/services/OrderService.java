package com.example.orderservice.services;

import com.example.orderservice.config.ContextHolder;
import com.example.orderservice.controllers.OrderException;
import com.example.orderservice.dtos.*;
import com.example.orderservice.models.Order;
import com.example.orderservice.models.OrderProduct;
import com.example.orderservice.repository.IOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderMapper orderMapper;
    private final IProductServiceClient productServiceClient;
    private final ContextHolder contextHolder;

    @Transactional
    @Override
    public OrderDto addOne(final OrderCreateDto orderCreateDto) {

        var order = new Order();

        var productsOrder = this.getOrderProducts(orderCreateDto, order);
        order.setUserId(this.contextHolder.getUserId());
        order.setProducts(productsOrder);

        var orderSaved = this.orderRepository.save(order);

        var hasStockUpdated = this.productServiceClient.updateStock(orderCreateDto.products());

        if (!hasStockUpdated) {
            throw new OrderException(OrderException.ERROR_UPDATE_STOCK);
        }

        return this.orderMapper.toDto(orderSaved);
    }

    @Override
    public OrderDto getOne(final Long orderId) {
        var order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderException.ORDER_DOES_NOT_EXIST));

        return this.orderMapper.toDto(order);
    }

    private List<OrderProduct> getOrderProducts(
            final OrderCreateDto orderCreateDto,
            final Order order) {

        var productsId = orderCreateDto.products()
                .stream()
                .map(OrderProductCreateDto::productId)
                .toList();

        var productById = this.productServiceClient.getProductById(productsId);

        return orderCreateDto.products()
                .stream()
                .map(orderProductCreateDtoToOrderProduct(productById, order))
                .toList();
    }

    private static Function<OrderProductCreateDto, OrderProduct> orderProductCreateDtoToOrderProduct(
            final Map<Long, ProductDto> productById,
            final Order order) {

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
