package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.course.spring.pojo.User;
import ru.course.spring.services.EmailService;
import ru.course.spring.services.OrderItemService;

import java.security.Principal;

@Controller
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/user/makeOrder")
    public String makeOrder(@RequestParam("street") String street,
                            @RequestParam("house") String house,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        try {
            // Создаем заказ
            orderItemService.createOrder(street, house, principal.getName());

            // Логика отправки письма
            String userEmail = orderItemService.getUserEmail(principal.getName()); // Получаем email пользователя
            if (userEmail != null) {
                String subject = "Подтверждение заказа";
                String message = String.format("Ваш заказ успешно оформлен на адрес: %s, дом %s. Спасибо за ваш заказ!", street, house);
                emailService.sendSimpleEmail(userEmail, subject, message);
            }

            redirectAttributes.addFlashAttribute("success", "Заказ успешно оформлен! Уведомление отправлено на вашу почту.");
            return "redirect:/user/orders"; // Перенаправление на страницу заказов
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при оформлении заказа: " + e.getMessage());
            return "redirect:/user/cart"; // Перенаправление обратно в корзину
        }
    }
}
