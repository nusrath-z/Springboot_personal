package com.nusrath.ecommerce.repository;

import com.nusrath.ecommerce.model.Product;

import java.util.List;
import com.nusrath.ecommerce.model.Category;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);
}
