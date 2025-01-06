package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.User;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.cartItemUser.id = :userID")
    List<CartItem> getCartItemsByUserId(@Param("userID") Long userID);
}
