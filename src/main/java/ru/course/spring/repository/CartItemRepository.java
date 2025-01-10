package ru.course.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.Product;
import ru.course.spring.pojo.User;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c WHERE c.cartItemUser.id = :userID")
    List<CartItem> getCartItemsByUserId(@Param("userID") Long userID);

    @Query("select c from CartItem c WHERE  c.cartItemUser = :user")
    List<CartItem> findByCartItemUser(User user);

    Optional<CartItem> findByCartItemUserAndCartItemProduct(User user, Product product);

    void deleteByCartItemUser(User user);
}
