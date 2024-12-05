package ru.course.spring.pojo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cart")
@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //cascade = CascadeType.ALL, orphalRemove = true(OneToMany)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy загрузка пользователя
    @JoinColumn(name = "user_id", nullable = false) // Внешний ключ в таблице Cart
    private User cartUser;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime cartCreatedAt;

    @OneToMany(mappedBy = "cartItemCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
