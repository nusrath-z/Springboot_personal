package com.nusrath.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.nusrath.ecommerce.dto.ProductRequest;
import com.nusrath.ecommerce.dto.ProductResponse;
import com.nusrath.ecommerce.model.Category;
import com.nusrath.ecommerce.model.Product;
import com.nusrath.ecommerce.repository.CategoryRepository;
import com.nusrath.ecommerce.repository.ProductRepository;
@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepo,
                          CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    // Fetch all
    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = findProduct(id);
        return toResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        Category category = findCategory(categoryId);
        return productRepo.findByCategory(category).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest request) {
        Category category = findCategory(request.getCategoryId());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product saved = productRepo.save(product);
        return toResponse(saved);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);
        Category category = findCategory(request.getCategoryId());

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product updated = productRepo.save(product);
        return toResponse(updated);
    }

    public void deleteProduct(Long id) {
        Product product = findProduct(id);
        productRepo.delete(product);
    }

    // ------------------- Helpers -------------------
    private Product findProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private Category findCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse res = new ProductResponse();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setDescription(product.getDescription());
        res.setPrice(product.getPrice());
        res.setStock(product.getStock());
        res.setCategory_id(product.getCategory().getId());
        res.setCategory(product.getCategory().getName());
        return res;
    }
}
