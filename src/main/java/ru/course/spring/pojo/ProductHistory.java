package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import ru.course.spring.ByteArrayConverter;

import java.util.List;

@Table(name = "ProductHistory")
@Entity
@Data
public class ProductHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String productName;

    @Column(name = "description")
    private String productDescription;

    @Column(name = "quantity")
    private int productQuantity;

    @Column(name = "price")
    private int productPriceCent;

    @Column(name = "image_data")
    private byte[] productImageData; // Поле для хранения изображения

    @OneToMany(mappedBy = "orderItemProductHistory",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItem;

    @Transient
    private String base64Image;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPriceCent() {
        return productPriceCent;
    }

    public void setProductPriceCent(int productPriceCent) {
        this.productPriceCent = productPriceCent;
    }

    public byte[] getProductImageData() {
        return productImageData;
    }

    public void setProductImageData(byte[] productImageData) {
        this.productImageData = productImageData;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}
