package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderTable")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User orderTableUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime orderTableCreatedAt;

    @Column(name = "city", nullable = false)
    private String orderTableCity;

    @Column(name = "street", nullable = false)
    private String orderTableStreet;

    @Column(name = "home", nullable = false)
    private String orderTableHome;

    @Column(name = "totalPrice", nullable = false)
    private int orderTableTotalPrice;

    @OneToMany(mappedBy = "orderItemOrderTable",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> OrderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOrderTableUser() {
        return orderTableUser;
    }

    public void setOrderTableUser(User orderTableUser) {
        this.orderTableUser = orderTableUser;
    }

    public LocalDateTime getOrderTableCreatedAt() {
        return orderTableCreatedAt;
    }

    public void setOrderTableCreatedAt(LocalDateTime orderTableCreatedAt) {
        this.orderTableCreatedAt = orderTableCreatedAt;
    }

    public String getOrderTableCity() {
        return orderTableCity;
    }

    public void setOrderTableCity(String orderTableCity) {
        this.orderTableCity = orderTableCity;
    }

    public String getOrderTableStreet() {
        return orderTableStreet;
    }

    public void setOrderTableStreet(String orderTableStreet) {
        this.orderTableStreet = orderTableStreet;
    }

    public String getOrderTableHome() {
        return orderTableHome;
    }

    public void setOrderTableHome(String orderTableHome) {
        this.orderTableHome = orderTableHome;
    }

    public int getOrderTableTotalPrice() {
        return orderTableTotalPrice;
    }

    public void setOrderTableTotalPrice(int orderTableTotalPrice) {
        this.orderTableTotalPrice = orderTableTotalPrice;
    }

    public List<OrderItem> getOrderItems() {
        return OrderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        OrderItems = orderItems;
    }
}