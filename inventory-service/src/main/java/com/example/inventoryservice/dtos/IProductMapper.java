package com.example.inventoryservice.dtos;

import org.mapstruct.*;
import com.example.inventoryservice.models.Product;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IProductMapper {
    Product toProduct(ProductCreateDto productCreateDto);

    ProductDto toDto(Product product);

    List<ProductDto> toDto(List<Product> products);

    void partialUpdate(ProductCreateDto productCreateDto, @MappingTarget Product product);

    Product toEntity(ProductStockQuantityDto productStockQuantityDto);

    ProductStockQuantityDto toDto1(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductStockQuantityDto productStockQuantityDto, @MappingTarget Product product);
}