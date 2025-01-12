package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.course.spring.pojo.Role;
import ru.course.spring.pojo.User;
import ru.course.spring.services.UserService;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String startPage(){
        return "redirect:/products";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam(required = false) String phoneNumber,
            Model model) {

        // Проверка минимальной длины пароля
        if (password.length() < 3) {
            model.addAttribute("error", "Пароль должен содержать не менее 3 символов");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phoneNumber", phoneNumber);
            return "register"; // Возврат на страницу регистрации
        }

        // Проверка существования электронной почты
        if (userService.findByUserEmail(email) != null) {
            model.addAttribute("error", "Электронная почта уже зарегистрирована");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phoneNumber", phoneNumber);
            return "register"; // Возврат на страницу регистрации
        }

        // Проверка существования номера телефона
        if (userService.findByUserPhoneNumber(phoneNumber) != null) {
            model.addAttribute("error", "Номер телефона уже зарегистрирован");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phoneNumber", phoneNumber);
            return "register"; // Возврат на страницу регистрации
        }

        // Если проверка прошла успешно, создаем и сохраняем пользователя
        User user = new User();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setUserEmail(email);
        user.setUserPhoneNumber(phoneNumber);
        user.setRole(Role.ROLE_USER); // Установка роли
        userService.save(user);

        return "redirect:/login"; // Перенаправление на страницу логина
    }

}
