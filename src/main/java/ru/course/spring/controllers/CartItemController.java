package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.User;
import ru.course.spring.services.CartItemService;
import ru.course.spring.services.UserService;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            @RequestParam("price") int price,
                            Principal principal) {
        // Получение имени пользователя (email или username)
        String userEmail = principal.getName();

        // Найдите пользователя в базе данных по имени (если нужно ID)
        User user = userService.findByUserEmail(userEmail);

        cartItemService.addToCart(productId, quantity, price, user.getId());
        return "redirect:/products";
    }

    @GetMapping("/cart")
    public String cart(Model model, Principal principal){
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }
        String userEmail = principal.getName();
        User user = userService.findByUserEmail(userEmail);

        if (user == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не найден
        }
        
        
        Long userId = user.getId(); // Получение ID пользователя
        List<CartItem> cartItems = cartItemService.getCartItems(userId);

        cartItems.forEach(cartItem -> {
            if (cartItem.getCartItemProduct().getProductImageData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(cartItem.getCartItemProduct().getProductImageData());
                cartItem.getCartItemProduct().setBase64Image("data:image/jpeg;base64," + base64Image);
            }
        });
        model.addAttribute("cartItems", cartItems);
        int totalSum = cartItems.stream()
                .mapToInt(item -> item.getCartItemProduct().getProductPriceCent() * item.getCartItemQuantity())
                .sum();
        model.addAttribute("totalSum", totalSum);
        return "cartPage";
    }
    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam("itemId") Long itemId) {
        cartItemService.removeItemFromCart(itemId);
        return "redirect:/cart";
    }
}