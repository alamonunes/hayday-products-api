package com.example.api;

import com.example.api.entity.Product;
import com.example.api.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class ApiExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiExampleApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(ProductService productService) {
        return args -> {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = TypeReference.class.getResourceAsStream("/hayday_goodlist_api.json");
            List<Product> products = mapper.readValue(inputStream, new TypeReference<List<Product>>() {
            });
            products.stream().forEach(product -> productService.saveProduct(product));

        };
    }
}