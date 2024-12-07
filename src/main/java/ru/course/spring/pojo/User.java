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

        @Column(name = "email")
        private String userEmail;

        @Column(name = "phoneNumber")
        private String userPhoneNumber;

        @Column(name = "username", nullable = false, unique = true)
        private String userName;

        @Column(name = "password", nullable = false)
        private String userPassword;

        @Column(name = "role", nullable = false)
        private String userRole; // Например, "USER", "ADMIN"

        @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Cart> carts;

        @OneToMany(mappedBy = "orderTableUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderTable> orderTables;
    }

