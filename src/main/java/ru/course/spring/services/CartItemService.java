package ru.course.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.Product;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.CartItemRepository;
import ru.course.spring.repository.ProductRepository;
import ru.course.spring.repository.UserRepository;

@Service
public class CartItemService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    public void addToCart(Long productId, int quantity, int price, Long id) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productId));

        CartItem cartItem = new CartItem();
        cartItem.setCartItemProduct(product);
        cartItem.setCartItemQuantity(quantity);
        cartItem.setCartItemPrice(price);
        User user = userRepository.getUserById(id);
        cartItem.setCartItemUser(user);
        cartItemRepository.save(cartItem); // Сохраняем в корзину
    }
}
