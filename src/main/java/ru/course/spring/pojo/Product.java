package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String name;
    @Column(name = "price")
    private Integer priceCent;
    @Column(name = "image")
    private String image;

    public Double getPriceInRubles() {
        return priceCent / 100.0;
    }

    // Удобный метод для установки цены в рублях
    public void setPriceInRubles(Double priceInRubles) {
        this.priceCent = (int) Math.round(priceInRubles * 100);
    }
}
