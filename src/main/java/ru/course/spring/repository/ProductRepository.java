package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.course.spring.pojo.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
