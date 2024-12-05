package ru.course.spring.repository;

import org.springframework.data.repository.CrudRepository;
import ru.course.spring.pojo.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
