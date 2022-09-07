package com.example.api.controller;

import com.example.api.entity.Product;
import com.example.api.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(productService.listProducts());
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
        if (optProduct.isPresent())
            return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @GetMapping("/s={field}")
    public ResponseEntity<List<Product>> listSortedProducts(@PathVariable String field,
                                                            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        List<Product> products = productService.listSortedProducts(field, order);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/p={offset}")
    public ResponseEntity<Page<Product>> listSortedProductWithPagination(@PathVariable int offset,
                                                            @RequestParam(name = "field", required = false, defaultValue = "id") String field,
                                                            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        Page<Product> products = productService.listSortedProductsWithPagination(offset, field, order);
        return ResponseEntity.ok(products);
    }

}
