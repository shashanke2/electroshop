package com.electroshop.service;

import com.electroshop.model.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product updatedProduct);
    void reduceStock(Long productId, int quantity);
    void deleteProduct(Long id);
    List<Product> searchProducts(String query);

}
