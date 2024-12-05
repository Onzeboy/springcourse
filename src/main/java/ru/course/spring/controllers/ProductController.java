package ru.course.spring.controllers;

import org.springframework.stereotype.Controller;
import ru.course.spring.repository.ProductRepository;

@Controller
public class ProductController  {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

}
