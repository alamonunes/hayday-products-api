package com.example.api.service;

import com.example.api.entity.Product;
import com.example.api.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listProducts() {
        return this.productRepository.findAll();
    }

    public List<Product> listSortedProducts(String field, String order) {
        return this.productRepository.findAll(Sort.by(Sort.Direction.fromString(order), field));
    }

    public List<Product> listSortedProductsWithPagination(Integer page, Integer size, String field,
            String order) {
        return this.productRepository
                .findAll(PageRequest.of(page, size, Sort.Direction.fromString(order), field)).getContent();
    }

    public Optional<Product> getProduct(Long id) {
        return this.productRepository.findById(id);
    }

    public Optional<Product> saveProduct(Product product) {
        if (Stream.of(product.getName(), product.getPrice(), product.getTime()).anyMatch(Objects::isNull))
            return Optional.empty();
        return Optional.of(this.productRepository.save(product));
    }

    public Optional<Product> deleteProduct(Long id) {
        Optional<Product> optProduct = this.productRepository.findById(id);
        if (optProduct.isPresent()) {
            this.productRepository.delete(optProduct.get());
            return optProduct;
        }
        return Optional.empty();
    }

    public Optional<Product> updateProduct(Long id, Product product) {
        Optional<Product> optProduct = this.productRepository.findById(id);
        if (optProduct.isEmpty())
            return Optional.empty();
        if (product.getName() != null)
            optProduct.get().setName(product.getName());
        if (product.getPrice() != null)
            optProduct.get().setPrice(product.getPrice());
        if (product.getTime() != null)
            optProduct.get().setTime(product.getTime());

        return Optional.of(this.productRepository.save(optProduct.get()));
    }

    public void deleteAll() {
        this.productRepository.deleteAll();
    }
}