package ru.course.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String email, @RequestParam(required = false) String phoneNumber) {
        User user = new User();
        user.setUserName(username);
        user.setUserPassword(password);
        user.setUserEmail(email);
        user.setUserPhoneNumber(phoneNumber);
        userService.save(user);
        return "redirect:/login";
    }
}
