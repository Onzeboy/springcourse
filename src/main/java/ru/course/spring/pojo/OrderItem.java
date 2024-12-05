package ru.course.spring.pojo;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderItem")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "orderTable_id", nullable = false)
    private OrderTable orderItemOrderTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "product_id", nullable = false)
    private Product orderItemProduct;

    @Column(name = "quantity", nullable = false)
    private Integer orderItemQuantity;

    @Column(name = "price", nullable = false)
    private Integer orderItemPrice;
}