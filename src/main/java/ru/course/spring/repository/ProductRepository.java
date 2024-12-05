package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.course.spring.pojo.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
