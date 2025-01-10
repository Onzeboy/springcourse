package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.course.spring.pojo.OrderItem;
import ru.course.spring.pojo.OrderStatus;
import ru.course.spring.pojo.OrderTable;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.OrderItemRepository;
import ru.course.spring.repository.OrderTableRepository;
import ru.course.spring.repository.UserRepository;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class OrderTableController {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @GetMapping("/user/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }
        String username = principal.getName();
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        List<OrderStatus> statuses = List.of(OrderStatus.CREATED, OrderStatus.SHIPPED);

        List<OrderTable> orders = orderTableRepository.findByUserIdAndStatuses(user.getId(), statuses);

        // Форматируем даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        orders.forEach(order -> order.setFormattedDate(order.getOrderTableCreatedAt().format(formatter)));

        model.addAttribute("orders", orders);

        return "/user/orderPage"; // Имя HTML-шаблона
    }

    @GetMapping("/user/orderHistory")
    public String getHistoryOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }
        String username = principal.getName();
        User user = userRepository.findByUserEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        List<OrderStatus> statuses = List.of(OrderStatus.DELIVERED, OrderStatus.CANCELED);

        List<OrderTable> orders = orderTableRepository.findByUserIdAndStatuses(user.getId(), statuses);

        // Форматируем даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        orders.forEach(order -> order.setFormattedDate(order.getOrderTableCreatedAt().format(formatter)));

        model.addAttribute("orders", orders);

        return "/user/orderHistoryPage"; // Имя HTML-шаблона
    }

    @GetMapping("/user/orders/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Перенаправление, если пользователь не аутентифицирован
        }
        List<OrderItem> orderItem = orderItemRepository.findByOrderItemOrderTableId(id);
        model.addAttribute("orderItem", orderItem);
        OrderTable order = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        String username = principal.getName();
        if (!order.getOrderTableUser().getUserEmail().equals(username)) {
            return "redirect:/products"; // Перенаправление, если пользователь не владелец заказа
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        order.setFormattedDate(order.getOrderTableCreatedAt().format(formatter));

        model.addAttribute("order", order);
        return "/user/orderDetails"; // HTML-шаблон для подробностей заказа
    }
    @GetMapping("/admin/allorders")
    public String getAllOrders(Model model) {
        List<OrderTable> orders = orderTableRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        orders.forEach(order -> order.setFormattedDate(order.getOrderTableCreatedAt().format(formatter)));
        model.addAttribute("orders", orders);
        return "/admin/adminOrders"; // Шаблон для отображения заказов администратора
    }

    @PostMapping("/admin/allorders/update")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        OrderTable order = orderTableRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));

        try {
            order.setOrderStatus(OrderStatus.valueOf(status));
            orderTableRepository.save(order); // Сохраняем изменения
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неверный статус заказа: " + status);
        }

        return "redirect:/admin/allorders"; // Перенаправляем обратно на страницу заказов
    }
    @GetMapping("/admin/allorders/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        List<OrderItem> orderItem = orderItemRepository.findByOrderItemOrderTableId(id);
        model.addAttribute("orderItem", orderItem);
        OrderTable order = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        model.addAttribute("order", order);
        return "/user/orderDetails"; // Шаблон для отображения деталей заказа
    }
}
