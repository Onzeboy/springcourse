package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CartItem")
public class  CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product cartItemProduct; // Связь с таблицей Product

    @Column(name = "quantity", nullable = false)
    private int cartItemQuantity;

    @Column(name = "price", nullable = false)
    private int cartItemPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User cartItemUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCartItemUser() {
        return cartItemUser;
    }

    public void setCartItemUser(User cartItemUser) {
        this.cartItemUser = cartItemUser;
    }

    public Product getCartItemProduct() {
        return cartItemProduct;
    }

    public void setCartItemProduct(Product cartItemProduct) {
        this.cartItemProduct = cartItemProduct;
    }

    public int getCartItemQuantity() {
        return cartItemQuantity;
    }

    public void setCartItemQuantity(int cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public int getCartItemPrice() {
        return cartItemPrice;
    }

    public void setCartItemPrice(int cartItemPrice) {
        this.cartItemPrice = cartItemPrice;
    }
}