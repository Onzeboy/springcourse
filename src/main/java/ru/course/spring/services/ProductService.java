package ru.course.spring.services;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.Product;
import ru.course.spring.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }
    public void save(Product product) {
        productRepository.save(product);
    }
}