package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.course.spring.pojo.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
