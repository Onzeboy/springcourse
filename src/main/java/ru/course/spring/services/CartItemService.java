package ru.course.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.Product;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.CartItemRepository;
import ru.course.spring.repository.ProductRepository;
import ru.course.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
        // Проверяем, есть ли уже этот продукт в корзине пользователя
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartItemUserAndCartItemProduct(user, product);

        if (existingCartItem.isPresent()) {
            // Если товар уже есть в корзине, увеличиваем количество
            CartItem cartItem = existingCartItem.get();
            cartItem.setCartItemQuantity(cartItem.getCartItemQuantity() + quantity);
            cartItemRepository.save(cartItem); // Обновляем количество в корзине
        } else {
            // Если товара еще нет в корзине, создаем новый элемент корзины
            CartItem cartItem = new CartItem();
            cartItem.setCartItemProduct(product);
            cartItem.setCartItemQuantity(quantity);
            cartItem.setCartItemPrice(price);
            cartItem.setCartItemUser(user);
            cartItemRepository.save(cartItem); // Сохраняем новый товар в корзину
        }
    }

    public List<CartItem> getCartItems(Long id) {
        return cartItemRepository.getCartItemsByUserId(id);
    }

    public void removeItemFromCart(Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Элемент корзины не найден: " + itemId));

        // Получаем продукт из элемента корзины
        Product product = cartItem.getCartItemProduct();

        // Возвращаем количество товара обратно в склад
        int removedQuantity = cartItem.getCartItemQuantity();
        product.setProductQuantity(product.getProductQuantity() + removedQuantity);

        // Сохраняем изменения продукта
        productRepository.save(product);

        // Удаляем элемент из корзины
        cartItemRepository.deleteById(itemId);
    }
}
