package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.course.spring.pojo.OrderTable;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.OrderTableRepository;
import ru.course.spring.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderTableController {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/orders")
        public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }
            String username = principal.getName();
            User user = userRepository.findByUserEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

            List<OrderTable> orders = orderTableRepository.findByOrderTableUser(user);
            model.addAttribute("orders", orders);
            return "orderPage"; // Имя HTML-шаблона
        }
}
