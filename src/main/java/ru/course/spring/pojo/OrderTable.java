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

    @OneToMany(mappedBy = "orderItemOrderTable",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> OrderItems;
}