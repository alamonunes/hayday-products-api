package com.example.api.service;

import com.example.api.entity.Product;
import com.example.api.repository.ProductRepository;
import org.springframework.data.domain.Page;
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
        return productRepository.findAll();
    }

    public List<Product> listSortedProducts(String field, String order) {
        return productRepository.findAll(Sort.by(Sort.Direction.fromString(order), field));
    }

    public Page<Product> listSortedProductsWithPagination(int offset, String field, String order) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, 15, Sort.Direction.fromString(order), field));
        return products;
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> saveProduct(Product product) {
        if (Stream.of(product.getName(), product.getPrice(), product.getTime()).anyMatch(Objects::isNull))
            return Optional.empty();
        return Optional.of(productRepository.save(product));
    }

    public Optional<Product> deleteProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()) {
            productRepository.delete(optProduct.get());
            return optProduct;
        }
        return Optional.empty();
    }

    public Optional<Product> updateProduct(Long id, Product product) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isEmpty())
            return Optional.empty();
        if(product.getName() != null)
            optProduct.get().setName(product.getName());
        if(product.getPrice() != null)
            optProduct.get().setPrice(product.getPrice());
        if(product.getTime() != null)
            optProduct.get().setTime(product.getTime());

        return Optional.of(productRepository.save(optProduct.get()));
    }

}