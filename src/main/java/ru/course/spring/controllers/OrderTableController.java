package ru.course.spring.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import ru.course.spring.services.OrderTableService;

import java.io.IOException;
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
    private OrderTableService orderTableService;

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

    @GetMapping("/admin/allorders/export")
    public void exportOrdersToExcel(HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");

            // Заголовки колонок
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Пользователь", "Дата", "Адрес", "Сумма", "Статус"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Получение данных заказов
            List<OrderTable> orders = orderTableService.getAllOrders();

            // Формат для даты
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

            // Заполнение данных
            int rowNum = 1;
            for (OrderTable order : orders) {
                Row row = sheet.createRow(rowNum++);

                // ID
                row.createCell(0).setCellValue(order.getId());

                // Имя пользователя
                row.createCell(1).setCellValue(order.getOrderTableUser().getUserName());

                // Дата создания заказа
                Cell dateCell = row.createCell(2);
                dateCell.setCellValue(java.sql.Timestamp.valueOf(order.getOrderTableCreatedAt())); // Преобразуем LocalDateTime в Timestamp
                dateCell.setCellStyle(dateCellStyle);

                // Адрес
                row.createCell(3).setCellValue(order.getOrderTableCity() + ", " +
                        order.getOrderTableStreet() + " " + order.getOrderTableHome());

                // Сумма
                row.createCell(4).setCellValue(order.getOrderTableTotalPrice() / 100.0);

                // Статус
                row.createCell(5).setCellValue(order.getOrderStatus().toString());
            }

            // Автоширина колонок
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Установка заголовков для скачивания файла
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");

            // Запись файла в выходной поток
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Ошибка при экспорте заказов в Excel.");
        }
    }
}
