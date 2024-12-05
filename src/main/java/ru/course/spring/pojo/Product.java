package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Product")
@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String productName;

    @Column(name = "price")
    private int productPriceCent;

    @Column(name = "image")
    private String productImage;

    @OneToMany(mappedBy = "cartItemProduct",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    private int priceCent;

    public Double getPriceInRubles() {
        return priceCent / 100.0;
    }

    // Удобный метод для установки цены в рублях
    public void setPriceInRubles(Double priceInRubles) {
        this.priceCent = (int) Math.round(priceInRubles * 100);
    }
}
