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

        @Column(name = "email", nullable = false, unique = true)
        private String userEmail;

        @Column(name = "phoneNumber")
        private String userPhoneNumber;

        @Column(name = "username", nullable = false, unique = true)
        private String userName;

        @Column(name = "password", nullable = false)
        private String userPassword;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "userRoleId")
        private Role userRole;

        @OneToMany(mappedBy = "cartUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Cart> carts;

        @OneToMany(mappedBy = "orderTableUser", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderTable> orderTables;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", userPhoneNumber='" + userPhoneNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole=" + userRole +
                '}';
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void setUserRole(String user) {
    }
}

