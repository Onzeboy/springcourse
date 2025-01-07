package ru.course.spring.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.course.spring.config.PasswordEncoderConfig;
import ru.course.spring.pojo.Role;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<User> getAllUsersExcept(Long currentUserId) {
        return userRepository.findAllByIdNot(currentUserId);
    }

    @Transactional
    public void updateUser(Long id, String username, String phone, String email, String roleString) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        user.setUserName(username);
        user.setUserEmail(email);
        user.setUserPhoneNumber(phone);
        // Преобразуем строку из формы в enum
        // Если передали невалидную строку, valueOf бросит IllegalArgumentException
        Role enumRole = Role.valueOf(roleString);

        user.setRole(enumRole);

        userRepository.save(user);
    }
}
