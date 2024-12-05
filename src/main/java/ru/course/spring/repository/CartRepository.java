package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.course.spring.pojo.Cart;
import ru.course.spring.pojo.CartItem;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
