package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.course.spring.services.OrderItemService;

import java.security.Principal;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("/makeOrder")
    public String makeOrder(@RequestParam("street") String street,
                           @RequestParam("house") String house,
                           Principal principal,
                           RedirectAttributes redirectAttributes) {
        try {
            orderItemService.createOrder(street, house, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Заказ успешно оформлен!");
            return "redirect:/orders"; // Перенаправление на страницу заказов
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при оформлении заказа: " + e.getMessage());
            return "redirect:/cart"; // Перенаправление обратно в корзину
        }
    }
}
