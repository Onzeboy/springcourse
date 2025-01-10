package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.course.spring.pojo.CartItem;
import ru.course.spring.pojo.Product;
import ru.course.spring.pojo.User;
import ru.course.spring.services.CartItemService;
import ru.course.spring.services.ProductService;
import ru.course.spring.services.UserService;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    @PostMapping("/user/addToCart")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        // Получение имени пользователя (email или username)
        String userEmail = principal.getName();

        // Найдите пользователя в базе данных по email
        User user = userService.findByUserEmail(userEmail);

        // Найдите продукт по ID
        Product product = productService.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден."));

        // Проверьте доступное количество
        if (product.getProductQuantity() < quantity) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Недостаточно товара в наличии. Доступно: " + product.getProductQuantity());
            return "redirect:/products";
        }

        // Уменьшите количество продукта
        product.setProductQuantity(product.getProductQuantity() - quantity);
        productService.save(product);

        // Добавьте товар в корзину
        cartItemService.addToCart(productId, quantity, product.getProductPriceCent(), user.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Товар добавлен в корзину.");
        return "mainPage";
    }


    @GetMapping("/user/cart")
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
        return "/user/cartPage";
    }
    @PostMapping("/user/cart/remove")
    public String removeItem(@RequestParam("itemId") Long itemId) {
        cartItemService.removeItemFromCart(itemId);
        return "redirect:/cart";
    }
}