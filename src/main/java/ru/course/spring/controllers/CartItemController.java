package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.course.spring.pojo.User;
import ru.course.spring.services.CartItemService;
import ru.course.spring.services.UserService;

import java.security.Principal;
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


}