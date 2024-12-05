package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "users") // Имя таблицы в БД
    @Entity
    @Data
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "username", nullable = false, unique = true)
        private String username;

        @Column(name = "password", nullable = false)
        private String password;

        @Column(name = "role", nullable = false)
        private String role; // Например, "USER", "ADMIN"

        @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Cart> carts;

        @OneToMany(mappedBy = "orderTableUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderTable> orderTables;
    }

