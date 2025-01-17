package ru.course.spring.controllers;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.course.spring.pojo.User;
import ru.course.spring.services.UserService;

import java.security.Principal;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Отображение списка пользователей
    @GetMapping("/admin/users")
    public String listUsers(Model model, Principal principal) {
        // Получаем ID текущего пользователя
        String currentUserEmail = principal.getName();
        // Предполагаем, что UserService имеет метод для получения пользователя по email
        // Можно добавить такой метод или изменить существующий
        Optional<User> currentUserOpt = Optional.ofNullable(userService.findByUserEmail(currentUserEmail));
        if (!currentUserOpt.isPresent()) {
            // Обработка случая, когда текущий пользователь не найден
            return "redirect:/login";
        }
        Long currentUserId = currentUserOpt.get().getId();

        List<User> users = userService.getAllUsersExcept(currentUserId);
        model.addAttribute("users", users);
        model.addAttribute("rolesList", Arrays.asList("ROLE_USER", "ROLE_ADMIN")); // Пример ролей
        return "admin/users"; // Шаблон Thymeleaf
    }

    // Обработка формы редактирования пользователя
    @PostMapping("/admin/users/edit")
    public String editUser(
            @RequestParam("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("phone_number") String phone,
            @RequestParam("role") String roleString,
            RedirectAttributes redirectAttributes // Для передачи сообщений об ошибках
    ) {
        try {
            userService.updateUser(id, username, phone, email, roleString);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлён.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка при обновлении пользователя.");
        }
        return "redirect:/admin/users";
    }
}
