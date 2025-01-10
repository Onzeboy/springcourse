package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.spring.pojo.ProductHistory;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {

}
