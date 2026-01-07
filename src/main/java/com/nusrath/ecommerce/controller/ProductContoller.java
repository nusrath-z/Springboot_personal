package com.nusrath.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nusrath.ecommerce.dto.ApiResponse;
import com.nusrath.ecommerce.dto.ProductRequest;
import com.nusrath.ecommerce.dto.ProductResponse;
import com.nusrath.ecommerce.service.ProductService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/products")
public class ProductContoller {
    
    private final ProductService productService;
    
    public ProductContoller(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ApiResponse.success(201, "Product created successfully", product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,
                                                      @RequestBody ProductRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ApiResponse.success(200, "Product updated successfully", product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success(200, "Product deleted successfully", "Product ID " + id + " deleted");
    }

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ApiResponse.success(200, "Products fetched successfully", products);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ApiResponse.success(200, "Product fetched successfully", product);
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProductResponse>> getByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> products = productService.getProductsByCategory(categoryId);
        return ApiResponse.success(200, "Products fetched successfully", products);
    }


    
    
}
