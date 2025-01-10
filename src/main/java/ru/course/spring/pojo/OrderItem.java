package ru.course.spring.pojo;

import jakarta.persistence.*;
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
    @JoinColumn(name = "orderTable_id", nullable = false)
    private OrderTable orderItemOrderTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productHistory_id", nullable = false)
    private ProductHistory orderItemProductHistory;

    @Column(name = "quantity", nullable = false)
    private int orderItemQuantity;

    @Column(name = "price", nullable = false)
    private int orderItemPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderTable getOrderItemOrderTable() {
        return orderItemOrderTable;
    }

    public void setOrderItemOrderTable(OrderTable orderItemOrderTable) {
        this.orderItemOrderTable = orderItemOrderTable;
    }

    public ProductHistory getOrderItemProductHistory() {
        return orderItemProductHistory;
    }

    public void setOrderItemProductHistory(ProductHistory orderItemProductHistory) {
        this.orderItemProductHistory = orderItemProductHistory;
    }

    public int getOrderItemQuantity() {
        return orderItemQuantity;
    }

    public void setOrderItemQuantity(int orderItemQuantity) {
        this.orderItemQuantity = orderItemQuantity;
    }

    public int getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(int orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }
}