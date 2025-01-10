package ru.course.spring.services;

import jakarta.transaction.Transactional;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.*;
import ru.course.spring.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Transactional
    public void createOrder(String street, String house, String username) {
        try {
            // Найти пользователя по имени пользователя
            User user = userRepository.findByUserEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

            // Получить все товары из корзины пользователя
            List<CartItem> cartItems = cartItemRepository.findByCartItemUser(user);
            if (cartItems.isEmpty()) {
                throw new RuntimeException("Корзина пуста");
            }

            // Создать заказ
            OrderTable order = new OrderTable();
            order.setOrderTableStreet(street);
            order.setOrderTableHome(house);
            order.setOrderTableCity("Муром"); // Привязываем заказ к пользователю
            order.setOrderTableUser(user);
            order.setOrderTableCreatedAt(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.CREATED);

            // Рассчитать общую стоимость товаров
            int totalPrice = cartItems.stream()
                    .mapToInt(cartItem -> cartItem.getCartItemProduct().getProductPriceCent() * cartItem.getCartItemQuantity())
                    .sum();
            order.setOrderTableTotalPrice(totalPrice);

            orderTableRepository.save(order);
            // Переносим товары из корзины в заказ
            for (CartItem cartItem : cartItems) {
                ProductHistory ph = new ProductHistory();

                ph.setProductName(cartItem.getCartItemProduct().getProductName());
                ph.setProductPriceCent(cartItem.getCartItemProduct().getProductPriceCent());
                ph.setProductDescription(cartItem.getCartItemProduct().getProductDescription());
                ph.setBase64Image(cartItem.getCartItemProduct().getBase64Image());
                ph.setProductImageData(cartItem.getCartItemProduct().getProductImageData());
                ph.setProductQuantity(cartItem.getCartItemQuantity());

                ProductHistory savedProductHistory = productHistoryRepository.save(ph);

                OrderItem orderItem = new OrderItem();

                orderItem.setOrderItemOrderTable(order);
                orderItem.setOrderItemProductHistory(savedProductHistory);
                orderItem.setOrderItemQuantity(cartItem.getCartItemQuantity());
                orderItem.setOrderItemPrice(savedProductHistory.getProductPriceCent() * cartItem.getCartItemQuantity());

                orderItemRepository.save(orderItem);
            }
            // Очищаем корзину
            cartItemRepository.deleteByCartItemUser(user);

        } catch (RuntimeException e) {
            // Логируем ошибку и повторно бросаем исключение
            throw e;
        } catch (Exception e) {
            // Перехватываем любые другие исключения
            throw new RuntimeException("Не удалось создать заказ, попробуйте позже");
        }
    }
    public String getUserEmail(String username) {
        User user = userRepository.findByUserToEmail(username);
        return user != null ? user.getUserEmail() : null;
    }
}
