package ru.course.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.Cart;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.Product;
import ru.course.spring.repository.CartItemRepository;
import ru.course.spring.repository.CartRepository;
import ru.course.spring.repository.ProductRepository;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

//    public void addProductToCart(Long cartId, Long productId, Integer quantity) {
//            Optional<Cart> cartOptional = cartRepository.findById(cartId);
//            Optional<Product> productOptional = productRepository.findById(productId);
//
//        if (cartOptional.isPresent() && productOptional.isPresent()) {
//            Cart cart = cartOptional.get();
//            Product product = productOptional.get();
//
//            Optional<CartItem> existingCartItemOptional = cart.getCartItems().stream()
//                    .filter(cartItem -> cartItem.getCartItemProduct().getId().equals(productId))
//                    .findFirst();
//
//            if (existingCartItemOptional.isPresent()) {
//                CartItem existingCartItem = existingCartItemOptional.get();
//                existingCartItem.setCartItemQuantity(existingCartItem.getCartItemQuantity() + quantity);
//                cartItemRepository.save(existingCartItem);
//            } else {
//                CartItem newCartItem = new CartItem();
//                newCartItem.setCartItemCart(cart);
//                newCartItem.setCartItemProduct(product);
//                newCartItem.setCartItemQuantity(quantity);
//                cartItemRepository.save(newCartItem);
//                cart.getCartItems().add(newCartItem);
//            }
//
//            cartRepository.save(cart);
//        }
//    }
}
