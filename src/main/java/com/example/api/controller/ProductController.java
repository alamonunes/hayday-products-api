package com.example.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.api.entity.Product;
import com.example.api.service.ProductService;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProducts(
            @RequestParam(name = "pag", required = false, defaultValue = "0") Integer pag,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "field", required = false, defaultValue = "id") String field,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        return ResponseEntity.ok(this.productService.listSortedProductsWithPagination(pag,
                size, field, order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> optProduct = productService.getProduct(id);
        if (optProduct.isPresent())
            return ResponseEntity.ok(optProduct.get());
        else
            return ResponseEntity.notFound().build();

    }

    @PostMapping()
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Optional<Product> optProduct = productService.saveProduct(product);
        if (optProduct.isPresent()) {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long id) {
        Optional<Product> optProduct = productService.updateProduct(id, product);
        if (optProduct.isEmpty())
            return ResponseEntity.notFound().build();
        else if (optProduct.isPresent())
            return ResponseEntity.ok(optProduct.get());
        else
            return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> optProduct = productService.deleteProduct(id);
        if (optProduct.isPresent())
            return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }

}
