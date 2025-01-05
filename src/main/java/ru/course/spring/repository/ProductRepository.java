package ru.course.spring.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.course.spring.pojo.Product;

import java.security.SecureRandom;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO product (category, description, image_data, name, price, quantity) VALUES (:category, :description, :imageData, :name, :price, :quantity)", nativeQuery = true)
//    void insertProduct(@Param("category") String category,
//                       @Param("description")String description,
//                       @Param("imageData") byte[] imageData,
//                       @Param("name") String name,
//                       @Param("price") int price,
//                       @Param("quantity") int quantity);
}
