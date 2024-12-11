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

    @Column(name = "category")
    private String productCategory;

    @Column(name = "price")
    private int productPriceCent;

    @Column(name = "imageURL")
    private String productImageURL;

    @OneToMany(mappedBy = "cartItemProduct",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductPriceCent() {
        return productPriceCent;
    }

    public void setProductPriceCent(int productPriceCent) {
        this.productPriceCent = productPriceCent;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }
}
