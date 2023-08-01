package com.example.inventoryservice.services;

import com.example.inventoryservice.controllers.ProductException;
import com.example.inventoryservice.dtos.IProductMapper;
import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.models.Product;
import com.example.inventoryservice.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final IProductMapper productMapper;
    private final IProductRepository productRepository;

    @Override
    public Product add(final ProductCreateDto productCreateDto) {

        var exists = productRepository.existsByName(productCreateDto.name());

        if (exists)
            throw new ProductException(ProductException.ALREADY_EXISTS_PRODUCT);

        var product = productMapper.toProduct(productCreateDto);

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public void increaseStock(final Long productId,
                              final ProductStockQuantityDto productStockQuantityDto) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductException.PRODUCT_DOES_NOT_EXIST));

        product.setQuantity(product.getQuantity() + productStockQuantityDto.quantity());

        productRepository.save(product);
    }

    @Override
    public void decreaseStock(final Long productId,
                              final ProductStockQuantityDto productStockQuantityDto) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ProductException.PRODUCT_DOES_NOT_EXIST));

        if (product.getQuantity() - productStockQuantityDto.quantity() < 0)
            throw new ProductException(ProductException.QUANTITY_LOWER_ZERO);

        product.setQuantity(product.getQuantity() - productStockQuantityDto.quantity());

        productRepository.save(product);
    }

    @Override
    public Product getOneBy(final Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException(ProductException.PRODUCT_DOES_NOT_EXIST));
    }

    @Override
    public List<Product> getByIds(final List<Long> ids) {
        return productRepository.findAllById(ids);
    }
}
