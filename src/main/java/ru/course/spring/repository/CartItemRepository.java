package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.spring.pojo.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
