package ru.course.spring.controllers;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.course.spring.pojo.User;

import java.security.Principal;
import java.util.*;

@Controller
public class UserController {

    // Отображение списка пользователей
    @GetMapping("/users")
    public String listUsers(Model model, Principal principal) {
        // Получаем ID текущего пользователя
        String currentUserEmail = principal.getName();
        // Предполагаем, что UserService имеет метод для получения пользователя по email
        // Можно добавить такой метод или изменить существующий
        Optional<User> currentUserOpt = userService.getUserByEmail(currentUserEmail);
        if (!currentUserOpt.isPresent()) {
            // Обработка случая, когда текущий пользователь не найден
            return "redirect:/login";
        }
        Long currentUserId = currentUserOpt.get().getId();

        List<User> users = userService.getAllUsersExcept(currentUserId);
        model.addAttribute("users", users);
        model.addAttribute("rolesList", Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER")); // Пример ролей
        return "admin/users"; // Шаблон Thymeleaf
    }

    // Обработка формы редактирования пользователя
    @PostMapping("/users/edit")
    public String editUser(@RequestParam("id") Long id,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam(value = "roles", required = false) List<String> roles,
                           Model model) {
        Set<String> rolesSet = roles != null ? new HashSet<>(roles) : new HashSet<>();
        try {
            userService.updateUser(id, username, email, rolesSet);
            model.addAttribute("successMessage", "Пользователь успешно обновлен.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }

}
