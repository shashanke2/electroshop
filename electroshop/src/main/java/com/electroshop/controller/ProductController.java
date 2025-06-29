package com.electroshop.controller;

import com.electroshop.dto.ProductDTO;
import com.electroshop.model.Product;
import com.electroshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. Add new product
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product saved = productService.addProduct(product);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    // 2. Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> list = productService.getAllProducts()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // 3. Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToDTO(productService.getProductById(id)));
    }

    // 4. Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO) {
        Product updated = productService.updateProduct(id, mapToEntity(productDTO));
        return ResponseEntity.ok(mapToDTO(updated));
    }

    // 5. Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // DTO to Entity
    private Product mapToEntity(ProductDTO p) {
        return new Product(
        		p.getId(), 
        		p.getName(), 
        		p.getDescription(), 
        		p.getPrice(), 
        		p.getStock(),
        		p.getCategory()
        );
    }

    private ProductDTO mapToDTO(Product p) {
        return new ProductDTO(
        		p.getId(), 
        		p.getName(), 
        		p.getDescription(), 
        		p.getPrice(), 
        		p.getStock(), 
        		p.getCategory()
        );
    }
}
