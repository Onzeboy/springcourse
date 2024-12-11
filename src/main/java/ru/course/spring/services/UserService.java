package ru.course.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.course.spring.config.PasswordEncoderConfig;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }
    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email).orElse(null);
    }
}
