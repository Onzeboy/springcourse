package ru.course.spring.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.course.spring.pojo.Role;
import ru.course.spring.pojo.User;
import ru.course.spring.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> findByUserPhoneNumber(String phone){
        return userRepository.findByUserPhoneNumber(phone);
    }

    public List<User> getAllUsersExcept(Long currentUserId) {
        return userRepository.findAllByIdNot(currentUserId);
    }

    @Transactional
    public void updateUser(Long id, String username, String phone, String email, String roleString) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        Optional<User> existingUserByEmail = userRepository.findByUserEmail(email);
        if (existingUserByEmail.isPresent() && !existingUserByEmail.get().getId().equals(id)) {
            throw new IllegalArgumentException("Этот email уже зарегистрирован.");
        }

        // Проверка уникальности номера телефона
        Optional<User> existingUserByPhone = userRepository.findByUserPhoneNumber(phone);
        if (existingUserByPhone.isPresent() && !existingUserByPhone.get().getId().equals(id)) {
            throw new IllegalArgumentException("Этот номер телефона уже зарегистрирован.");
        }
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
